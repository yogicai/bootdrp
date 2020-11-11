package com.bootdo.report.service;

import com.bootdo.common.config.Constant;
import com.bootdo.common.enumeration.InstituteType;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.R;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.report.dao.ReportDao;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author caiyz
 * @since 2020-11-10 14:48
 */
@Service
public class ReportService {
    @Resource
    private ReportDao reportDao;


    @Transactional
    public R sRecon(Map<String, Object> params){
        String type = MapUtils.getString(params, "type");
        String showDebt = MapUtils.getString(params, "showDebt", "1"); //是否查询有欠款客户（0:是，其他:否）
        String start = MapUtils.getStringB(params, "start", DateUtils.getYearBegin());
        String end = MapUtils.getStringB(params, "end", DateUtils.getYearEnd());
        String billRegion = StringUtil.substring(start, 10) + "_" + StringUtil.substring(end, 10);

        List<Map<String, Object>> data;
        if (InstituteType.CUSTOMER.name().equals(type)) {
            data = reportDao.sReconC(ImmutableMap.of("start", start, "end", end,"showDebt", showDebt,"instituteId", MapUtils.getString(params, "instituteId")));
        } else {
            data = reportDao.sReconV(ImmutableMap.of("start", start, "end", end,"showDebt", showDebt,"instituteId", MapUtils.getString(params, "instituteId")));
        }
        return R.ok(ImmutableMap.of("result", data, "billRegion", billRegion, "start", StringUtil.substring(start, 10), "end", StringUtil.substring(end, 10)));
    }

    @Transactional
    public R mainTab(Map<String, Object> params){
        List<Map<String, Object>> data = Lists.newArrayList();
        String type = MapUtils.getString(params, "type");
        if (Constant.MAIN_TAB_CUSTOMER.equals(type)) {
            data = reportDao.mainTabCustomer(params);
        } else if (Constant.MAIN_TAB_PRODUCT.equals(type)) {
            data = reportDao.mainTabProduct(params);
        }
        return R.ok(ImmutableMap.of("result", data));
    }

    @Transactional
    public R saleProduct(Map<String, Object> params){
        List<Map<String, Object>> data = reportDao.saleProduct(params);
        String startDate = "9999-99-99", endDate = "0000-00-00";
        for (Map<String, Object> map : data) {
            startDate = startDate.compareTo(MapUtils.getString(map, "startDate")) < 0 ? startDate : MapUtils.getString(map, "startDate");
            endDate = endDate.compareTo(MapUtils.getString(map, "endDate")) > 0 ? endDate : MapUtils.getString(map, "endDate");
        }
        return R.ok(ImmutableMap.of("result", data, "billRegion", startDate + "_" + endDate, "start", startDate, "end", endDate));
    }
}
