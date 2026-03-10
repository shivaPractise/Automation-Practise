package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Locator.WaitForOptions;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.WaitForSelectorOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.Options;

import extentlisteners.ExtentListeners;

public class BaseTest {

	private Playwright playwright;
	public Browser browser;
	public Page page;
	private static Properties OR = new Properties();
	public static FileInputStream fis;
	private Logger log = Logger.getLogger(this.getClass().getName());

	private static ThreadLocal<Playwright> pw = new ThreadLocal<>();
	private static ThreadLocal<Browser> br = new ThreadLocal<>();
	private static ThreadLocal<Page> pg = new ThreadLocal<>();

	public static Playwright getPlaywright() {
		return pw.get();
	}

	public static Browser getBrowser() {
		return br.get();
	}

	public static Page getPage() {
		return pg.get();
	}

	@BeforeSuite
	public void setUp() {
		PropertyConfigurator.configure("src/test/resources/properties/log4j.properties");
		log.info("Test execution Started");
		try {
			fis = new FileInputStream("src/test/resources/properties/OR.properties");
			OR.load(fis);
			log.info("OR Properties file loaded");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Browser getBrowser(String browserName) {
		playwright = Playwright.create();
		pw.set(playwright);
		switch (browserName) {
		case "chrome":
			log.info("Launching Chrome Browser");
			return getPlaywright().chromium()
					.launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
		case "headless":
			log.info("Launching in headless mode");
			return getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
		case "firefox":
			log.info("Launching Firefox Browser");
			return getPlaywright().firefox()
					.launch(new BrowserType.LaunchOptions().setChannel("firefox").setHeadless(false));
		default:
			throw new IllegalArgumentException("Unsupported browser: " + browserName);
		}
	}

	public void navigate(Browser browser, String url) {
		this.browser = browser;
		br.set(browser);
		page = getBrowser().newPage();
		pg.set(page);

		getPage().navigate(url);
		log.info("Navigated to :" + url);

		// Alert handling
		getPage().onDialog(dialog -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			dialog.accept();
			System.out.println("Alert message: " + dialog.message());
		});
		
	}

	

	public void click(String locatorKey) {
	    try {
	        Locator locator = getPage().locator(OR.getProperty(locatorKey));
	        
	        // Wait until the element is visible before clicking
	        locator.waitFor(new Locator.WaitForOptions()
	                .setState(WaitForSelectorState.VISIBLE));
	        
	        locator.click();

	        log.info("Clicking on an element : " + locatorKey);
	        ExtentListeners.getExtent().info("Clicking on an element : " + locatorKey);
	    } catch (Throwable t) {
	        log.severe("Error while clicking on the element :" + t.getMessage());
	        ExtentListeners.getExtent().fail("Error while clicking on the element :" + t.getMessage());
	        Assert.fail(t.getMessage());
	    }
	}
	

	public boolean isElementPresent(String locatorKey) {
		try {
			getPage().waitForSelector(OR.getProperty(locatorKey), new WaitForSelectorOptions().setTimeout(3000));
			String locator = OR.getProperty(locatorKey);
			int count = page.locator(locator).count();

			if (count > 0) {
				log.info("Element found: " + locatorKey);
				ExtentListeners.getExtent().info("Element present: " + locatorKey);
				return true;
			} else {
				log.warning("Element not found: " + locatorKey);
				ExtentListeners.getExtent().fail("Element not present: " + locatorKey);
				return false;
			}
		} catch (Exception e) {
			log.severe("Error while checking element presence: " + e.getMessage());
			ExtentListeners.getExtent().fail("Error while finding the element: " + e.getMessage());
			return false;
		}
	}

	public void type(String locatorKey, String value) {
		try {
			Locator locator = getPage().locator(OR.getProperty(locatorKey));
			locator.waitFor(new Locator.WaitForOptions()
	                .setState(WaitForSelectorState.VISIBLE));

			locator.fill(value);
			log.info("Typing in an Element : " + locatorKey + " and entered the value as :" + value);
			ExtentListeners.getExtent()
					.info("Typing in an Element : " + locatorKey + " and entered the value as :" + value);
		} catch (Throwable t) {

			log.severe("Error while typing in an Element : " + t.getMessage());
			ExtentListeners.getExtent().fail(
					"Error while typing in an Element : " + t.getMessage() + " error message is :" + t.getMessage());
			Assert.fail(t.getMessage());
		}

	}

	public void select(String locatorKey, String value) {
		try {
			 getPage().selectOption(OR.getProperty(locatorKey), new SelectOption().setLabel(value));
			
			log.info("Selecting in an Element : " + locatorKey + " and Selected the value as :" + value);
			ExtentListeners.getExtent()
					.info("Selecting in an Element : " + locatorKey + " and Selected the value as :" + value);
		} catch (Throwable t) {

			log.severe("Error while Selecting in an Element : " + t.getMessage());
			ExtentListeners.getExtent().fail(
					"Error while Selecting in an Element : " + t.getMessage() + " error message is :" + t.getMessage());
			Assert.fail(t.getMessage());
		}

	}
	@AfterMethod
	public void quit() {
		if (getPage() != null) {
			getBrowser().close();
			getPage().close();
			//getPlaywright().close();

	}
}
}