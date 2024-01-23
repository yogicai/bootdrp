package com.bootdo.modular.workbench.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.modular.engage.result.BalanceTotalResult;
import com.bootdo.modular.engage.service.ProductBalanceService;
import com.bootdo.modular.report.result.SEBillTotalResult;
import com.bootdo.modular.report.result.SEDebtTotalResult;
import com.bootdo.modular.report.result.echart.EChartOption;
import com.bootdo.modular.workbench.service.WorkbenchService;
import com.bootdo.modular.system.controller.BaseController;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 工作台
 *
 * @author yogiCai
 * @date 2018-02-25 11:17:02
 */
@Api(tags = "工作台")
@Controller
@RequestMapping("/workbench")
public class WorkbenchController extends BaseController {
    @Resource
    private WorkbenchService workbenchService;
    @Resource
    private ProductBalanceService productBalanceService;

    /**
     * 销售总额 + 销售毛利(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pSeTotal")
    public R pSeTotal(@RequestBody Map<String, Object> params, Model model) {
        SEBillTotalResult result;
        int days = MapUtil.getInt(params, "days", 0);
        //过去多少天
        String type = MapUtil.getStr(params, "type");
        //WEEK MONTH
        Map<String, Object> param = StrUtil.isEmpty(type) ? ImmutableMap.of("billDateStart", DateUtils.getStartStr(-days), "audit", AuditStatus.YES.name()) : ImmutableMap.of("billDateStart", DateUtils.getStartStr(type), "audit", AuditStatus.YES.name());
        result = workbenchService.pBalanceTotal(param);
        return R.ok().put("result", result);
    }

    /**
     * 客户欠款 + 供应商欠款(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pDebtTotal")
    public R pDebtTotal(@RequestBody Map<String, Object> params, Model model) {
        SEDebtTotalResult result = workbenchService.pDebtTotal(ImmutableMap.of("audit", AuditStatus.YES.name()));
        return R.ok().put("result", result);
    }

    /**
     * 订单趋势图(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrend")
    public R pSEBillTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = workbenchService.pBillTrend(params);
        return R.ok().put("result", option);
    }

    /**
     * 订单趋势饼图(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrendPie")
    public R pSEBillTrendPie(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = workbenchService.pBillTrendPie(params);
        return R.ok().put("result", option);
    }

    /**
     * 营业利润 + 营业净现金流(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTotal")
    public R pCashTotal(@RequestBody Map<String, Object> params, Model model) {
        return workbenchService.pCashTotal(ImmutableMap.of("audit", AuditStatus.YES.name(), "billDate", DateUtils.getYearBegin()));
    }

    /**
     * 营业利润 + 营业净现金流 (首页趋势图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTrend")
    public R pCashTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = workbenchService.pCashTrend(params);
        return R.ok().put("result", option);
    }

    /**
     * 历史订单趋势图 订单金额 订单数 毛利润 欠款 (首页趋势图)
     */
    @ResponseBody
    @PostMapping(value = "/pHisPCashTrend")
    public R pHisPBillTrend(@RequestBody Map<String, Object> params, Model model) {
        EChartOption option = workbenchService.pHisPBillTrend(params);
        return R.ok().put("result", option);
    }

    /**
     * 库存余量 + 成本(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBalanceTotal")
    public R pBalanceTotal(@RequestBody Map<String, Object> params, Model model) {
        params.put("status", 1);
        BalanceTotalResult result = productBalanceService.pBalanceTotal(params);
        return R.ok().put("result", result);
    }
}
