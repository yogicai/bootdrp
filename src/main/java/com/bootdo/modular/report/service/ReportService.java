package com.bootdo.modular.report.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.InstituteType;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.modular.report.dao.ReportDao;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author L
 * @since 2020-11-10 14:48
 */
@Service
public class ReportService {
    @Resource
    private ReportDao reportDao;


    @Transactional(readOnly = true)
    public R sRecon(Map<String, Object> params) {
        String type = MapUtil.getStr(params, "type");
        String showDebt = MapUtil.getStr(params, "showDebt", "1"); //是否查询有欠款客户（0:是，其他:否）
        String start = MapUtil.getStr(params, "start", DateUtils.getYearBegin());
        String end = MapUtil.getStr(params, "end", DateUtils.getYearEnd());
        String billRegion = StrUtil.subPre(start, 10) + "_" + StrUtil.subPre(end, 10);

        List<Map<String, Object>> data;
        if (InstituteType.CUSTOMER.name().equals(type)) {
            data = reportDao.sReconC(ImmutableMap.of("start", start, "end", end, "showDebt", showDebt, "instituteId", MapUtil.getStr(params, "instituteId")));
        } else {
            data = reportDao.sReconV(ImmutableMap.of("start", start, "end", end, "showDebt", showDebt, "instituteId", MapUtil.getStr(params, "instituteId")));
        }
        return R.ok(ImmutableMap.of("result", data, "billRegion", billRegion, "start", StrUtil.subPre(start, 10), "end", StrUtil.subPre(end, 10)));
    }

    @Transactional(readOnly = true)
    public R saleProduct(Map<String, Object> params) {
        List<Map<String, Object>> data = reportDao.saleProduct(params);
        String startDate = "9999-99-99", endDate = "0000-00-00";
        for (Map<String, Object> map : data) {
            startDate = startDate.compareTo(MapUtil.getStr(map, "startDate")) < 0 ? startDate : MapUtil.getStr(map, "startDate");
            endDate = endDate.compareTo(MapUtil.getStr(map, "endDate")) > 0 ? endDate : MapUtil.getStr(map, "endDate");
        }
        return R.ok(ImmutableMap.of("result", data, "billRegion", startDate + "_" + endDate, "start", startDate, "end", endDate));
    }
}
