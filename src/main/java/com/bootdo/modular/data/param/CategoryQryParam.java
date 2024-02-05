package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类目管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryQryParam extends BaseParam {

    @ApiModelProperty(value = "类目名称")
    private String name;

    @ApiModelProperty(value = "类目分类")
    private String type;

    @ApiModelProperty(value = "状态")
    private String status;

}
