package com.bootdo.modular.po.param;

import com.bootdo.core.enums.OrderStatus;
import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 采购单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class OrderQryParam extends BaseParam {

    @ApiModelProperty(value = "供应商（多个逗号分隔）")
    private String vendorId;

    @ApiModelProperty(value = "订单类型")
    private String billType;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "订单状态")
    private String status;

    @ApiModelProperty(value = "订单状态过滤")
    private List<OrderStatus> statusNot;

}
