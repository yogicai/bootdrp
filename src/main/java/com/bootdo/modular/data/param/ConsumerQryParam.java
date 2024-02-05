package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class ConsumerQryParam extends BaseParam {

    @ApiModelProperty(value = "客户分类")
    private String type;

    @ApiModelProperty(value = "客户状态")
    private String status;

}
