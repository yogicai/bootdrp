package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账户管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class AccountQryParam extends BaseParam {

    @ApiModelProperty(value = "类目分类")
    private String type;
}
