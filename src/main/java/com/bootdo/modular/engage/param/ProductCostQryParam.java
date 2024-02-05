package com.bootdo.modular.engage.param;

import com.bootdo.core.enums.CostType;
import com.bootdo.core.enums.CostVersion;
import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品成本管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class ProductCostQryParam extends BaseParam {

    @ApiModelProperty(value = "商品编号")
    private Integer productNo;

    @ApiModelProperty(value = "商品类目")
    private String productType;

    @ApiModelProperty(value = "成本类型")
    private CostType costType;

    @ApiModelProperty(value = "成本版本")
    private CostVersion version;

}
