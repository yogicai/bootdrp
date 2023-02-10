package com.bootdo.data.service;

import com.bootdo.common.config.Constant;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.enumeration.BillType;
import com.bootdo.common.enumeration.CostType;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.data.dao.ProductDao;
import com.bootdo.data.domain.ProductDO;
import com.bootdo.engage.dao.ProductCostDao;
import com.bootdo.engage.domain.ProductCostDO;
import com.bootdo.po.dao.OrderEntryDao;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.po.domain.OrderEntryDO;
import com.bootdo.engage.dao.ProductBalanceDao;
import com.bootdo.wh.dao.WHOrderEntryDao;
import com.bootdo.wh.domain.WHOrderDO;
import com.bootdo.wh.domain.WHOrderEntryDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Service
public class CostAmountCalculator {
    @Autowired
    private ProductCostDao productCostDao;
    @Autowired
    private OrderEntryDao orderEntryDao;
    @Autowired
    private WHOrderEntryDao whOrderEntryDao;
    @Autowired
    private ProductBalanceDao whReportDao;
    @Autowired
    private ProductDao productDao;


    private final EnumSet<BillType> incBillTypeSet = EnumSet.of(BillType.CG_ORDER, BillType.WH_RK_ORDER);
    private final EnumSet<BillType> desBillTypeSet = EnumSet.of(BillType.TH_ORDER, BillType.WH_CK_ORDER);

