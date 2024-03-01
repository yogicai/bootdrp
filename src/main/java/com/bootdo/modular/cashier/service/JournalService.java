package com.bootdo.modular.cashier.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.core.utils.HttpServletUtil;
import com.bootdo.core.utils.PoiUtil.InnerExportParams;
import com.bootdo.modular.cashier.dao.JournalDao;
import com.bootdo.modular.cashier.dao.SalaryDao;
import com.bootdo.modular.cashier.domain.RecordDO;
import com.bootdo.modular.cashier.param.JournalGeneralParam;
import com.bootdo.modular.cashier.result.JournalGeneralResult;
import com.bootdo.modular.cashier.result.JournalGeneralResult.*;
import com.bootdo.modular.cashier.service.chart.XSSFChartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * 经营概况
 *
 * @author L
 * @since 2024-01-08 13:35
 */
@Slf4j
@Service
public class JournalService extends ServiceImpl<JournalDao, RecordDO> {
    @Resource
    private JournalDao journalDao;
    @Resource
    private SalaryDao salaryDao;
    @Resource
    private RecordService recordService;
    @Resource
    private XSSFChartService xssfChartService;


    public JournalGeneralResult general(JournalGeneralParam param) {
        JournalGeneralResult result = new JournalGeneralResult();

        Map<String, Object> beanToMap = BeanUtil.beanToMap(param);
        //账户支出流水明细
        List<Map<String, Object>> flowRecordList = journalDao.flowRecordList(beanToMap);

        //按年份+账户，统计资金流水
        List<AccountItem> flowRecordBeanList = flowRecordList.stream()
                .collect(Collectors.groupingBy(m -> CollUtil.join(CollUtil.valuesOfKeys(m, "year", "account"), StrUtil.COMMA), Collectors.toList()))
                .entrySet()
                .stream()
                .map(entry -> {
                    String[] keyArr = StrUtil.splitToArray(entry.getKey(), StrUtil.COMMA);
                    AccountItem accountItem = new AccountItem(keyArr[0], keyArr[1]);
                    entry.getValue().forEach(m -> {
                        //现金流map转bean
                        BeanUtil.descForEach(AccountItem.class, action -> {
                            //现金流map转bean
                            Excel excel = action.getField().getAnnotation(Excel.class);
                            //支出类型
                            String excelName = ObjectUtil.defaultIfNull(excel, Excel::name, null);
                            //支出类型
                            String costType = MapUtil.getStr(m, "costType", StrUtil.EMPTY);

                            if (StrUtil.equals(costType, excelName) && action.getFieldType().equals(BigDecimal.class)) {
                                action.setValue(accountItem, MapUtil.get(m, "payAmount", BigDecimal.class, BigDecimal.ZERO));
                            }
                        });
                    });
                    return accountItem;
                })
                .sorted(Comparator.comparing(AccountItem::getYear))
                .collect(Collectors.toList());

        //账户现金流合计
        Map<String, AccountItem> flowRecordAccountMap = flowRecordBeanList.stream()
                .collect(Collectors.groupingBy(AccountItem::getAccount, LinkedHashMap::new, Collector.of(AccountItem::new, (o, n) -> {
                            BeanUtil.descForEach(AccountItem.class, action -> {
                                if (action.getField().getType().equals(BigDecimal.class)) {
                                    action.setValue(o, NumberUtil.add((BigDecimal) action.getValue(o), (BigDecimal) action.getValue(n)));
                                } else {
                                    action.setValue(o, action.getValue(n));
                                }
                            });
                        }, (o, p) -> o))
                );

        //年份现金流合计
        Map<String, AccountItem> flowRecordYearMap = flowRecordBeanList.stream()
                .collect(Collectors.groupingBy(AccountItem::getYear, LinkedHashMap::new, Collector.of(AccountItem::new, (o, n) -> {
                            BeanUtil.descForEach(AccountItem.class, action -> {
                                if (action.getField().getType().equals(BigDecimal.class)) {
                                    action.setValue(o, NumberUtil.add((BigDecimal) action.getValue(o), (BigDecimal) action.getValue(n)));
                                } else {
                                    action.setValue(o, action.getValue(n));
                                }
                            });
                        }, (o, p) -> o))
                );

        //设置工资
        Map<String, SalaryRecord> salaryRecordMap = salaryDao.salaryRecordList(beanToMap)
                .stream().collect(Collectors.toMap(SalaryRecord::getYear, Function.identity(), (o, n) -> o));

        //经营年度统计
        List<OperateItem> operateItemList = journalDao.generalFlowYear(beanToMap);

        //2017年销售额来年excel手工数据，毛利率按28.12%算
        Date billStart = Date.from(LocalDate.of(2017, 12, 31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        if (DateUtil.compare(param.getStart(), billStart) <= 0) {
            operateItemList.add(0, OperateItem.builder()
                    .year("2017")
                    .totalAmount(NumberUtil.toBigDecimal(721009))
                    .profitAmount(NumberUtil.toBigDecimal(202747.73))
                    .build());
        }

        operateItemList.forEach(item -> {
            //毛利润率
            item.setProfitRate(NumberUtil.div(item.getProfitAmount(), item.getTotalAmount(), 2));
            //父母费用
            item.setParentFee(ObjectUtil.defaultIfNull(flowRecordYearMap.get(item.getYear()), AccountItem::getParent, BigDecimal.ZERO));
            //生活开支
            item.setLivingFee(ObjectUtil.defaultIfNull(flowRecordYearMap.get(item.getYear()), AccountItem::getLiving, BigDecimal.ZERO));
            //运营费用
            item.setOperationFee(ObjectUtil.defaultIfNull(flowRecordYearMap.get(item.getYear()), AccountItem::getOperation, BigDecimal.ZERO));
            //工资费用
            item.setSalaryFee(ObjectUtil.defaultIfNull(salaryRecordMap.get(item.getYear()), SalaryRecord::getTotalAmount, BigDecimal.ZERO));
            //营销费用
            item.setMarketFee(ObjectUtil.defaultIfNull(flowRecordYearMap.get(item.getYear()), AccountItem::getMarket, BigDecimal.ZERO));
            //净利润
            item.setNetProfit(NumberUtil.sub(item.getProfitAmount(), item.getParentFee(), item.getLivingFee(), item.getOperationFee(), item.getSalaryFee(), item.getMarketFee(), 2));
            //净利润率
            item.setNetProfitRate(NumberUtil.div(item.getNetProfit(), item.getTotalAmount(), 2));
            //已实现净利润 = 净利润 - 欠款
            item.setNetProfitRealized(NumberUtil.sub(item.getNetProfit(), item.getDebtAmount(), 2));
            //已支付工资
            item.setSalaryRealized(ObjectUtil.defaultIfNull(flowRecordYearMap.get(item.getYear()), AccountItem::getSalary, BigDecimal.ZERO));
        });

        //月现金流
        List<OperateMonthItem> operateItemMonthList = journalDao.generalFlowMonth(beanToMap);
        //欠款明细
        List<DebtItem> debtItemList = journalDao.debtRecordList(beanToMap);

        //账户流水明细
        Map<String, List<RecordDO>> flowRecordMap = recordService.list(beanToMap).stream()
                .collect(Collectors.groupingBy(RecordDO::getAccount, Collectors.toList()));

        //经营年份
        result.setStartYear(DateUtil.format(param.getStart(), DatePattern.NORM_YEAR_PATTERN));
        result.setEndYear(DateUtil.format(param.getEnd(), DatePattern.NORM_YEAR_PATTERN));
        //账户现金流合计、年份现金流合计、经营情况
        result.setFlowAccountStatList(flowRecordAccountMap.values());
        result.setFlowAccountYearList(flowRecordYearMap.values());
        result.setOperateYearList(operateItemList);
        //账户现金流水、月现金流、欠款明细
        result.setFlowRecordMap(flowRecordMap);
        result.setOperateMonthList(operateItemMonthList);
        result.setDebtItemList(debtItemList);

        return result;
    }

    /**
     * 1、EasyPoi xlsx模板循环导出是，合计行公式被清空问题（xls是正常的）；ps: <a href="https://segmentfault.com/a/1190000043415590">...</a>
     * 2、EasyPoi 千分位（numFormat="#,##0.00"）导出的excel是文本类型，excel有数值类型错误提示；ps:numberFormat设置成小数（”0.00“），设置单元格式格式（PoiUtil.InnerExcelExportStylerDefaultImpl）
     */
    public void export(JournalGeneralParam param) {
        try {
            JournalGeneralResult result = general(param);

            Map<String, Object> statMap = MapUtil.<String, Object>builder()
                    .put("startYear", result.getStartYear())
                    .put("endYear", result.getEndYear())
                    .put("flowAccountStatList", result.getFlowAccountStatList())
                    .put("flowAccountYearList", result.getFlowAccountYearList())
                    .put("operateYearList", result.getOperateYearList())
                    .build();
            //汇总统计
            Workbook workbook = ExcelExportUtil.exportExcel(MapUtil.of(0, statMap), new TemplateExportParams("doc/OperateStat.xlsx"));

            //账户流水明细、月现金流、客户欠款
            BeanUtil.descForEach(JournalGeneralResult.class, action -> {
                ExcelCollection excelCollection = action.getField().getAnnotation(ExcelCollection.class);
                if (ObjectUtil.isNotNull(excelCollection)) {
                    Object fieldValue = action.getValue(result);
                    if (fieldValue instanceof Map) {
                        Convert.convert(new TypeReference<Map<String, Collection<?>>>() {
                                }, fieldValue)
                                .forEach((key, value) -> {
                                    ExcelExportService service = new ExcelExportService();
                                    ExportParams params = new InnerExportParams();
                                    //过滤sheet name非法字符
                                    params.setSheetName(StrUtil.replaceChars(key, "[]:\\*?/（） ", StrUtil.EMPTY));
                                    service.createSheet(workbook, params, excelCollection.type(), value);
                                });
                    } else if (fieldValue instanceof Collection) {
                        ExcelExportService service = new ExcelExportService();
                        ExportParams params = new InnerExportParams();
                        params.setSheetName(excelCollection.name());
                        service.createSheet(workbook, params, excelCollection.type(), (Collection<?>) fieldValue);
                    }
                }
            });

            //月现金流 网线图
            xssfChartService.drawChart((XSSFSheet) workbook.getSheet("月现金流"), OperateMonthItem.class);

            //excel导出
            HttpServletResponse response = HttpServletUtil.getResponse();
            String fileName = URLEncoder.encode(StrUtil.format("经营概况{}-{}.xlsx", result.getStartYear(), result.getEndYear()), CharsetUtil.UTF_8);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            log.error("JournalService.export error! param:{}", param, e);
        }
    }

}
