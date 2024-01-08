package com.bootdo.modular.po.convert;

import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillSource;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.core.utils.OrderUtils;
import com.bootdo.core.utils.ShiroUtils;
import com.bootdo.modular.data.domain.AccountDO;
import com.bootdo.modular.data.domain.StockDO;
import com.bootdo.modular.data.domain.VendorDO;
import com.bootdo.modular.po.param.OrderEntryVO;
import com.bootdo.modular.po.param.OrderVO;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.domain.OrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderDO;
import com.bootdo.modular.rp.domain.RPOrderEntryDO;
import com.bootdo.modular.rp.domain.RPOrderSettleDO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @create: 2018-03-06 20:54
 **/
public class OrderConverter {

    public static List<OrderEntryDO> convertOrderEntry(OrderVO orderVO, OrderDO orderDO, Map<String, StockDO> stockDOMap) {
        List<OrderEntryDO> entryDOList = Lists.newArrayList();
        BigDecimal entryAmountTotal = BigDecimal.ZERO;
        BigDecimal qtyTotal = BigDecimal.ZERO;
        for (OrderEntryVO vo: orderVO.getEntryVOList()) {
            OrderEntryDO orderEntryDO = new OrderEntryDO();
            orderEntryDO.setBillNo(orderDO.getBillNo());
            orderEntryDO.setEntryId(vo.getEntryId());
            orderEntryDO.setEntryName(vo.getEntryName());
            orderEntryDO.setEntryUnit(vo.getEntryUnit());
            orderEntryDO.setEntryPrice(vo.getEntryPrice());
            orderEntryDO.setStockNo(vo.getStockNo());
            orderEntryDO.setStockName(stockDOMap.get(vo.getStockNo()) != null ? stockDOMap.get(vo.getStockNo()).getStockName() : "");
            orderEntryDO.setTotalQty(vo.getTotalQty());
            orderEntryDO.setEntryAmount(vo.getEntryAmount());
            orderEntryDO.setDiscountAmount(vo.getDiscountAmount());
            orderEntryDO.setDiscountRate(vo.getDiscountRate());
            orderEntryDO.setPurchaseFee(vo.getPurchaseFee());
            orderEntryDO.setTotalAmount(vo.getTotalAmount());
            orderEntryDO.setRemark(vo.getRemark());
            entryAmountTotal = entryAmountTotal.add(vo.getEntryAmount());
            qtyTotal = NumberUtils.add(qtyTotal, vo.getTotalQty());
            entryDOList.add(orderEntryDO);
        }
        orderDO.setTotalQty(qtyTotal);
        orderDO.setEntryAmount(entryAmountTotal);
        return entryDOList;
    }

    public static OrderDO convertOrder(OrderVO orderVO, VendorDO vendorDO) {
        OrderDO orderDO = new OrderDO();
        orderDO.setBillNo(StrUtil.isEmpty(orderVO.getBillNo()) ? OrderUtils.generateOrderNoCG(orderVO.getBillType()) : orderVO.getBillNo());
        orderDO.setBillType(orderVO.getBillType());
        orderDO.setBillDate(orderVO.getBillDate());
        orderDO.setVendorId(orderVO.getVendorId());
        orderDO.setVendorName(vendorDO.getName());
        orderDO.setEntryAmount(orderVO.getEntryAmountTotal());
        orderDO.setDiscountAmount(orderVO.getDiscountAmountTotal());
        orderDO.setDiscountRate(orderVO.getDiscountRateTotal());
        orderDO.setPurchaseFee(orderVO.getPurchaseFeeTotal());
        orderDO.setFinalAmount(orderVO.getFinalAmountTotal());
        orderDO.setPaymentAmount(orderVO.getPaymentAmountTotal());
        orderDO.setTotalAmount(NumberUtils.add(orderVO.getFinalAmountTotal(), orderVO.getPurchaseFeeTotal()));
        orderDO.setStatus(OrderStatus.fromPayment(orderVO.getPaymentAmountTotal(), orderDO.getTotalAmount()));
        orderDO.setSettleAccount(orderVO.getSettleAccountTotal());
        orderDO.setBillerId(ShiroUtils.getUser().getUserId().toString());
        orderDO.setAuditStatus(AuditStatus.NO);
        orderDO.setRemark(orderVO.getRemark());
        return orderDO;
    }

    public static  List<RPOrderSettleDO> convertRPOrderSettle(RPOrderDO rpOrderDO, OrderDO orderDO, Map<String, AccountDO> accountDOMap) {
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

    public static  List<RPOrderEntryDO> convertRPOrderEntry(RPOrderDO rpOrderDO, OrderDO orderDO) {
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

    public static  RPOrderDO convertRPOrder(OrderDO orderDO) {
        BillType billType = BillType.CG_ORDER.equals(orderDO.getBillType()) ? BillType.CW_FK_ORDER : BillType.CW_SK_ORDER;
        RPOrderDO rpOrderDO = new RPOrderDO();
        rpOrderDO.setBillDate(orderDO.getBillDate());
        rpOrderDO.setBillNo(OrderUtils.generateOrderNoCW(billType));
        rpOrderDO.setBillType(billType);
        rpOrderDO.setDebtorId(orderDO.getVendorId());
        rpOrderDO.setDebtorName(orderDO.getVendorName());
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


    public static Map<String, OrderDO> convertOrderMap(List<OrderDO> list) {
        Map<String, OrderDO> result = Maps.newHashMap();
        for (OrderDO orderDO : list) {
            result.put(orderDO.getBillNo(), orderDO);
        }
        return result;
    }
}
