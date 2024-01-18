package com.bootdo.modular.data.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.enums.CostType;
import com.bootdo.core.exception.BootServiceExceptionEnum;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.dao.ProductDao;
import com.bootdo.modular.data.domain.ProductDO;
import com.bootdo.modular.engage.dao.ProductBalanceDao;
import com.bootdo.modular.engage.dao.ProductCostDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.po.dao.OrderEntryDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.domain.OrderEntryDO;
import com.bootdo.modular.se.dao.SEOrderEntryDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.domain.SEOrderEntryDO;
import com.bootdo.modular.wh.dao.WHOrderEntryDao;
import com.bootdo.modular.wh.domain.WHOrderDO;
import com.bootdo.modular.wh.domain.WHOrderEntryDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * @author L
 */
@Service
public class CostAmountCalculator {
    @Resource
    private ProductCostDao productCostDao;
    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private SEOrderEntryDao seOrderEntryDao;
    @Resource
    private WHOrderEntryDao whOrderEntryDao;
    @Resource
    private ProductBalanceDao whReportDao;
    @Resource
    private ProductDao productDao;


    private final EnumSet<BillType> incBillTypeSet = EnumSet.of(BillType.CG_ORDER, BillType.WH_RK_ORDER);
    private final EnumSet<BillType> desBillTypeSet = EnumSet.of(BillType.TH_ORDER, BillType.WH_CK_ORDER, BillType.XS_ORDER);

