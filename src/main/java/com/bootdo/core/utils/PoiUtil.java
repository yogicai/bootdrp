package com.bootdo.core.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import cn.afterturn.easypoi.util.PoiValidationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bootdo.core.consts.OrderStatusCode;
import com.bootdo.core.excel.ClassExcelVerifyHandler;
import com.bootdo.core.excel.ExcelDictHandlerImpl;
import com.bootdo.core.exception.assertion.BizServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 简单导入导出工具类
 *
 * @author L
 * @since 2022-02-6 23:36
 */
@Slf4j
public class PoiUtil {

    /**
     * 使用流的方式导出excel
     *
     * @param excelName 要导出的文件名称，如Users.xls
     * @param pojoClass Excel实体类
     * @param data      要导出的数据集合
     */
    public static void exportExcelWithStream(String excelName, Class<?> pojoClass, Collection<?> data) {
        try {
            HttpServletResponse response = HttpServletUtil.getResponse();
            String fileName = URLEncoder.encode(excelName, CharsetUtil.UTF_8);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            Workbook workbook = ExcelExportUtil.exportExcel(new InnerExportParams(), pojoClass, data);
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            log.error(">>> 导出数据异常：{}", e.getMessage());
        }
    }

    /**
     * 导出excel工具类   多个excel
     * <p>
     * * 要保证  sheetName列表的顺序,dateList的date顺序,sheetName的name顺序保持一致
     *
     * @param pojoClassList 导出数据实体集合
     * @param dateList      导出数据集合列表
     * @param sheetNameList 导出多个sheet的name集合,按顺序
     */
    public static void exportExcelWithStream(String excelName, List<Class<?>> pojoClassList, List<List<?>> dateList, List<String> sheetNameList) {
        try {
            List<Map<String, Object>> sheetList = new ArrayList<>();

            for (int i = 0; i < sheetNameList.size(); i++) {
                ExportParams exportParams = new ExportParams();
                exportParams.setSheetName(sheetNameList.get(i));
                HashMap<String, Object> exportMap = new HashMap<>();
                exportMap.put("title", exportParams);
                exportMap.put("entity", pojoClassList.get(i));
                exportMap.put("data", dateList.get(i));
                sheetList.add(exportMap);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(sheetList, ExcelType.HSSF);

            HttpServletResponse response = HttpServletUtil.getResponse();
            String fileName = URLEncoder.encode(excelName, CharsetUtil.UTF_8);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();

        } catch (Exception e) {
            log.error(">>> 导出数据异常：{}", e.getMessage(), e);
        }
    }

    /**
     * 使用文件的方式导出excel
     *
     * @param filePath  文件路径，如 d:/demo/demo.xls
     * @param pojoClass Excel实体类
     * @param data      要导出的数据集合
     */
    public static void exportExcelWithFile(String filePath, Class pojoClass, Collection data) {

        try {
            //先创建父文件夹
            cn.hutool.core.io.FileUtil.mkParentDirs(filePath);
            File file = cn.hutool.core.io.FileUtil.file(filePath);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, data);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            log.error(">>> 导出数据异常：{}", e.getMessage());
        }

    }

    /**
     * 根据文件路径来导入Excel
     *
     * @param filePath   文件路径
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        List<T> list = new ArrayList<>();

        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (Exception e) {
            log.error(">>> 导入数据异常：{}", e.getMessage(), e);
        }
        return list;
    }

    /**
     * 生成workbook
     *
     * @param file 上传的文件
     */
    public static Workbook getWorkBook(MultipartFile file) throws IOException {
        //这样写excel能兼容03和07
        InputStream is = file.getInputStream();
        Workbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (Exception ex) {
            is = file.getInputStream();
            hssfWorkbook = new XSSFWorkbook(is);
        }
        return hssfWorkbook;
    }

    public static int getSheetNum(MultipartFile file) throws IOException {
        return getWorkBook(file).getNumberOfSheets();
    }

