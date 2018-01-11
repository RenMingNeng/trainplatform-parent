package com.bossien.train.util;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


// excel文档处理工具类
public class ExcelUtil {
	// 学员档案excel临时目录
	public static String TP_EXCEL_ARCHIVE_TEMP_PATH = System.getProperty("java.io.tmpdir") + File.separator + "archive";

	// 读取excel数据
	public static List<List<String>> getSheetDataByExcel(String filePath) throws FileNotFoundException {
		List<List<String>> tableData = new ArrayList<List<String>>();
		FileInputStream fileIn = new FileInputStream(filePath);
		
		// 根据excel不同版本读取
//		String suffix = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
//		// 03版 excel文件
//		if ("xls".equalsIgnoreCase(suffix)) {
//			return getSheetDataByExcel2003(fileIn, tableData);
//		}
//		// 非03版，即07、10版 excel文件
//		return getSheetDataByExcel2007(fileIn, tableData);
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(fileIn);
		} catch (Exception e){
			e.printStackTrace();
		}
		if (wb instanceof HSSFWorkbook)
		{
			return getSheetDataByExcel2003(new FileInputStream(filePath), tableData);
		}
		else //if (wb instanceof XSSFWorkbook)
		{
			return getSheetDataByExcel2007(new FileInputStream(filePath), tableData);
		}
	}
	
	private static List<List<String>> getSheetDataByExcel2007(FileInputStream fileIn, List<List<String>> tableData) {
		try {
			XSSFWorkbook wb = new XSSFWorkbook(fileIn);
			XSSFSheet sheet = wb.getSheetAt(0);
			// 用表头来确定列数
			short lastCellNum = sheet.getRow(0).getLastCellNum();
			int lastRowNum = sheet.getLastRowNum();
			for (int rowNum = 2; rowNum <= lastRowNum; rowNum++) {
				XSSFRow row = sheet.getRow(rowNum);
				if(null==row) {
                    continue;
                }
				List<String> rowData = new ArrayList<String>();
				for (short i = 0; i < lastCellNum; i++) {
					XSSFCell cell = row.getCell(i);
					rowData.add(cell == null ? "" : getCellValue(cell));
				}
				if(!rowData.isEmpty()) {
                    tableData.add(rowData);
                }
			}
			return tableData;
		} catch (Exception e) {
			return null;
		}
	}

	private static List<List<String>> getSheetDataByExcel2003(FileInputStream fileIn, List<List<String>> tableData) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook(fileIn);
			HSSFSheet sheet = wb.getSheetAt(0);
			// 用表头来确定列数
			short lastCellNum = sheet.getRow(0).getLastCellNum();
			int lastRowNum = sheet.getLastRowNum();
			for (int rowNum = 2; rowNum <= lastRowNum; rowNum++) {
				HSSFRow row = sheet.getRow(rowNum);
				if(null==row) {
                    continue;
                }
				List<String> rowData = new ArrayList<String>();
				for (short i = 0; i < lastCellNum; i++) {
					HSSFCell cell = row.getCell(i);
					rowData.add(cell == null ? "" : getCellValue(cell));
				}
				if(!rowData.isEmpty()) {
                    tableData.add(rowData);
                }
			}
			return tableData;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 创建excel
	 * @param data 例：表里面的数据[{varName：张三,varInt:18,varDept:武当},{varName：李四,varInt:20,varDept:少林}]
	 * @param tableHeadMap 例：字段名称{varName：姓名，varAge：年龄，varDept：部门}
	 * @return XSSFWorkbook 工作表对象
	 */
	public static XSSFWorkbook createExcel(List<Map<String, Object>> data,
			Map<String, Object> tableHeadMap) {
		if (null == data){
			data = new ArrayList<Map<String, Object>>();
		}
		XSSFWorkbook xb = new XSSFWorkbook();
		XSSFSheet sheet = xb.createSheet();
		XSSFRow row0 = sheet.createRow(0);
		int j = 0;
		for (String key : tableHeadMap.keySet()) {
			// 设置表头
			row0.createCell(j).setCellValue((String)tableHeadMap.get(key));
			j++;
		}

		for (int i = 0; i < data.size(); i++) {
			XSSFRow rowi = sheet.createRow(i + 1);
			Map<String, Object> dataMap = data.get(i);
			int cellIndex = 0;
			for(String key : tableHeadMap.keySet()){
				rowi.createCell(cellIndex).setCellValue(
						(String) dataMap.get(key));
				cellIndex++;
			}
			cellIndex = 0;
		}
		return xb;
	}

	//导出excel方法
	public static void  exportExcelFile(HttpServletResponse response,List<Map<String, Object>> lMap, Map<String, Object> headerMap){
		try {
			// 生成文件
			XSSFWorkbook workbook =createExcel(lMap, headerMap);
			OutputStream out = response.getOutputStream();
			workbook.write(new BufferedOutputStream(out));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取单元格的value
	public static String getCellValue(Cell cell){
		String result = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			result = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			result = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			result = null;
			break;
		case Cell.CELL_TYPE_FORMULA:
			Workbook wb = cell.getSheet().getWorkbook();
			CreationHelper creationHelper = wb.getCreationHelper();
			FormulaEvaluator formulaEvaluator = creationHelper.createFormulaEvaluator();
			result = getCellValue(formulaEvaluator.evaluateInCell(cell));
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)){
				Date date = cell.getDateCellValue();
				result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			}else{
				result = NumberToTextConverter.toText(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
				result = cell.getRichStringCellValue().toString();
			break;
		default:
			break;
		}
		return result.trim();
	}

	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[4096]);
	}

	public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		long count = 0L;

		int n1;
		for(boolean n = false; -1 != (n1 = input.read(buffer)); count += (long)n1) {
			output.write(buffer, 0, n1);
		}

		return count;
	}

}
