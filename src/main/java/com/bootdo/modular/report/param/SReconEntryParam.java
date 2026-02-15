package com.bootdo.modular.report.param;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.enums.InstituteType;
import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.report.enums.DimensionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SReconEntryParam extends BaseParam {

    @Schema(description = "客户、供应商")
    private String instituteId;

    @Schema(description = "报表类型：（CUSTOMER、VENDOR）")
    private InstituteType type;

    @Schema(description = "单据日期")
    private String billRegion;

    @Schema(description = "统计维度")
    private DimensionType dimensionType;


    @Override
    public Date getStart() {
        return getBillRegionDate()[0];
    }

    @Override
    public Date getEnd() {
        return getBillRegionDate()[1];
    }

    private Date[] getBillRegionDate() {
        if (DimensionType.USER.equals(dimensionType)) {
            List<String> dates = StrUtil.split(this.billRegion, StrUtil.UNDERLINE, true, true);
            Date startDate = DateUtil.beginOfDay(DateUtil.parse(dates.get(0)));
            Date endDate = DateUtil.endOfDay(DateUtil.parse(dates.get(1)));
            return new Date[]{startDate, endDate};
        } else if (DimensionType.DAY.equals(dimensionType)) {
            Date date = DateUtil.parse(this.billRegion);
            Date startDate = DateUtil.beginOfDay(date);
            Date endDate = DateUtil.endOfDay(date);
            return new Date[]{startDate, endDate};
        } else if (DimensionType.MONTH.equals(dimensionType)) {
            Date date = DateUtil.parse(this.billRegion, DatePattern.NORM_MONTH_PATTERN);
            Date startDate = DateUtil.beginOfMonth(date);
            Date endDate = DateUtil.endOfMonth(date);
            return new Date[]{startDate, endDate};
        } else if (DimensionType.YEAR.equals(dimensionType)) {
            Date date = DateUtil.parse(this.billRegion, DatePattern.NORM_YEAR_PATTERN);
            Date startDate = DateUtil.beginOfYear(date);
            Date endDate = DateUtil.endOfYear(date);
            return new Date[]{startDate, endDate};
        }
        return new Date[]{null, null};
    }
}
