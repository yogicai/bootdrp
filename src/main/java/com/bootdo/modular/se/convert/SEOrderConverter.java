package com.bootdo.modular.se.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.*;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.core.utils.OrderUtils;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.domain.ConsumerDO;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.rp.domain.PointEntryDO;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.domain.SEOrderEntryDO;
import com.bootdo.modular.se.param.SEOrderEntryVO;
import com.bootdo.modular.se.param.SEOrderVO;
import com.bootdo.modular.system.domain.UserDO;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @since 2018-03-06 20:54
 **/
public class SEOrderConverter {

    public static List<SEOrderEntryDO> convertOrderEntry(SEOrderVO orderVO, SEOrderDO orderDO, Map<String, StockDO> stockDOMap, Map<String, ProductCostDO> costDOMap, Map<String, BigDecimal> purchaseMap) {
        List<SEOrderEntryDO> entryDOList = Lists.newArrayList();
        BigDecimal entryAmountTotal = BigDecimal.ZERO;
        BigDecimal costAmountTotal = BigDecimal.ZERO;
        BigDecimal qtyTotal = BigDecimal.ZERO;
        int sort = 1;
        for (SEOrderEntryVO vo : orderVO.getEntryVOList()) {
            SEOrderEntryDO orderEntryDO = new SEOrderEntryDO();
            orderEntryDO.setShopNo(orderDO.getShopNo());
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
            orderEntryDO.setSort(sort++);
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
        orderDO.setShopNo(orderVO.getShopNo());
        orderDO.setBillNo(StrUtil.isEmpty(orderVO.getBillNo()) ? OrderUtils.generateOrderNoXS() : orderVO.getBillNo());
        orderDO.setBillType(BillType.XS_ORDER);
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
        orderDO.setStatus(OrderStatus.fromPayment(orderVO.getPaymentAmountTotal(), orderDO.getTotalAmount()));
        orderDO.setSettleAccount(orderVO.getSettleAccountTotal());
        orderDO.setBillerId(orderVO.getBillerId());
        orderDO.setBillerName(userDO.getName());
        orderDO.setBillSource(ObjUtil.defaultIfNull(orderVO.getBillSource(), BillSource.USER));
        orderDO.setAuditStatus(AuditStatus.NO);
        orderDO.setRemark(orderVO.getRemark());
        return orderDO;
    }

    public static List<RPOrderSettleDO> convertRPOrderSettle(RPOrderDO rpOrderDO, SEOrderDO orderDO, AccountDO accountDO) {
        RPOrderSettleDO rpOrderSettleDO = new RPOrderSettleDO();
        rpOrderSettleDO.setShopNo(rpOrderDO.getShopNo());
        rpOrderSettleDO.setBillNo(rpOrderDO.getBillNo());
        rpOrderSettleDO.setSettleAccount(orderDO.getSettleAccount());
        rpOrderSettleDO.setSettleName(accountDO.getName());
        rpOrderSettleDO.setPaymentAmount(orderDO.getPaymentAmount());
        rpOrderSettleDO.setRemark("");
        return CollUtil.newArrayList(rpOrderSettleDO);
    }

    public static List<RPOrderEntryDO> convertRPOrderEntry(RPOrderDO rpOrderDO, SEOrderDO orderDO) {
        RPOrderEntryDO rpOrderEntryDO = new RPOrderEntryDO();
        rpOrderEntryDO.setShopNo(rpOrderDO.getShopNo());
        rpOrderEntryDO.setBillNo(rpOrderDO.getBillNo());
        rpOrderEntryDO.setSrcBillDate(orderDO.getBillDate());
        rpOrderEntryDO.setSrcBillType(orderDO.getBillType());
        rpOrderEntryDO.setSrcBillNo(orderDO.getBillNo());
        rpOrderEntryDO.setSrcTotalAmount(orderDO.getTotalAmount());
        rpOrderEntryDO.setSrcPaymentAmount(BigDecimal.ZERO);
        rpOrderEntryDO.setCheckAmount(orderDO.getPaymentAmount());
        return CollUtil.newArrayList(rpOrderEntryDO);
    }

    public static RPOrderDO convertRPOrder(SEOrderDO orderDO) {
        RPOrderDO rpOrderDO = new RPOrderDO();
        rpOrderDO.setBillDate(orderDO.getBillDate());
        rpOrderDO.setShopNo(orderDO.getShopNo());
        rpOrderDO.setBillNo(OrderUtils.generateOrderNoCW(BillType.CW_SK_ORDER));
        rpOrderDO.setBillType(BillType.CW_SK_ORDER);
        rpOrderDO.setDebtorId(orderDO.getConsumerId());
        rpOrderDO.setDebtorName(orderDO.getConsumerName());
        rpOrderDO.setCheckId(ShiroUtils.getUserId().toString());
        rpOrderDO.setCheckName(ShiroUtils.getUser().getName());
        rpOrderDO.setPaymentAmount(orderDO.getPaymentAmount());
        rpOrderDO.setCheckAmount(orderDO.getPaymentAmount());
        rpOrderDO.setDiscountAmount(BigDecimal.ZERO);
        rpOrderDO.setAuditStatus(AuditStatus.YES);
        rpOrderDO.setBillSource(BillSource.SYSTEM);
        rpOrderDO.setRemark(Constant.PO_RP_ORDER_REMARK);
        return rpOrderDO;
    }

    public static PointEntryDO convertPointDO(SEOrderDO orderDO, AuditStatus auditStatus, BigDecimal scale) {
        PointEntryDO pointEntryDO = new PointEntryDO();
        pointEntryDO.setShopNo(orderDO.getShopNo());
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

}