    /**
     * 采购单审核计算成本信息，退货单
     */
    public CostAmountIResult calcPOBillCost(OrderDO orderDO, AuditStatus auditStatus) {
        List<String> entryNoList = Lists.newArrayList();
        CostType costType = BillType.CG_ORDER.equals(orderDO.getBillType()) ? CostType.PO_CG : CostType.PO_TH;
        List<OrderEntryDO>  entryDOList = orderEntryDao.list(ImmutableMap.of("billNo", orderDO.getBillNo()));
        for (OrderEntryDO entry : entryDOList) {
            entryNoList.add(entry.getEntryId());
        }
        //商品采购价信息
        Map<String, BigDecimal> purchaseMap = convertPurchase(entryNoList);
        //查询商品成本单价(最近那个记录)
        Map<String, ProductCostDO> costDOMap = convertProductCost(entryNoList);
        //计算商品库存信息
        Map<String, BigDecimal> inventoryMap = convertInventoryMap(entryNoList);
        //订单采购费用均摊到分录 + 商品采购金额
        Map<String, BigDecimal> entryAmountFee = convertAmountFeeMap(orderDO, entryDOList);
        //计算商品单价成本
        CostAmountIResult detail = new CostAmountIResult();
        List<ProductCostDO> productCostDOList = Lists.newArrayList();
        for (OrderEntryDO entry : entryDOList) {
            /**
             * CG_ORDER     WH_RK_ORDER    +   AUDIT       1 ：库存增加
             * CG_ORDER     WH_RK_ORDER    +   UNAUDIT    -1 ：库存减少
             * TH_ORDER     WH_CK_ORDER	   +   AUDIT      -1 ：库存减少
             * TH_ORDER     WH_CK_ORDER	   +   UNAUDIT     1 ：库存增加
             */
            BigDecimal mulFactor = (incBillTypeSet.contains(orderDO.getBillType()) && AuditStatus.YES.equals(auditStatus)
                    || desBillTypeSet.contains(orderDO.getBillType()) && AuditStatus.NO.equals(auditStatus) )? BigDecimal.ONE : BigDecimal.valueOf(-1);
            BigDecimal inventory = NumberUtils.toBigDecimal(inventoryMap.get(entry.getEntryId())); //库存数量
            BigDecimal inventoryC = inventory.add(NumberUtils.mul(NumberUtils.toBigDecimal(entry.getTotalQty()), mulFactor)); //库存数量 +　本次商品数量

            ProductCostDO productCostDO = new ProductCostDO();

            //退货单不影响单位成本，但要重新计算商品总成本及库存数量；没有历史单位成本的商品，单位成本价取采购价
            if (BillType.TH_ORDER.equals(orderDO.getBillType())) {
                BigDecimal costPrice = costDOMap.containsKey(entry.getEntryId()) ? NumberUtils.toBigDecimal(costDOMap.get(entry.getEntryId()).getCostPrice()) : purchaseMap.get(entry.getEntryId());
                productCostDO.setProductNo(entry.getEntryId());
                productCostDO.setEntryPrice(entry.getEntryPrice());
                productCostDO.setCostAmount(NumberUtils.mul(costPrice, inventoryC));
                productCostDO.setCostQty(inventoryC);
                productCostDO.setCostBalance(inventory);
                productCostDO.setCostPrice(costPrice);
                productCostDO.setCostDate(DateUtils.nowDate());
                productCostDO.setCostType(costType.name());
                productCostDO.setRelateNo(orderDO.getBillNo());
                productCostDO.setRemark(String.format(Constant.COST_REMARK, costType.getRemark(), auditStatus.getRemark1()));

                detail.getCostMap().put(entry.getEntryId(), productCostDO);
                productCostDOList.add(productCostDO);
                continue;
            }

            if (AuditStatus.YES.equals(auditStatus)) {
                //当前库存小于等于0 || 历史库存小于等于0 && 当前库存大于0，则取当前这一单的数据 计算库存成本、单价成本
                if (inventoryC.compareTo(BigDecimal.ZERO) <= 0 || inventory.compareTo(BigDecimal.ZERO) <= 0 && inventoryC.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal amountFee = NumberUtils.toBigDecimal(entryAmountFee.get(entry.getEntryId())); //本次商品均摊费用 +　本次商品金额
                    BigDecimal costPrice = NumberUtils.div(amountFee, NumberUtils.toBigDecimal(entry.getTotalQty()), 6); //库存单位成本

                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setCostAmount(NumberUtils.mul(costPrice, inventoryC));
                    productCostDO.setCostQty(inventoryC);
                    productCostDO.setCostBalance(inventory);
                    productCostDO.setCostPrice(costPrice);
                    productCostDO.setCostDate(DateUtils.nowDate());
                    productCostDO.setCostType(costType.name());
                    productCostDO.setRelateNo(orderDO.getBillNo());
                    productCostDO.setRemark(String.format(Constant.COST_REMARK, costType.getRemark(), auditStatus.getRemark1()));
                } else if (inventory.compareTo(BigDecimal.ZERO) > 0) {
                    //历史库存大于0 && 当前库存大于0则，取当前这一单的数据+ 历史库存成本 计算库存成本、单价成本
                    BigDecimal costPrice = costDOMap.containsKey(entry.getEntryId()) ? NumberUtils.toBigDecimal(costDOMap.get(entry.getEntryId()).getCostPrice()) : BigDecimal.ZERO; //库存单位成本
                    BigDecimal amountFee = NumberUtils.toBigDecimal(entryAmountFee.get(entry.getEntryId())); //本次商品均摊费用 +　本次商品金额
                    BigDecimal amountFeeCost = NumberUtils.mul(costPrice, inventory).add(NumberUtils.mul(amountFee, mulFactor)); //库存成本 +　本次费用　+ 本次商品金额

                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setCostAmount(amountFeeCost);
                    productCostDO.setCostQty(inventoryC);
                    productCostDO.setCostBalance(inventory);
                    productCostDO.setCostPrice(NumberUtils.div(amountFeeCost, inventoryC, 6));
                    productCostDO.setCostDate(DateUtils.nowDate());
                    productCostDO.setCostType(costType.name());
                    productCostDO.setRelateNo(orderDO.getBillNo());
                    productCostDO.setRemark(String.format(Constant.COST_REMARK, costType.getRemark(), auditStatus.getRemark1()));
                }
            } else {
                //当前库存小于等于0则，库存成本、单价成本 都为0（已经没有库存了审核可能是因为采购单维护错误，所以不用上次计算出来的历史成本，用采购价）
                if (inventoryC.compareTo(BigDecimal.ZERO) <= 0) {
                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setCostAmount(NumberUtils.mul(purchaseMap.get(entry.getEntryId()), inventoryC));
                    productCostDO.setCostQty(inventoryC);
                    productCostDO.setCostBalance(inventory);
                    productCostDO.setCostPrice(purchaseMap.get(entry.getEntryId()));
                    productCostDO.setCostDate(DateUtils.nowDate());
                    productCostDO.setCostType(costType.name());
                    productCostDO.setRelateNo(orderDO.getBillNo());
                    productCostDO.setRemark(String.format(Constant.COST_REMARK, costType.getRemark(), auditStatus.getRemark1()));
                } else if (inventory.compareTo(BigDecimal.ZERO) > 0) {
                    //历史库存大于0 && 当前库存大于0则，取当前这一单的数据+ 历史库存成本 计算库存成本、单价成本
                    BigDecimal costPrice = costDOMap.containsKey(entry.getEntryId()) ? NumberUtils.toBigDecimal(costDOMap.get(entry.getEntryId()).getCostPrice()) : BigDecimal.ZERO; //库存单位成本
                    BigDecimal amountFee = NumberUtils.toBigDecimal(entryAmountFee.get(entry.getEntryId())); //本次商品均摊费用 +　本次商品金额
                    BigDecimal amountFeeCost = NumberUtils.mul(costPrice, inventory).add(NumberUtils.mul(amountFee, mulFactor)); //库存成本 +　本次费用　+ 本次商品金额

                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setCostAmount(amountFeeCost);
                    productCostDO.setCostQty(inventoryC);
                    productCostDO.setCostBalance(inventory);
                    productCostDO.setCostPrice(NumberUtils.div(amountFeeCost, inventoryC, 6));
                    productCostDO.setCostDate(DateUtils.nowDate());
                    productCostDO.setCostType(costType.name());
                    productCostDO.setRelateNo(orderDO.getBillNo());
                    productCostDO.setRemark(String.format(Constant.COST_REMARK, costType.getRemark(), auditStatus.getRemark1()));
                }
            }
            detail.getCostMap().put(entry.getEntryId(), productCostDO);
            productCostDOList.add(productCostDO);
        }
        productCostDao.saveBatch(productCostDOList);
        return detail;
    }

