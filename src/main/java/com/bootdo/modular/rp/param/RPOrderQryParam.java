package com.bootdo.modular.rp.param;

import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收付款单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RPOrderQryParam extends BaseParam {

    @Schema(description = "核销人员ID")
    private String checkId;

    @Schema(description = "订单类型")
    private String billType;

    @Schema(description = "订单来源")
    private String billSource;

    @Schema(description = "审核状态")
    private String auditStatus;

    @Schema(description = "关联订单号")
    private String srcBillNo;

    @Schema(description = "出纳订单号")
    private String billNo;

}
