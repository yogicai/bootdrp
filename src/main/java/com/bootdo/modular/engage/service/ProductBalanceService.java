package com.bootdo.modular.engage.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.factory.PageFactory;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.data.service.CostAmountCalculator;
import com.bootdo.modular.data.service.CostAmountIResult;
import com.bootdo.modular.engage.dao.ProductBalanceDao;
import com.bootdo.modular.engage.domain.ProductCostDO;
import com.bootdo.modular.engage.param.BalanceAdjustParam;
import com.bootdo.modular.engage.param.BalanceQryParam;
import com.bootdo.modular.engage.param.EntryBalanceQryParam;
import com.bootdo.modular.engage.result.*;
import com.bootdo.modular.wh.result.WHProductInfo;
import com.bootdo.modular.wh.result.WHStockInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author L
 * @since 2020-11-10 14:48
 */
@Service
public class ProductBalanceService {
    @Resource
    private ProductBalanceDao productBalanceDao;
    @Resource
    private ProductCostService productCostService;
    @Resource
    private CostAmountCalculator costAmountCalculator;

    private final Set<BillType> poBillSet = CollUtil.newHashSet(BillType.CG_ORDER, BillType.WH_RK_ORDER);
    private final Set<BillType> seBillSet = CollUtil.newHashSet(BillType.TH_ORDER, BillType.WH_CK_ORDER);


    @Transactional(rollbackFor = Exception.class)
    public BalanceResult pBalance(BalanceQryParam param) {
        BalanceResult result = new BalanceResult()
                .setToDate(StrUtil.blankToDefault(DateUtil.formatDate(param.getToDate()), DateUtils.currentDate()));
        // 按商品 ID分类整理库存信息
        List<BalanceItemResult> list = productBalanceDao.pBalance(BeanUtil.beanToMap(param));
        TreeMap<String, List<BalanceItemResult>> listMap = list.stream()
                .collect(Collectors.groupingBy(BalanceItemResult::getNo, TreeMap::new, Collectors.toList()));
        // 商品仓库信息
        TreeMap<String, String> stockMap = list.stream().filter(item -> StrUtil.isNotEmpty(item.getStockNo()))
                .collect(Collectors.toMap(BalanceItemResult::getStockNo, BalanceItemResult::getStockName, (o, n) -> o, TreeMap::new));
        // 处理商品成本信息
        Map<String, ProductCostDO> costDOMap = productCostService.listLate(null).stream()
                .collect(Collectors.toMap(ProductCostDO::getProductNo, Function.identity(), (o, n) -> o));

        // 是否查询零库存商品（0:是，其他:否）
        boolean showSto = StrUtil.equals(param.getShowSto(), "0");
        // 处理商品信息及总库存信息
        for (Map.Entry<String, List<BalanceItemResult>> entry : listMap.entrySet()) {
            WHProductInfo productInfo = convertProductInfo(entry.getValue(), costDOMap);
            // 零库存商品
            if (showSto && productInfo.getInventory().compareTo(BigDecimal.ZERO) <= 0) {
                result.getProductInfoList().add(productInfo);
            }
            // 全部商品
            if (!showSto) {
                result.getProductInfoList().add(productInfo);
            }
        }
        // 处理各仓库库存信息
        for (WHProductInfo productInfo : result.getProductInfoList()) {
            for (Map.Entry<String, String> entry : stockMap.entrySet()) {
                if (productInfo.getStockInfoMap().containsKey(entry.getKey())) {
                    productInfo.getStockInfoList().add(productInfo.getStockInfoMap().get(entry.getKey()));
                } else {
                    productInfo.getStockInfoList().add(new WHStockInfo());
                }
            }
        }
        result.getStockList().addAll(stockMap.values());
        return result;
    }

