package com.bootdo.modular.se.param;

import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 销售单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class SeOrderQryParam extends BaseParam {

    @ApiModelProperty(value = "订单类型")
    private String consumerId;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "订单状态过滤")
    private List<OrderStatus> statusNot;

    @ApiModelProperty(value = "订单类型")
    private String billSource;

}
