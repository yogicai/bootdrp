package com.bootdo.modular.rp.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.rp.enums.PointSearchType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 积分管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class PointQryParam extends BaseParam {

    @ApiModelProperty(value = "类目分类")
    private PointSearchType type;

    @ApiModelProperty(value = "积分状态")
    private String status;

    @ApiModelProperty(value = "客户ID")
    private String consumerId;

}
