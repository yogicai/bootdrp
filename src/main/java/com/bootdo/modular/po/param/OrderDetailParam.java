package com.bootdo.modular.po.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 销售单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class OrderDetailParam {

    @NotBlank
    @Schema(description = "订单编号")
    private String billNo;

}
