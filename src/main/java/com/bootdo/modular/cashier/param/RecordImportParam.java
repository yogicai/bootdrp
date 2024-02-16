package com.bootdo.modular.cashier.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 导入
 *
 * @author L
 * @since 2023-03-27 14:53
 */
@Data
public class RecordImportParam {

    @NotBlank
    @ApiModelProperty(value = "所属店铺")
    private String shopNo;

    @NotNull
    @ApiModelProperty(value = "单据文件流")
    private MultipartFile file;
}
