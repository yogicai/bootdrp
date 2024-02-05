package com.bootdo.modular.report.param;

import com.bootdo.core.enums.InstituteType;
import com.bootdo.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SReconParam extends BaseParam {

    @ApiModelProperty(value = "客户、供应商")
    private String instituteId;

    @ApiModelProperty(value = "报表类型：（CUSTOMER、VENDOR）")
    private InstituteType type;

    @ApiModelProperty(value = "是否查询有欠款客户：（0：是、其他：否）")
    private String showDebt;

}