    /**
     * 采购单审核计算成本信息，退货单
     */
    public CostAmountIResult calcPOBillCost(OrderDO orderDO, AuditStatus auditStatus) {

        CostType costType = BillType.CG_ORDER.equals(orderDO.getBillType()) ? CostType.PO_CG : CostType.PO_TH;
        //商品
        List<OrderEntryDO> entryDOList = orderEntryDao.list(ImmutableMap.of("billNo", orderDO.getBillNo()));
        List<String> entryNoList = entryDOList.stream().map(OrderEntryDO::getEntryId).collect(Collectors.toList());
        //商品采购价信息
        Map<String, BigDecimal> purchaseMap = convertPurchase(entryNoList);
        //查询商品成本单价(最近那个记录)
        Map<String, ProductCostDO> costDOMap = convertProductCost(entryNoList);
        //计算商品库存信息
        //Map<String, BigDecimal> inventoryMap = convertInventoryMap(entryNoList);
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
            BigDecimal mulFactor = convertMulFactor(orderDO.getBillType(), auditStatus);
            //库存数量
            BigDecimal inventory = ObjectUtil.defaultIfNull(costDOMap.get(entry.getEntryId()), ProductCostDO::getCostQty, BigDecimal.ZERO);
            //库存数量 +　本次商品数量
            BigDecimal inventoryC = inventory.add(NumberUtils.mul(NumberUtils.toBigDecimal(entry.getTotalQty()), mulFactor));

            ProductCostDO productCostDO = new ProductCostDO();

            //退货单不影响单位成本，但要重新计算商品总成本及库存数量；没有历史单位成本的商品，单位成本价取采购价
            if (BillType.TH_ORDER.equals(orderDO.getBillType())) {
                BigDecimal costPrice = ObjectUtil.defaultIfNull(costDOMap.get(entry.getEntryId()), ProductCostDO::getCostPrice, purchaseMap.get(entry.getEntryId()));
                productCostDO.setProductNo(entry.getEntryId());
                productCostDO.setEntryPrice(entry.getEntryPrice());
                productCostDO.setEntryQty(entry.getTotalQty());
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
                    //本次商品均摊费用 +　本次商品金额
                    BigDecimal amountFee = NumberUtils.toBigDecimal(entryAmountFee.get(entry.getEntryId()));
                    //库存单位成本
                    BigDecimal costPrice = NumberUtils.div(amountFee, NumberUtils.toBigDecimal(entry.getTotalQty()), 6);

                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setEntryQty(entry.getTotalQty());
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
                    BigDecimal costPrice = ObjectUtil.defaultIfNull(costDOMap.get(entry.getEntryId()), ProductCostDO::getCostPrice, BigDecimal.ZERO);
                    //本次商品均摊费用 +　本次商品金额
                    BigDecimal amountFee = NumberUtils.toBigDecimal(entryAmountFee.get(entry.getEntryId()));
                    //库存成本 +　本次费用　+ 本次商品金额
                    BigDecimal amountFeeCost = NumberUtils.mul(costPrice, inventory).add(NumberUtils.mul(amountFee, mulFactor));

                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setEntryQty(entry.getTotalQty());
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
                    productCostDO.setEntryQty(entry.getTotalQty());
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
                    BigDecimal costPrice = ObjectUtil.defaultIfNull(costDOMap.get(entry.getEntryId()), ProductCostDO::getCostPrice, BigDecimal.ZERO);
                    //本次商品均摊费用 +　本次商品金额
                    BigDecimal amountFee = NumberUtils.toBigDecimal(entryAmountFee.get(entry.getEntryId()));
                    //库存成本 +　本次费用　+ 本次商品金额
                    BigDecimal amountFeeCost = NumberUtils.mul(costPrice, inventory).add(NumberUtils.mul(amountFee, mulFactor));

                    productCostDO.setProductNo(entry.getEntryId());
                    productCostDO.setEntryPrice(entry.getEntryPrice());
                    productCostDO.setEntryQty(entry.getTotalQty());
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

    /**
     * 销售单审核，计算库存
     */
    public CostAmountIResult calcSEBillCost(SEOrderDO orderDO, AuditStatus auditStatus) {
        //销售单
        List<SEOrderEntryDO> entryDOList = seOrderEntryDao.list(ImmutableMap.of("billNo", orderDO.getBillNo()));
        //销售单商品
        List<String> entryNoList = entryDOList.stream().map(SEOrderEntryDO::getEntryId).collect(Collectors.toList());
        //查询商品成本单价(最近那个记录)
        Map<String, ProductCostDO> costDOMap = convertProductCost(entryNoList);
        //计算商品单价成本
        CostAmountIResult detail = new CostAmountIResult();
        List<ProductCostDO> productCostDOList = Lists.newArrayList();
        for (SEOrderEntryDO entry : entryDOList) {
            /**
             * XS_ORDER     +   AUDIT       1 ：库存增加
             * XS_ORDER     +   UNAUDIT    -1 ：库存减少
             */
            BigDecimal mulFactor = convertMulFactor(orderDO.getBillType(), auditStatus);
            //目前商品成本
            ProductCostDO preProductCostDO = costDOMap.get(entry.getEntryId());
            BootServiceExceptionEnum.PRODUCT_COST_NOT_FOUND.assertNotNull(preProductCostDO, entry.getEntryId(), entry.getEntryName());
            //库存数量
            BigDecimal inventory = NumberUtils.toBigDecimal(preProductCostDO.getCostQty());
            //库存数量 +　本次商品数量
            BigDecimal inventoryC = inventory.add(NumberUtils.mul(NumberUtils.toBigDecimal(entry.getTotalQty()), mulFactor));

            //销售单库存计算，不影响单位成本，但要重新计算商品总成本及库存数量
            ProductCostDO productCostDO = new ProductCostDO();
            productCostDO.setProductNo(entry.getEntryId());
            productCostDO.setEntryPrice(preProductCostDO.getEntryPrice());
            productCostDO.setEntryQty(entry.getTotalQty());
            productCostDO.setCostAmount(NumberUtils.mul(preProductCostDO.getCostPrice(), inventoryC));
            productCostDO.setCostQty(inventoryC);
            productCostDO.setCostBalance(inventory);
            productCostDO.setCostPrice(preProductCostDO.getCostPrice());
            productCostDO.setCostDate(DateUtils.nowDate());
            productCostDO.setCostType(CostType.SE_XS.name());
            productCostDO.setRelateNo(orderDO.getBillNo());
            productCostDO.setRemark(String.format(Constant.COST_REMARK, CostType.SE_XS.getRemark(), auditStatus.getRemark1()));

            detail.getCostMap().put(entry.getEntryId(), productCostDO);
            productCostDOList.add(productCostDO);
        }
        productCostDao.saveBatch(productCostDOList);
        return detail;

    }

    /**
     * 其他入库单、其他出库单，审核计算成本信息
     */
    public CostAmountIResult calcWHBillCost(WHOrderDO orderDO, AuditStatus auditStatus) {
        CostType costType = BillType.WH_RK_ORDER.equals(orderDO.getBillType()) ? CostType.WH_RK : CostType.WH_CK;
        //商品
        List<WHOrderEntryDO> entryDOList = whOrderEntryDao.list(ImmutableMap.of("billNo", orderDO.getBillNo()));
        List<String> entryNoList = entryDOList.stream().map(WHOrderEntryDO::getEntryId).collect(Collectors.toList());
        //商品采购价信息
        Map<String, BigDecimal> purchaseMap = convertPurchase(entryNoList);
        //查询商品成本单价(最近那个记录)
        Map<String, ProductCostDO> costDOMap = convertProductCost(entryNoList);
        //计算商品库存信息
        //Map<String, BigDecimal> inventoryMap = convertInventoryMap(entryNoList);
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
            BigDecimal mulFactor = convertMulFactor(orderDO.getBillType(), auditStatus);
            //库存数量
            BigDecimal inventory = ObjectUtil.defaultIfNull(costDOMap.get(entry.getEntryId()), ProductCostDO::getCostQty, BigDecimal.ZERO);
            //库存数量 +　本次商品数量
            BigDecimal inventoryC = inventory.add(NumberUtils.mul(NumberUtils.toBigDecimal(entry.getTotalQty()), mulFactor));

            BigDecimal costPrice = ObjectUtil.defaultIfNull(costDOMap.get(entry.getEntryId()), ProductCostDO::getCostPrice, purchaseMap.get(entry.getEntryId()));

            //仓库调整库存不影响单位成本，但要重新计算商品总成本及库存数量（因为仓库调整单没有商品进货价信息重新计算单位成本不合理）
            //没有历史单位成本的商品，单位成本价取采购价
            ProductCostDO productCostDO = new ProductCostDO();
            productCostDO.setProductNo(entry.getEntryId());
            productCostDO.setEntryPrice(entry.getEntryPrice());
            productCostDO.setEntryQty(entry.getTotalQty());
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
     * 手工调整库存
     */
    public CostAmountIResult adjustBillCost(List<String> entryNoList) {
        //查询商品成本单价(最近那个记录)
        Map<String, ProductCostDO> costDOMap = convertProductCost(entryNoList);
        //计算商品库存信息
        Map<String, BigDecimal> inventoryMap = convertInventoryMap(entryNoList);

        CostAmountIResult detail = new CostAmountIResult();
        List<ProductCostDO> productCostDOList = Lists.newArrayList();

        costDOMap.forEach((k, v) -> {
            if (inventoryMap.containsKey(k) && v.getCostQty().compareTo(inventoryMap.get(k)) != 0) {

                ProductCostDO productCostDO = BeanUtil.copyProperties(v, ProductCostDO.class, "id");
                //变更前库存
                BigDecimal inventory = productCostDO.getCostQty();
                //变更后库存
                BigDecimal inventoryC = inventoryMap.get(k);
                //成本单价
                BigDecimal costPrice = productCostDO.getCostPrice();
                //备注说明
                String remark = inventory.compareTo(inventoryC) > 0 ? "减少" : "增加";

                //仓库调整库存不影响单位成本，但要重新计算商品总成本及库存数量（因为仓库调整单没有商品进货价信息重新计算单位成本不合理）
                productCostDO.setProductNo(v.getProductNo());
                productCostDO.setEntryPrice(v.getEntryPrice());
                productCostDO.setEntryQty(inventory.subtract(inventoryC).abs());
                productCostDO.setCostAmount(NumberUtils.mul(costPrice, inventoryC));
                productCostDO.setCostQty(inventoryC);
                productCostDO.setCostBalance(inventory);
                productCostDO.setCostPrice(costPrice);
                productCostDO.setCostDate(DateUtils.nowDate());
                productCostDO.setCostType(CostType.MANUAL.name());
                productCostDO.setRelateNo(StrUtil.EMPTY);
                productCostDO.setRemark(String.format(Constant.COST_REMARK, CostType.MANUAL.getRemark(), remark));

                detail.getCostMap().put(k, productCostDO);
                productCostDOList.add(productCostDO);
            }
        });

        productCostDao.saveBatch(productCostDOList);
        return detail;
    }

    /**
     * 库存增加 1、库存减少 -1
     * CG_ORDER     WH_RK_ORDER     +   AUDIT       1 ：库存增加
     * CG_ORDER     WH_RK_ORDER     +   UNAUDIT    -1 ：库存减少
     * TH_ORDER     WH_CK_ORDER     +   AUDIT      -1 ：库存减少
     * TH_ORDER     WH_CK_ORDER     +   UNAUDIT     1 ：库存增加
     * XS_ORDER                     +   AUDIT       1 ：库存增加
     * XS_ORDER                     +   UNAUDIT    -1 ：库存减少
     */
    private BigDecimal convertMulFactor(BillType billType, AuditStatus auditStatus) {
        boolean flag1 = incBillTypeSet.contains(billType) && AuditStatus.YES.equals(auditStatus);
        boolean flag2 = desBillTypeSet.contains(billType) && AuditStatus.NO.equals(auditStatus);
        return (flag1 || flag2) ? BigDecimal.ONE : BigDecimal.valueOf(-1);
    }

    /**
     * 订单采购费用均摊到分录
     */
    private Map<String, BigDecimal> convertAmountFeeMap(OrderDO orderDO, List<OrderEntryDO> entryDOList) {

        Map<String, BigDecimal> result = Maps.newHashMap();
        BigDecimal purchaseFee = NumberUtils.toBigDecimal(orderDO.getPurchaseFee());
        BigDecimal totalAmountTotal = entryDOList.stream().map(OrderEntryDO::getTotalAmount).reduce(BigDecimal.ZERO, NumberUtils::add);

        int count = 1;
        BigDecimal avgFeeTotal = BigDecimal.ZERO;

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
        return productDao.list(ImmutableMap.of("nos", entryNoList))
                .stream()
                .collect(Collectors.toMap(k -> k.getNo().toString(), ProductDO::getPurchasePrice, (o, n) -> o));
    }

    /**
     * 查询商品成本单价(最近那个记录)
     */
    private Map<String, ProductCostDO> convertProductCost(List<String> entryNoList) {
        return productCostDao.listLate(ImmutableMap.of("productNos", entryNoList, "latest", true))
                .stream()
                .collect(Collectors.toMap(ProductCostDO::getProductNo, v -> v, (o, n) -> o));
    }

    /**
     * 计算商品库存信息
     */
    private Map<String, BigDecimal> convertInventoryMap(List<String> entryNoList) {
        //采购、销售、其他入出库 商品
        List<Map<String, Object>> list = whReportDao.pBalance(ImmutableMap.of("products", entryNoList, "status", 1));
        //商品库存
        return list.stream()
                .collect(Collectors.groupingBy(
                        map -> MapUtil.getStr(map, "no"),
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, this::defaultStockAmount, BigDecimal::add))
                );
    }

    private BigDecimal defaultStockAmount(Map<String, Object> map) {
        BillType billType = BillType.fromValue(MapUtil.getStr(map, "bill_type"));
        if (incBillTypeSet.contains(billType)) {
            return BigDecimal.valueOf(MapUtil.getInt(map, "total_qty"));
        } else {
            return BigDecimal.valueOf(MapUtil.getInt(map, "total_qty") * -1L);
        }
    }

}
