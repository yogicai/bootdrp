package com.bootdo.excel.param;

import com.bootdo.common.enumeration.BillType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Date;

/** 
 * 导入订单
 * 
 * @author caiyz
 * @since 2023-03-27 14:53
 */
@Data
public class OrderImportParam {

    @NotNull
    @ApiModelProperty(value = "单据日期_开始")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date billDateB;

    @NotNull
    @ApiModelProperty(value = "单据日期_开始")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date billDateE;

    @NotNull
    @ApiModelProperty(value = "单据类型")
    private BillType billType;

    @NotNull
    @ApiModelProperty(value = "单据文件流")
    private MultipartFile file;
}