    /**
     * 根据接收的Excel文件来导入Excel,并封装成实体类
     */
    public static <T extends IExcelDataModel & IExcelModel> List<T> importExcelMore(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass, boolean throwEx, Class<?>... verfiyGroup) {
        ImportParams params = new InnerImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerify(true);
        //非集合双表头导入bug，处理：1、非集合不要用双表头；2、设置导入参数verifyFileSplit=false(https://gitee.com/lemur/easypoi/issues/I1YJ6D)
        params.setVerifyFileSplit(false);

        ExcelImportResult<T> result = null;
        try {
            result = ExcelImportUtil.importExcelMore(file.getInputStream(), pojoClass, params);
        } catch (Exception e) {
            log.error(">>> 导入数据异常：{}", e.getMessage(), e);
        }
        //校验异常处理
        Map<Integer, String> errorMsgMap = verifyExcelCollection(result, pojoClass, verfiyGroup);

        if (CollUtil.isNotEmpty(errorMsgMap) && throwEx) {

            String errorMsg = errorMsgMap.entrySet()
                    .stream()
                    .map(entry -> StrUtil.format("第{}行：{}！", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(StrUtil.LF));

            throw new BizServiceException(OrderStatusCode.ERROR, StrUtil.maxLength(errorMsg, 1000));
        }
        return ObjectUtil.isNotNull(result) ? result.getList() : Collections.emptyList();
    }

    /**
     * 校验子集合
     */
    private static <T extends IExcelDataModel & IExcelModel> Map<Integer, String> verifyExcelCollection(ExcelImportResult<T> result, Class<T> pojoClass, Class<?>... verfiyGroup) {
        //校验异常信息 Map<行号, errorMsg>
        Map<Integer, String> errorMsgMap = new HashMap<>();

        if (ObjectUtil.isNotNull(result)) {
            //合并结果集
            List<T> resultList = CollUtil.unionAll(result.getFailList(), result.getList());
            //校验异常信息 Map<行号, errorMsg>
            errorMsgMap = result.getFailList().stream()
                    .collect(Collectors.toMap(k -> k.getRowNum() + 1, v -> v.getErrorMsg(), (o, n) -> n, LinkedHashMap::new));
            //excel行
            Map<Integer, String> finalErrorMsgMap = errorMsgMap;

            resultList.forEach(record -> {
                //校验集合属性
                BeanUtil.descForEach(pojoClass, action -> {
                    if (action.getField().isAnnotationPresent(ExcelCollection.class)) {

                        Object value = action.getValue(record);
                        //子集校验
                        if (value instanceof List) {
                            List<?> collection = (List<?>) action.getValue(record);

                            for (int i = 0; i < collection.size(); i++) {
                                //excel行号
                                int rowNum = record.getRowNum() + i;
                                Object object = collection.get(i);
                                String errorMsg = PoiValidationUtil.validation(object, verfiyGroup);
                                //校验提示
                                if (object instanceof IExcelModel && StrUtil.isNotBlank(errorMsg)) {
                                    ((IExcelModel) object).setErrorMsg(errorMsg);
                                }
                                //行号
                                if (object instanceof IExcelDataModel) {
                                    ((IExcelDataModel) object).setRowNum(rowNum);
                                }
                                if (StrUtil.isNotBlank(errorMsg)) {
                                    finalErrorMsgMap.compute(rowNum + 1, (key, oldValue) -> StrUtil.join(StrUtil.COMMA, StrUtil.nullToEmpty(oldValue), errorMsg));
                                }
                            }
                        }

                    }
                });
            });
        }

        return errorMsgMap;
    }

    /**
     * 导入自定义
     */
    public static class InnerImportParams extends ImportParams {
        public InnerImportParams() {
            super.setDictHandler(SpringUtil.getBean(ExcelDictHandlerImpl.class));
            super.setVerifyHandler(SpringUtil.getBean(ClassExcelVerifyHandler.class));
        }
    }

    /**
     * 导出自定义
     */
    public static class InnerExportParams extends ExportParams {
        public InnerExportParams() {
            super.setHeight((short) 6);
            super.setStyle(InnerExcelExportStylerDefaultImpl.class);
            super.setDictHandler(SpringUtil.getBean(ExcelDictHandlerImpl.class));
        }
    }


    /**
     * 表头自定义
     */
    public static class InnerExcelExportStylerDefaultImpl extends ExcelExportStylerDefaultImpl {

        private CellStyle moneyCellStyle;

        public InnerExcelExportStylerDefaultImpl(Workbook workbook) {
            super(workbook);
            createMoneyCellStyler();
        }

        @Override
        public CellStyle getTitleStyle(short color) {
            CellStyle titleStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true); // 字体加粗
            titleStyle.setFont(font);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);//居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//设置颜色
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleStyle.setBorderRight(BorderStyle.DOTTED);//设置右边框
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setWrapText(true);
            return titleStyle;
        }

        @Override
        public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
            //小数类型都设置成 千分位格式
            if (entity != null && entity.getType() == 10 && StrUtil.endWith(entity.getNumFormat(), "0.00")) {
                return moneyCellStyle;
            }
            return super.getStyles(noneStyler, entity);
        }

        private void createMoneyCellStyler() {
            moneyCellStyle = workbook.createCellStyle();
            moneyCellStyle.setAlignment(HorizontalAlignment.RIGHT);
            moneyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            moneyCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("#,##0.00"));
        }
    }

}
