/***********************************************************************
 * @author 			:		Srinivas Hippargi
 * @description		: 		This class contains action methods which is used for performing 
 * 							action while executing script such as Click, SendKeys 
 */

package com.automation.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.automation.library.GenericLib;
import com.automation.listener.MyExtentListeners;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.AndroidKeyMetastate;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

public class MobileActionUtil {

	public final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void waitForElementToLoad(int milliSeconds) throws InterruptedException {
		Thread.sleep(milliSeconds);
	}

	/**
	 * Description : This method has fluent wait implementation for element to load
	 * which is polling every 250 miliseconds
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void waitForElement(WebElement element, AppiumDriver driver, String elementName, int seconds)
			throws IOException {
		try {
			logger.info("---------Waiting for visibility of element---------" + elementName);
			MobileActionUtil.isEleDisplayed(element, driver, 2, 1, elementName);
//			WebDriverWait wait = new WebDriverWait(driver, seconds);
//			wait.until(ExpectedConditions.visibilityOf(element));
//			logger.info("---------Element is visible---------" + elementName);
		} catch (Exception e) {
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			logger.info("---------Element is not visible---------" + elementName);
		} catch (AssertionError e) {
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			logger.info("---------Element is not visible---------" + elementName);
			throw e;
		}

	}

	/**
	 * Description : This method has fluent wait implementation for element to load
	 * which is polling every 250 miliseconds
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void waitForToastMsg(WebElement element, AppiumDriver driver, String eleName, int seconds)
			throws IOException {
		try {
			logger.info("---------Waiting for visibility of Toast Message---------" + element);

			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element)) != null);
			logger.info("---------Element is visible---------" + element);
		} catch (Exception e) {

		}
	}

	/**
	 * Description: This method helps to verify whether given web Element is present
	 * page or not.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws IOException
	 */
	public static void isEleDisplayed(WebElement element, AppiumDriver driver, String elementName) throws IOException {
		try {
			logger.info("---------Verifying element is displayed or not ---------");

			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			if (element.isDisplayed()) {
				System.out.println(elementName + "------ is displayed");
				MyExtentListeners.test.pass("Verify " + "\'" + elementName + "\'" + " is displayed || " + "\'"
						+ elementName + "\'" + " is displayed ");
			}
		} catch (RuntimeException e) {

			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not displayed");
			throw e;
		}
	}

	/**
	 * Description: This method helps to verify whether given web Element is present
	 * page or not.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean isEleDisplayed(WebElement element, AppiumDriver driver, int seconds, int loop,
			String elementName) throws IOException, InterruptedException {

		boolean flag = false;

		int count = loop;
		while (count > 0) {
			try {
				logger.info("---------Verifying element is displayed or not ---------");
				count--;
				element.isDisplayed();
				flag = true;
				break;

			} catch (RuntimeException e) {
				Thread.sleep(seconds * 1000);
				flag = false;
			}
//			MyExtentListeners.test.pass("Verify " + "\'" + elementName + "\'" + " is displayed  || " + "\'" + elementName
//					+ "\'" + " is displayed ");
		}
		return flag;
	}

	/**
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws IOException
	 */

	public static void verifyElementIsDisplayed(WebElement element, AndroidDriver driver, String elementName)
			throws IOException {
		try {
			logger.info("---------Verifying element is displayed or not ---------");
			WebDriverWait wait = new WebDriverWait(driver, 15);

			if (wait.until(ExpectedConditions.visibilityOf(element)) != null) {
				MyExtentListeners.test.pass("Verify " + "\'" + elementName + "\'" + " is displayed  || " + "\'"
						+ elementName + "\'" + " is displayed ");
				System.out.println(elementName + "------ is displayed");
			}

		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is displayed  || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not displayed");
		}
	}

