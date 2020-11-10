package com.automation.setup;

import org.testng.annotations.Test;
import com.automation.init.InitializePages;
import com.automation.library.BaseLib;
import com.automation.library.ExcelLibrary;
import com.automation.library.GenericLib;

/*----------------------------------------------------------------------------
 * This Test Script performs the following:
 * Verify app response when tap Enroll Shukran Card
 * Verify if user can able to view the mobile drop down list of country code
 * Verify if user can able to select the country codes
 * Verify if the selected country code displayed on enroll with mobile screen
 * Verify if user can able to scroll up & down the list of country
 * From choose country screen verify App response when user tap back (<---)
 * ---------------------------------------------------------------------------
*/

public class Setup_TS001 extends BaseLib{

	@Test
	public void ts001() throws Exception {
		InitializePages init = new InitializePages(globalVar.driver);
//		init.navigateToHome.navigateToHomePage();
		String un = ExcelLibrary.getExcelData(GenericLib.sTestData, "ScriptsInput", 1, 0);
		String pwd = ExcelLibrary.getExcelData(GenericLib.sTestData, "ScriptsInput", 1, 1);
	}
}
