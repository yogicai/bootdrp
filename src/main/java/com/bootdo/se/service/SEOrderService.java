package com.bootdo.se.service;

import com.bootdo.common.dao.DictDao;
import com.bootdo.common.domain.DictDO;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.BillSource;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.data.dao.AccountDao;
import com.bootdo.data.domain.AccountDO;
import com.bootdo.data.service.CostAmountCalculator;
import com.bootdo.rp.convert.RPOrderConverter;
import com.bootdo.rp.dao.PointEntryDao;
import com.bootdo.rp.dao.RPOrderDao;
import com.bootdo.rp.dao.RPOrderEntryDao;
import com.bootdo.rp.dao.RPOrderSettleDao;
import com.bootdo.rp.domain.PointEntryDO;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.rp.domain.RPOrderSettleDO;
import com.bootdo.se.convert.SEOrderConverter;
import com.bootdo.se.dao.SEOrderDao;
import com.bootdo.se.dao.SEOrderEntryDao;
import com.bootdo.se.domain.SEOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SEOrderService {
    @Autowired
    private SEOrderDao orderDao;
    @Autowired
    private SEOrderEntryDao orderEntryDao;
    @Autowired
    private PointEntryDao pointEntryDao;
    @Autowired
    private DictDao dictDao;
    @Autowired
    private RPOrderDao rpOrderDao;
    @Autowired
    private RPOrderEntryDao rpOrderEntryDao;
    @Autowired
    private RPOrderSettleDao rpOrderSettleDao;
    @Autowired
    private AccountDao accountDao;
    @Resource
    private CostAmountCalculator costAmountCalculator;


    public SEOrderDO get(Integer id) {
        return orderDao.get(id);
    }

    public List<SEOrderDO> list(Map<String, Object> map) {
        return orderDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return orderDao.count(map);
    }

    public int save(SEOrderDO order) {
        return orderDao.save(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public int audit(Map<String, Object> params) {
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtils.getString(params, "auditStatus"));
        List<SEOrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", MapUtils.getList(params, "billNos")));
        //去除已经是审核（未审核）状态的订单
        List<SEOrderDO> orderDOList1 = orderDOList.stream()
                .filter(orderDO ->!auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //计算客户积分、生成收付款单
        if (CollectionUtils.isNotEmpty(orderDOList1)) {
            handleCostAndAudit(orderDOList1, auditStatus);
            handlePoint(orderDOList1, auditStatus);
            handleRPOrder(orderDOList1, auditStatus);
        }
        return orderDao.audit(params);
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
                rpOrderDO.setBillNo(StringUtil.isEmpty(rpBillNo) ? rpOrderDO.getBillNo() : rpBillNo);
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
        return orderDao.remove(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchRemove(List<String> billNos) {
        orderDao.delete(ImmutableMap.of("billNos", billNos));
        orderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }

}
