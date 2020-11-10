package com.automation.library;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelLibrary {

	public static int getExcelRowCount(String sPath, String sSheet) {
		int iRowNum = 0;
		try {

			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			iRowNum = sht.getLastRowNum();
			// System.out.println(sht.getLastRowNum());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return iRowNum;
	}

	public static String getExcelData(String sPath, String sSheet, int rowNo, int cellNo) {
		DataFormatter dataFormatter = new DataFormatter();

		int iRowNum = 0;
		String data = null;
		try {

			FileInputStream fis = new FileInputStream(sPath);
			// Workbook wb = new XSSFWorkbook(fis);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			// FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
			iRowNum = sht.getLastRowNum();
			if (rowNo <= iRowNum) {
				Cell cell = sht.getRow(rowNo).getCell(cellNo);
				// System.out.println(cellValue);
				data = dataFormatter.formatCellValue(cell);

				/*
				 * switch(cell.getCellType()) { case Cell.CELL_TYPE_STRING: data =
				 * cell.getStringCellValue(); break; case Cell.CELL_TYPE_NUMERIC: if
				 * (DateUtil.isCellDateFormatted(cell)) { data =
				 * cell.getDateCellValue().toString(); } else { data =
				 * Double.toString(cell.getNumericCellValue()); } break;
				 * 
				 * }
				 */
				// data = sht.getRow(rowNo).getCell(cellNo).getStringCellValue();
			} else {
				System.out.println("Please provide valid Row Count");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

	public static void writeExcelData(String sPath, String sSheet, int rowNo, int cellNo, String value) {
		FileInputStream fis =null;
		try {
			 fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			Row row = null;
			Cell cell = null;
			if (sht.getRow(rowNo) != null) {

				// row = sh.getRow(i);
				row = sht.getRow(rowNo);
				if (row.getCell(cellNo) != null) {
					cell = row.getCell(cellNo);
					cell.setCellValue(value);
				} else {
					row.createCell(cellNo).setCellValue(value);
				}
			} else {
				row = sht.createRow(rowNo);
				row.createCell(cellNo).setCellValue(value);
			}

			FileOutputStream fos = new FileOutputStream(sPath);
			wb.write(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null != fis)
			{
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static void writeExcelData(String sPath, String sSheet, ArrayList<ArrayList<String>> value) {
		try {

			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			Row row = null;

			for (int i = 0; i < value.size(); i++) {
				ArrayList<String> columnData = new ArrayList<String>();
				columnData = value.get(i);
				System.out.println(columnData);
				for (int j = 0; j < columnData.size(); j++) {

					if (sht.getRow(j) != null) {
						// row = sh.getRow(i);
						sht.getRow(j).createCell(i).setCellValue(columnData.get(j));
					} else {
						row = sht.createRow(j);
						row.createCell(i).setCellValue(columnData.get(j));
					}

				}

			}

			FileOutputStream fos = new FileOutputStream(sPath);
			wb.write(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int getExcelCellCount(String sPath, String sSheet, int rowNo) {
		int iRowNum = 0;
		int cellCount = 0;

		try {
			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			iRowNum = sht.getLastRowNum();
			if (rowNo <= iRowNum) {
				cellCount = sht.getRow(rowNo).getLastCellNum();
				// System.out.println(cellCount);
			} else {
				System.out.println("Please provide the valid Row Count");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cellCount;

	}
	
	/**
	  * @author Amjath khan
	  * This method writes the data into the excel without changing the format
    */
	
	public static void writeExcel(String sPath, String sSheet, int rowNo, int cellNo, String value) {
		try {

			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			Row row = null;
			Cell cell = null;
			if (sht.getRow(rowNo) != null) {

				// row = sh.getRow(i);
				row = sht.getRow(rowNo);
				if (row.getCell(cellNo) != null) {
					cell = row.getCell(cellNo);
					cell.setCellValue(Double.parseDouble(value));
				} else {
					row.createCell(cellNo).setCellValue(Double.parseDouble(value));
				}
			} else {
				row = sht.createRow(rowNo);
				row.createCell(cellNo).setCellValue(Double.parseDouble(value));
			}

			FileOutputStream fos = new FileOutputStream(sPath);
			wb.write(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
