package com.bootdo.modular.system.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class SysFileParam extends BaseParam {

    @ApiModelProperty(value = "类别")
    private String type;
}
