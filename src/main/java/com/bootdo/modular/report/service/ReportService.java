package com.bootdo.modular.report.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.bootdo.core.enums.InstituteType;
import com.bootdo.core.pojo.response.R;
import com.bootdo.modular.report.dao.ReportDao;
import com.bootdo.modular.report.param.SReconParam;
import com.bootdo.modular.report.param.SaleProductParam;
import com.google.common.collect.ImmutableMap;
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


    /**
     * 客户应收欠款
     * 供应商应付款
     */
    @Transactional(readOnly = true)
    public R sRecon(SReconParam param) {
        Map<String, Object> params = BeanUtil.beanToMap(param);
        String startStr = DateUtil.formatDate(param.getStart());
        String endStr = DateUtil.formatDate(param.getEnd());

        List<Map<String, Object>> data;
        if (InstituteType.CUSTOMER.equals(param.getType())) {
            data = reportDao.sReconC(params);
        } else {
            data = reportDao.sReconV(params);
        }
        return R.ok(ImmutableMap.of("result", data, "billRegion", startStr + "_" + endStr, "start", startStr, "end", endStr));
    }

    /**
     * 销售统计报表
     */
    @Transactional(readOnly = true)
    public R saleProduct(SaleProductParam param) {
        List<Map<String, Object>> data = reportDao.saleProduct(BeanUtil.beanToMap(param));
        String startDate = "9999-99-99", endDate = "0000-00-00";
        for (Map<String, Object> map : data) {
            startDate = startDate.compareTo(MapUtil.getStr(map, "startDate")) < 0 ? startDate : MapUtil.getStr(map, "startDate");
            endDate = endDate.compareTo(MapUtil.getStr(map, "endDate")) > 0 ? endDate : MapUtil.getStr(map, "endDate");
        }
        return R.ok(ImmutableMap.of("result", data, "billRegion", startDate + "_" + endDate, "start", startDate, "end", endDate));
    }
}
