package testcases;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;

import base.BaseTest;
import utilities.ExcelReader;

public class AddCutomerTest extends BaseTest {
//Customer test case
	@Test(dataProvider = "getData")
	public void addCust(String runMode, String firstName, String lastName, String postCode, String br) throws InterruptedException {
		
		Browser browser = getBrowser(br);

		navigate(browser, "https://way2automation.com/angularjs-protractor/banking/#/login");

		Thread.sleep(2000);

		click("bankManager_login");
	
		click("add_cst_btn");

		type("first_name",firstName);
		type("last_name",lastName);
		type("post_code",postCode);
		Thread.sleep(2000);
		click("customer_submit");
		Thread.sleep(2000);
		
		


	}

	@DataProvider
	public Object[][] getData() {

		String sheetName = "addCustomer";

		ExcelReader excel = new ExcelReader("src/test/resources/excel/testData.xlsx");

		int rowNum = excel.getRowCount(sheetName);

		int colNum = excel.getColumnCount(sheetName);

		Object[][] data = new Object[rowNum - 1][colNum];

		for (int rows = 2; rows <= rowNum; rows++) {

			for (int cols = 0; cols < colNum; cols++) {

				data[rows - 2][cols] = excel.getCellData(sheetName, cols, rows);

			}

		}
		return data;

	}

}