    public CostAmountIResult calcWHBillCost(WHOrderDO orderDO, AuditStatus auditStatus) {
        List<String> entryNoList = Lists.newArrayList();
        CostType costType = BillType.WH_RK_ORDER.equals(orderDO.getBillType()) ? CostType.WH_RK : CostType.WH_CK;
        List<WHOrderEntryDO>  entryDOList = whOrderEntryDao.list(ImmutableMap.of("billNo", orderDO.getBillNo()));
        for (WHOrderEntryDO entry : entryDOList) {
            entryNoList.add(entry.getEntryId());
        }
        //商品采购价信息
        Map<String, BigDecimal> purchaseMap = convertPurchase(entryNoList);
        //查询商品成本单价(最近那个记录)
        Map<String, ProductCostDO> costDOMap = convertProductCost(entryNoList);
        //计算商品库存信息
        Map<String, BigDecimal> inventoryMap = convertInventoryMap(entryNoList);

        //计算商品单价成本
        CostAmountIResult detail = new CostAmountIResult();
        List<ProductCostDO> productCostDOList = Lists.newArrayList();
        for (WHOrderEntryDO entry : entryDOList) {
            /**
             * CG_ORDER     WH_RK_ORDER    +   AUDIT       1 ：库存增加
             * CG_ORDER     WH_RK_ORDER    +   UNAUDIT    -1 ：库存减少
             * TH_ORDER     WH_CK_ORDER	   +   AUDIT      -1 ：库存减少
             * TH_ORDER     WH_CK_ORDER	   +   UNAUDIT     1 ：库存增加
             */
            BigDecimal mulFactor = (incBillTypeSet.contains(orderDO.getBillType()) && AuditStatus.YES.equals(auditStatus)
                    || desBillTypeSet.contains(orderDO.getBillType()) && AuditStatus.NO.equals(auditStatus) )? BigDecimal.ONE : BigDecimal.valueOf(-1);
            BigDecimal inventory = NumberUtils.toBigDecimal(inventoryMap.get(entry.getEntryId())); //库存数量
            BigDecimal inventoryC = inventory.add(NumberUtils.mul(NumberUtils.toBigDecimal(entry.getTotalQty()), mulFactor)); //库存数量 +　本次商品数量
            BigDecimal costPrice = costDOMap.containsKey(entry.getEntryId()) ? NumberUtils.toBigDecimal(costDOMap.get(entry.getEntryId()).getCostPrice()) : purchaseMap.get(entry.getEntryId());

            //仓库调整库存不影响单位成本，但要重新计算商品总成本及库存数量（因为仓库调整单没有商品进货价信息重新计算单位成本不合理）
            //没有历史单位成本的商品，单位成本价取采购价
            ProductCostDO productCostDO = new ProductCostDO();
            productCostDO.setProductNo(entry.getEntryId());
            productCostDO.setEntryPrice(entry.getEntryPrice());
            productCostDO.setCostAmount(NumberUtils.mul(costPrice, inventoryC));
            productCostDO.setCostQty(inventoryC);
            productCostDO.setCostBalance(inventory);
            productCostDO.setCostPrice(costPrice);
            productCostDO.setCostDate(DateUtils.nowDate());
            productCostDO.setCostType(costType.name());
            productCostDO.setRelateNo(orderDO.getBillNo());
            productCostDO.setRemark(String.format(Constant.COST_REMARK, costType.getRemark(), auditStatus.getRemark1()));

            detail.getCostMap().put(entry.getEntryId(), productCostDO);
            productCostDOList.add(productCostDO);
        }
        productCostDao.saveBatch(productCostDOList);
        return detail;
    }

