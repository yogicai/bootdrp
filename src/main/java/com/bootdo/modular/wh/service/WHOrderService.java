package com.bootdo.modular.wh.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.modular.data.service.CostAmountCalculator;
import com.bootdo.modular.wh.dao.WHOrderDao;
import com.bootdo.modular.wh.dao.WHOrderEntryDao;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class WHOrderService {
    @Resource
    private WHOrderDao whOrderDao;
    @Resource
    private WHOrderEntryDao whOrderEntryDao;
    @Resource
    private CostAmountCalculator costAmountCalculator;

    public WHOrderDO get(Integer id) {
        return whOrderDao.get(id);
    }

    public List<WHOrderDO> list(Map<String, Object> map) {
        return whOrderDao.list(map);
    }

    public int count(Map<String, Object> map) {
        return whOrderDao.count(map);
    }

    public int save(WHOrderDO order) {
        return whOrderDao.save(order);
    }

    public int update(WHOrderDO order) {
        return whOrderDao.update(order);
    }

    public int remove(Integer id) {
        return whOrderDao.remove(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int audit(Map<String, Object> params) {
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus"));
        List<WHOrderDO> orderDOList = whOrderDao.list(ImmutableMap.of("billNos", MapUtil.get(params, "billNos", List.class)));
        //去除已经是审核（未审核）状态的订单
        List<WHOrderDO> orderDOList1 = orderDOList.stream()
                .filter(orderDO -> !auditStatus.equals(orderDO.getAuditStatus())).collect(Collectors.toList());
        //审核采购单重新计算商品成本
        if (CollectionUtils.isNotEmpty(orderDOList1)) {
            for (WHOrderDO orderDO : orderDOList1) {
                costAmountCalculator.calcWHBillCost(orderDO, auditStatus);
                whOrderDao.audit(ImmutableMap.of("billNo", orderDO.getBillNo(), "auditStatus", auditStatus.name()));
            }
        }
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchRemove(List<String> billNos) {
        whOrderDao.delete(ImmutableMap.of("billNos", billNos));
        whOrderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }

}
