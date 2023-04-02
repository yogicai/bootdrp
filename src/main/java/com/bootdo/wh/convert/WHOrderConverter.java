package com.bootdo.wh.convert;

import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.OrderUtils;
import com.bootdo.common.utils.ShiroUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.data.domain.ConsumerDO;
import com.bootdo.data.domain.StockDO;
import com.bootdo.engage.domain.ProductCostDO;
import com.bootdo.wh.controller.request.WHOrderEntryVO;
import com.bootdo.wh.controller.request.WHOrderVO;
import com.bootdo.wh.domain.WHOrderDO;
import com.bootdo.wh.domain.WHOrderEntryDO;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @data 2018-03-06 20:54
 **/
public class WHOrderConverter {

    public static List<WHOrderEntryDO> convertOrderEntry(WHOrderVO orderVO, WHOrderDO orderDO, Map<String, StockDO> stockDOMap, Map<String, ProductCostDO> costDOMap) {
        List<WHOrderEntryDO> entryDOList = Lists.newArrayList();
        BigDecimal entryAmountTotal = BigDecimal.ZERO;
        BigDecimal qtyTotal = BigDecimal.ZERO;
        for (WHOrderEntryVO vo: orderVO.getEntryVOList()) {
            WHOrderEntryDO orderEntryDO = new WHOrderEntryDO();
            orderEntryDO.setBillNo(orderDO.getBillNo());
            orderEntryDO.setEntryId(vo.getEntryId());
            orderEntryDO.setEntryName(vo.getEntryName());
            orderEntryDO.setEntryUnit(vo.getEntryUnit());
            orderEntryDO.setEntryPrice(vo.getEntryPrice());
            orderEntryDO.setCostPrice(NumberUtils.toBigDecimal(costDOMap.get(vo.getEntryId()) != null ? costDOMap.get(vo.getEntryId()).getCostPrice() : BigDecimal.ZERO));
            orderEntryDO.setStockNo(vo.getStockNo());
            orderEntryDO.setStockName(stockDOMap.get(vo.getStockNo()) != null ? stockDOMap.get(vo.getStockNo()).getStockName() : "");
            orderEntryDO.setTotalQty(vo.getTotalQty());
            orderEntryDO.setEntryAmount(vo.getEntryAmount());

            entryAmountTotal = entryAmountTotal.add(vo.getEntryAmount());
            qtyTotal = NumberUtils.add(qtyTotal, vo.getTotalQty());
            entryDOList.add(orderEntryDO);
        }
        orderDO.setTotalQty(qtyTotal);
        orderDO.setEntryAmount(entryAmountTotal);
        return entryDOList;
        }

    public static WHOrderDO convertOrder(WHOrderVO orderVO, ConsumerDO consumerDO) {
        WHOrderDO orderDO = new WHOrderDO();
        orderDO.setBillNo(StringUtil.isEmpty(orderVO.getBillNo()) ? OrderUtils.generateOrderNoWH(orderVO.getBillType()) : orderVO.getBillNo());
        orderDO.setBillType(orderVO.getBillType());
        orderDO.setServiceType(orderVO.getServiceType());
        orderDO.setBillDate(orderVO.getBillDate());
        orderDO.setDebtorId(orderVO.getDebtorId());
        orderDO.setDebtorName(consumerDO.getName());
        orderDO.setAuditStatus(AuditStatus.NO);
        orderDO.setRemark(orderVO.getRemark());
        orderDO.setOperatorId(ShiroUtils.getUser().getUserId().toString());
        orderDO.setOperatorName(ShiroUtils.getUser().getUsername());
        return orderDO;
    }
}
