package com.bootdo.modular.engage.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手动调整库存
 *
 * @author caiyz
 * @since 2023-04-04 17:30
 */
@Data
public class BalanceAdjustParam {

    @ApiModelProperty(value = "商品编号，多个逗号分隔")
    private String productNos;

}
