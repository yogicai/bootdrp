package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShopQryParam extends BaseParam {

    @ApiModelProperty(value = "店铺名称")
    private String name;

    @ApiModelProperty(value = "店铺管理员")
    private String managerId;

    @ApiModelProperty(value = "状态")
    private String status;

}
