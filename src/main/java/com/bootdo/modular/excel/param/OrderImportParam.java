package com.bootdo.modular.excel.param;

import com.bootdo.core.enums.BillType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @ApiModelProperty(value = "单据日期_开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @NotNull
    @ApiModelProperty(value = "单据日期_开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    @NotNull
    @ApiModelProperty(value = "单据类型")
    private BillType billType;

    @NotBlank
    @ApiModelProperty(value = "所属店铺")
    private String shopNo;

    @NotNull
    @ApiModelProperty(value = "单据文件流")
    private MultipartFile file;
}
