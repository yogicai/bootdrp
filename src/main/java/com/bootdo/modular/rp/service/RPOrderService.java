package com.bootdo.modular.rp.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.po.convert.OrderConverter;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.rp.dao.RPOrderDao;
import com.bootdo.modular.rp.dao.RPOrderEntryDao;
import com.bootdo.modular.rp.dao.RPOrderSettleDao;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.se.convert.SEOrderConverter;
import com.bootdo.modular.se.dao.SEOrderDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class RPOrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private SEOrderDao seOrderDao;
    @Resource
    private RPOrderDao rpOrderDao;
    @Resource
    private RPOrderEntryDao rpOrderEntryDao;
    @Resource
    private RPOrderSettleDao rpOrderSettleDao;

    private final EnumSet<BillType> poBillSet = EnumSet.of(BillType.CG_ORDER, BillType.TH_ORDER);
    private final EnumSet<BillType> seBillSet = EnumSet.of(BillType.XS_ORDER);

    public RPOrderDO get(Integer id) {
        return rpOrderDao.get(id);
    }

    public List<RPOrderDO> list(Map<String, Object> map) {
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

    public int count(Map<String, Object> map) {
        return rpOrderDao.count(map);
    }

    /**
     * 收付款财务单审核要更新，源订单的已付金额
     */
    @Transactional(rollbackFor = Exception.class)
    public int audit(Map<String, Object> params) {
        AuditStatus auditStatus = AuditStatus.fromValue(MapUtil.getStr(params, "auditStatus"));
        List<RPOrderDO> list = rpOrderDao.list(ImmutableMap.of("billNos", MapUtil.get(params, "billNos", List.class)));
        Set<String> billNoSet = Sets.newHashSet();
        //过滤已审核或未审核的订单
        for (RPOrderDO orderDO : list) {
            if (!auditStatus.equals(orderDO.getAuditStatus())) {
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
            BillType billType = entry.getValue().get(0).getSrcBillType();
            if (checkAmount.compareTo(BigDecimal.ZERO) != 0 && poBillSet.contains(billType)) {
                BigDecimal totalAmount = orderDOMap.get(billNo).getTotalAmount();
                BigDecimal paymentAmount = NumberUtils.add(orderDOMap.get(billNo).getPaymentAmount(), checkAmount);
                OrderStatus status = paymentAmount.compareTo(totalAmount) >= 0 ? OrderStatus.FINISH_PAY : (paymentAmount.compareTo(BigDecimal.ZERO) == 0 ? OrderStatus.WAITING_PAY : OrderStatus.PART_PAY);
                orderDao.update(ImmutableMap.of("cBillNo", billNo, "status", status.name(), "paymentAmount", paymentAmount));
            } else if (checkAmount.compareTo(BigDecimal.ZERO) != 0 && seBillSet.contains(billType)) {
                BigDecimal totalAmount = seOrderDOMap.get(billNo).getTotalAmount();
                BigDecimal paymentAmount = NumberUtils.add(seOrderDOMap.get(billNo).getPaymentAmount(), checkAmount);
                OrderStatus status = paymentAmount.compareTo(totalAmount) >= 0 ? OrderStatus.FINISH_PAY : (paymentAmount.compareTo(BigDecimal.ZERO) == 0 ? OrderStatus.WAITING_PAY : OrderStatus.PART_PAY);
                seOrderDao.update(ImmutableMap.of("cBillNo", billNo, "status", status.name(), "paymentAmount", paymentAmount));
            }
        }

        return rpOrderDao.audit(params);
    }

    @Transactional(rollbackFor = Exception.class)
    public int batchRemove(List<String> billNos) {
        rpOrderDao.delete(ImmutableMap.of("billNos", billNos));
        rpOrderEntryDao.delete(ImmutableMap.of("billNos", billNos));
        rpOrderSettleDao.delete(ImmutableMap.of("billNos", billNos));
        return 1;
    }
}
