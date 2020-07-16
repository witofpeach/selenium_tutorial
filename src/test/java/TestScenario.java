import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestScenario {

    private WebDriver driver;
    private WebDriverWait driverWait;

    public static final String BASE_URL = "https://www.rgs.ru/";

    @Before
    public void startUp() {

        System.setProperty("webdriver.gecko.driver", "webdriver\\geckodriver.exe");
        driver = new FirefoxDriver();

        driver.manage().window().maximize();

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        driverWait = new WebDriverWait(driver, 10);

        driver.get(BASE_URL);
    }


    @Test
    public void testScenario() throws InterruptedException {

        WebElement buttonMenu = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//div[@id='main-navbar']//a[@data-toggle='dropdown' and contains(text(), 'Меню')]")));
        buttonMenu.click();

        WebElement buttonDMS = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//a[contains(text(), 'ДМС')]")));
        buttonDMS.click();

        WebElement headerDMS = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//h1[@class='content-document-header']")));
        Assert.assertEquals("Wrong header!", "ДМС — добровольное медицинское страхование", headerDMS.getText());

        WebElement buttonSendRequest = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//a[contains(text(), 'Отправить заявку')]")));
        buttonSendRequest.click();

        try {
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//h4//b[contains(text(), 'Заявка на добровольное медицинское страхование')]")));
        } catch (Exception e) {
            System.out.println("Waited for the request form for too long!");
        }

        WebElement inputLastName = driver.findElement(By
                .xpath("//input[@name='LastName']"));

        WebElement inputFirstName = driver.findElement(By
                .xpath("//input[@name='FirstName']"));

        WebElement inputMiddleName = driver.findElement(By
                .xpath("//input[@name='MiddleName']"));

        WebElement selectRegion = driver.findElement(By
                .xpath("//select[@name='Region']"));

        WebElement inputPhoneNumber = driver.findElement(By
                .xpath("//label[contains(text(), 'Телефон')]//..//input"));

        WebElement inputEmail = driver.findElement(By
                .xpath("//input[@name='Email']"));

        WebElement inputDate = driver.findElement(By
                .xpath("//label[contains(text(), 'Предпочитаемая дата контакта')]//..//input"));

        WebElement inputComment = driver.findElement(By
                .xpath("//textarea[@name='Comment']"));

        WebElement acceptRulesCheckbox = driver.findElement(By
                .xpath("//input[@type='checkbox']"));

        WebElement buttonSubmit = driver.findElement(By
                .xpath("//button[@id='button-m']"));


        inputLastName.sendKeys("Рыжов");
        inputFirstName.sendKeys("Максим");
        inputMiddleName.sendKeys("Владимирович");

        new Select(selectRegion).selectByIndex(1);

        inputPhoneNumber.click();

        String phoneNumber = "9999879089";

        for (char digit : phoneNumber.toCharArray()) {
            inputPhoneNumber.sendKeys(String.valueOf(digit));
            Thread.sleep(100);
        }

        inputEmail.sendKeys("qwertyqwerty");

        inputDate.click();

        Thread.sleep(1000);

        int[] date = {0, 2, 1, 1, 0, 2, 0, 2, 0};

        for (int digit : date) {
            inputDate.sendKeys(String.valueOf(digit));
            Thread.sleep(50);
        }

        inputComment.sendKeys("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.");

        acceptRulesCheckbox.click();

        Assert.assertEquals("Wrong last name!", "Рыжов", inputLastName.getAttribute("value"));
        Assert.assertEquals("Wrong name!", "Максим", inputFirstName.getAttribute("value"));
        Assert.assertEquals("Wrong middle name!", "Владимирович", inputMiddleName.getAttribute("value"));
        Assert.assertEquals("Wrong region!", "77", selectRegion.getAttribute("value"));
        Assert.assertEquals("Wrong phone number!", "+7 (999) 987-90-89", inputPhoneNumber.getAttribute("value"));
        Assert.assertEquals("Wrong email!", "qwertyqwerty", inputEmail.getAttribute("value"));
        Assert.assertEquals("Wrong date!", "21.10.2020", inputDate.getAttribute("value"));
        Assert.assertEquals("Wrong comment!", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.", inputComment.getAttribute("value"));

        buttonSubmit.click();

        try {
            driverWait.until(ExpectedConditions.visibilityOfElementLocated(By
                    .xpath("//span[contains(text(), 'Введите адрес электронной почты')]")));
        } catch (Exception e) {
            System.out.println("Error - 'wrong email' not found!");
        }

    }

    @After
    public void tearDown()  {
        driver.quit();
    }
}
