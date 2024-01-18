package com.bootdo.modular.report.service;

import cn.hutool.core.map.MapUtil;
import com.bootdo.core.consts.Constant;
import com.bootdo.core.pojo.response.R;
import com.bootdo.core.utils.DateUtils;
import com.bootdo.core.utils.NumberUtils;
import com.bootdo.modular.po.dao.OrderDao;
import com.bootdo.modular.po.domain.OrderDO;
import com.bootdo.modular.report.dao.SEReportDao;
import com.bootdo.modular.report.enums.BillStatType;
import com.bootdo.modular.report.enums.EChartSeriesType;
import com.bootdo.modular.report.result.SEBillTotalResult;
import com.bootdo.modular.report.result.SEDebtTotalResult;
import com.bootdo.modular.report.result.echart.EChartOption;
import com.bootdo.modular.report.result.echart.PieData;
import com.bootdo.modular.se.dao.SEOrderDao;
import com.bootdo.modular.se.domain.SEOrderDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class SEReportService {
    @Resource
    private SEOrderDao seOrderDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private SEReportDao seReportDao;

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
    private final List<String> month_series = Lists.newArrayList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");


    public SEBillTotalResult pBalanceTotal(Map<String, Object> params) {
        SEBillTotalResult result = new SEBillTotalResult();
        List<SEOrderDO> list = seOrderDao.list(params);
        for (SEOrderDO seOrderDO : list) {
            result.setProfit(NumberUtils.add(result.getProfit(), NumberUtils.subtract(seOrderDO.getTotalAmount(), seOrderDO.getCostAmount())));
            result.setTotalAmount(NumberUtils.add(result.getTotalAmount(), seOrderDO.getTotalAmount()));
        }
        return result;
    }

    public SEDebtTotalResult pDebtTotal(Map<String, Object> params) {
        SEDebtTotalResult result = new SEDebtTotalResult();
        List<SEOrderDO> seList = seOrderDao.list(params);
        for (SEOrderDO seOrderDO : seList) {
            result.setDebtAmount(NumberUtils.add(result.getDebtAmount(), NumberUtils.subtract(seOrderDO.getTotalAmount(), seOrderDO.getPaymentAmount())));
        }
        List<OrderDO> list = orderDao.list(params);
        for (OrderDO orderDO : list) {
            result.setDebtVAmount(NumberUtils.add(result.getDebtVAmount(), NumberUtils.subtract(orderDO.getTotalAmount(), orderDO.getPaymentAmount())));
        }
        return result;
    }

    public EChartOption pBillTrend(Map<String, Object> params) {
        EChartOption option = new EChartOption(1, 2, 3);
        String type = MapUtil.getStr(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<Map<String, Object>> seList = seReportDao.pBillTrend(params);

        option.getXAxis().get(0).getData().addAll(DateUtils.getDaySerial(type));

        BigDecimal billCount = BigDecimal.ZERO;
        BigDecimal maxYAxis = BigDecimal.ZERO;
        List<String> dayTimeSerial = DateUtils.getDayTimeSerial(type);
        for (String time : dayTimeSerial) {
            boolean exists = false;
            for (Map<String, Object> map : seList) {
                if (StringUtils.equals(time, MapUtil.getStr(map, "otime"))) {
                    BigDecimal count = MapUtil.get(map, "count", BigDecimal.class, BigDecimal.ZERO);
                    BigDecimal totalAmount = MapUtil.get(map, "totalAmount", BigDecimal.class, BigDecimal.ZERO);
                    billCount = billCount.compareTo(count) < 0 ? count : billCount;
                    maxYAxis = maxYAxis.compareTo(totalAmount) < 0 ? totalAmount : maxYAxis;

                    option.getSeries().get(0).getData().add(MapUtil.get(map, "totalAmount", BigDecimal.class, BigDecimal.ZERO));
                    option.getSeries().get(1).getData().add(NumberUtils.subtract(MapUtil.get(map, "totalAmount", BigDecimal.class, BigDecimal.ZERO), MapUtil.get(map, "paymentAmount", BigDecimal.class, BigDecimal.ZERO)));
                    option.getSeries().get(2).getData().add(MapUtil.getInt(map, "count"));
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
        List<Map<String, Object>> seList = seReportDao.pBillTrendPie(params);

        int count = 1;
        BigDecimal profitAmountOther = BigDecimal.ZERO, totalAmountOther = BigDecimal.ZERO;
        for (Map<String, Object> map : seList) {
            if (count <= TOP_COUNT) {
                option.getLegend().getData().add(MapUtil.getStr(map, "name"));
                option.getSeries().get(0).getData().add(new PieData(MapUtil.getStr(map, "name"), MapUtil.get(map, "totalAmount", BigDecimal.class, BigDecimal.ZERO)));
                option.getSeries().get(1).getData().add(new PieData(MapUtil.getStr(map, "name"), MapUtil.get(map, "profitAmount", BigDecimal.class, BigDecimal.ZERO)));
            } else {
                profitAmountOther = NumberUtils.add(profitAmountOther, MapUtil.get(map, "totalAmount", BigDecimal.class, BigDecimal.ZERO));
                totalAmountOther = NumberUtils.add(totalAmountOther, MapUtil.get(map, "profitAmount", BigDecimal.class, BigDecimal.ZERO));
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

    public R pCashTotal(Map<String, Object> params) {
        List<Map<String, Object>> list = seReportDao.pCashTrend(params);
        if (CollectionUtils.isNotEmpty(list)) {
            return R.ok(ImmutableMap.of("profitAmountT", MapUtil.get(list.get(0), "profitAmount", BigDecimal.class, BigDecimal.ZERO), "cashFlowAmountT", MapUtil.get(list.get(0), "cashFlowAmount", BigDecimal.class, BigDecimal.ZERO)));
        }
        return R.ok(ImmutableMap.of("profitAmountT", 0, "cashFlowAmountT", 0));
    }

    public EChartOption pCashTrend(Map<String, Object> params) {
        EChartOption option = new EChartOption(1, 2, 3);
        String type = MapUtil.getStr(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<Map<String, Object>> seList = seReportDao.pCashTrend(params);

        option.getXAxis().get(0).getData().addAll(DateUtils.getDaySerial(type));

        BigDecimal maxYAxis = BigDecimal.ZERO;
        List<String> dayTimeSerial = DateUtils.getDayTimeSerial(type);
        for (String time : dayTimeSerial) {
            boolean exists = false;
            for (Map<String, Object> map : seList) {
                if (StringUtils.equals(time, MapUtil.getStr(map, "otime"))) {
                    BigDecimal profitAmount = MapUtil.get(map, "profitAmount", BigDecimal.class, BigDecimal.ZERO);
                    maxYAxis = maxYAxis.compareTo(profitAmount) < 0 ? profitAmount : maxYAxis;

                    option.getSeries().get(0).getData().add(MapUtil.get(map, "profitAmount", BigDecimal.class, BigDecimal.ZERO));
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

    public EChartOption pHisPBillTrend(Map<String, Object> params) {

        //图表数据类型
        BillStatType type = BillStatType.valueOf(MapUtil.getStr(params, "type"));
        //销售单历史数据
        List<Map<String, Object>> seList = seReportDao.pHisPBillTrend(params);

        TreeSet<String> yearSet = new TreeSet<>();
        MultiKeyMap<String, Map<String, Object>> multiKeyMap = new MultiKeyMap<>();
        seList.forEach(m -> {
            multiKeyMap.put(MapUtil.getStr(m, "otime"), MapUtil.getStr(m, "time"), m);
            yearSet.add(MapUtil.getStr(m, "otime"));
        });

        //图表数据
        EChartOption option = new EChartOption(1, 1, yearSet.size());
        //图表数据Series
        List<String> yearList = new ArrayList<>(yearSet);
        IntStream.rangeClosed(0, yearList.size() - 1).forEach(i -> {
            IntStream.rangeClosed(1, 12).forEach(m -> {
                String year = yearList.get(i);
                double value = MapUtil.getDouble(multiKeyMap.get(year, String.valueOf(m)), type.getValue(), 0.0);
                option.getSeries().get(i).getData().add(value);
            });

            option.getSeries().get(i).setType(EChartSeriesType.BAR.getValue());
            option.getSeries().get(i).setName(yearList.get(i) + "年");
            option.getLegend().getData().add(yearList.get(i) + "年");
        });


        option.getTitle().setText(type.getText());
        option.getXAxis().get(0).getData().addAll(month_series);
        //销售单Series最大值
        double maxYAxis = option.getSeries().stream().flatMap(s -> s.getData().stream()).mapToDouble(s -> Double.parseDouble(s.toString())).max().getAsDouble();
        //设置图表Y轴坐标
        option.getYAxis().get(0).setMax(NumberUtils.roundIntervalCeil(BigDecimal.valueOf(maxYAxis), 4, 5));
        option.getYAxis().get(0).setInterval(NumberUtils.roundInterval(BigDecimal.valueOf(maxYAxis), 4));

        return option;
    }
}
