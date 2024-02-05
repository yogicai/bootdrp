package com.bootdo.modular.system.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class SysDeptParam extends BaseParam {

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门ID（多个逗号分隔）")
    private String deptId;


}