	/**
	 * Description: this method will click on element which is provided.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws Exception
	 * @throws Exception
	 */
	public static void clickElement(WebElement element, AppiumDriver driver, String elementName) throws Exception {

		try {
			
			logger.info("---------Verifying element is displayed or not ---------");
			waitForElement(element, driver, elementName, 3);
			element.click();
			logger.info("After Click on: " + elementName);
			MyExtentListeners.test.pass("Verify user is able to click on " + "\'" + elementName + "\'"
					+ " ||  User is able to click on " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + "  || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");
			throw error;
		} catch (Exception error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + " || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");
			throw error;
		}

	}

	/**
	 * Description: this method hides keyboard
	 * 
	 * @param driver
	 */

	public static void hideKeyboard(AppiumDriver driver) {
		try {
			driver.hideKeyboard();
			Thread.sleep(2000);
		} catch (Throwable e) {

		}
	}

	/**
	 * Description: this method clear texts from textbox/edit box and type the value
	 * which is provided
	 * 
	 * @param element
	 * @param value
	 * @param elementName
	 * @param driver
	 * @throws Exception
	 */
	public static void clearAndType(WebElement element, String value, String elementName, AndroidDriver driver)
			throws Exception {
		try {
			logger.info("---------Method clear and type  ---------");
			element.clear();
			logger.info(elementName + " is cleared");
			element.sendKeys(value);
			driver.pressKeyCode(AndroidKeyCode.KEYCODE_TAB, AndroidKeyMetastate.META_SHIFT_ON);
			logger.info(value + " is entered in " + elementName);
			hideKeyboard(driver);
			logger.info(" hide keyboard");
			MyExtentListeners.test.pass("Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName
					+ "\'" + " || User is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'");
		} catch (AssertionError error) {

			MyExtentListeners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type on " + "\'" + elementName + "\'");
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type in " + "\'" + elementName + "\'");
		}

	}

	/**
	 * Description: this method type the value which is provided
	 * 
	 * @param element
	 * @param value
	 * @param elementName
	 * @param driver
	 * @throws Exception
	 */
	public static void type(WebElement element, String value, String elementName, AppiumDriver driver)
			throws Exception {
		try {
			logger.info("---------Method type  ---------");
			Thread.sleep(1000);
			element.sendKeys(value);
			// Thread.sleep(300);
			hideKeyboard(driver);
			logger.info("---------hide keyboard  ---------");
			MyExtentListeners.test.pass("Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName
					+ "\'" + " || User is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type on " + elementName);
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(
					"Verify user is able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'"
							+ " || User is not able to type " + "\'" + value + "\'" + "in " + "\'" + elementName + "\'",
					ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("Unable to type in " + elementName);
		}

	}

	/**
	 * Description:Explicit wait to check element is clickable
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void isEleClickable(WebElement element, AppiumDriver driver, String eleName) throws IOException {
		try {
			logger.info("---------Method is Element clickable  ---------");
			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(element)) != null);
			System.out.println(" element is clickable ");
		} catch (AssertionError e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " is clickable || "
					+ "\'" + eleName + "\'" + " is not clickable", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			System.out.println(" element is not clickable ");
			throw e;
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " is clickable || "
					+ "\'" + eleName + "\'" + " is not clickable", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			System.out.println(" element is not clickable ");

		}
	}

	/**
	 * Description: wait till page load until progress bar is invisible
	 * 
	 * @param eleName
	 * @param driver
	 * @param pageName
	 * @throws IOException
	 */

	public static void waitTillPageLoad(String eleName, AppiumDriver driver, String pageName, int seconds)
			throws IOException {
		try {
			logger.info("---------Method waiting for invisibility of progress bar  ---------");
			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(seconds, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(
					(wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.className("android.widget.ProgressBar")))),
					"On clicking" + eleName + " Page is on load, Unable to proceed");
			MyExtentListeners.test.pass(" Verify On clicking " + "\'" + eleName + "\''" + " user is redirected to "
					+ "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''");
		} catch (AssertionError e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");
			throw e;
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");

		}
	}

	/**
	 * Description: Fetch text from element and return as string
	 * 
	 * @param elename
	 * @param driver
	 * @param elementName
	 * @return
	 * @throws IOException
	 */

	public static String gettext(WebElement elename, AppiumDriver driver, String elementName) throws IOException {
		logger.info("--------- get text from element  ---------");
		String eleText = null;
		try {
//			isEleDisplayed(elename, driver, elementName);
			isEleDisplayed(elename, driver, 2, 1, elementName);
			eleText = elename.getText();
		} catch (Exception e) {
			MyExtentListeners.test.addScreenCaptureFromPath(MobileActionUtil.capture(driver));
			Assert.fail("Unable to fetch text from " + "\'" + elename + "\'");

		}
		return eleText;
	}

	/**
	 * Description: This method verify expected result contains in actual result
	 * 
	 * @param actResult
	 * @param expResult
	 * @throws IOException
	 */

	public static void verifyContainsText(String actResult, String expResult, AppiumDriver driver) throws IOException {
		if (actResult.contains(expResult)) {
			MyExtentListeners.test.pass("Verify  Expected : " + "\'" + expResult + "\''" + " contains  Actual :  "
					+ actResult + "  || Expected : " + "\'" + expResult + "\''" + "contains  Actual :  " + actResult);

		} else {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify  Expected : " + "\'" + expResult + "\''"
					+ " contains  Actual :  " + actResult + " ||  Expected : " + "\'" + expResult + "\''"
					+ " does not contains  Actual :  " + actResult, ExtentColor.RED));
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, expResult));

		}
	}

	/**
	 * 
	 * @param actResult
	 * @param expResult
	 * @param desc
	 */

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method verify expected result contains in actual result
	 */

	public static void verifyContainsText(String actResult, String expResult, String desc) throws Exception {
		if (actResult.contains(expResult)) {
			MyExtentListeners.test.pass("Verify  Expected : " + "\'" + expResult + "\''" + " contains  Actual :  "
					+ actResult + "  || Expected : " + "\'" + expResult + "\''" + "contains  Actual :  " + actResult);

		} else {

			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify  Expected : " + "\'" + expResult + "\''"
					+ " contains  Actual :  " + actResult + " ||  Expected : " + "\'" + expResult + "\''"
					+ " does not contains  Actual :  " + actResult, ExtentColor.RED));

			throw new Exception();

		}
	}

	/**
	 * Description: This method verify expected result equals in actual result
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method verify expected result equals in actual result
	 */

	public static void verifyEqualsText(String desc, String actResult, String expResult) throws Exception {
		if (expResult.equalsIgnoreCase(actResult)) {
			MyExtentListeners.test.pass("Verify " + desc + " ||  Expected : " + "\'" + expResult + "\''"
					+ " eqauls  to Actual :  " + actResult);
		} else {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + desc + "  || Expected : " + "\'" + expResult
					+ "\''" + " not eqauls to  Actual :  " + "\'" + actResult + "\'", ExtentColor.RED));
			throw new Exception();
		}
	}

	/**
	 * Description: This method verify expected result not equals in actual result
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	public static void verifyNotEqualsText(String desc, String actResult, String expResult) {
		if (!(expResult.equalsIgnoreCase(actResult))) {
			MyExtentListeners.test.pass("Verify " + desc + " is printed on receipt or not" + " ||  Expected : " + "\'"
					+ expResult + "\''" + " not  to Actual :  " + actResult);
		} else {
			MyExtentListeners.test
					.fail(MarkupHelper
							.createLabel(
									"Verify " + desc + " is printed on receipt or not" + "  || Expected : " + "\'"
											+ expResult + "\''" + "  eqauls to  Actual :  " + "\'" + actResult + "\'",
									ExtentColor.RED));
		}
	}

	/**
	 * Description: This method verify actual result is not null
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	public static void verifyIsNull(String actResult, String desc) {
		if (actResult == null) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Value is null", ExtentColor.RED));
			throw new RuntimeException();
		} else {
			MyExtentListeners.test.pass("Verify" + desc + " is Printed on Receipt or not" + " || " + desc + " : "
					+ actResult + " is printed on receipt");
		}
	}

	/**
	 * 
	 * Description: This method to scroll left side based on device height and width
	 * 
	 * @param value
	 * @param startX
	 * @param endX
	 * @param driver
	 * @throws Exception
	 */

	public static void swipeRightToLeft(int value, double startX, double endX, AppiumDriver driver) throws Exception {
		try {
			Thread.sleep(1000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int startx = (int) (dSize.width * startX);
				int endx = (int) (dSize.width * endX);
				int starty = dSize.height / 2;
				driver.swipe(startx, starty, endx, starty, 1000);

			}
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "SwipeLeft"));
			throw e;
		}
	}

	/**
	 * Description: This method to scroll Right side based on device height and
	 * width
	 * 
	 * @param value
	 * @param startx
	 * @param endx
	 * @param driver
	 * @throws Exception
	 */
	public static void swipeLefToRight(int value, double startx, double endx, AppiumDriver driver) throws Exception {
		try {
			Thread.sleep(1000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int startX = (int) (dSize.width * startx);
				int endX = (int) (dSize.width * endx);
				int starty = dSize.height / 2;
				driver.swipe(startX, starty, endX, starty, 1000);
			}
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "SwipeRight"));
			throw e;

		}
	}

	/**
	 * Description: This method to scroll Up side based on device height and width
	 * 
	 * @param value
	 * @param driver
	 * @param starty1
	 * @param endy1
	 * @throws Exception
	 */

	public static void swipeBottomToTop(int value, AppiumDriver driver, double starty1, double endy1) throws Exception {
		try {
			Thread.sleep(1000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int starty = (int) (dSize.height * starty1);
				int endy = ((int) (dSize.height * endy1));
				int startx = dSize.width / 2;
				driver.swipe(startx, starty, startx, endy, 1000);
				MyExtentListeners.test.pass("User is able to perform scroll action from bottom to top");
			}
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "SwipeUp"));
			throw e;
		}
	}

	/**
	 * Description: This method to scroll Bottom side based on device height and
	 * width
	 * 
	 * @param value
	 * @param driver
	 * @param starty1
	 * @param endy1
	 * @throws Exception
	 */
	public static void swipeTopToBottom(int value, AppiumDriver driver, double starty1, double endy1) throws Exception {
		try {
			Thread.sleep(1000);

			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int starty = (int) (dSize.height * starty1);
				int endy = (int) (dSize.height * endy1);
				int startx = dSize.width / 2;
				driver.swipe(startx, starty, startx, endy, 1000);
				MyExtentListeners.test.pass("User is able to perform scroll action from top to bottom");
			}
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "SwipeDown"));
			throw e;
		}
	}

	/**
	 * Description: This method is to long press on element upto 3 seconds then
	 * release
	 * 
	 * @param driver
	 * @param element
	 * @throws IOException
	 */
	public static void performLongPress(AppiumDriver driver, WebElement element) throws IOException {

		try {
			TouchAction act1 = new TouchAction((MobileDriver) driver);
			act1.longPress(element).waitAction(3000).release().perform();
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "LongPress"));

		}
	}

	/**
	 * Description: This method is to long press on element upto 3 seconds then
	 * release
	 * 
	 * @param driver
	 * @param element
	 * @throws IOException
	 */
	public static void performTouchTap(AppiumDriver driver, WebElement element) throws IOException {

		try {
			TouchAction act1 = new TouchAction((MobileDriver) driver);
			act1.moveTo(element).tap(element).perform();
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "LongPress"));

		}
	}

	/**
	 * Description: This method is to capture screenshot
	 * 
	 * @param driver
	 * @param screenShotName
	 * @return
	 * @throws IOException
	 */

	public static String capture(AppiumDriver driver, String screenShotName) throws IOException {
		File source = driver.getScreenshotAs(OutputType.FILE);
		String dest = MyExtentListeners.screenShotPath +screenShotName + ".png";
		System.out.println(dest);
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);
		return dest;
	}

	public static String capture(AppiumDriver driver) throws IOException {
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		String sDate = sdf.format(date);

		File source = driver.getScreenshotAs(OutputType.FILE);
		String dest = MyExtentListeners.screenShotPath + "/ " + sDate + ".png";
		System.out.println(dest);
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);
		return dest;
	}

	public static String captureElementScreenshot(AppiumDriver driver, WebElement element) throws IOException {

		// Get entire page screenshot
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullImg = ImageIO.read(src);

		// Get the location of element on the page
		Point point = element.getLocation();

		// Get width and height of the element
		int elementWidth = element.getSize().getWidth();
		int elementHeight = element.getSize().getHeight();

		// Crop the entire page screenshot to get only element screenshot
		BufferedImage elementScreenshot = fullImg.getSubimage(point.getX(), point.getY(), elementWidth, elementHeight);
		ImageIO.write(elementScreenshot, "png", src);

		// Copy the element screenshot to disk
		String dest = MyExtentListeners.screenShotPath + element + ".png";
		File destination = new File(dest);
		FileUtils.copyFile(src, destination);
		return dest;
	}

	/**
	 * 
	 * Description: Method for Scrolling to particular element based on direction
	 * and device size
	 * 
	 * @param maxScroll
	 * @param start
	 * @param end
	 * @param scrollType
	 * @param element
	 * @param driver
	 * @throws Exception
	 */
	public static void scrollToElement(int maxScroll, double start, double end, String scrollType, WebElement element,
			AppiumDriver driver) throws Exception {

		while (maxScroll != 0) {
			try {
				if (element.isDisplayed()) {
					maxScroll++;
					break;
				}
			} catch (Exception e) {
				switch (scrollType.toUpperCase()) {
				case ("DOWN"):
					swipeTopToBottom(1, driver, start, end);
					break;

				case ("UP"):
					swipeBottomToTop(1, driver, start, end);
					break;

				case ("LEFT"):
					swipeRightToLeft(1, start, end, driver);
					break;

				case ("RIGHT"):
					swipeLefToRight(1, start, end, driver);
					break;

				default:
					MyExtentListeners.test.warning(MarkupHelper.createLabel(" Invalid Swipe type", ExtentColor.AMBER));

					break;

				}

			}
			maxScroll--;
		}
	}

	/**
	 * 
	 * @param driver
	 * @return
	 * @throws IOException
	 */

