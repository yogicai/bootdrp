package com.bootdo.modular.data.param;

import com.bootdo.core.enums.CategoryType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类目树
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class CategoryTreeParam {

    @ApiModelProperty(value = "类目ID")
    private String categoryId;

    @ApiModelProperty(value = "类目分类")
    private CategoryType type;

}
