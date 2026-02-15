package com.bootdo.modular.workbench.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.enums.AuditStatus;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.po.service.OrderService;
import com.bootdo.modular.report.enums.BillStatType;
import com.bootdo.modular.report.enums.EChartSeriesType;
import com.bootdo.modular.report.result.SEBillTotalResult;
import com.bootdo.modular.report.result.SEDebtTotalResult;
import com.bootdo.modular.report.result.echart.EChartOption;
import com.bootdo.modular.report.result.echart.PieData;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.bootdo.modular.se.service.SEOrderService;
import com.bootdo.modular.workbench.dao.WorkbenchDao;
import com.bootdo.modular.workbench.param.PBalanceParam;
import com.bootdo.modular.workbench.param.SEBillTotalParam;
import com.bootdo.modular.workbench.result.*;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * @author L
 * @since 2020-11-10 14:48
 */
@Service
public class WorkbenchService {
    @Resource
    private OrderService orderService;
    @Resource
    private SEOrderService seOrderService;
    @Resource
    private WorkbenchDao workbenchDao;

    /**
     * 饼图展示前十名
     */
    public static final int TOP_COUNT = 9;
    /**
     * 饼图展示前十名
     */
    private static final String TOP_NAME = "其他";
    /**
     * 12月份
     */
    private final List<String> month_series = CollUtil.newArrayList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");


    public SEBillTotalResult pBalanceTotal(SEBillTotalParam param) {
        SEBillTotalResult result = new SEBillTotalResult();
        List<SEOrderDO> list = seOrderService.list(Wrappers.lambdaQuery(SEOrderDO.class).ge(SEOrderDO::getBillDate, param.getBillDateStart())
                .eq(ObjectUtil.isNotEmpty(param.getShopNo()), SEOrderDO::getShopNo, param.getShopNo()).eq(SEOrderDO::getAuditStatus, param.getAuditStatus()));
        for (SEOrderDO seOrderDO : list) {
            result.setProfit(NumberUtils.add(result.getProfit(), NumberUtils.subtract(seOrderDO.getTotalAmount(), seOrderDO.getCostAmount())));
            result.setTotalAmount(NumberUtils.add(result.getTotalAmount(), seOrderDO.getTotalAmount()));
        }
        return result;
    }

    public SEDebtTotalResult pDebtTotal(PBalanceParam param) {
        SEDebtTotalResult result = new SEDebtTotalResult();
        List<SEOrderDO> seList = seOrderService.list(Wrappers.lambdaQuery(SEOrderDO.class)
                .eq(ObjectUtil.isNotEmpty(param.getShopNo()), SEOrderDO::getShopNo, param.getShopNo()).eq(SEOrderDO::getAuditStatus, AuditStatus.YES));
        for (SEOrderDO seOrderDO : seList) {
            result.setDebtAmount(NumberUtils.add(result.getDebtAmount(), NumberUtils.subtract(seOrderDO.getTotalAmount(), seOrderDO.getPaymentAmount())));
        }
        List<OrderDO> list = orderService.list(Wrappers.lambdaQuery(OrderDO.class)
                .eq(ObjectUtil.isNotEmpty(param.getShopNo()), OrderDO::getShopNo, param.getShopNo()).eq(OrderDO::getAuditStatus, AuditStatus.YES));
        for (OrderDO orderDO : list) {
            result.setDebtVAmount(NumberUtils.add(result.getDebtVAmount(), NumberUtils.subtract(orderDO.getTotalAmount(), orderDO.getPaymentAmount())));
        }
        return result;
    }