    /**
     * 订单采购费用均摊到分录
     */
    private Map<String, BigDecimal> convertAmountFeeMap(OrderDO orderDO, List<OrderEntryDO> entryDOList) {
        Map<String, BigDecimal> result = Maps.newHashMap();
        BigDecimal purchaseFee = NumberUtils.toBigDecimal(orderDO.getPurchaseFee());
        BigDecimal avgFeeTotal = BigDecimal.ZERO, totalAmountTotal = BigDecimal.ZERO;
        for (OrderEntryDO entryDO : entryDOList) {
            totalAmountTotal = NumberUtils.add(totalAmountTotal, entryDO.getTotalAmount());
        }
        int count = 1;
        for (OrderEntryDO entryDO : entryDOList) {
            BigDecimal avgFeeAmount;
            if (count == entryDOList.size()) {
                avgFeeAmount = NumberUtils.subtract(purchaseFee, avgFeeTotal);
            } else {
                avgFeeAmount = NumberUtils.mul(NumberUtils.div(entryDO.getTotalAmount(), totalAmountTotal), purchaseFee);
                avgFeeTotal = NumberUtils.add(avgFeeAmount, avgFeeTotal);
            }
            result.put(entryDO.getEntryId(), NumberUtils.add(entryDO.getTotalAmount(), avgFeeAmount));
            count = count + 1;
        }
        return result;
    }

    /**
     * 查询商品采购价
     */
    private Map<String, BigDecimal> convertPurchase(List<String> entryNoList) {
        List<ProductDO> ProductDOList = productDao.list(ImmutableMap.of("nos", entryNoList));
        Map<String, BigDecimal> purchaseMap = Maps.newHashMap();
        for (ProductDO productDO : ProductDOList) {
            purchaseMap.put(productDO.getNo().toString(), productDO.getPurchasePrice());
        }
        return purchaseMap;
    }

    /**
     * 查询商品成本单价(最近那个记录)
     */
    private Map<String, ProductCostDO> convertProductCost(List<String> entryNoList) {
        List<ProductCostDO> costDOList = productCostDao.listLate(ImmutableMap.of("productNos", entryNoList, "latest", true));
        Map<String, ProductCostDO> costDOMap = Maps.newHashMap();
        for (ProductCostDO productCostDO : costDOList) {
            costDOMap.putIfAbsent(productCostDO.getProductNo(), productCostDO);
        }
        return costDOMap;
    }

    /**
     * 计算商品库存信息
     */
    private Map<String, BigDecimal> convertInventoryMap(List<String> entryNoList) {
        Map<String, BigDecimal> result = Maps.newHashMap();
        TreeMap<String, List<Map<String, Object>>> mapList = Maps.newTreeMap();
        List<Map<String, Object>> list = whReportDao.pBalance(ImmutableMap.of("products", entryNoList));
        for (Map<String, Object> map : list) {
            String key = MapUtils.getString(map, "no");
            if (!mapList.containsKey(key)) {
                mapList.put(key, Lists.newArrayList());
            }
            mapList.get(key).add(map);
        }

        for (Map.Entry<String, List<Map<String, Object>>> entry : mapList.entrySet()) {
            BigDecimal inventoryTotal = BigDecimal.ZERO;
            for (Map<String, Object> map : entry.getValue()) {
                inventoryTotal = inventoryTotal.add(defaultStockAmount(map));
            }
            result.put(entry.getKey(), inventoryTotal);
        }
        return result;
    }

    private BigDecimal defaultStockAmount(Map<String, Object> map) {
        BillType billType = BillType.fromValue(MapUtils.getString(map, "bill_type"));
        if (incBillTypeSet.contains(billType)) {
            return BigDecimal.valueOf(MapUtils.getIntValue(map, "total_qty"));
        } else {
            return BigDecimal.valueOf(MapUtils.getIntValue(map, "total_qty") * -1L);
        }
    }

}
