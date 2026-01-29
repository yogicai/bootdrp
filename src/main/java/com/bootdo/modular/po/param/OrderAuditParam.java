package com.bootdo.modular.po.param;

import com.bootdo.core.enums.AuditStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    @Schema(description = "单据号")
    private List<String> billNos;

    @NotNull
    @Schema(description = "审核状态")
    private AuditStatus auditStatus;

}
