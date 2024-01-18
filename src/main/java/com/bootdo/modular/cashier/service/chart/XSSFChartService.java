package com.bootdo.modular.cashier.service.chart;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.bootdo.core.exception.BootServiceExceptionEnum;
import com.bootdo.modular.cashier.enums.DataTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;


/**
 * POI画图
 * <a href="https://blog.csdn.net/u014644574/article/details/105695787">...</a>
 *
 * @author L
 * @since 2024-01-08 13:35
 */
@Slf4j
@Component
public class XSSFChartService {

    /**
     * excel图表
     */
    public void drawChart(XSSFSheet sheet, Class<?> clazz) {
        //是否有数据
        if (sheet.getLastRowNum() <= 1) {
            return;
        }
        //最后一列下标
        int lastCol = Arrays.stream(ReflectUtil.getFields(clazz))
                .mapToInt(field -> ObjectUtil.defaultIfNull(field.getAnnotation(Excel.class), e -> NumberUtil.parseInt(e.orderNum()), 0))
                .max()
                .orElse(0);

        //创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //前四个默认0，[0,5]：从0列5行开始;[7,26]:宽度7个单元格，26向下扩展到26行
        //默认宽度(14-8)*12
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, lastCol + 1, 0, 20, 20);
        //创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        //标题
        chart.setTitleText(sheet.getSheetName());
        //标题覆盖
        chart.setTitleOverlay(false);

        //图例位置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP);

        //分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        //值(Y轴)轴,标题位置
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        //LINE：折线图，
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);

        Field[] fieldArr = ReflectUtil.getFields(clazz, field -> field.isAnnotationPresent(Excel.class)
                && DataTypeEnum.CHART_CATEGORY.equalCode(field.getAnnotation(Excel.class).dict()));

        BootServiceExceptionEnum.CHART_CATEGORY_NOT_FOUND.assertNotEmpty(fieldArr, clazz.getSimpleName());

        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
        XDDFDataSource<String> timeSeries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, sheet.getLastRowNum(), 0, 0));

        //Y轴数据
        BeanUtil.descForEach(clazz, action -> {
            Excel annotation = action.getField().getAnnotation(Excel.class);

            if (annotation != null) {
                DataTypeEnum dataTypeEnum = DataTypeEnum.fromCode(annotation.dict());
                int colNum = NumberUtil.parseInt(annotation.orderNum());

                if (DataTypeEnum.CHART_DATA.equals(dataTypeEnum)) {
                    //数据，单元格范围位置[1, 0]到[1, 6]
                    XDDFNumericalDataSource<Double> dataSeries = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, sheet.getLastRowNum(), colNum, colNum));
                    //图表加载数据
                    XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(timeSeries, dataSeries);
                    //折线图例标题
                    series.setTitle(annotation.name(), null);
                    //曲线
                    series.setSmooth(true);
                    series.setMarkerStyle(MarkerStyle.NONE);
                }
            }
        });

        //绘制
        chart.plot(data);
    }

}
