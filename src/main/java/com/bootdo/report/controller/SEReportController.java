package com.bootdo.report.controller;

import cn.hutool.core.map.MapUtil;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.enumeration.AuditStatus;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.R;
import com.bootdo.common.utils.StringUtil;
import com.bootdo.report.controller.response.SEBillTotalResult;
import com.bootdo.report.controller.response.SEDebtTotalResult;
import com.bootdo.report.controller.response.echart.EChartOption;
import com.bootdo.report.service.SEReportService;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 入库出库单
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */

@Controller
@RequestMapping("/report")
@Api(value = "首页-销售单报表")
public class SEReportController extends BaseController {
    @Resource
    private SEReportService seReportService;

    /**
     * 销售总额 + 销售毛利(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pSeTotal")
    @RequiresPermissions("wh:report:pBalance")
    public R pSeTotal(@RequestBody Map<String, Object> params, Model model) {
        SEBillTotalResult result;
        int days = MapUtil.getInt(params, "days", 0);
        //过去多少天
        String type = MapUtil.getStr(params, "type");
        //WEEK MONTH
        Map<String, Object> param = StringUtil.isEmpty(type) ? ImmutableMap.of("billDateStart", DateUtils.getStartStr(-days), "audit", AuditStatus.YES.name()) : ImmutableMap.of("billDateStart", DateUtils.getStartStr(type), "audit", AuditStatus.YES.name());
        result = seReportService.pBalanceTotal(param);
        return R.ok().put("result", result);
    }

    /**
     * 客户欠款 + 供应商欠款(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pDebtTotal")
    @RequiresPermissions("wh:report:pBalance")
    public R pDebtTotal(@RequestBody Map<String, Object> params, Model model) {
        SEDebtTotalResult result = seReportService.pDebtTotal(ImmutableMap.of("audit", AuditStatus.YES.name()));
        return R.ok().put("result", result);
    }

    /**
     * 订单趋势图(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrend")
    @RequiresPermissions("wh:report:pBalance")
    public R pSEBillTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = seReportService.pBillTrend(params);
        return R.ok().put("result", option);
    }

    /**
     * 订单趋势饼图(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrendPie")
    @RequiresPermissions("wh:report:pBalance")
    public R pSEBillTrendPie(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = seReportService.pBillTrendPie(params);
        return R.ok().put("result", option);
    }

    /**
     * 营业利润 + 营业净现金流(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTotal")
    @RequiresPermissions("wh:report:pBalance")
    public R pCashTotal(@RequestBody Map<String, Object> params, Model model) {
        return seReportService.pCashTotal(ImmutableMap.of("audit", AuditStatus.YES.name(), "billDate", DateUtils.getYearBegin()));
    }

    /**
     * 营业利润 + 营业净现金流 (首页趋势图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTrend")
    @RequiresPermissions("wh:report:pBalance")
    public R pCashTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = seReportService.pCashTrend(params);
        return R.ok().put("result", option);
    }

    /**
     * 历史订单趋势图 订单金额 订单数 毛利润 欠款 (首页趋势图)
     */
    @ResponseBody
    @PostMapping(value = "/pHisPCashTrend")
    @RequiresPermissions("wh:report:pBalance")
    public R pHisPBillTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = seReportService.pHisPBillTrend(params);
        return R.ok().put("result", option);
    }
}
