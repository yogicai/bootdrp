package com.bootdo.modular.rp.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收付款单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RPOrderQryParam extends BaseParam {

    @ApiModelProperty(value = "订单类型")
    private String checkId;

    @ApiModelProperty(value = "订单类型")
    private String billType;

    @ApiModelProperty(value = "订单来源")
    private String billSource;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "关联订单号")
    private String srcBillNo;

}