//	public static String getToastMessage(AppiumDriver driver) throws IOException {
//		String result = null;
//		File scfile = driver.getScreenshotAs(OutputType.FILE);
//		FileUtils.copyFile(scfile, new File(GenericLib.sDirPath + "/toasts/screen.png"));
//		ITesseract instance = new Tesseract();
//		try {
//			result = instance.doOCR(scfile);
//		} catch (TesseractException e) {
//		}
//		System.out.println("************* Toast message text************** " + result.toString());
//		return result;
//	}

	public static void getToastMessage(AppiumDriver driver, String sFile, String expected) throws IOException {
		String result = null;
		File scfile = driver.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scfile, new File(GenericLib.sDirPath + "/toasts/screen.png"));
		ITesseract instance = new Tesseract();
		try {
			result = instance.doOCR(scfile);
		} catch (TesseractException e) {
		}
		System.out.println("************* Toast message text************** " + result.toString());
//		Assert.assertEquals(result, GenericLib.getProprtyValue(sFile, expected));
		System.out.println(result);
		boolean status = result.contains(expected);
		Assert.assertEquals(status, true);
		MyExtentListeners.test.pass("Toast message is displaying the expected " + expected + " message");
//		return result;
	}

	/**
	 * Description : Functional Verification
	 * 
	 * @param desc
	 * @param actResult
	 * @param expResult
	 */

	public static void verifyEqualsText_Funct(String desc, String actResult, String expResult) {
		// if (expResult.equalsIgnoreCase(actResult)) {
		if (expResult.equals(actResult)) {
			MyExtentListeners.test.pass("Verify " + desc + " is displayed or not " + " ||  Expected : " + "\'"
					+ expResult + "\''" + " eqauls  to Actual :  " + actResult);
		} else {
			MyExtentListeners.test.fail(MarkupHelper
					.createLabel("Verify " + desc + " is diaplayed or not" + "  || Expected : " + "\'" + expResult
							+ "\''" + " not eqauls to  Actual :  " + "\'" + actResult + "\'", ExtentColor.RED));
		}
	}

	/**
	 * 
	 * @param actResult
	 * @param expResult
	 * @param desc
	 */

	public static void verifyContainsText_Funct(String actResult, String expResult, String desc) {
		if (actResult.contains(expResult)) {
			MyExtentListeners.test.pass("Verify Text" + desc + " is displayed or not " + " ||  Expected : " + "\'"
					+ expResult + "\''" + " eqauls  to Actual :  " + actResult);

		} else {
			MyExtentListeners.test.fail(MarkupHelper
					.createLabel("Verify Text" + desc + " is diaplayed or not" + "  || Expected : " + "\'" + expResult
							+ "\''" + " not eqauls to  Actual :  " + "\'" + actResult + "\'", ExtentColor.RED));

		}
	}

	public static void isEleIsEnabled(WebElement element, AppiumDriver driver, String elementName) throws IOException {
		try {
			logger.info("---------Verifying element is Enabled or not ---------");

			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			if (element.isEnabled()) {
				System.out.println(elementName + "------ is displayed");
				MyExtentListeners.test.pass("Verify " + "\'" + elementName + "\'" + " is enabled || " + "\'"
						+ elementName + "\'" + " is enabled ");
			}
		} catch (RuntimeException e) {

			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is enabled || " + "\'" + elementName + "\'" + " is not enabled ", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is not Enabled");
			throw e;
		}
	}

	public static void isEleIsSelected_funct(WebElement element, AppiumDriver driver, String elementName)
			throws IOException {
		try {
			logger.info("---------Verifying element is Selected or not ---------");

			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(1, TimeUnit.MINUTES)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			if (element.isSelected() == false) {
				System.out.println(elementName + "------ is  Not Selected");
				MyExtentListeners.test.pass("Verify " + "\'" + elementName + "\'" + " is Not Selected || " + "\'"
						+ elementName + "\'" + " is Not Selected ");
			}
		} catch (RuntimeException e) {

			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
					+ " is Selected || " + "\'" + elementName + "\'" + " is  Selected ", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			System.out.println(elementName + "------ is Selected");
			throw e;
		}
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method is to move to element and click refresh
	 */

	public static void actionClick(WebElement element, AppiumDriver driver, String elementName) throws IOException {

		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).click().build().perform();
			MyExtentListeners.test.pass("Verify user is able to click on " + "\'" + elementName + "\'"
					+ " ||  User is able to click on " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + "  || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw error;
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + " || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));

		}

	}

	/*
	 * @author: Srinivas Hippargi
	 * 
	 * Description: Tap on particular element based size co-orinates
	 */
	public static void tapOnElement(double x, double y, AppiumDriver driver) throws InterruptedException {
		Thread.sleep(5000);
		Dimension dSize = driver.manage().window().getSize();
		int sx1 = driver.manage().window().getSize().getWidth();
		int sx2 = driver.manage().window().getSize().getHeight();
		int sX = (int) (dSize.width * x);
		int sY = (int) (dSize.height * y);
		driver.tap(1, sX, sY, 1);
		System.out.println("Tapped");
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: handles webwiew and native_app mode
	 */
	public static void switchToView(AppiumDriver driver) {
		Set<String> contextNames = driver.getContextHandles();

		for (String contextName : contextNames) {
			System.out.println("Avaliable Context: " + contextName);
		}
		for (String contextName : contextNames) {
			if (contextName.contains("NATIVE_APP")) {
				driver.context(contextName);
				System.out.println(contextName);
			} else {
				driver.context(contextName);
				System.out.println(contextName);
			}
		}
	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method is to fetch the system date and time in
	 * yyyy-mm-ddThh-mm-ss
	 * 
	 */
	public static String getSystemDate() {
		SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		Date finalNewDate = calendar.getTime();
		dateTimeInGMT.setTimeZone(istTimeZone);
		String finalNewDateString = dateTimeInGMT.format(finalNewDate);
		System.out.println(finalNewDateString);
		return finalNewDateString;

	}

	/*
	 * @author:Srinivas Hippargi
	 * 
	 * Description: This method will extract device id (IMEI)
	 * 
	 */
	public static String getDeviceId() throws Exception {

		Process proc = Runtime.getRuntime().exec("adb shell dumpsys iphonesubinfo");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		// read the output from the command
		String s = null;
		String deviceId = "";
		while ((s = stdInput.readLine()) != null) {
			if (s.contains("Device ID")) {
				String[] s1 = s.trim().split(" ");
				deviceId = s1[s1.length - 1];
				System.out.println("IMEI-----------" + deviceId);
				break;
			}
		}
		if (deviceId.equals("") || deviceId == null || deviceId.equals(" ")) {

			throw new Exception(" Please connect the device");
		}
		return deviceId.trim();
	}

	/**
	 * Description: wait till page load until progress bar is invisible
	 * 
	 * @param eleName
	 * @param driver
	 * @param pageName
	 * @throws IOException
	 */

	public static void waitTillProgressBarLoad(String eleName, AppiumDriver driver, String pageName, int seconds)
			throws IOException {
		try {
			logger.info("---------Method waiting for invisibility of progress bar  ---------");
			Wait<AppiumDriver> wait = new FluentWait<AppiumDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
			Assert.assertTrue(
					(wait.until(ExpectedConditions
							.invisibilityOfElementLocated(By.id("com.stellapps.usb:id/progressBar")))),
					"On clicking" + eleName + " Page is on load, Unable to proceed");
			MyExtentListeners.test.pass(" Verify On clicking " + "\'" + eleName + "\''" + " user is redirected to "
					+ "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''");
		} catch (AssertionError e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");
			throw e;
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(" Verify On clicking " + "\'" + eleName + "\''"
					+ " user is redirected to " + "\'" + pageName + "\''" + "  ||  On clicking " + "\'" + eleName
					+ "\''" + " user is not redirected to " + "\'" + pageName + "\''", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			Assert.fail("On clicking " + "\'" + eleName + "\''" + ", Page is on load, Unable to proceed");

		}
	}

	public static String readOTPFromNotifications(AppiumDriver driver, WebElement element, String elementName,
			int digit) throws IOException {

		String OTP = null;
		try {
			logger.info("-------------Action to perform open notifications-----------");
			AndroidDriver androidDriver = (AndroidDriver) driver;
			androidDriver.openNotifications();
			waitForElement(element, driver, elementName, 10);
			String SMS = element.getText().split("code:")[0];
			Pattern pattern = Pattern.compile("(\\d{" + digit + "})");
			Matcher matcher = pattern.matcher(SMS);
			if (matcher.find()) {
				OTP = matcher.group(0);
			}
			MyExtentListeners.test.pass("User is able to retrieve OTP from " + "\'" + elementName);
		} catch (AssertionError error) {
			MyExtentListeners.test.fail(MarkupHelper
					.createLabel("User is not able to retrieve OTP from " + "\'" + elementName, ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("User is not able to retrieve OTP from " + "\'" + elementName + "\'");

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw error;
		} catch (Exception error) {
			MyExtentListeners.test.fail(MarkupHelper
					.createLabel("User is able to retrieve OTP from " + "\'" + elementName, ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("User is not able to retrieve OTP from " + "\'" + elementName);

		}

		return OTP;

	}

	public static String decimalRoundingOff(String data, String format) {

		String str = data;

		switch (format) {

		case ".0":
			if (!(str.contains("."))) {
				str = str + ".0";
			}
			break;

		case ".00":
			if (!(str.contains("."))) {
				str = str + ".00";
			} else if ((str.substring(str.indexOf('.'))).length() == 2) {
				str = str + "0";
			}
			break;
		}

		return str;
	}

	/* To write the data to the cell */
	public void setExcelData(String xlPath, String sheetName, int rowNo, int cellNo, String value) {
		try {
			FileInputStream fis = new FileInputStream(xlPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sheet = wb.getSheet(sheetName);
			Row row = sheet.getRow(rowNo);
			Cell cell = row.getCell(cellNo);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value);
			FileOutputStream fileOut = new FileOutputStream(xlPath);
			wb.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author TYSS - Shivaraj G Description: this method will scroll to the
	 *         particular text which is provided.
	 * 
	 * 
	 */
	public static void scrollToPerticularText(AndroidDriver driver, String text) throws Exception {

		try {
			logger.info("---------Verifying the control is scrolling to the particular text or not ---------");
			Thread.sleep(3000);
			driver.findElementsByAndroidUIAutomator("new UiScrollable(new UiSelector()"
					+ ".scrollable(true)).scrollIntoView(" + "new UiSelector().text(\"" + text + "\"))");
			logger.info("After scrolling to the text: " + text);
			MyExtentListeners.test.pass("Verify user is able to scroll to " + "\'" + text + "\'"
					+ " ||  User is able to scroll to " + "\'" + text + "\'");
		} catch (AssertionError error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to scroll to " + "\'" + text + "\'"
					+ "  || User is not able to scroll to " + "\'" + text + "\'", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, text));
			Assert.fail("unable to scroll to " + "\'" + text + "\'");

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, text));
			throw error;
		} catch (Exception error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to scroll to " + "\'" + text + "\'"
					+ " || User is not able to scroll to " + "\'" + text + "\'", ExtentColor.RED));
			Assert.fail("unable to scroll to " + "\'" + text + "\'");
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, text));
			throw error;
		}

	}

	/**
	 * Description:Explicit wait to check element is clickable
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void verifyText(WebElement element, AndroidDriver driver, String eleName, String sKey)
			throws IOException {
		try {
			logger.info("---------verifying the text---------");
			String actualResult = element.getText();
			Assert.assertEquals(actualResult, GenericLib.getProprtyValue(GenericLib.sValidationFile, sKey));
			MyExtentListeners.test
					.pass(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " text is same as expected || "
							+ "\'" + eleName + "\'" + " text is same as expected", ExtentColor.GREEN));
		} catch (AssertionError e) {
			MyExtentListeners.test
					.fail(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " text is same as expected  || "
							+ "\'" + eleName + "\'" + " text is not same as expected", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			throw e;
		} catch (Exception e) {
			MyExtentListeners.test
					.fail(MarkupHelper.createLabel("Verify " + "\'" + eleName + "\'" + " text is same as expected  || "
							+ "\'" + eleName + "\'" + " text is not same as expected", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));

		}
	}

	/**
	 * Description:Explicit wait to check element is clickable
	 * 
	 * @param element
	 * @param driver
	 * @param eleName
	 * @throws IOException
	 */
	public static void swipeToParticularText(String textValue, AndroidDriver driver, String eleName)
			throws IOException {
		try {
			logger.info("---------verifying the text---------");
			driver.findElementsByAndroidUIAutomator("new UiScrollable(new UiSelector()"
					+ ".scrollable(true)).scrollIntoView(" + "new UiSelector().text(\"" + textValue + "\"))");
			MyExtentListeners.test.pass(MarkupHelper.createLabel(
					"Verify user can able to swipe to the " + "\'" + eleName + "\'" + " field. || " + "\'"
							+ "User can successfully able to swipe to the " + eleName + "\'" + " field.",
					ExtentColor.GREEN));
		} catch (AssertionError e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(
					"Verify user can able to swipe to the " + "\'" + eleName + "\'" + " field. || " + "\'"
							+ " User can successfully able to swipe to the " + eleName + "\'" + " field. ",
					ExtentColor.RED));
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));
			throw e;
		} catch (Exception e) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel(
					"Verify user can able to swipe to the " + "\'" + eleName + "\'" + "  field. || " + "\'"
							+ " User can successfully able to swipe to the " + eleName + "\'" + " field. ",
					ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, eleName));

		}
	}

	public static void backNavigate(AppiumDriver driver) {
		try {

		Thread.sleep(10000);
		// driver.navigate().back();
		((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
		Thread.sleep(1000);
		((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
		}
		catch (Throwable e) {

		}
		}

	/**
	 * @author TYSS - Raghaw
	 */
	public static void swipeRightToLeftInMobile(int value, double startX, double endX, AppiumDriver driver)
			throws Exception {
		try {
			Thread.sleep(3000);
			System.out.println("inside swipe");
			for (int i = 1; i <= value; i++) {
				Dimension dSize = driver.manage().window().getSize();
				int startx = (int) (dSize.width * startX);
				int endx = (int) (dSize.width * endX);
				int starty = dSize.height / 4;
				driver.swipe(startx, starty, endx, starty, 3000);
				System.out.println(" Co-ordinates " + startx + " " + endx + " " + starty);

			}
		} catch (Exception e) {

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, "SwipeLeft"));
			throw e;
		}
	}
	
	/**
	 * @author TYSS - Raghawvenddraa C B
	 * Description: This method will get the month and date in the calendar option
	 * 
	 * 
	 */
	
	public static void calender_Data(AppiumDriver driver, int months,int day) 
	{
		
		int selectMonth=months;
		int selectDay=day;
		String selectedDate=null;
		String selectedYear=null;
		for(int i=1;i<=selectMonth;i++) 
		{
			driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='Previous month']")).click();
		}
		driver.findElement(By.xpath("//android.view.View[@text="+selectDay+"]")).click();
		selectedDate=driver.findElement(By.xpath("//android.widget.TextView[@resource-id='android:id/date_picker_header_date']")).getText();
		
		selectedYear=driver.findElement(By.xpath("//android.widget.TextView[@resource-id='android:id/date_picker_header_year']")).getText();
		
		System.out.println("Selected Date is "+selectedDate+" "+selectedYear);
		driver.findElement(By.xpath("//android.widget.Button[@text='DONE']")).click();
//		driver.findElement(By.xpath("//android.widget.Button[@resource-id='android:id/button1']")).click();
	}
	
	/**
	 * This method is used to drag the element 
	 * @author Amjath khan
	 * @param element
	 */
	public static void swipeTheElementRightToLeft( WebElement element, AndroidDriver driver)
	{
		Point p = element.getLocation();
		int startx = (p.getX()*20);
		int starty = p.getY();
		Dimension Size = element.getSize();
		int endx = (int)(startx*0.20);
		int endy = starty;
		TouchAction ta = new TouchAction(driver);
		ta.press(startx, starty).waitAction(2000).moveTo(endx, endy).release().perform();
	}
	
	/**
	 * This method is used to drag the element 
	 * @author Shivaraj
	 * @param element
	 */
	public static void swipeTheElementLeftToRight( WebElement element, AndroidDriver driver)
	{
		Point p = element.getLocation();
		int startx = (int)(p.getX()*0.15);
		int starty = p.getY();
		Dimension Size = element.getSize();
		int endx = (int)(startx*18);
		int endy = starty;
		TouchAction ta = new TouchAction(driver);
		ta.press(startx, starty).waitAction(2000).moveTo(endx, endy).release().perform();
	}
	
	public static void verifyElementPresent_Click(WebElement element, AndroidDriver driver, String elementName, int seconds)
			throws IOException 
	{
		
		try {
			logger.info("---------Waiting for visibility of element---------" + element);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(ExpectedConditions.visibilityOf(element));
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ element present");
//			Wait<AppiumDriver> wait1 = new FluentWait<AppiumDriver>(driver).withTimeout(seconds, TimeUnit.SECONDS)
//					.pollingEvery(250, TimeUnit.MICROSECONDS).ignoring(NoSuchElementException.class);
//			 Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element))
			// !=
			// null);
			logger.info("---------Element is visible---------" + element);
		} catch (Exception e) {
//			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
//					+ " is displayed || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			logger.info("---------Element is not visible---------" + element);
		} catch (AssertionError e) {
//			MyExtentListners.test.fail(MarkupHelper.createLabel("Verify " + "\'" + elementName + "\'"
//					+ " is displayed || " + "\'" + elementName + "\'" + " is not displayed ", ExtentColor.RED));
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			logger.info("---------Element is not visible---------" + element);
			throw e;
		}
			}





//Raghaw method
	
	/**
	 * Description: this method will click on element which is provided.
	 * 
	 * @param element
	 * @param driver
	 * @param elementName
	 * @throws Exception 
	 * @throws Exception
	 */
	public static void click_Element(List<WebElement> element, AppiumDriver driver, String elementName) throws Exception {

		try {
			logger.info("---------Verifying element is displayed or not ---------");
			//waitForElement(element, driver, elementName, 10);
			element.get(0).click();
			logger.info("After Click on: "+elementName);
			MyExtentListeners.test.pass("Verify user is able to click on " + "\'" + elementName + "\'"
					+ " ||  User is able to click on " + "\'" + elementName + "\'");
		} catch (AssertionError error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + "  || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");

			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw error;
		} catch (Exception error) {
			MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
					+ "\'" + " || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));
			Assert.fail("unable to Click on " + "\'" + elementName + "\'");
			MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
			throw error;
		}

	}	
	
	
	
	//Raghaw method for verify element
	
		/**
		 * Description: this method will click on element which is provided.
		 * 
		 * @param element
		 * @param driver
		 * @param elementName
		 * @throws Exception 
		 * @throws Exception
		 */
		public static void verify_Element(List<WebElement> element,WebElement elet, AppiumDriver driver, String elementName) throws Exception {

			try {
				logger.info("---------Verifying element is displayed or not ---------");
				//waitForElement(element, driver, elementName, 10);
				for(int i=0;i<element.size();i++) {
				String Expectedtext=elet.getText();
				String AcutalText=element.get(i).getText();
				System.out.println("expected test----> " +Expectedtext);
				System.out.println("text values present---> "+AcutalText);
				
				if(AcutalText.contentEquals(Expectedtext)) {
					element.get(i).click();
					break;
				}
				}
				logger.info("After Click on: "+elementName);
				MyExtentListeners.test.pass("Verify user is able to click on " + "\'" + elementName + "\'"
						+ " ||  User is able to click on " + "\'" + elementName + "\'");
			} catch (AssertionError error) {
				MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
						+ "\'" + "  || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));

				MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
				Assert.fail("unable to Click on " + "\'" + elementName + "\'");

				MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
				throw error;
			} catch (Exception error) {
				MyExtentListeners.test.fail(MarkupHelper.createLabel("Verify user is able to click on " + "\'" + elementName
						+ "\'" + " || User is not able to click on " + "\'" + elementName + "\'", ExtentColor.RED));
				Assert.fail("unable to Click on " + "\'" + elementName + "\'");
				MyExtentListeners.test.addScreenCaptureFromPath(capture(driver, elementName));
				throw error;
			}

		}

		
		//Raghaw method for Toast Message
		
		/**
		 * This method is used to get Toast message 
		 * @author Raghawvenddraa
		 * @param element
		 */
		
		public static void getToast_Message(AppiumDriver driver, String expected) throws IOException {
			String result = null; 
			File scfile = driver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scfile, new File(GenericLib.sDirPath + "/toasts/screen.png"));
			ITesseract instance = new Tesseract();
			File tessDataFolder = LoadLibs.extractTessResources("tessdata");
			instance.setDatapath(tessDataFolder.getAbsolutePath());
			try {
				result = instance.doOCR(scfile); 
			} 	catch (TesseractException e) {
			}
			System.out.println("************ Toast message text************* " + result.toString());
//			Assert.assertEquals(result, GenericLib.getProprtyValue(sFile, expected));
			System.out.println(result);
			boolean status = result.contains(expected);
			Assert.assertEquals(status, true);
			MyExtentListeners.test.pass("Toast message is displaying the expected " + expected + " message");
//			return result; 
		}

		
}
