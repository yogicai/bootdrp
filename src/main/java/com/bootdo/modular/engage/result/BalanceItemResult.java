package com.bootdo.modular.engage.result;

import com.bootdo.core.enums.BillType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class BalanceItemResult {

    @Schema(description = "店铺编号")
    private String shopNo;

    @Schema(description = "商品编号")
    private String no;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品单位")
    private String unit;

    @Schema(description = "商品类别")
    private String type;

    @Schema(description = "商品品牌")
    private String brand;

    @Schema(description = "商品条形码")
    private String barCode;


    @Schema(description = "单据类型")
    private BillType billType;


    @Schema(description = "仓库编号")
    private String stockNo;

    @Schema(description = "仓库名称")
    private String stockName;

    @Schema(description = "商品单价")
    private BigDecimal entryPrice;

    @Schema(description = "商品数量")
    private BigDecimal totalQty;

    @Schema(description = "商品单位")
    private String entryUnit;

    @Schema(description = "金额")
    private BigDecimal entryAmount;

    @Schema(description = "合计金额")
    private BigDecimal totalAmount;

}
