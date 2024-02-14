package com.bootdo.modular.data.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 缓存_类目数据
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class CategoryDataResult {

    @ApiModelProperty(value = "类目ID")
    private String categoryId;
    @ApiModelProperty(value = "父类目ID")
    private String parentId;
    @ApiModelProperty(value = "父类目名称")
    private String name;
    @ApiModelProperty(value = "类目分类")
    private String type;
    @ApiModelProperty(value = "类目排序")
    private String orderNum;
    @ApiModelProperty(value = "数据编号")
    private String dataId;
    @ApiModelProperty(value = "数据名称")
    private String dataName;
    @ApiModelProperty(value = "数据状态")
    private String status;
    @ApiModelProperty(value = "店铺编号")
    private String shopNo;

}
