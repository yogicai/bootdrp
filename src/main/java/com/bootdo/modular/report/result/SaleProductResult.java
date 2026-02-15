package com.bootdo.modular.report.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SaleProductResult {

    @Schema(name = "单据日期")
    private String billRegion;

    @Schema(description = "数据明细")
    private List<SaleProductItem> itemList;

}
