/***********************************************************************
* @description			: 		Implemented Application Precondition and Postconditions
* @Variables			: 	  	Declared and Initialised AndroidDriver and WebDriver, Instance for GlobalVariables Page
* @BeforeSuiteMethod	: 		DB connection for xyz
* @BeforeTest			: 		Desired Capabilities for launching app and launching portal		
************************************************************************
*/

package com.automation.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.zeroturnaround.zip.ZipUtil;

import com.automation.init.GlobalVariables;
import com.automation.listener.MyExtentListeners;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class BaseLib {
	public static GlobalVariables globalVar = new GlobalVariables();
	private AppiumDriverLocalService service;
	private AppiumServiceBuilder builder;

	static {
		if (globalVar.sUDID != null) {
			globalVar.iPort = Integer.parseInt(System.getProperty("PORT"));
			globalVar.sUDID = System.getProperty("UDID");
			globalVar.sPlatformVersion = System.getProperty("VERSION");
			globalVar.sDeviceName = System.getProperty("DEVICENAME");
			globalVar.sXcodeOrgId = System.getProperty("XCODEORGID");

		} else {

			int rowCount = ExcelLibrary.getExcelRowCount(GenericLib.sConfigPath, "config");
//			System.out.println("List of devices/emulators available in config is = " + rowCount);
			ArrayList<String> deviceCount = new ArrayList<String>();
			int runStatus = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Run Status");
			for (int i = 1; i <= rowCount; i++) {

				if (ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, runStatus).equalsIgnoreCase("Yes")) {
					deviceCount.add(ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, runStatus));
				}
			}
//			System.out.println("Tests will be run on " + );
//			System.out.println("Total connected device count "+ deviceCount.size());
			
			if (String.valueOf(deviceCount.size()).equalsIgnoreCase("1")) {
				for (int i = 1; i <= rowCount; i++) {

					if (ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, runStatus)
							.equalsIgnoreCase("Yes")) {

						int port = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Port");
						int udid = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Device UDID");
						int devName = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config", "Device Name");
						int devVersion = GenericLib.getHeaderColumnIndex(GenericLib.sConfigPath, "config",
								"Device Version");

						globalVar.iPort = Integer
								.parseInt(ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, port).trim());
						globalVar.sUDID = ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, udid).trim();
						globalVar.sDeviceName = ExcelLibrary.getExcelData(GenericLib.sConfigPath, "config", i, devName)
								.trim();
						globalVar.sPlatformVersion = ExcelLibrary
								.getExcelData(GenericLib.sConfigPath, "config", i, devVersion).trim();
					}
				}
			} else {

				System.out.println("************PLEASE SELECT ONE DEVICE IN CONFIG******************");
			}
		}
	}

	public static long startTime;
	public static long endtTime;
	public static String sStartTime;
	public static String sStartTime1;
	public static String sEndTime;
	public static String causePartMsg;
	public static String path = null;
	public static Date date1 = null;
	public static Date date2 = null;
	public static Date date3 = null;//
	public static Date date4 = null;//
	static int a = 1;
	static int b = 0;
	static int c = 1;
	static int totalScripts = a-1 
			+ Integer.parseInt(GenericLib.getProprtyValue(GenericLib.sValidationFile, "Class_Count"));

	@BeforeSuite(alwaysRun = true)
	public void createSheet() throws  IOException {
		date1 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String sDate = sdf.format(date1);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String sDate1 = sdf1.format(date1);
		sStartTime = sDate;
		sStartTime1 = sDate1;
		FileInputStream fis = new FileInputStream(new File(GenericLib.sEXcelResult));
		Workbook wb = new XSSFWorkbook(fis);
		Sheet sh = wb.getSheet("Dashboard");
		path = GenericLib.sEXcelCopy + sStartTime1 + ".xlsx";
		FileOutputStream fos = new FileOutputStream(new File(path));
		wb.setForceFormulaRecalculation(true);
		wb.write(fos);
		wb.close();
		fis.close();
		fos.close();
		ExcelLibrary.writeExcelData(path, "Dashboard", 2, 2, sStartTime);
		
	}

//	@BeforeSuite
//	public void startAppiumServer() throws Exception {
//		//Build the Appium service
//		builder = new AppiumServiceBuilder();
//		builder.withIPAddress("127.0.0.1");
//		builder.usingPort(4723);
//		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
//		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
//		
//		//Start the server with the builder
//		service = AppiumDriverLocalService.buildService(builder);
//		service.start();
//	}

	/**
	 * Description : This Function launch the app based on capabilities provided by
	 * testng.xml file
	 * 
	 * @param port
	 * @param UDID
	 * @param version
	 * @param deviceName
	 * @throws Exception
	 */
	@BeforeMethod(alwaysRun = true)
	public void _LaunchApp() throws Exception {
		// ** Launch Application
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, globalVar.sPlatformVersion);
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, globalVar.sDeviceName);
		cap.setCapability(MobileCapabilityType.UDID, globalVar.sUDID);
