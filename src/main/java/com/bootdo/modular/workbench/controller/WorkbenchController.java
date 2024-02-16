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
import com.bootdo.modular.workbench.service.WorkbenchService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
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
 * @since 2018-02-25 11:17:02
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
     * 1、库存余量 + 成本(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pBalanceTotal")
    public R pBalanceTotal(@RequestBody PBalanceParam param) {
        Map<String, Object> params = MapUtil.<String, Object>builder()
                .put(StrUtil.isNotBlank(param.getShopNo()), "shopNo", CollUtil.newArrayList(param.getShopNo()))
                .put("status", CommonStatus.ENABLE.getValue())
                .build();
        BalanceTotalResult result = productBalanceService.pBalanceTotal(params);
        return R.ok().put("result", result);
    }

    /**
     * 2、销售总额 + 销售毛利(首页统计图2)
     */
    @ResponseBody
    @PostMapping(value = "/pSeTotal")
    public R pSeTotal(@RequestBody PBalanceParam param) {
        SEBillTotalResult result = workbenchService.pBalanceTotal(SEBillTotalParam.builder()
                .billDateStart(DateUtils.getStartStr(Constant.Q_MONTH))
                .auditStatus(AuditStatus.YES)
                .shopNo(param.getShopNo())
                .build());
        return R.ok().put("result", result);
    }

    /**
     * 3、客户欠款 + 供应商欠款(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pDebtTotal")
    public R pDebtTotal(@RequestBody PBalanceParam param) {
        SEDebtTotalResult result = workbenchService.pDebtTotal(param);
        return R.ok().put("result", result);
    }

    /**
     * 4、营业利润 + 营业净现金流(首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTotal")
    public R pCashTotal(@RequestBody PBalanceParam param) {
        return workbenchService.pCashTotal(MapUtil.<String, Object>builder()
                .put("audit", AuditStatus.YES.name())
                .put("billDate", DateUtils.getYearBegin())
                .put("shopNo", param.getShopNo())
                .build());
    }

    /**
     * 营业利润 + 营业净现金流 (首页统计图)
     */
    @ResponseBody
    @PostMapping(value = "/pCashTrend")
    public R pCashTrend(@RequestBody PBillTrendParam param) {
        EChartOption option = workbenchService.pCashTrend(BeanUtil.beanToMap(param));
        return R.ok().put("result", option);
    }

    /**
     * 订单趋势图
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrend")
    public R pBillTrend(@RequestBody PBillTrendParam param) {
        EChartOption option = workbenchService.pBillTrend(BeanUtil.beanToMap(param));
        return R.ok().put("result", option);
    }

    /**
     * 订单趋势饼图
     */
    @ResponseBody
    @PostMapping(value = "/pBillTrendPie")
    public R pBillTrendPie(@RequestBody PBillTrendParam param) {
        EChartOption option = workbenchService.pBillTrendPie(BeanUtil.beanToMap(param));
        return R.ok().put("result", option);
    }

    /**
     * 历史订单趋势图 订单金额 订单数 毛利润 欠款 (首页趋势图)
     */
    @ResponseBody
    @PostMapping(value = "/pHisCashTrend")
    public R pHisCashTrend(@RequestBody PBillTrendParam param) {
        EChartOption option = workbenchService.pHisBillTrend(BeanUtil.beanToMap(param));
        return R.ok().put("result", option);
    }

}