    private WHProductInfo convertProductInfo(List<BalanceItemResult> mapList, Map<String, ProductCostDO> costDOMap) {
        WHProductInfo productInfo = new WHProductInfo();
        if (CollUtil.isEmpty(mapList)) {
            return productInfo;
        }
        BigDecimal qtyTotal = BigDecimal.ZERO;
        // 历史商品数量
        BigDecimal inventoryTotal = BigDecimal.ZERO;
        // 商品库存
        BigDecimal entryAmountTotal = BigDecimal.ZERO;
        // 历史商品金额
        BigDecimal totalAmountTotal = BigDecimal.ZERO;
        // 历史商品金额 + 历史费用（分录级别）
        for (BalanceItemResult item : mapList) {
            inventoryTotal = inventoryTotal.add(defaultStockAmount(item));
            qtyTotal = qtyTotal.add(defaultEntryAmount(item, item.getTotalQty()));
            entryAmountTotal = entryAmountTotal.add(defaultEntryAmount(item, item.getEntryAmount()));
            totalAmountTotal = totalAmountTotal.add(defaultEntryAmount(item, item.getTotalAmount()));

            String stockNo = item.getStockNo();
            String stockName = item.getStockName();
            if (!productInfo.getStockInfoMap().containsKey(stockNo)) {
                WHStockInfo stockInfo = new WHStockInfo();
                stockInfo.setStockNo(stockNo);
                stockInfo.setStockName(stockName);
                stockInfo.addTotalQty(defaultStockAmount(item));
                productInfo.getStockInfoMap().put(stockNo, stockInfo);
            } else {
                productInfo.getStockInfoMap().get(stockNo).addTotalQty(defaultStockAmount(item));
            }
        }
        ProductCostDO costDO = costDOMap.get(mapList.get(0).getNo());
        productInfo.setShopNo(mapList.get(0).getShopNo());
        productInfo.setEntryId(mapList.get(0).getNo());
        productInfo.setEntryName(mapList.get(0).getName());
        productInfo.setEntryBarcode(mapList.get(0).getBarCode());
        productInfo.setEntryUnit(mapList.get(0).getUnit());
        // 累计入库（采购入库、盘点入库）商品数量、均价、金额
        productInfo.setQtyTotal(qtyTotal);
        productInfo.setEntryPrice(NumberUtils.div(entryAmountTotal, qtyTotal));
        productInfo.setEntryAmount(entryAmountTotal);
        // 实际库存数量（采购入库 + 盘点入库 - 退货出库 - 盘点出库）商品数量、均价、金额
        productInfo.setInventory(inventoryTotal);
        productInfo.setCostPrice(costDO != null ? costDO.getCostPrice() : BigDecimal.ZERO);
        productInfo.setCostAmount(NumberUtils.mul(productInfo.getCostPrice(), inventoryTotal));
        return productInfo;
    }

    public BalanceTotalResult pBalanceTotal(Map<String, Object> params) {
        BalanceTotalResult result = new BalanceTotalResult();
        // 处理商品成本信息
        Map<String, ProductCostDO> costDOMap = productCostService.listLate(null).stream()
                .collect(Collectors.toMap(ProductCostDO::getProductNo, Function.identity(), (o, n) -> o));
        // 处理每个商品的库存数量
        Map<String, BigDecimal> productMap = productBalanceDao.pBalance(params).stream()
                .collect(Collectors.toMap(BalanceItemResult::getNo, this::defaultStockAmount, NumberUtil::add));
        // 计算库存总数量、成本
        for (Map.Entry<String, BigDecimal> entry : productMap.entrySet()) {
            BigDecimal costPrice = costDOMap.get(entry.getKey()) != null ? costDOMap.get(entry.getKey()).getCostPrice() : BigDecimal.ZERO;
            result.setQtyTotal(NumberUtils.add(result.getQtyTotal(), entry.getValue()));
            result.setTotalAmount(NumberUtils.add(result.getTotalAmount(), NumberUtils.mul(entry.getValue(), costPrice)));
        }
        return result;
    }


    /**
     * 报表-商品库存余额-商品库存变更明细
     */
    @Transactional(readOnly = true)
    public Page<EntryBalanceResult> pBalanceEntry(EntryBalanceQryParam param) {
        return productBalanceDao.pBalanceEntry(PageFactory.defaultPage(), BeanUtil.beanToMap(param));
    }

    @Transactional(readOnly = true)
    public EntryBalanceSumResult pBalanceEntryCountSum(EntryBalanceQryParam param) {
        return productBalanceDao.pBalanceEntryCountSum(BeanUtil.beanToMap(param));
    }

    /**
     * 手动调整库存
     */
    @Transactional(rollbackFor = Exception.class)
    public Set<String> pBalanceAdjust(BalanceAdjustParam balanceAdjustParam) {

        List<String> productNoList;
        if (StrUtil.isNotBlank(balanceAdjustParam.getProductNos())) {
            productNoList = StrUtil.split(balanceAdjustParam.getProductNos(), StrUtil.COMMA);
        } else {
            productNoList = productCostService.listLate(null).stream()
                    .map(ProductCostDO::getProductNo).collect(Collectors.toList());
        }
        CostAmountIResult result = costAmountCalculator.adjustBillCost(productNoList);
        return result.getCostMap().keySet();
    }

    private BigDecimal defaultStockAmount(BalanceItemResult balanceItem) {
        if (poBillSet.contains(balanceItem.getBillType())) {
            return balanceItem.getTotalQty();
        } else {
            return balanceItem.getTotalQty().negate();
        }
    }

    private BigDecimal defaultEntryAmount(BalanceItemResult balanceItem, BigDecimal value) {
        return poBillSet.contains(balanceItem.getBillType()) ? value : BigDecimal.ZERO;
    }
}
