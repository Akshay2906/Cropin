package com.automation.pages;



import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.automation.library.BasePage;
import com.automation.library.GenericLib;
import com.automation.util.MobileActionUtil;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class DashboardSalesOrganizationPage extends BasePage {
	public  DashboardSalesOrganizationPage(AndroidDriver driver)
	{
		super(driver);
	}
	
	// Element to click Organization Button
		@FindBy(xpath = "(//android.widget.TextView[@content-desc='BottomTabName'])[3]")
	    private WebElement organBtn;
		
	// Element to click on left Navigation Button
		@FindBy(xpath ="//android.widget.Button[@content-desc='BackButton']")
		private WebElement leftNavBtn;
		
	// Element to click on right Navigation Button
		@FindBy(xpath ="//android.widget.Button[@content-desc='DrillButton']")
		private WebElement rightNavBtn;
			
	// Element to select territory
		@FindBy(xpath ="//android.widget.TextView[@text='UAE']")
		private WebElement selectUAE;
		
		// Element to select specific Area
		@FindBy(xpath = "//android.widget.TextView[@text='Dubai & Sharjah']")
		private WebElement selectArea;
		
	//Element to select specific store
		@FindBy(xpath ="//android.widget.TextView[@text='CP - Mall of Emirates']")
		private WebElement selectSpecificStore;
		
	// Element to click Back Button
		@FindBy(xpath ="//android.widget.Button[@content-desc='WifiButton']")
		private WebElement backBtn;
	
		

		/**
		 * @author Amjath khan
		 * this method clicks on the organization button
		 * @throws Exception
		 */
		public void salesOrganizationBtn() throws Exception
		{
			GenericLib.explicitWait(driver,"//android.widget.TextView[@text='Organization']");
			MobileActionUtil.clickElement(organBtn, driver, "Organization Button");
		}
		/**
		 * @author Amjath khan
		 * This method selects UAE and navigate to particular store in selected Area
		 * @throws Exception
		 */
		public void verifyLocation() throws Exception 
		{
		       for(int i=0;i<3;i++) 
		       {
		    	   GenericLib.explicitWait(driver,"//android.widget.Button[@content-desc='BackButton']");
		    	   MobileActionUtil.clickElement(leftNavBtn, driver,"Left Navigation Button"); 	      
		       }
		       MobileActionUtil.scrollToPerticularText(driver,"UAE");
		       MobileActionUtil.waitForElementToLoad(1500);
		       MobileActionUtil.clickElement(selectUAE, driver,"Select UAE");
		       GenericLib.explicitWait(driver,"//android.widget.Button[@content-desc='DrillButton']");
		       MobileActionUtil.clickElement(rightNavBtn, driver,"Right Navigation Button");
		      
		       MobileActionUtil.scrollToPerticularText(driver,"Dubai & Sharjah");
		       MobileActionUtil.clickElement(selectArea, driver,"Select Area");
		       GenericLib.explicitWait(driver,"//android.widget.Button[@content-desc='DrillButton']");
		       MobileActionUtil.clickElement(rightNavBtn, driver,"Right Navigation Button");
		       MobileActionUtil.waitForElementToLoad(2500);
		       MobileActionUtil.clickElement(rightNavBtn, driver,"Right Navigation Button");
		       MobileActionUtil.scrollToPerticularText(driver,"CP - Mall of Emirates");
		       MobileActionUtil.clickElement(selectSpecificStore, driver,"Specific Store");
		       GenericLib.explicitWait(driver,"//android.widget.TextView[@content-desc='OrgTextLabel']");
		       TouchAction ta = new TouchAction(driver);
	    	   ta.tap(200,550).perform();
		}
         
		
}
