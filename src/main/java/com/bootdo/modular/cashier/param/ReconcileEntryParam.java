package com.bootdo.modular.cashier.param;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.bootdo.core.enums.BillType;
import com.bootdo.core.pojo.base.param.BaseParam;
import com.bootdo.modular.cashier.enums.DateTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class ReconcileEntryParam extends BaseParam {

    @Schema(description = "单据类型")
    private BillType billType;

    @Schema(description = "单据日期")
    private String billRegion;

    @Schema(description = "日期类型")
    private DateTypeEnum dateType;


    @Override
    public Date getStart() {
        return getBillRegionDate()[0];
    }

    @Override
    public Date getEnd() {
        return getBillRegionDate()[1];
    }

    private Date[] getBillRegionDate() {
        if (DateTypeEnum.DAY.equals(dateType)) {
            Date date = DateUtil.parse(this.billRegion);
            Date startDate = DateUtil.beginOfDay(date);
            Date endDate = DateUtil.endOfDay(date);
            return new Date[]{startDate, endDate};
        } else if (DateTypeEnum.MONTH.equals(dateType)) {
            Date date = DateUtil.parse(this.billRegion, DatePattern.NORM_MONTH_PATTERN);
            Date startDate = DateUtil.beginOfMonth(date);
            Date endDate = DateUtil.endOfMonth(date);
            return new Date[]{startDate, endDate};
        } else if (DateTypeEnum.YEAR.equals(dateType)) {
            Date date = DateUtil.parse(this.billRegion, DatePattern.NORM_YEAR_PATTERN);
            Date startDate = DateUtil.beginOfYear(date);
            Date endDate = DateUtil.endOfYear(date);
            return new Date[]{startDate, endDate};
        }
        return new Date[]{null, null};
    }
}
