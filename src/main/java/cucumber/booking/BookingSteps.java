package cucumber.booking;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.booking.BookingPage;
import patterns.WebDriverSingleton;

public class BookingSteps

{
	private static final String URL = "https://www.booking.com";
	private BookingPage bookingPage;
	private WebDriver webDriver;

	public BookingSteps() {		
		webDriver = WebDriverSingleton.getInstance();
		bookingPage = new BookingPage(webDriver);
	}

	@Given("^I am on main booking page$")
	public void loadMainPage() {
		webDriver.get(URL);
		webDriver.manage().window().maximize();
	}

	@When("^I select dates and search by Moscow$")
	public void selectDatesAndSearch() {
		bookingPage.calendar();
		bookingPage.arrivalDate();
		bookingPage.departureDate();
		bookingPage.searchByCity("Moscow");
	}

	@Then("I see there are hotels for required dates")
	public void checkHotelsList() {
		Assert.assertTrue(bookingPage.getHotelList().size() > 0);
	}

	@When("^I select dates and search by Moscow and sort by rating and get first hotel$")
	public void selectDatesAndSearchAndSort() {
		bookingPage.calendar();
		bookingPage.arrivalDate();
		bookingPage.departureDate();
		bookingPage.searchByCity("Moscow");
		bookingPage.sortRaiting();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bookingPage.getHotelList().get(0).click();
	}

	@Then("I see that rating >=9")
	public void checkHotelRaiting() {
		Assert.assertTrue(bookingPage.getHotelRating() >= 9.0);
	}
	
	@After()
	public void takeScreenshot(Scenario scenario) throws IOException {
		File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);	
		String name = scenario.getName();
		FileUtils.copyFile(scrFile, new File("c:\\tmp\\"+name +".png"));		
		WebDriverSingleton.closeWebBrowser();
	}	
}