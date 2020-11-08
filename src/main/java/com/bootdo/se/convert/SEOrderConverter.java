package com.bootdo.se.convert;

import com.bootdo.common.config.Constant;
import com.bootdo.common.enumeration.*;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.OrderUtils;
import com.bootdo.common.utils.ShiroUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.data.domain.AccountDO;
import com.bootdo.data.domain.ConsumerDO;
import com.bootdo.data.domain.ProductCostDO;
import com.bootdo.data.domain.StockDO;
import com.bootdo.rp.domain.PointEntryDO;
import com.bootdo.rp.domain.RPOrderDO;
import com.bootdo.rp.domain.RPOrderEntryDO;
import com.bootdo.rp.domain.RPOrderSettleDO;
import com.bootdo.se.controller.request.SEOrderEntryVO;
import com.bootdo.se.controller.request.SEOrderVO;
import com.bootdo.se.domain.SEOrderDO;
import com.bootdo.se.domain.SEOrderEntryDO;
import com.bootdo.system.domain.UserDO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: yogiCai
 * @create: 2018-03-06 20:54
 **/
public class SEOrderConverter {

    public static List<SEOrderEntryDO> convertOrderEntry(SEOrderVO orderVO, SEOrderDO orderDO, Map<String, StockDO> stockDOMap, Map<String, ProductCostDO> costDOMap, Map<String, BigDecimal> purchaseMap) {
        List<SEOrderEntryDO> entryDOList = Lists.newArrayList();
        BigDecimal entryAmountTotal = BigDecimal.ZERO;
        BigDecimal costAmountTotal = BigDecimal.ZERO;
        BigDecimal qtyTotal = BigDecimal.ZERO;
        for (SEOrderEntryVO vo: orderVO.getEntryVOList()) {
            SEOrderEntryDO orderEntryDO = new SEOrderEntryDO();
            orderEntryDO.setBillNo(orderDO.getBillNo());
            orderEntryDO.setEntryId(vo.getEntryId());
            orderEntryDO.setEntryName(vo.getEntryName());
            orderEntryDO.setEntryUnit(vo.getEntryUnit());
            orderEntryDO.setEntryPrice(vo.getEntryPrice());
            orderEntryDO.setCostPrice(NumberUtils.toBigDecimal(costDOMap.get(vo.getEntryId()) != null ? costDOMap.get(vo.getEntryId()).getCostPrice() : purchaseMap.get(vo.getEntryId())));
            orderEntryDO.setStockNo(vo.getStockNo());
            orderEntryDO.setStockName(stockDOMap.get(vo.getStockNo()) != null ? stockDOMap.get(vo.getStockNo()).getStockName() : "");
            orderEntryDO.setTotalQty(vo.getTotalQty());
            orderEntryDO.setCostAmount(NumberUtils.mul(orderEntryDO.getCostPrice(), orderEntryDO.getTotalQty()));
            orderEntryDO.setEntryAmount(vo.getEntryAmount());
            orderEntryDO.setDiscountAmount(vo.getDiscountAmount());
            orderEntryDO.setDiscountRate(vo.getDiscountRate());
            orderEntryDO.setPurchaseFee(vo.getPurchaseFee());
            orderEntryDO.setTotalAmount(vo.getTotalAmount());
            orderEntryDO.setRemark(vo.getRemark());
            entryAmountTotal = entryAmountTotal.add(vo.getEntryAmount());
            costAmountTotal = NumberUtils.add(costAmountTotal, orderEntryDO.getCostAmount());
            qtyTotal = NumberUtils.add(qtyTotal, vo.getTotalQty());
            entryDOList.add(orderEntryDO);
        }
        orderDO.setTotalQty(qtyTotal);
        orderDO.setCostAmount(costAmountTotal);
        orderDO.setEntryAmount(entryAmountTotal);
        return entryDOList;
    }

    public static SEOrderDO convertOrder(SEOrderVO orderVO, UserDO userDO, ConsumerDO consumerDO) {
        SEOrderDO orderDO = new SEOrderDO();
        orderDO.setBillNo(StringUtil.isEmpty(orderVO.getBillNo()) ? OrderUtils.generateOrderNoXS() : orderVO.getBillNo());
        orderDO.setBillType(BillType.XS_ORDER.name());
        orderDO.setBillDate(orderVO.getBillDate());
        orderDO.setConsumerId(orderVO.getConsumerId());
        orderDO.setConsumerName(consumerDO.getName());
        orderDO.setEntryAmount(orderVO.getEntryAmountTotal());
        orderDO.setDiscountAmount(orderVO.getDiscountAmountTotal());
        orderDO.setDiscountRate(orderVO.getDiscountRateTotal());
        orderDO.setPurchaseFee(orderVO.getPurchaseFeeTotal());
        orderDO.setFinalAmount(orderVO.getFinalAmountTotal());
        orderDO.setPaymentAmount(orderVO.getPaymentAmountTotal());
        orderDO.setExpenseFee(orderVO.getExpenseFeeTotal());
        orderDO.setTotalAmount(NumberUtils.add(orderVO.getFinalAmountTotal(), orderVO.getExpenseFeeTotal()));
        orderDO.setStatus(BigDecimal.ZERO.equals(orderVO.getPaymentAmountTotal()) ? OrderStatus.WAITING_PAY.name() : (orderVO.getPaymentAmountTotal().equals(orderDO.getTotalAmount()) ? OrderStatus.FINISH_PAY.name(): OrderStatus.PART_PAY.name()));
        orderDO.setSettleAccount(orderVO.getSettleAccountTotal());
        orderDO.setBillerId(orderVO.getBillerId());
        orderDO.setBillerName(userDO.getName());
        orderDO.setAuditStatus(AuditStatus.NO.name());
        orderDO.setRemark(orderVO.getRemark());
        return orderDO;
    }

