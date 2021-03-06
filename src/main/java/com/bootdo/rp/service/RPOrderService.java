package com.bootdo.rp.service;

import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.BillType;
import com.bootdo.common.enumeration.OrderStatus;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.po.convert.OrderConverter;
import com.bootdo.po.dao.OrderDao;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.rp.dao.RPOrderDao;
import com.bootdo.rp.dao.RPOrderEntryDao;
import com.bootdo.rp.dao.RPOrderSettleDao;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.rp.domain.RPOrderSettleDO;
import com.bootdo.se.convert.SEOrderConverter;
import com.bootdo.se.dao.SEOrderDao;
import com.bootdo.se.domain.SEOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class RPOrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SEOrderDao seOrderDao;
	@Autowired
	private RPOrderDao rpOrderDao;
    @Autowired
    private RPOrderEntryDao rpOrderEntryDao;
    @Autowired
    private RPOrderSettleDao rpOrderSettleDao;

    private final Set<String> poBillSet = Sets.newHashSet(BillType.CG_ORDER.name(), BillType.TH_ORDER.name());
    private final Set<String> seBillSet = Sets.newHashSet(BillType.XS_ORDER.name());
	
	public RPOrderDO get(Integer id){
		return rpOrderDao.get(id);
	}
	
	public List<RPOrderDO> list(Map<String, Object> map){
        List<RPOrderDO> rpOrderDOList = rpOrderDao.list(map);
        Set<String> billNoSet = Sets.newHashSet();
        for (RPOrderDO orderDO : rpOrderDOList) {
            billNoSet.add(orderDO.getBillNo());
        }
        List<RPOrderEntryDO> rpOrderEntryDOList = rpOrderEntryDao.list(ImmutableMap.of("billNos", billNoSet));
        List<RPOrderSettleDO> rpOrderSettleDOList = rpOrderSettleDao.list(ImmutableMap.of("billNos", billNoSet));

        for (RPOrderDO orderDO : rpOrderDOList) {
            for (RPOrderEntryDO entryDO : rpOrderEntryDOList) {
                if (orderDO.getBillNo().equals(entryDO.getBillNo())) {
                    orderDO.getEntryDOList().add(entryDO);
                }
            }
            for (RPOrderSettleDO settleDO : rpOrderSettleDOList) {
                if (orderDO.getBillNo().equals(settleDO.getBillNo())) {
                    orderDO.getSettleDOList().add(settleDO);
                }
            }
        }
		return rpOrderDOList;
	}
	
	public int count(Map<String, Object> map){
        return rpOrderDao.count(map);
	}

    /**
     * 收付款财务单审核要更新，源订单的已付金额
     */
    @Transactional
    public int audit(Map<String, Object> params){
        AuditStatus auditStatus = AuditStatus.fromValue(org.apache.commons.collections.MapUtils.getString(params, "auditStatus"));
        List<RPOrderDO> list = rpOrderDao.list(ImmutableMap.of("billNos", MapUtils.getList(params, "billNos")));
        Set<String> billNoSet = Sets.newHashSet();
        //过滤已审核或未审核的订单
        for (RPOrderDO orderDO : list) {
            if (!auditStatus.equals(AuditStatus.fromValue(orderDO.getAuditStatus()))) {
                billNoSet.add(orderDO.getBillNo());
            }
        }
        if (billNoSet.size() <= 0) return 0;

        //按源订单号归类订单分录，计算本次需更新的源订单支付金额
        Map<String, List<RPOrderEntryDO>> listMap = Maps.newHashMap();
        Set<String> poBillNoSet = Sets.newHashSet(), seBillNoSet = Sets.newHashSet();
        List<RPOrderEntryDO> list1 = rpOrderEntryDao.list(ImmutableMap.of("billNos", billNoSet));
        for (RPOrderEntryDO entryDO : list1) {
            if (!listMap.containsKey(entryDO.getSrcBillNo())) {
                listMap.put(entryDO.getSrcBillNo(), Lists.newArrayList());
            }
            listMap.get(entryDO.getSrcBillNo()).add(entryDO);
            if (poBillSet.contains(entryDO.getSrcBillType())) {
                poBillNoSet.add(entryDO.getSrcBillNo());
            } else if (seBillSet.contains(entryDO.getSrcBillType())) {
                seBillNoSet.add(entryDO.getSrcBillNo());
            }
        }
        //更新源订单已付金额
        List<OrderDO> orderDOList = orderDao.list(ImmutableMap.of("billNos", poBillNoSet));
        List<SEOrderDO> seOrderDOList = seOrderDao.list(ImmutableMap.of("billNos", seBillNoSet));
        Map<String, OrderDO> orderDOMap = OrderConverter.convertOrderMap(orderDOList);
        Map<String, SEOrderDO> seOrderDOMap = SEOrderConverter.convertOrderMap(seOrderDOList);
        for (Map.Entry<String, List<RPOrderEntryDO>> entry : listMap.entrySet()) {
            BigDecimal checkAmount = BigDecimal.ZERO;
            for (RPOrderEntryDO entryDO : entry.getValue()) {
                checkAmount = NumberUtils.add(checkAmount, NumberUtils.mul(entryDO.getCheckAmount(), AuditStatus.YES.equals(auditStatus) ? BigDecimal.ONE : BigDecimal.valueOf(-1)));
            }
            String billNo = entry.getValue().get(0).getSrcBillNo();
            String billType = entry.getValue().get(0).getSrcBillType();
            if (checkAmount.compareTo(BigDecimal.ZERO) != 0 && poBillSet.contains(billType)) {
                BigDecimal totalAmount = orderDOMap.get(billNo).getTotalAmount();
                BigDecimal paymentAmount = NumberUtils.add(orderDOMap.get(billNo).getPaymentAmount(), checkAmount);
                OrderStatus status = paymentAmount.compareTo(totalAmount) >= 0 ? OrderStatus.FINISH_PAY : (paymentAmount.compareTo(BigDecimal.ZERO)==0 ? OrderStatus.WAITING_PAY : OrderStatus.PART_PAY);
                orderDao.update(ImmutableMap.of("cBillNo", billNo, "status", status.name(), "paymentAmount", paymentAmount));
            } else if (checkAmount.compareTo(BigDecimal.ZERO) != 0 && seBillSet.contains(billType)) {
                BigDecimal totalAmount = seOrderDOMap.get(billNo).getTotalAmount();
                BigDecimal paymentAmount = NumberUtils.add(seOrderDOMap.get(billNo).getPaymentAmount(), checkAmount);
                OrderStatus status = paymentAmount.compareTo(totalAmount) >= 0 ? OrderStatus.FINISH_PAY : (paymentAmount.compareTo(BigDecimal.ZERO)==0 ? OrderStatus.WAITING_PAY : OrderStatus.PART_PAY);
                seOrderDao.update(ImmutableMap.of("cBillNo", billNo, "status", status.name(), "paymentAmount", paymentAmount));
            }
        }

        return rpOrderDao.audit(params);
    }

    @Transactional
    public int batchRemove(List<String> billNos){
        rpOrderDao.delete(ImmutableMap.of("billNos", billNos));
        rpOrderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        rpOrderSettleDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }
}
