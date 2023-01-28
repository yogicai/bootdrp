package com.bootdo.report.service;

import com.bootdo.common.config.Constant;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.common.utils.MapUtils;
import com.bootdo.common.utils.NumberUtils;
import com.bootdo.common.utils.R;
import com.bootdo.po.dao.OrderDao;
import com.bootdo.po.domain.OrderDO;
import com.bootdo.report.controller.response.SEBillTotalResult;
import com.bootdo.report.controller.response.SEDebtTotalResult;
import com.bootdo.report.controller.response.echart.EChartOption;
import com.bootdo.report.controller.response.echart.PieData;
import com.bootdo.report.dao.SEReportDao;
import com.bootdo.report.enums.BillStatType;
import com.bootdo.report.enums.EChartSeriesType;
import com.bootdo.se.dao.SEOrderDao;
import com.bootdo.se.domain.SEOrderDO;
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
 * @author caiyz
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

    /** 饼图展示前十名 */
    private final int topCount = 9;
    /** 饼图展示前十名 */
    private final String topName = "其他";
    /** 12月份 */
    private final List<String> month_series = Lists.newArrayList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");


    public SEBillTotalResult pBalanceTotal(Map<String, Object> params) {
        SEBillTotalResult result = new SEBillTotalResult();
        List<SEOrderDO>  list = seOrderDao.list(params);
        for (SEOrderDO seOrderDO : list) {
            result.setProfit(NumberUtils.add(result.getProfit(), NumberUtils.subtract(seOrderDO.getTotalAmount(), seOrderDO.getCostAmount())));
            result.setTotalAmount(NumberUtils.add(result.getTotalAmount(), seOrderDO.getTotalAmount()));
        }
        return result;
    }

    public SEDebtTotalResult pDebtTotal(Map<String, Object> params) {
        SEDebtTotalResult result = new SEDebtTotalResult();
        List<SEOrderDO>  seList = seOrderDao.list(params);
        for (SEOrderDO seOrderDO : seList) {
            result.setDebtAmount(NumberUtils.add(result.getDebtAmount(), NumberUtils.subtract(seOrderDO.getTotalAmount(), seOrderDO.getPaymentAmount())));
        }
        List<OrderDO>  list = orderDao.list(params);
        for (OrderDO orderDO : list) {
            result.setDebtVAmount(NumberUtils.add(result.getDebtVAmount(), NumberUtils.subtract(orderDO.getTotalAmount(), orderDO.getPaymentAmount())));
        }
        return result;
    }

    public EChartOption pBillTrend(Map<String, Object> params) {
        EChartOption option = new EChartOption(1, 2, 3);
        String type = MapUtils.getString(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<Map<String, Object>> seList = seReportDao.pBillTrend(params);

        option.getxAxis().get(0).getData().addAll(DateUtils.getDaySerial(type));

        BigDecimal billCount = BigDecimal.ZERO;
        BigDecimal maxYAxis = BigDecimal.ZERO;
        List<String> dayTimeSerial = DateUtils.getDayTimeSerial(type);
        for (String time : dayTimeSerial) {
            boolean exists = false;
            for (Map<String, Object> map : seList) {
                if (StringUtils.equals(time, MapUtils.getString(map, "otime"))) {
                    BigDecimal count = MapUtils.getBigDecimal(map, "count");
                    BigDecimal totalAmount = MapUtils.getBigDecimal(map, "totalAmount");
                    billCount = billCount.compareTo(count) < 0 ? count : billCount;
                    maxYAxis = maxYAxis.compareTo(totalAmount) < 0 ? totalAmount : maxYAxis;

                    option.getSeries().get(0).getData().add(MapUtils.getBigDecimal(map, "totalAmount"));
                    option.getSeries().get(1).getData().add(NumberUtils.subtract(MapUtils.getBigDecimal(map, "totalAmount"), MapUtils.getBigDecimal(map, "paymentAmount")));
                    option.getSeries().get(2).getData().add(MapUtils.getInteger(map, "count"));
                    exists = true; break;
                }
            }
            if (!exists) {
                option.getSeries().get(0).getData().add(0.0D);
                option.getSeries().get(1).getData().add(0.0D);
                option.getSeries().get(2).getData().add(0);
            }
        }

        option.getyAxis().get(0).setMax(NumberUtils.roundIntervalCeil(maxYAxis, 4, 5));
        option.getyAxis().get(0).setInterval(NumberUtils.roundInterval(maxYAxis, 4));
        option.getyAxis().get(1).setMax(NumberUtils.roundIntervalCeil(billCount, 4, 5));
        option.getyAxis().get(1).setInterval(NumberUtils.roundInterval(billCount, 4));

        return option;
    }

    public EChartOption pBillTrendPie(Map<String, Object> params) {
        EChartOption option = new EChartOption(0, 0, 2);
        String type = MapUtils.getString(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<Map<String, Object>> seList = seReportDao.pBillTrendPie(params);

        int count = 1;
        BigDecimal profitAmountOther = BigDecimal.ZERO, totalAmountOther = BigDecimal.ZERO;
        for (Map<String, Object> map : seList) {
            if (count <= topCount) {
                option.getLegend().getData().add(MapUtils.getString(map, "name"));
                option.getSeries().get(0).getData().add(new PieData(MapUtils.getString(map, "name"), MapUtils.getBigDecimal(map, "totalAmount")));
                option.getSeries().get(1).getData().add(new PieData(MapUtils.getString(map, "name"), MapUtils.getBigDecimal(map, "profitAmount")));
            } else {
                profitAmountOther = NumberUtils.add(profitAmountOther, MapUtils.getBigDecimal(map, "totalAmount"));
                totalAmountOther = NumberUtils.add(totalAmountOther, MapUtils.getBigDecimal(map, "profitAmount"));
            }
        }
        if (topCount < seList.size()) {
            option.getLegend().getData().add(topName);
            option.getSeries().get(0).getData().add(new PieData(topName, profitAmountOther));
            option.getSeries().get(1).getData().add(new PieData(topName, totalAmountOther));
        }
        return option;
    }

    public R pCashTotal(Map<String, Object> params) {
        List<Map<String, Object>>  list = seReportDao.pCashTrend(params);
        if (CollectionUtils.isNotEmpty(list)) {
            return R.ok(ImmutableMap.of("profitAmountT",MapUtils.getBigDecimal(list.get(0), "profitAmount"), "cashFlowAmountT",MapUtils.getBigDecimal(list.get(0), "cashFlowAmount")));
        }
        return R.ok(ImmutableMap.of("profitAmountT",0, "cashFlowAmountT",0));
    }

    public EChartOption pCashTrend(Map<String, Object> params) {
        EChartOption option = new EChartOption(1, 2, 3);
        String type = MapUtils.getString(params, "type", Constant.Q_MONTH);
        params.put("billDate", DateUtils.getDayStartStr(type));
        List<Map<String, Object>> seList = seReportDao.pCashTrend(params);

        option.getxAxis().get(0).getData().addAll(DateUtils.getDaySerial(type));

        BigDecimal maxYAxis = BigDecimal.ZERO;
        List<String> dayTimeSerial = DateUtils.getDayTimeSerial(type);
        for (String time : dayTimeSerial) {
            boolean exists = false;
            for (Map<String, Object> map : seList) {
                if (StringUtils.equals(time, MapUtils.getString(map, "otime"))) {
                    BigDecimal profitAmount = MapUtils.getBigDecimal(map, "profitAmount");
                    maxYAxis = maxYAxis.compareTo(profitAmount) < 0 ? profitAmount : maxYAxis;

                    option.getSeries().get(0).getData().add(MapUtils.getBigDecimal(map, "profitAmount"));
                    exists = true; break;
                }
            }
            if (!exists) {
                option.getSeries().get(0).getData().add(0.0D);
            }
        }
        option.getyAxis().get(0).setMax(NumberUtils.roundIntervalCeil(maxYAxis, 4, 5));
        option.getyAxis().get(0).setInterval(NumberUtils.roundInterval(maxYAxis, 4));
        return option;
    }

    public EChartOption pHisPBillTrend(Map<String, Object> params) {

        //图表数据类型
        BillStatType type = BillStatType.valueOf(MapUtils.getString(params, "type"));
        //销售单历史数据
        List<Map<String, Object>> seList = seReportDao.pHisPBillTrend(params);

        TreeSet yearSet = new TreeSet();
        MultiKeyMap<String, Map> multiKeyMap = new MultiKeyMap();
        seList.stream().forEach(m -> {
            multiKeyMap.put(MapUtils.getString(m, "otime"), MapUtils.getString(m, "time"), m);
            yearSet.add(MapUtils.getString(m, "otime"));
        });

        //图表数据
        EChartOption option = new EChartOption(1, 1, yearSet.size());
        //图表数据Series
        List<String> yearList = new ArrayList<>(yearSet);
        IntStream.rangeClosed(0, yearList.size() -1).forEach(i -> {
            IntStream.rangeClosed(1, 12).forEach(m -> {
                String year = yearList.get(i);
                double value = MapUtils.getDoubleValue(multiKeyMap.get(year, String.valueOf(m)), type.getValue());
                option.getSeries().get(i).getData().add(value);
            });

            option.getSeries().get(i).setType(EChartSeriesType.BAR.getValue());
            option.getSeries().get(i).setName(yearList.get(i) + "年");
            option.getLegend().getData().add(yearList.get(i) + "年");
        });


        option.getTitle().setText(type.getText());
        option.getxAxis().get(0).getData().addAll(month_series);
        //销售单Series最大值
        double maxYAxis = option.getSeries().stream().flatMap(s -> s.getData().stream()).mapToDouble(s -> Double.valueOf(s.toString())).max().getAsDouble();
        //设置图表Y轴坐标
        option.getyAxis().get(0).setMax(NumberUtils.roundIntervalCeil(BigDecimal.valueOf(maxYAxis), 4, 5));
        option.getyAxis().get(0).setInterval(NumberUtils.roundInterval(BigDecimal.valueOf(maxYAxis), 4));

        return option;
    }
}