//		cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
		cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
		cap.setCapability("systemPort",8200);
		cap.setCapability(MobileCapabilityType.FULL_RESET, false);
		cap.setCapability(MobileCapabilityType.NO_RESET, true);
		cap.setCapability("waitForIdleTimeout", 5000); 
		cap.setCapability("newCommandTimeout", 450000);
		cap.setCapability("adbExecTimeout", 450000);
		cap.setCapability("autoGrantPermissions", true);
		cap.setCapability("autoAcceptAlert", true);
		cap.setCapability("allowDenyPermission", true);
//		cap.setCapability(MobileCapabilityType.APP, GenericLib.apkFilePath);
		cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.landmarkgroup.sahla");
		cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "crc646ee66886a7a00442.SplashScreen");
		globalVar.driver = new AndroidDriver(new URL("http://127.0.0.1:" + globalVar.iPort + "/wd/hub"), cap);
		globalVar.driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		date3 = new Date();//
		
	}

	@AfterMethod(alwaysRun = true)
	public void result(ITestResult res) throws Exception {
		String stat = null;
		String test = res.getName();
		String testCls=res.getMethod().getTestClass().getName();
		String[] str = testCls.split("_");
		String testClsName=str[3];
		String test1 = test.substring(0,2).toUpperCase();
		String testName = test1+""+test.substring(2);
		
		int status = res.getStatus();
		if (status == 1) {
			stat = "PASS";
		} else if (status == 2) {
			stat = "FAIL";
		} else {
			stat = "SKIP";
		}
		if(stat.equals("FAIL"))
		{
		Throwable testResultException = res.getThrowable();
		  if (testResultException instanceof InvocationTargetException) {
		    testResultException = ((InvocationTargetException) testResultException).getCause();
		  }
		  String causeFullMsg = testResultException.toString();
		  causePartMsg = causeFullMsg.substring(0, causeFullMsg.indexOf(":"));
//		  System.out.println(causePartMsg);
		  
		  ExcelLibrary.writeExcelData(path, "Data", c, 4,causePartMsg);
//		  ExcelLibrary.writeExcelData(path, "Data", c, 5,"Need to fix");
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sDate = sdf.format(date);
		date4 = new Date();//
		long execution = date4.getTime() - date3.getTime();//
		String executionTime = GenericLib.printDifference(execution);//
		
		for (int i = a; i <= totalScripts; i++) {
			for (int j = 0; j < 1; j++) {
				ExcelLibrary.writeExcelData(path, "Data", i, j++, testClsName);
				ExcelLibrary.writeExcelData(path, "Data", i, j++, testName);
				ExcelLibrary.writeExcelData(path, "Data", i, j++, stat);
				ExcelLibrary.writeExcelData(path, "Data", i, j, executionTime);
			}
		}
		a++;
		b++;
		c++;
	}
	
	

	@AfterSuite(alwaysRun = true)
	public void finish(ITestContext context) throws Exception {
		date2 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String sDate = sdf.format(date2);
		sEndTime = sDate;

		long execution = date2.getTime() - date1.getTime();
		ExcelLibrary.writeExcelData(path, "Dashboard", 3, 2, sEndTime);
		String executionTime = GenericLib.printDifference(execution);
		ExcelLibrary.writeExcelData(path, "Dashboard", 4, 2, "" + executionTime);
		ExcelLibrary.writeExcel(path, "Dashboard", 5, 2, "" + b);
		
//		FileUtils.copyFileToDirectory(new File("./ExcelResult/" + sStartTime + ".xlsx"), new File(MyExtentListeners.excelDir));
		File sourceFile = new File("./ExcelResult/" + sStartTime + ".xlsx");
		File destFile = new File(MyExtentListeners.excelDir);
		FileUtils.copyFileToDirectory(sourceFile, destFile);
		System.out.println("Creating Zip Report.....");
		ZipUtil.pack(new File("./reports/" + MyExtentListeners.folName), new File("./reports/jenkins_mailer/ExtentReport.zip"));
		System.out.println("------Converted zip folder------");
	}

//	@AfterSuite
//	public void stopAppiumServer() throws Exception {
//		service.stop();
//	}

}
