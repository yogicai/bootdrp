package com.bootdo.modular.data.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仓库管理
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class StockQryParam extends BaseParam {

    @ApiModelProperty(value = "状态")
    private String status;

    
}
