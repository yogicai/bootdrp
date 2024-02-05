package com.bootdo.modular.wh.result;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class WHStockInfo {
    /**
     * 仓库编号
     */
    @ExcelIgnore
    private String stockNo;
    /**
     * 仓库名称
     */
    @Excel(name = "仓库名称", needMerge = true)
    private String stockName;
    /**
     * 库存数量
     */
    @Excel(name = "库存数量", needMerge = true)
    private BigDecimal totalQty = BigDecimal.ZERO;

    public void addTotalQty(BigDecimal totalQty) {
        this.totalQty = this.totalQty.add(totalQty);
    }

}
