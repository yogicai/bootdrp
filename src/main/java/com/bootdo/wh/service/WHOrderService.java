package com.bootdo.wh.service;

import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.data.service.CostAmountCalculator;
import com.bootdo.wh.dao.WHOrderDao;
import com.bootdo.wh.dao.WHOrderEntryDao;
import com.bootdo.wh.domain.WHOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class WHOrderService {
	@Autowired
	private WHOrderDao orderDao;
    @Autowired
    private WHOrderEntryDao orderEntryDao;
    @Autowired
    private CostAmountCalculator costAmountCalculator;
	
	public WHOrderDO get(Integer id){
		return orderDao.get(id);
	}
	
	public List<WHOrderDO> list(Map<String, Object> map){
		return orderDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return orderDao.count(map);
	}
	
	public int save(WHOrderDO order){
		return orderDao.save(order);
	}
	
	public int update(WHOrderDO order){
		return orderDao.update(order);
	}
	
	public int remove(Integer id){
		return orderDao.remove(id);
	}

	@Transactional
    public int audit(Map<String, Object> params){
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtils.getString(params, "auditStatus"));
        List<WHOrderDO>  orderDOList = orderDao.list(ImmutableMap.of("billNos", MapUtils.getObject(params, "billNos")));
        List<WHOrderDO> orderDOList1 = Lists.newArrayList();
        //去除已经是审核（未审核）状态的订单
        for (WHOrderDO orderDO : orderDOList) {
            if (!auditStatus.name().equals(orderDO.getAuditStatus())) {
                orderDOList1.add(orderDO);
            }
        }
        //审核采购单重新计算商品成本
        if (CollectionUtils.isNotEmpty(orderDOList1)) {
            for (WHOrderDO orderDO : orderDOList1) {
                costAmountCalculator.calcWHBillCost(orderDO, auditStatus);
                orderDao.audit(ImmutableMap.of("billNo", orderDO.getBillNo(), "auditStatus", auditStatus.name()));
            }
        }
        return 1;
    }

    @Transactional
    public int batchRemove(List<String> billNos){
        orderDao.delete(ImmutableMap.of("billNos", billNos));
        orderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }
	
}
