package ngweb.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ThreadGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;

import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("WeakerAccess")
public class NGWebBasicsTest
{
  private static final Logger LOG
    = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

  private static final String KUSHAN_KAMASIRI = "Kushan Amarasiri";

  private static final String[] LOG_MESSAGE_ARRAY = {
    "the use of the NGWebDriver library was a success -- the name {} was added to ",
    "the target website and Angular specific locators were used"
  };

  private static final String LOG_MESSAGE = StringUtils.join(LOG_MESSAGE_ARRAY);

  private static WebDriver webDriver;
  private static NgWebDriver ngWebDriver;

  @Before
  public void setup()
  {
    LOG.warn("entering the before method");

    ChromeOptions chromeOptions = new ChromeOptions()
      .setHeadless(true);

    webDriver = ThreadGuard.protect(new ChromeDriver(chromeOptions));
    webDriver.get("https://hello-angularjs.appspot.com/sorttablecolumn");

    ngWebDriver = new NgWebDriver((JavascriptExecutor) webDriver);

    LOG.warn("exiting the before method");
  }

  @Test
  public void demonstrateAddingANewUserToTheTargetSite()
  {
    LOG.warn("entering the test method");

    LOG.warn("java.home is {}", System.getProperty("java.home"));
    LOG.warn("java.version is {}", System.getProperty("java.version"));

    ngWebDriver.waitForAngularRequestsToFinish();

    webDriver.findElement(ByAngular.model("name")).sendKeys(KUSHAN_KAMASIRI);
    webDriver.findElement(ByAngular.model("employees")).sendKeys("30");
    webDriver.findElement(ByAngular.model("headoffice")).sendKeys("Colombo");
    webDriver.findElement(ByAngular.buttonText("Submit")).click();

    ngWebDriver.waitForAngularRequestsToFinish();

    String nameEntered = webDriver.findElement(ByAngular.repeater("company in companies")
        .row(4)
        .column("name"))
        .getText();

    LOG.info(LOG_MESSAGE, nameEntered);

    Assertions.assertThat(nameEntered).isEqualTo(KUSHAN_KAMASIRI);

    LOG.warn("exiting the test method");
  }

  @After
  public void tearDown()
  {
    LOG.warn("entering the after method");

    webDriver.close();
    webDriver.quit();

    LOG.warn("exiting the after method");
  }
}
