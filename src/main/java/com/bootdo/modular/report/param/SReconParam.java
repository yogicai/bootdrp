package com.bootdo.modular.report.param;

import com.bootdo.core.enums.InstituteType;
import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.report.enums.DimensionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SReconParam extends BaseParam {

    @Schema(description = "客户、供应商")
    private String instituteId;

    @Schema(description = "报表类型：（CUSTOMER、VENDOR）")
    private InstituteType type;

    @Schema(description = "是否查询有欠款客户：（0：是、其他：否）")
    private String showDebt;

    @Schema(description = "统计维度")
    private DimensionType dimensionType;

}
