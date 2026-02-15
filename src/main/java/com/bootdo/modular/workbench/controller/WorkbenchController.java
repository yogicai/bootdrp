package com.bootdo.modular.workbench.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.enums.CommonStatus;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.modular.engage.result.BalanceTotalResult;
import com.bootdo.modular.engage.service.ProductBalanceService;
import com.bootdo.modular.report.result.SEBillTotalResult;
import com.bootdo.modular.report.result.SEDebtTotalResult;
import com.bootdo.modular.report.result.echart.EChartOption;
import com.bootdo.modular.system.controller.BaseController;
import com.bootdo.modular.workbench.param.PBalanceParam;
import com.bootdo.modular.workbench.param.PBillTrendParam;
import com.bootdo.modular.workbench.param.SEBillTotalParam;
import com.bootdo.modular.workbench.result.CashTotalResult;
import com.bootdo.modular.workbench.service.WorkbenchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 工作台
 *
 * @author yogiCai
 * @since 2018-02-25 11:17:02
 */
@Tag(name = "工作台")
@Controller
@RequestMapping("/workbench")
public class WorkbenchController extends BaseController {
    @Resource
    private WorkbenchService workbenchService;
    @Resource
    private ProductBalanceService productBalanceService;


    /**
     * 1、库存余量 + 成本(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBalanceTotal")
    public R<BalanceTotalResult> pBalanceTotal(@RequestBody PBalanceParam param) {
        Map<String, Object> params = MapUtil.<String, Object>builder()
                .put(StrUtil.isNotBlank(param.getShopNo()), "shopNo", CollUtil.newArrayList(param.getShopNo()))
                .put("status", CommonStatus.ENABLE.getValue())
                .build();
        return R.ok(productBalanceService.pBalanceTotal(params));
    }

    /**
     * 2、销售总额 + 销售毛利(首页统计图2)
     */
    @ResponseBody
    @PostMapping(value = "/pSeTotal")
    public R<SEBillTotalResult> pSeTotal(@RequestBody PBalanceParam param) {
        SEBillTotalResult result = workbenchService.pBalanceTotal(SEBillTotalParam.builder()
                .billDateStart(DateUtils.getStartStr(Constant.Q_MONTH))
                .auditStatus(AuditStatus.YES)
                .shopNo(param.getShopNo())
                .build());
        return R.ok(result);
    }

    /**
     * 3、客户欠款 + 供应商欠款(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pDebtTotal")
    public R<SEDebtTotalResult> pDebtTotal(@RequestBody PBalanceParam param) {
        return R.ok(workbenchService.pDebtTotal(param));
    }

    /**
     * 4、营业利润 + 营业净现金流(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTotal")
    public R<CashTotalResult> pCashTotal(@RequestBody PBalanceParam param) {
        return R.ok(workbenchService.pCashTotal(MapUtil.<String, Object>builder()
                .put("audit", AuditStatus.YES.name())
                .put("billDate", DateUtils.getYearBegin())
                .put("shopNo", param.getShopNo())
                .build())
        );
    }

    /**
     * 营业利润 + 营业净现金流 (首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTrend")
    public R<EChartOption> pCashTrend(@RequestBody PBillTrendParam param) {
        return R.ok(workbenchService.pCashTrend(BeanUtil.beanToMap(param)));
    }

    /**
     * 订单趋势图
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrend")
    public R<EChartOption> pBillTrend(@RequestBody PBillTrendParam param) {
        return R.ok(workbenchService.pBillTrend(BeanUtil.beanToMap(param)));
    }

    /**
     * 订单趋势饼图
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrendPie")
    public R<EChartOption> pBillTrendPie(@RequestBody PBillTrendParam param) {
        return R.ok(workbenchService.pBillTrendPie(BeanUtil.beanToMap(param)));
    }

    /**
     * 历史订单趋势图 订单金额 订单数 毛利润 欠款 (首页趋势图)
     */
    @ResponseBody
    @PostMapping(value = "/pHisCashTrend")
    public R<EChartOption> pHisCashTrend(@RequestBody PBillTrendParam param) {
        return R.ok(workbenchService.pHisBillTrend(BeanUtil.beanToMap(param)));
    }

}