    public static List<RPOrderSettleDO> convertRPOrderSettle(RPOrderDO rpOrderDO, SEOrderDO orderDO, Map<String, AccountDO> accountDOMap) {
        List<RPOrderSettleDO> rpOrderSettleDOList = Lists.newArrayList();
        RPOrderSettleDO rpOrderSettleDO = new RPOrderSettleDO();
        rpOrderSettleDO.setBillNo(rpOrderDO.getBillNo());
        rpOrderSettleDO.setSettleAccount(orderDO.getSettleAccount());
        rpOrderSettleDO.setSettleName(accountDOMap.get(orderDO.getSettleAccount()).getName());
        rpOrderSettleDO.setPaymentAmount(orderDO.getPaymentAmount());
        rpOrderSettleDO.setRemark("");
        rpOrderSettleDOList.add(rpOrderSettleDO);
        return rpOrderSettleDOList;
    }

    public static List<RPOrderEntryDO> convertRPOrderEntry(RPOrderDO rpOrderDO, SEOrderDO orderDO) {
        List<RPOrderEntryDO> rpOrderEntryDOList = Lists.newArrayList();
        RPOrderEntryDO rpOrderEntryDO = new RPOrderEntryDO();
        rpOrderEntryDO.setBillNo(rpOrderDO.getBillNo());
        rpOrderEntryDO.setSrcBillDate(orderDO.getBillDate());
        rpOrderEntryDO.setSrcBillType(orderDO.getBillType());
        rpOrderEntryDO.setSrcBillNo(orderDO.getBillNo());
        rpOrderEntryDO.setSrcTotalAmount(orderDO.getTotalAmount());
        rpOrderEntryDO.setSrcPaymentAmount(BigDecimal.ZERO);
        rpOrderEntryDO.setCheckAmount(orderDO.getPaymentAmount());
        rpOrderEntryDOList.add(rpOrderEntryDO);
        return rpOrderEntryDOList;
    }

    public static RPOrderDO convertRPOrder(SEOrderDO orderDO) {
        RPOrderDO rpOrderDO = new RPOrderDO();
        rpOrderDO.setBillDate(orderDO.getBillDate());
        rpOrderDO.setBillNo(OrderUtils.generateOrderNoCW(BillType.CW_SK_ORDER));
        rpOrderDO.setBillType(BillType.CW_SK_ORDER.name());
        rpOrderDO.setDebtorId(orderDO.getConsumerId());
        rpOrderDO.setDebtorName(orderDO.getConsumerName());
        rpOrderDO.setCheckId(ShiroUtils.getUserId().toString());
        rpOrderDO.setCheckName(ShiroUtils.getUser().getName());
        rpOrderDO.setPaymentAmount(orderDO.getPaymentAmount());
        rpOrderDO.setCheckAmount(orderDO.getPaymentAmount());
        rpOrderDO.setDiscountAmount(BigDecimal.ZERO);
        rpOrderDO.setAuditStatus(AuditStatus.YES.name());
        rpOrderDO.setBillSource(BillSource.SYSTEM.name());
        rpOrderDO.setRemark(Constant.PO_RP_ORDER_REMARK);
        return rpOrderDO;
    }

    public static PointEntryDO convertPointDO(SEOrderDO orderDO, AuditStatus auditStatus, BigDecimal scale) {
        PointEntryDO pointEntryDO = new PointEntryDO();
        pointEntryDO.setConsumerId(orderDO.getConsumerId());
        pointEntryDO.setConsumerName(orderDO.getConsumerName());
        pointEntryDO.setSource(PointSource.ORDER.name());
        pointEntryDO.setStatus(PointStatus.NORMAL.name());
        pointEntryDO.setPoint(NumberUtils.mul(orderDO.getTotalAmount(), scale).multiply(AuditStatus.YES.equals(auditStatus) ? BigDecimal.ONE : BigDecimal.valueOf(-1)));
        pointEntryDO.setRemark(String.format(Constant.SE_RP_POINT_REMARK, AuditStatus.YES.equals(auditStatus) ? "审核" : "反审核"));
        pointEntryDO.setRelateNo(orderDO.getBillNo());
        pointEntryDO.setTotalAmount(orderDO.getTotalAmount());
        return pointEntryDO;
    }

    public static Map<String, SEOrderDO> convertOrderMap(List<SEOrderDO> list) {
        Map<String, SEOrderDO> result = Maps.newHashMap();
        for (SEOrderDO orderDO : list) {
            result.put(orderDO.getBillNo(), orderDO);
        }
        return result;
    }
}
