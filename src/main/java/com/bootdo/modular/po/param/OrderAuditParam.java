package com.bootdo.modular.po.param;

import com.bootdo.core.enums.AuditStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 销售单
 *
 * @author L
 * @since 2024-01-26 15:48
 */
@Data
public class OrderAuditParam {

    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "单据号")
    private List<String> billNos;

    @NotNull
    @ApiModelProperty(value = "审核状态")
    private AuditStatus auditStatus;

}
