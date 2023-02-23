import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;


public class SeleniumCombinedTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(); //This line starts the chrome driver
        driver.manage().window().maximize(); //This line maximize the driver
        driver.get("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test(priority = 1)
    public void testRadioButtonSelection() {
        WebElement radioButton3 = driver.findElement(By.cssSelector("input[value='radio3']"));
        radioButton3.click();
        Assert.assertTrue(radioButton3.isSelected(), "Radio button 3 should be selected");
        WebElement radioButton2 = driver.findElement(By.cssSelector("input[value='radio2']"));
        radioButton2.click();
        Assert.assertTrue(radioButton2.isSelected(), "Radio button 2 should be selected");
        Assert.assertFalse(radioButton3.isSelected(), "Radio button 3 should not be selected");
    }

    @Test(priority = 2)
    public void testSuggestionFeature() throws InterruptedException {
        WebElement suggestionField = driver.findElement(By.id("autocomplete"));
        suggestionField.sendKeys("South");
        Thread.sleep(1000);
        WebElement southAfricaOption = driver.findElement(By.xpath("(//div[text()='South Africa'])[1]"));
        southAfricaOption.click();
        Assert.assertEquals(suggestionField.getAttribute("value"), "South Africa", "Suggestion field should have value 'South Africa'");
        suggestionField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        suggestionField.sendKeys("Republic");
        Thread.sleep(1000);
        WebElement firstOption = driver.findElement(By.xpath("//li[contains(@class,'ui-menu-item')][1]"));
        firstOption.click();
        Assert.assertEquals(suggestionField.getAttribute("value"), "Congo, the Democratic Republic of the", "Suggestion field should have value 'Democratic Republic of the Congo'");
    }

    @Test(priority = 3)
    public void testCheckBox() {
        // Find all the checkboxes and store them in a list
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
// Check all the checkboxes and validate that they are all checked
        for (WebElement checkbox : checkboxes) {
            checkbox.click();
            if (!checkbox.isSelected()) {
                System.out.println("Checkbox " + checkbox.getAttribute("value") + " is not checked");
            }
        }
// Uncheck the first checkbox and validate that the other two remain checked
        checkboxes.get(0).click();
        if (checkboxes.get(1).isSelected() && checkboxes.get(2).isSelected()) {
            System.out.println("Checkbox 1 is unchecked and other checkboxes remain checked");
        } else {
            System.out.println("Error: Checkbox 1 is unchecked but other checkboxes are also unchecked");
        }
    }

    @Test(priority = 4)
    public void testShowHide() {
        // Find the element to show/hide
        WebElement element = driver.findElement(By.id("displayed-text"));
// Click on the hide button and validate that the element is hidden
        driver.findElement(By.id("hide-textbox")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.invisibilityOf(element));
        if (element.isDisplayed()) {
            System.out.println("Error: Element is still displayed after clicking Hide button");
        } else {
            System.out.println("Element is hidden after clicking Hide button");
        }
// Click on the show button and validate that the hidden element is shown
        driver.findElement(By.id("show-textbox")).click();
        wait.until(ExpectedConditions.visibilityOf(element));
        if (element.isDisplayed()) {
            System.out.println("Element is shown after clicking Show button");
        } else {
            System.out.println("Error: Element is still hidden after clicking Show button");
        }
    }

    @Test(priority = 5)
    public void testWebTable() {
        // Find the table element
        WebElement table = driver.findElement(By.xpath("//*[contains(@class, 'tableFixHead')]"));
// Get the rows in the table
        List<WebElement> rows = table.findElements(By.tagName("tr"));
// Find the column index for Name, Location, and Amount
        int nameIndex = -1, locationIndex = -1, amountIndex = -1, positionIndex = -1;
        WebElement headerRow = rows.get(0);
        List<WebElement> headers = headerRow.findElements(By.tagName("th"));
        for (int i = 0; i < headers.size(); i++) {
            String headerText = headers.get(i).getText().trim();
            System.out.println(headerText+i);
            if (headerText.equals("Name")) {
                System.out.println(i);
                nameIndex = i;
            } else if (headerText.equals("Position")) {
                positionIndex = i;
            }else if (headerText.equals("City")) {
                locationIndex = i;
            } else if (headerText.equals("Amount")) {
                amountIndex = i;
            }
        }
// Find the row with Joe Postman from Chennai
        boolean found = false;
        int totalAmount = 0;
        for (int i = 1; i < rows.size(); i++) {
            List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
            System.out.print(cells);
            if (cells.get(nameIndex).getText().trim().equals("Joe") && cells.get(positionIndex).getText().trim().equals("Postman") &&cells.get(locationIndex).getText().trim().equals("Chennai")) {
                found = true;
                String amountText = cells.get(amountIndex).getText().trim();
                int amount = Integer.parseInt(amountText);
                System.out.println("Amount for Joe Postman from Chennai: " + amount);
                totalAmount += amount;
            } else {
                String amountText = cells.get(amountIndex).getText().trim();
                int amount = Integer.parseInt(amountText);
                totalAmount += amount;
            }
        }
// Validate that the row for Joe Postman from Chennai has an amount of 46
        if (found && totalAmount == 46) {
            System.out.println("Total amount collected is " + totalAmount);
        } else {
            System.out.println("Error: Unable to find Joe Postman from Chennai or total amount is incorrect");
        }

    }
    @Test(priority = 6)
    public void testIFrame() {
        // Switch to the iframe
        driver.switchTo().frame("iframe-name");
// Find and interact with an element within the iframe
        WebElement iframeParagraph = driver.findElement(By.tagName("p"));
        System.out.println(iframeParagraph.getText());
// Switch back to the default content
        driver.switchTo().defaultContent();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}