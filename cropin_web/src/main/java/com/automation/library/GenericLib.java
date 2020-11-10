/***********************************************************************
* @author 			:		Shivaraj G
* @description		: 		Application Independent Methods
* @methods 			: 		getConfigValue(),setConfigValue(),toReadExcelData(),setCellExeclData().
*/
package com.automation.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GenericLib {

	public final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static String sFile;
	public static String sDirPath = System.getProperty("user.dir");
	public static String sConfigPath = sDirPath + "/src/main/resources/config/config.xlsx";
	public static String sValidationFile = sDirPath + "/src/test/resources/testdata/validations.properties";
	public static String sTestData = sDirPath + "/src/test/resources/testdata/testdata.xlsx";
	public static String apkFilePath = sDirPath
			+ "/src\\test\\resources\\app\\Sahla Store_com.landmarkgroup.sahla_2.24(127).apk";
	public static String sEXcelResult = sDirPath + "/src/test/resources/template/Master_Report.xlsx";
	public static String sEXcelCopy = sDirPath + "/ExcelResult/";

	/*
	 * Description: To read the basic environment settings data from config file
	 * based on Property file value
	 */
	public static String getProprtyValue(String sFile, String sKey) {
		Properties prop = new Properties();
		String sValue = null;
		try {
			InputStream input = new FileInputStream(sFile);
			prop.load(input);
			sValue = prop.getProperty(sKey);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sValue;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: To read the basic environment settings data from config file
	 */
	public static Properties getPropertyFile(String sFile) {
		Properties prop = new Properties();
		String sValue = null;
		try {
			InputStream input = new FileInputStream(sFile);
			prop.load(input);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:To set the basic environment settings data from config file
	 */
	public static void setPropertyValue(String sFile, String sKey, String sValue) {
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File(sFile));
			prop.load(fis);
			fis.close();
			FileOutputStream fos = new FileOutputStream(new File(sFile));
			prop.setProperty(sKey, sValue);
			prop.store(fos, "Updated  file with " + "Key " + sKey + " and Value " + sValue);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:To read test data from excel sheet based on TestcaseID
	 */
	public static String[] toReadExcelData(String sFilepath, String sSheet, String sTestCaseID) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			for (int i = 0; i <= iRowNum; i++) {
				if (sht.getRow(i).getCell(0).toString().equals(sTestCaseID)) {
					int iCellNum = sht.getRow(i).getPhysicalNumberOfCells();
					sData = new String[iCellNum];
					for (int j = 0; j < iCellNum; j++) {
						Cell cell = sht.getRow(i).getCell(j);
						sData[j] = dataFormatter.formatCellValue(cell);

					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:To read test data from excel sheet based on TestcaseID
	 */
	public static String[] toReadColumnData(String sFilepath, String sSheet, int coloumn) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			for (int i = 1; i <= iRowNum; i++) {
				sData = new String[iRowNum];
				Cell cell = sht.getRow(i).getCell(coloumn);
				sData[i - 1] = dataFormatter.formatCellValue(cell);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:To read test data from excel sheet based on TestcaseID
	 */
	public static String[] readTestCaseIds(String sFilepath, String sSheet) {
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			sData = new String[iRowNum];
			for (int i = 1; i <= iRowNum; i++) {
				sData[i - 1] = sht.getRow(i).getCell(0).getStringCellValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:To read test data from excel sheet based on MilkAnalyzer
	 */
	public static String[] toReadMilkAnalyzerData(String sFilepath, String sSheet, String milkAnalyzerType,
			String configType) {
		DataFormatter dataFormatter = new DataFormatter();

		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			for (int i = 0; i <= iRowNum; i++) {
				if (sht.getRow(i).getCell(0).toString().equals(milkAnalyzerType)) {
					if (sht.getRow(i).getCell(1).toString().equals(configType)) {
						int iCellNum = sht.getRow(i).getPhysicalNumberOfCells();
						sData = new String[iCellNum];
						for (int j = 0; j < iCellNum; j++) {
							Cell cell = sht.getRow(i).getCell(j);
							sData[j] = dataFormatter.formatCellValue(cell);

						}
						break;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header | TEST_CASE_NO
	 */

	public static int getColumnIndex(String filepath, String sSheet, String colName) {
		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, "TEST_CASE_ID");
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}
	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header | TEST_CASE_NO
	 */

	public static int readColumnIndex(String filepath, String sSheet, String firstCol, String colName) {
		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, firstCol);
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header | Sample Template Datas
	 */

	public static Multimap<String, String> readColumnDataMatrixFromExcel(String filepath, String sSheet,
			String firstColumn, String[] columns) throws Exception {

		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, firstColumn);
		Multimap<String, String> multimap = ArrayListMultimap.create();
		Map<String, String> map = new LinkedHashMap<String, String>();

		int index = 0;
		for (int i = 0; i < columns.length; i++) {
			index = GenericLib.readColumnIndex(filepath, sSheet, firstColumn, columns[i]);
			map.put(columns[i], index + "");
		}
		FileInputStream fis = new FileInputStream(filepath);
		Workbook wb = (Workbook) WorkbookFactory.create(fis);
		Sheet sht = wb.getSheet(sSheet);
		int iRowNum = sht.getLastRowNum();
		for (int i = 1; i <= iRowNum; i++) {
			for (int j = 0; j < columns.length; j++) {
				multimap.put(sht.getRow(i).getCell(Integer.parseInt(map.get(columns[0]))).getStringCellValue(),
						sht.getRow(i).getCell(Integer.parseInt(map.get(columns[j]))).getStringCellValue());
			}
		}
		return multimap;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header | Farmer Id
	 */

	public static int readFarmerTempHeadings(String filepath, String sSheet, String colName) {
		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, "Farmer Id");
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header | S No
	 */

	public static int getReportColumnIndex(String filepath, String sSheet, String colName) {
		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, "S No");
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header | MILK_ANALYZER
	 */

	public static int getConfigurationIndex(String filepath, String sSheet, String colName) {
		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, "MILK_ANALYZER");
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Method to read data based on row header
	 */

	public static int getHeaderColumnIndex(String filepath, String sSheet, String colName) {
		String[] firstRow = GenericLib.toReadExcelData(filepath, sSheet, "SI No");
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:Method is used to set data in excel sheet
	 */

	public static void setCellData(String filePath, String sSheet, String sTestCaseID, String columnName, String value)
			throws Exception {
		int columnNumber = getColumnIndex(filePath, sSheet, columnName);
		try {
			FileInputStream fis = new FileInputStream(filePath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			// logger.info("----------Sheet " + sSheet);
			int lastRowNum = sht.getLastRowNum();
			for (int i = 0; i <= lastRowNum; i++) {
				if (sht.getRow(i).getCell(0).toString().equals(sTestCaseID)) {
					Row rowNum = sht.getRow(i);
					Cell cell = rowNum.getCell(columnNumber);
					if (cell == null) {
						cell = rowNum.createCell(columnNumber);
						cell.setCellValue(value);
					} else {
						cell.setCellValue(value);
					}
					break;
				}
			}
			FileOutputStream fileOut = new FileOutputStream(filePath);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}

	public static void executeBatchCommmand(String command) {
		try {
			String line;
			ArrayList<String> deviceUDID = new ArrayList<String>();
			Process p = Runtime.getRuntime().exec("cmd /c " + command);

			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				// logger.info(line);
				deviceUDID.add(line);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				logger.info("" + line);
			}

			bre.close();
			p.waitFor();

		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: To split and return the array
	 */
	public static String[] getSplittedArray(String str, String splitChar) {
		return str.split(splitChar);
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Extract the string based on previous and next strings and
	 * occurrences
	 */
	public static String getString(String str, String startStr, int startOccurance, String endStr, int endOccurance) {
		return str.substring(str.indexOf(startStr, startOccurance) + startStr.length(),
				str.indexOf(endStr, endOccurance));
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Extract the string based on previous and next strings
	 */
	public static String getString(String str, String startStr, String endStr) {
		return str.substring(str.indexOf(startStr) + startStr.length(), str.indexOf(endStr));
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description:To read test data from excel sheet based on TestcaseID
	 */
	public static String[] toReadExcelDataWithNull(String sFilepath, String sSheet, String sTestCaseID) {
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			for (int i = 0; i <= iRowNum; i++) {
				if (sht.getRow(i).getCell(0).toString().equals(sTestCaseID)) {
					int iCellNum = sht.getRow(i).getPhysicalNumberOfCells();
					sData = new String[iCellNum];
					for (int j = 0; j < iCellNum; j++) {
						String value = sht.getRow(i).getCell(j).getStringCellValue();
						if (value.equals("") || value == null || value.equals(" ")) {
							value = "novalue";
						}
						sData[j] = value;
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 */

	public static String readCellData(String xlPath, String sheetName, int rowNumber, int cellNumber)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		FileInputStream fis = new FileInputStream(xlPath);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet(sheetName);
		DataFormatter dataFormatter = new DataFormatter();

		Row row = sh.getRow(rowNumber);
		Cell cell = row.getCell(cellNumber);
		String data = dataFormatter.formatCellValue(cell);

		return data;

	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 */
	public static String[] readInputDataListFromConfig(String path, String sheetName, String testCaseName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		ArrayList<Integer> index = new ArrayList<Integer>();
		index = findCellIndexOfLabelInExcel(path, sheetName, testCaseName);
		System.out.println("Index of given testcasename :: " + index);
		System.out.println(index.get(0));
		System.out.println(index.get(1));

		String[] inputDataRows = (readCellData(path, sheetName, index.get(0), ((index.get(1)) + 1))).split(",");

		return inputDataRows;
	}

	static boolean labelFoundInExcel = false;
	static DataFormatter dataFormatter = new DataFormatter();

	/*
	 * @author: Srinivas Hippargi
	 * 
	 */
	public static ArrayList<String> getAllValuesOfSelectBoxFromExcel(String xlPath, String sheetName, String content) {
		ArrayList<String> valueOfLabelInExcel = new ArrayList<String>();
		ArrayList<Integer> cellIndexOfLabelInExcel = new ArrayList<Integer>();
		try {
			cellIndexOfLabelInExcel = findCellIndexOfLabelInExcel(xlPath, sheetName, content);

			FileInputStream fis = new FileInputStream(xlPath);
			Workbook w1 = WorkbookFactory.create(fis);
			Sheet s1 = w1.getSheet(sheetName);
			if (cellIndexOfLabelInExcel.size() == 0) {
				System.out.println("Label Not Found In Excel");
				// Assert.fail("Label Not Found In Excel");
			} else {
				for (int i = cellIndexOfLabelInExcel.get(0) + 1; i <= s1.getLastRowNum(); i++) {
					Row r1 = s1.getRow(i);
					try {
						Cell c1 = r1.getCell(cellIndexOfLabelInExcel.get(1));
						String data = dataFormatter.formatCellValue(c1);
						if (data != null) {
							valueOfLabelInExcel.add(data);
						}
					} catch (NullPointerException e) {
						break;
					}
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return valueOfLabelInExcel;
	}

	public static ArrayList<Integer> findCellIndexOfLabelInExcel(String xlPath, String sheetName, String content)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		ArrayList<Integer> labelIndexInExcel = new ArrayList<Integer>();

		FileInputStream fis = new FileInputStream(xlPath);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet(sheetName);

		for (int i = 0; i <= sh.getLastRowNum(); i++) {
			Row row = sh.getRow(i);
			try {
				int lastCellCount = row.getLastCellNum();
				for (int j = 0; j < lastCellCount; j++) {

					try {
						Cell cellValue = row.getCell(j);
						String value = dataFormatter.formatCellValue(cellValue);
						if (value.equals(content)) {
							labelIndexInExcel.add(i);
							labelIndexInExcel.add(j);
							labelFoundInExcel = true;
							break;
						}
					} catch (Exception e) {

					}
				}

			} catch (NullPointerException e) {

			}
		}

		/*
		 * if(labelFoundInExcel==false) {
		 * 
		 * // Assert.fail("Given Label Is Not Found In the Excel Sheet");
		 * System.out.println(("Given Label Is Not Found In the Excel Sheet")); }
		 */
		return labelIndexInExcel;

	}

	public static void explicitWait(WebDriver driver, String locator) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
	}

	public static String printDifference(long timeinmilli) {
		String elapsedHours1 = null;
		String elapsedMinutes1 = null;
		String elapsedSeconds1 = null;
		long difference = timeinmilli;

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;

		long elapsedHours = difference / hoursInMilli;
		difference = difference % hoursInMilli;

		long elapsedMinutes = difference / minutesInMilli;
		difference = difference % minutesInMilli;

		long elapsedSeconds = difference / secondsInMilli;
		if (elapsedHours <= 9) {
			elapsedHours1 = "0" + elapsedHours;
		} else {
			elapsedHours1 = elapsedHours + "";
		}
		if (elapsedMinutes <= 9) {
			elapsedMinutes1 = "0" + elapsedMinutes;
		} else {
			elapsedMinutes1 = elapsedMinutes + "";
		}
		if (elapsedSeconds <= 9) {
			elapsedSeconds1 = "0" + elapsedSeconds;
		} else {
			elapsedSeconds1 = elapsedSeconds + "";
		}

		return elapsedHours1 + ":" + elapsedMinutes1 + ":" + elapsedSeconds1;

	}

}
