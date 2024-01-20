package com.bootdo.modular.cashier.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入订单
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class RecordQryParam extends BaseParam {

    @ApiModelProperty(value = "交易渠道")
    private String type;

    @ApiModelProperty(value = "交易账号")
    private String account;

    @ApiModelProperty(value = "交易方向")
    private String payDirect;

    @ApiModelProperty(value = "交易状态")
    private String payStatus;

    @ApiModelProperty(value = "交易类型")
    private String tradeClass;

    @ApiModelProperty(value = "数据来源")
    private String source;

    @ApiModelProperty(value = "资金用途")
    private String costType;

}
