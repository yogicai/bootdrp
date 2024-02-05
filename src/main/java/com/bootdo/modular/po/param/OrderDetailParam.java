package com.bootdo.modular.po.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 销售单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class OrderDetailParam {

    @NotBlank
    @ApiModelProperty(value = "订单编号")
    private String billNo;

}
