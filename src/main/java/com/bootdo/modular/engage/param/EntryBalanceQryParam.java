package com.bootdo.modular.engage.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品库存
 *
 * @author L
 * @since 2024-02-21 13:14
 */
@Data
public class EntryBalanceQryParam extends BaseParam {

    @Schema(description = "商品编号")
    private String entryId;

    @Schema(description = "商品状态")
    private String status;

}
