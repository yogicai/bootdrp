package com.bootdo.modular.excel.param;

import com.bootdo.core.enums.BillType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 导入订单
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class OrderImportParam {

    @NotNull
    @Schema(description = "单据日期_开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @NotNull
    @Schema(description = "单据日期_开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    @NotNull
    @Schema(description = "单据类型")
    private BillType billType;

    @NotBlank
    @Schema(description = "所属店铺")
    private String shopNo;

    @NotNull
    @Schema(description = "单据文件流")
    private MultipartFile file;
}
