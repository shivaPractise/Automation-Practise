package testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;

import base.BaseTest;

public class BankManagerLoginTest extends BaseTest {

	@Test
	public void logInAsBankManager() throws InterruptedException {

		Browser browser = getBrowser("chrome");

		navigate(browser, "https://way2automation.com/angularjs-protractor/banking/#/login");

		Thread.sleep(3000);

		click("bankManager_login");

		Assert.assertTrue(isElementPresent("add_cst_btn"), "Bank mamnager login failed");
		
		
	}

}
