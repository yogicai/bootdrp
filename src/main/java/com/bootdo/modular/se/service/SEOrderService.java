package com.bootdo.modular.se.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.dao.AccountDao;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.service.CostAmountCalculator;
import com.bootdo.modular.rp.convert.RPOrderConverter;
import com.bootdo.modular.rp.dao.PointEntryDao;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.dao.RPOrderEntryDao;
import com.bootdo.modular.rp.dao.RPOrderSettleDao;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.se.convert.SEOrderConverter;
import com.bootdo.modular.se.dao.SEOrderDao;
import com.bootdo.modular.se.dao.SEOrderEntryDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.system.dao.DictDao;
import com.bootdo.modular.system.domain.DictDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class SEOrderService {
    @Resource
    private SEOrderDao seOrderDao;
    @Resource
    private SEOrderEntryDao seOrderEntryDao;
    @Resource
    private PointEntryDao pointEntryDao;
    @Resource
    private DictDao dictDao;
    @Resource
    private RPOrderDao rpOrderDao;
    @Resource
    private RPOrderEntryDao rpOrderEntryDao;
    @Resource
    private RPOrderSettleDao rpOrderSettleDao;
    @Resource
    private AccountDao accountDao;
    @Resource
    private CostAmountCalculator costAmountCalculator;


    public SEOrderDO get(Integer id) {
        return seOrderDao.get(id);
    }

    public List<SEOrderDO> list(Map<String, Object> map) {
        return seOrderDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return seOrderDao.count(map);
    }

    public int save(SEOrderDO order) {
        return seOrderDao.save(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public int audit(Map<String, Object> params) {
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus"));
        List<SEOrderDO> orderDOList = seOrderDao.list(ImmutableMap.of("billNos", MapUtil.get(params, "billNos", List.class)));
        //去除已经是审核（未审核）状态的订单
        List<SEOrderDO> orderDOList1 = orderDOList.stream()
                .filter(orderDO -> !auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //计算客户积分、生成收付款单
        if (CollectionUtils.isNotEmpty(orderDOList1)) {
            handleCostAndAudit(orderDOList1, auditStatus);
            handlePoint(orderDOList1, auditStatus);
            handleRPOrder(orderDOList1, auditStatus);
        }
        return seOrderDao.audit(params);
    }

    /**
     * 重新计算商品成本及审核订单
     */
    private int handleCostAndAudit(List<SEOrderDO> orderDOList, AuditStatus auditStatus) {
        for (SEOrderDO orderDO : orderDOList) {
            costAmountCalculator.calcSEBillCost(orderDO, auditStatus);
        }
        return 1;
    }

    /**
     * 客户积分
     */
    private void handlePoint(List<SEOrderDO> orderDOList, AuditStatus auditStatus) {
        List<DictDO> dictDOList = dictDao.list(ImmutableMap.of("type", "point_scale"));
        BigDecimal scale = CollectionUtils.isEmpty(dictDOList) ? BigDecimal.valueOf(0.1D) : NumberUtils.toBigDecimal(dictDOList.get(0).getValue());
        List<PointEntryDO> pointEntryDOList = Lists.newArrayList();
        for (SEOrderDO orderDO : orderDOList) {
            pointEntryDOList.add(SEOrderConverter.convertPointDO(orderDO, auditStatus, scale));
        }
        pointEntryDao.saveBatch(pointEntryDOList);
    }

    /**
     * 付款单入库
     */
    private int handleRPOrder(List<SEOrderDO> orderDOList, AuditStatus auditStatus) {
        if (AuditStatus.NO.equals(auditStatus)) return 0;
        for (SEOrderDO orderDO : orderDOList) {
            //是否存在手工创建的收付款单，若存在则系统不自动处理收付款单
            int countRP = rpOrderDao.countRP(ImmutableMap.of("billSource", BillSource.USER.name(), "srcBillNo", orderDO.getBillNo()));
            if (countRP <= 0) {
                String rpBillNo = "";
                List<RPOrderDO> rpOrderDOList = rpOrderDao.list(ImmutableMap.of("srcBillNo", orderDO.getBillNo()));
                if (rpOrderDOList.size() > 0) {
                    rpBillNo = rpOrderDOList.get(0).getBillNo();
                    rpOrderDao.delete(ImmutableMap.of("billNo", rpBillNo));
                    rpOrderEntryDao.delete(ImmutableMap.of("billNo", rpBillNo));
                    rpOrderSettleDao.delete(ImmutableMap.of("billNo", rpBillNo));
                }
                if (orderDO.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    continue; //本次付款为0不用生成收款单
                }
                List<AccountDO> accountDOList = accountDao.list(ImmutableMap.of("nos", ImmutableSet.of(orderDO.getSettleAccount())));
                RPOrderDO rpOrderDO = SEOrderConverter.convertRPOrder(orderDO);
                //源订单号要保留
                rpOrderDO.setBillNo(StrUtil.isEmpty(rpBillNo) ? rpOrderDO.getBillNo() : rpBillNo);
                List<RPOrderEntryDO> rpOrderEntryDOList = SEOrderConverter.convertRPOrderEntry(rpOrderDO, orderDO);
                List<RPOrderSettleDO> rpOrderSettleDOList = SEOrderConverter.convertRPOrderSettle(rpOrderDO, orderDO, RPOrderConverter.convertAccountMap(accountDOList));
                rpOrderDao.save(rpOrderDO);
                rpOrderEntryDao.saveBatch(rpOrderEntryDOList);
                rpOrderSettleDao.saveBatch(rpOrderSettleDOList);
            }
        }
        return 1;
    }

    public int remove(Integer id) {
        return seOrderDao.remove(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchRemove(List<String> billNos) {
        seOrderDao.delete(ImmutableMap.of("billNos", billNos));
        seOrderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }

}
