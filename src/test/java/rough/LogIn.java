package rough;

import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;

import base.BaseTest;

public class LogIn extends BaseTest{
	
	@Test
	public void doLogin() {
		Browser browser = getBrowser("chrome");
		
		navigate(browser , "http://google.com");
		type("search","playwright");
	}
	
	@Test
	public void doEmailLogin() {
		Browser browser = getBrowser("firefox");
		
		navigate(browser , "http://gmail.com");
		type("userName","shivakrishna.s@mydbsync.com");
	}


}
