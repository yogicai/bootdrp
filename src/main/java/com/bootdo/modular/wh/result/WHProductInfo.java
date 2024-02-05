package com.bootdo.modular.wh.result;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class WHProductInfo {
    /**
     * 商品ID
     */
    @Excel(name = "商品ID", needMerge = true)
    private String entryId;
    /**
     * 商品名称
     */
    @Excel(name = "商品名称", needMerge = true, width = 20)
    private String entryName;
    /**
     * 条形码
     */
    @ExcelIgnore
    private String entryBarcode;
    /**
     * 商品单位
     */
    @Excel(name = "商品单位", dict = "data_unit", needMerge = true)
    private String entryUnit;
    /**
     * 单位数量
     */
    @Excel(name = "入库商品数量", needMerge = true)
    private BigDecimal qtyTotal;
    /**
     * 商品单价
     */
    @Excel(name = "入库商品单价", needMerge = true)
    private BigDecimal entryPrice;
    /**
     * 商品金额
     */
    @Excel(name = "入库商品金额", needMerge = true)
    private BigDecimal entryAmount;
    /**
     * 库存数量
     */
    @Excel(name = "库存商品数量", needMerge = true)
    private BigDecimal inventory;
    /**
     * 库存单位成本
     */
    @Excel(name = "库存商品单价", needMerge = true, numFormat = "#.00")
    private BigDecimal costPrice;
    /**
     * 库存成本（商品成本 + 费用成本）
     */
    @Excel(name = "库存商品金额（商品成本 + 费用成本）", needMerge = true)
    private BigDecimal costAmount;
    /**
     * 仓库库存明细
     */
    @ExcelCollection(name = "仓库库存明细")
    private final List<WHStockInfo> stockInfoList = Lists.newArrayList();

    @ExcelIgnore
    @JsonIgnore
    private final Map<String, WHStockInfo> stockInfoMap = Maps.newHashMap();

}
