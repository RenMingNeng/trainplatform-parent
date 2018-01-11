package com.bossien.train.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

public class ExportExcelUtils<T> {

    public static OutputStream setExcelHeard(HttpServletResponse response, String fileName) throws Exception {
//        String fileName = "培训平台数据统计" + System.currentTimeMillis() + ".xls";
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName.getBytes("GB2312")), "ISO8859-1"));
        return response.getOutputStream();
    }

    public static OutputStream setExcelHeardZip(HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("application/x-zip-compressed;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName.getBytes("GB2312")), "ISO8859-1"));
        return response.getOutputStream();
    }

    /**
     * 导出list
     *
     * @param t
     * @param output
     * @throws Exception
     */
    public void exportExcel(List<T> t, OutputStream output) throws Exception {
        XSSFWorkbook wk = new XSSFWorkbook();
        XSSFSheet sheet = wk.createSheet();
        sheet.setDefaultColumnWidth(10*256);
        exportListObject(t, 0, wk, sheet);
        wk.write(output);
    }

    /**
     * 导出map
     *
     * @param map
     * @param title
     * @param output
     * @throws Exception
     */
    public void exportExcel(LinkedHashMap<String, Object> map, String title, OutputStream output) throws Exception {
        XSSFWorkbook wk = new XSSFWorkbook();
        XSSFSheet sheet = wk.createSheet();
        sheet.setDefaultColumnWidth(10*256);
        exportMapObject(map, title, wk, sheet);
        wk.write(output);
    }

    /**
     * 导出map + list
     *
     * @param t
     * @param output
     * @throws Exception
     */
    public void exportExcel(LinkedHashMap<String, Object> map, String title, List<Map<String, Object>> t, OutputStream output) throws Exception {
        XSSFWorkbook wk = new XSSFWorkbook();
        XSSFSheet sheet = wk.createSheet();

        int length = map.keySet().size();
        if(t.size() > 0){
            length = 8;
        }

        for (int i = 0; i< length; i++){
            if(i == 1 || i == 3){
                sheet.setColumnWidth(i, 40*256);
                continue;
            }
            sheet.setColumnWidth(i, 18*256);
        }
        int rowCount = exportMapObject(map, title, wk, sheet);
        Map<String, Object> tableHeadMap = MapUtils.newLinkedHashMap();
        tableHeadMap.put("userName", "用户名称");
        tableHeadMap.put("deptName", "单位名称");
        tableHeadMap.put("requirementStudyTime", "应修学时(单位分)");
        tableHeadMap.put("updateTotalStudyTime", "已修总学时(单位分)");
        tableHeadMap.put("yetAnswered", "已答题量");
        tableHeadMap.put("correctAnswered", "答对题量");
        tableHeadMap.put("correctRate", "答题正确率(%)");
        tableHeadMap.put("examScore", "考试成绩");
        createExcel(t, tableHeadMap, rowCount+1, wk, sheet);
        wk.write(output);
    }

    public static void createExcel(List<Map<String, Object>> data,
            Map<String, Object> tableHeadMap, int rowCount, XSSFWorkbook wk, XSSFSheet sheet) {
        if (null == data){
            data = new ArrayList<Map<String, Object>>();
        }
        XSSFRow row0 = sheet.createRow(rowCount);
        int j = 0;
        for (String key : tableHeadMap.keySet()) {
            // 设置表头
            row0.createCell(j).setCellValue((String)tableHeadMap.get(key));
            j++;
        }

        for (int i = 0; i < data.size(); i++) {
            XSSFRow rowi = sheet.createRow(rowCount + i + 1);
            Map<String, Object> dataMap = data.get(i);
            int cellIndex = 0;
            for(String key : tableHeadMap.keySet()){
                rowi.createCell(cellIndex).setCellValue(
                        (String) dataMap.get(key));
                cellIndex++;
            }
            cellIndex = 0;
        }
    }

    /**
     * 导出excel文件  输出字段上 加 @Excel(name="xxxx")注解
     *
     * @param t      输出实体对象
     * @return
     * @throws Exception
     */
    public void exportListObject(List<T> t, int rowCount, XSSFWorkbook wk, XSSFSheet sheet) throws Exception {
        XSSFRow row = null;
        XSSFRow row0 = sheet.createRow(rowCount);
        XSSFCell cell = null;
        XSSFCell cell0 = null;
        HashMap<String, String> hashmap = new HashMap<String, String>();// 存放有注解的值
        if(t.size() > 0){
            Field[] fieldarg = t.get(0).getClass().getDeclaredFields();
            for (Field fieldtemp : fieldarg) {
                String tempstring = fieldtemp.getAnnotation(Excel.class) != null ? fieldtemp.getAnnotation(Excel.class).name() : null;
                if (tempstring != null) {
                    hashmap.put(fieldtemp.getName().toLowerCase(), tempstring);
                }
            }
            int j = rowCount + 1;
            for (T tempt : t) {
                int i = 0;
                row = sheet.createRow(j);
                Field[] fieldarg2 = tempt.getClass().getDeclaredFields();
                for (Field fieldtemp2 : fieldarg2) {
                    // System.out.println(hashmap.containsKey(fieldtemp2.getName().toLowerCase())+"------------"+fieldtemp2.getType().getSimpleName());
                    DateConverter dateConverter = new DateConverter();
                    ConvertUtils.register(dateConverter, String.class);
                    if (hashmap.containsKey(fieldtemp2.getName().toLowerCase())) {
                        cell = row.createCell(i);
                        if (j == rowCount + 1) {
                            cell0 = row0.createCell(i);// 放中文名字
                            cell0.setCellValue(hashmap.get(fieldtemp2.getName().toLowerCase()));// 中文名字值
                            cell0.setCellStyle(setCellStyle(wk));
                            // if(fieldtemp2.getType().getSimpleName().equals("Date"))用DateConverter来处理了
                            cell.setCellValue(BeanUtils.getProperty(tempt, fieldtemp2.getName()));// 实际值
                        } else {
                            cell.setCellValue(BeanUtils.getProperty(tempt, fieldtemp2.getName()));
                        }
                        i++;
                    }
                }
                j++;
            }
        }
        sheet.autoSizeColumn(1, true);
        //System.out.println("基于流写入执行完毕!");
    }

    /**
     * 导出map对象
     * @param map
     * @param title
     * @return
     * @throws Exception
     */
    public int exportMapObject(LinkedHashMap<String, Object> map, String title, XSSFWorkbook wk, XSSFSheet sheet) throws Exception {
        //表格默认是有5个横行 单个排列  其余的一行排列2个
        int rowCount = map.size() / 2 + 2;
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell1 = row.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 3));
        cell1.setCellValue(title);
        XSSFCellStyle style = setCellStyle(wk);
        cell1.setCellStyle(style);

        int m = 1, n = 0, count = 1;
        // 创建数据行
        XSSFRow rowdata = sheet.createRow(m);
        XSSFCell cellkey = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String keyData = entry.getKey().toString();

            String valueData = String.valueOf(entry.getValue());
            cellkey = rowdata.createCell(n);
            cellkey.setCellStyle(style);
            cellkey.setCellValue(keyData);

            Cell cellvalue = rowdata.createCell(n + 1);
            cellvalue.setCellValue(valueData);
            cellvalue.setCellStyle(style);
            //1 4 5 6 7
            if (count % 2 == 1) {
                m++;
                rowdata = sheet.createRow(m);
                n = 0;
            } else {
                n = 2;
            }
            count++;
        }

        sheet.autoSizeColumn(1, true);
        //System.out.println("基于流写入执行完毕!");
        return rowCount;
    }

    /**
     * 设置单元的样式
     *
     * @param wb
     * @return
     */
    public XSSFCellStyle setCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        centerStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        return centerStyle;
    }
}
