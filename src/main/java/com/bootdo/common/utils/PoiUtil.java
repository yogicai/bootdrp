package com.bootdo.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * 简单导入导出工具类
 *
 * @author caiyz
 * @since 2022-02-6 23:36
 */
public class PoiUtil {

    private static final Log log = Log.get();

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
        //判断文件是否存在
        if (ObjectUtil.isEmpty(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (Exception e) {
            log.error(">>> 导入数据异常：{}", e.getMessage());
        }
        return list;
    }

    /**
     * 根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param file       上传的文件
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (ObjectUtil.isNull(file)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (Exception e) {
            log.error(">>> 导入数据异常：{}", e.getMessage());
        }
        return list;
    }


    /**
     * 表头自定义
     */
    public static class InnerExportParams extends ExportParams {

        public InnerExportParams() {
            super.setHeight((short) 8);
            super.setStyle(InnerExcelExportStylerDefaultImpl.class);
        }

    }


    /**
     * 表头自定义
     */
    public static class InnerExcelExportStylerDefaultImpl extends ExcelExportStylerDefaultImpl {

        public InnerExcelExportStylerDefaultImpl(Workbook workbook) {
            super(workbook);
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
    }

}