    public EChartOption pBillTrend(Map<String, Object> params) {
        EChartOption option = new EChartOption(1, 2, 3);
        String type = MapUtil.getStr(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<BillTrendItem> seList = workbenchDao.pBillTrend(params);

        option.getXAxis().get(0).getData().addAll(DateUtils.getDaySerial(type));

        BigDecimal billCount = BigDecimal.ZERO;
        BigDecimal maxYAxis = BigDecimal.ZERO;
        List<String> dayTimeSerial = DateUtils.getDayTimeSerial(type);
        for (String time : dayTimeSerial) {
            boolean exists = false;
            for (BillTrendItem item : seList) {
                if (StrUtil.equals(time, item.getOTime())) {
                    BigDecimal count = item.getCount();
                    BigDecimal totalAmount = item.getTotalAmount();
                    billCount = billCount.compareTo(count) < 0 ? count : billCount;
                    maxYAxis = maxYAxis.compareTo(totalAmount) < 0 ? totalAmount : maxYAxis;

                    option.getSeries().get(0).getData().add(totalAmount);
                    option.getSeries().get(1).getData().add(NumberUtils.subtract(totalAmount, item.getPaymentAmount()));
                    option.getSeries().get(2).getData().add(count);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                option.getSeries().get(0).getData().add(0.0D);
                option.getSeries().get(1).getData().add(0.0D);
                option.getSeries().get(2).getData().add(0);
            }
        }

        option.getYAxis().get(0).setMax(NumberUtils.roundIntervalCeil(maxYAxis, 4, 5));
        option.getYAxis().get(0).setInterval(NumberUtils.roundInterval(maxYAxis, 4));
        option.getYAxis().get(1).setMax(NumberUtils.roundIntervalCeil(billCount, 4, 5));
        option.getYAxis().get(1).setInterval(NumberUtils.roundInterval(billCount, 4));

        return option;
    }

    public EChartOption pBillTrendPie(Map<String, Object> params) {
        EChartOption option = new EChartOption(0, 0, 2);
        String type = MapUtil.getStr(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<BillTrendPieItem> seList = workbenchDao.pBillTrendPie(params);

        int count = 1;
        BigDecimal profitAmountOther = BigDecimal.ZERO, totalAmountOther = BigDecimal.ZERO;
        for (BillTrendPieItem item : seList) {
            if (count <= TOP_COUNT) {
                option.getLegend().getData().add(item.getName());
                option.getSeries().get(0).getData().add(new PieData(item.getName(), item.getTotalAmount()));
                option.getSeries().get(1).getData().add(new PieData(item.getName(), item.getProfitAmount()));
            } else {
                profitAmountOther = NumberUtils.add(profitAmountOther, item.getProfitAmount());
                totalAmountOther = NumberUtils.add(totalAmountOther, item.getTotalAmount());
            }
            count++;
        }
        if (TOP_COUNT < seList.size()) {
            option.getLegend().getData().add(TOP_NAME);
            option.getSeries().get(0).getData().add(new PieData(TOP_NAME, profitAmountOther));
            option.getSeries().get(1).getData().add(new PieData(TOP_NAME, totalAmountOther));
        }
        return option;
    }

    public CashTotalResult pCashTotal(Map<String, Object> params) {
        List<CashTrendItem> itemList = workbenchDao.pCashTrend(params);
        return itemList.stream().findFirst().map(item -> new CashTotalResult()
                .setProfitAmountT(item.getProfitAmount())
                .setCashFlowAmountT(item.getCashFlowAmount())
        ).orElseGet(CashTotalResult::new);
    }

    public EChartOption pCashTrend(Map<String, Object> params) {
        EChartOption option = new EChartOption(1, 2, 3);
        String type = MapUtil.getStr(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<CashTrendItem> seList = workbenchDao.pCashTrend(params);

        option.getXAxis().get(0).getData().addAll(DateUtils.getDaySerial(type));

        BigDecimal maxYAxis = BigDecimal.ZERO;
        List<String> dayTimeSerial = DateUtils.getDayTimeSerial(type);
        for (String time : dayTimeSerial) {
            boolean exists = false;
            for (CashTrendItem trendItem : seList) {
                if (StrUtil.equals(time, trendItem.getOTime())) {
                    BigDecimal profitAmount = trendItem.getProfitAmount();
                    maxYAxis = maxYAxis.compareTo(profitAmount) < 0 ? profitAmount : maxYAxis;

                    option.getSeries().get(0).getData().add(profitAmount);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                option.getSeries().get(0).getData().add(0.0D);
            }
        }
        option.getYAxis().get(0).setMax(NumberUtils.roundIntervalCeil(maxYAxis, 4, 5));
        option.getYAxis().get(0).setInterval(NumberUtils.roundInterval(maxYAxis, 4));
        return option;
    }

    public EChartOption pHisBillTrend(Map<String, Object> params) {

        //图表数据类型
        BillStatType type = BillStatType.valueOf(MapUtil.getStr(params, "type"));
        //销售单历史数据
        List<HisPBillTrendItem> seList = workbenchDao.pHisBillTrend(params);

        TreeSet<String> yearSet = new TreeSet<>();
        MultiKeyMap<String, HisPBillTrendItem> multiKeyMap = new MultiKeyMap<>();
        seList.forEach(item -> {
            multiKeyMap.put(item.getOTime(), item.getTime(), item);
            yearSet.add(item.getOTime());
        });

        //图表数据
        EChartOption option = new EChartOption(1, 1, yearSet.size());
        //图表数据Series
        List<String> yearList = new ArrayList<>(yearSet);
        IntStream.rangeClosed(0, yearList.size() - 1).forEach(i -> {
            IntStream.rangeClosed(1, 12).forEach(m -> {
                String year = yearList.get(i);
                BigDecimal value = BeanUtil.getProperty(multiKeyMap.get(year, String.valueOf(m)), type.getValue());
                option.getSeries().get(i).getData().add(NumberUtil.nullToZero(value));
            });

            option.getSeries().get(i).setType(EChartSeriesType.BAR.getValue());
            option.getSeries().get(i).setName(yearList.get(i) + "年");
            option.getLegend().getData().add(yearList.get(i) + "年");
        });


        option.getTitle().setText(type.getText());
        option.getXAxis().get(0).getData().addAll(month_series);
        //销售单Series最大值
        double maxYAxis = option.getSeries().stream().flatMap(s -> s.getData().stream()).mapToDouble(s -> Double.parseDouble(s.toString())).max().orElse(NumberUtils.DOUBLE_ZERO);
        //设置图表Y轴坐标
        option.getYAxis().get(0).setMax(NumberUtils.roundIntervalCeil(BigDecimal.valueOf(maxYAxis), 4, 5));
        option.getYAxis().get(0).setInterval(NumberUtils.roundInterval(BigDecimal.valueOf(maxYAxis), 4));

        return option;
    }
}
