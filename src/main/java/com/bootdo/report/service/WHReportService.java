package com.bootdo.report.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.enumeration.BillType;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.engage.dao.ProductCostDao;
import com.bootdo.engage.domain.ProductCostDO;
import com.bootdo.report.controller.response.WHPBalanceResult;
import com.bootdo.report.controller.response.WHPBalanceTotalResult;
import com.bootdo.report.dao.WHReportDao;
import com.bootdo.wh.controller.response.WHProductInfo;
import com.bootdo.wh.controller.response.WHStockInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author caiyz
 * @since 2020-11-10 14:48
 */
@Service
public class WHReportService {
    @Resource
    private WHReportDao whReportDao;
    @Resource
    private ProductCostDao productCostDao;

    private final Set<String> poBillSet = Sets.newHashSet(BillType.CG_ORDER.name(), BillType.WH_RK_ORDER.name());
    private final Set<String> seBillSet = Sets.newHashSet(BillType.TH_ORDER.name(), BillType.WH_CK_ORDER.name());

    @Transactional(rollbackFor = Exception.class)
    public WHPBalanceResult pBalance(Map<String, Object> params) {
        List<Map<String, Object>> list = whReportDao.pBalance(params);
        TreeMap<String, List<Map<String, Object>>> listMap = Maps.newTreeMap();
        TreeMap<String, String> stockMap = Maps.newTreeMap();
        WHPBalanceResult result = new WHPBalanceResult();

        result.setToDate(StringUtils.defaultIfBlank(MapUtil.getStr(params, "toDate"), DateUtils.currentDate()));
        //按商品ID分类整理库存信息
        for (Map<String, Object> map : list) {
            String key = MapUtil.getStr(map, "no");
            if (!listMap.containsKey(key)) {
                listMap.put(key, Lists.newArrayList());
                if (!StringUtil.isEmpty(MapUtil.getStr(map, "stock_no"))) {
                    stockMap.put(MapUtil.getStr(map, "stock_no"), MapUtil.getStr(map, "stock_name"));
                }
            }
            listMap.get(key).add(map);
        }
        //处理商品成本信息
        Map<String, ProductCostDO> costDOMap = Maps.newHashMap();
        List<ProductCostDO> costDOList = productCostDao.listLate(ImmutableMap.of("latest", true));
        for (ProductCostDO costDO : costDOList) {
            costDOMap.put(costDO.getProductNo(), costDO);
        }
        //处理商品信息及总库存信息
        //是否查询零库存商品（0:是，其他:否）
        boolean showSto = MapUtil.getInt(params, "showSto", 1) == 0;
        for (Map.Entry<String, List<Map<String, Object>>> entry : listMap.entrySet()) {
            WHProductInfo productInfo = convertProductInfo(entry.getValue(), costDOMap);
            //零库存商品
            if (showSto && BigDecimal.ZERO.equals(productInfo.getInventory())) {
                result.getProductInfoList().add(productInfo);
            }
            //全部商品
            if (!showSto) {
                result.getProductInfoList().add(productInfo);
            }
        }
        //处理各仓库库存信息
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

    private WHProductInfo convertProductInfo(List<Map<String, Object>> mapList, Map<String, ProductCostDO> costDOMap) {
        WHProductInfo productInfo = new WHProductInfo();
        if (CollectionUtils.isEmpty(mapList)) {
            return productInfo;
        }
        BigDecimal qtyTotal = BigDecimal.ZERO;
        //历史商品数量
        BigDecimal inventoryTotal = BigDecimal.ZERO;
        //商品库存
        BigDecimal entryAmountTotal = BigDecimal.ZERO;
        //历史商品金额
        BigDecimal totalAmountTotal = BigDecimal.ZERO;
        //历史商品金额 + 历史费用（分录级别）
        for (Map<String, Object> map : mapList) {
            inventoryTotal = inventoryTotal.add(defaultStockAmount(map));
            qtyTotal = qtyTotal.add(defaultEntryAmount(map, "total_qty"));
            entryAmountTotal = entryAmountTotal.add(defaultEntryAmount(map, "entry_amount"));
            totalAmountTotal = totalAmountTotal.add(defaultEntryAmount(map, "total_amount"));

            String stockNo = MapUtil.getStr(map, "stock_no");
            String stockName = MapUtil.getStr(map, "stock_name");
            if (!productInfo.getStockInfoMap().containsKey(stockNo)) {
                WHStockInfo stockInfo = new WHStockInfo();
                stockInfo.setStockNo(stockNo);
                stockInfo.setStockName(stockName);
                stockInfo.addTotalQty(defaultStockAmount(map));
                productInfo.getStockInfoMap().put(stockNo, stockInfo);
            } else {
                productInfo.getStockInfoMap().get(stockNo).addTotalQty(defaultStockAmount(map));
            }
        }
        ProductCostDO costDO = costDOMap.get(MapUtil.getStr(mapList.get(0), "no"));
        productInfo.setEntryId(MapUtil.getStr(mapList.get(0), "no"));
        productInfo.setEntryName(MapUtil.getStr(mapList.get(0), "name"));
        productInfo.setEntryBarcode(MapUtil.getStr(mapList.get(0), "bar_code"));
        productInfo.setEntryUnit(MapUtil.getStr(mapList.get(0), "unit"));
        //累计入库（采购入库、盘点入库）商品数量、均价、金额
        productInfo.setQtyTotal(qtyTotal);
        productInfo.setEntryPrice(NumberUtils.div(entryAmountTotal, qtyTotal));
        productInfo.setEntryAmount(entryAmountTotal);
        //实际库存数量（采购入库 + 盘点入库 - 退货出库 - 盘点出库）商品数量、均价、金额
        productInfo.setInventory(inventoryTotal);
        productInfo.setCostPrice(costDO != null ? costDO.getCostPrice() : BigDecimal.ZERO);
        productInfo.setCostAmount(NumberUtils.mul(productInfo.getCostPrice(), inventoryTotal));
        return productInfo;
    }

    public WHPBalanceTotalResult pBalanceTotal(Map<String, Object> params) {
        WHPBalanceTotalResult result = new WHPBalanceTotalResult();
        List<Map<String, Object>> list = whReportDao.pBalance(params);
        //处理商品成本信息
        Map<String, ProductCostDO> costDOMap = Maps.newHashMap();
        List<ProductCostDO> costDOList = productCostDao.listLate(ImmutableMap.of("latest", true));
        for (ProductCostDO costDO : costDOList) {
            costDOMap.put(costDO.getProductNo(), costDO);
        }
        //处理每个商品的库存数量
        Map<String, BigDecimal> productMap = Maps.newHashMap();
        for (Map<String, Object> map : list) {
            String no = MapUtil.getStr(map, "no");
            productMap.put(no, NumberUtils.add(MapUtils.getBigDecimal(productMap, no), defaultStockAmount(map)));
        }
        //计算库存总数量、成本
        for (Map.Entry<String, BigDecimal> entry : productMap.entrySet()) {
            BigDecimal costPrice = costDOMap.get(entry.getKey()) != null ? costDOMap.get(entry.getKey()).getCostPrice() : BigDecimal.ZERO;
            result.setQtyTotal(NumberUtils.add(result.getQtyTotal(), entry.getValue()));
            result.setTotalAmount(NumberUtils.add(result.getTotalAmount(), NumberUtils.mul(entry.getValue(), costPrice)));
        }
        return result;
    }

    /**
     * 查询商品库存余额（用于销售单添加商品展示用）
     */
    public Map<String, BigDecimal> queryPBalance(Map<String, Object> params) {
        Map<String, BigDecimal> result = new HashMap<>();
        List<Map<String, Object>> list = whReportDao.pBalance(params);
        for (Map<String, Object> map : list) {
            result.put(MapUtil.getStr(map, "no"), NumberUtils.add(MapUtils.getBigDecimal(result, MapUtil.getStr(map, "no")), defaultStockAmount(map)));
        }
        return result;
    }

    private BigDecimal defaultStockAmount(Map<String, Object> map) {
        String billType = MapUtil.getStr(map, "bill_type");
        if (poBillSet.contains(billType)) {
            return BigDecimal.valueOf(MapUtil.getInt(map, "total_qty", 0));
        } else {
            return BigDecimal.valueOf(MapUtil.getInt(map, "total_qty", 0) * -1L);
        }
    }

    private BigDecimal defaultEntryAmount(Map<String, Object> map, String key) {
        String billType = MapUtil.getStr(map, "bill_type");
        return BigDecimal.valueOf(poBillSet.contains(billType) ? MapUtil.getInt(map, key, 0) : 0);
    }
}
