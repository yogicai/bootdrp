package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class ProductQryParam extends BaseParam {

    @ApiModelProperty(value = "类目分类")
    private String type;

    @ApiModelProperty(value = "状态")
    private String status;

}
