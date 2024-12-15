package com.qa.nai;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ShopJavaSeliniumTest {
    public static final Logger logger = LogManager.getLogger(ShopJavaSeliniumTest.class);

    WebDriver driver;
    String driverPath = "/usr/local/bin/geckodriver"; // Your geckodriver path
    String screenshotPath = "/Users/fevenbelay/Downloads/Selinium_Project 4/screenshots";
    String url = "http://www.amazon.com/";
    String msg = "Item not found or price/color does not match expectations.";
    String testDataFile = "/Users/fevenbelay/Downloads/Selinium_Project 4/demoapp/src/test/data.xlsx"; // Your Excel path

    String item,type, color, price;
    String amazonSearchTextBoxId = "twotabsearchtextbox";
    String amazonSearchButtonId = "nav-search-submit-button";

    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");

    @BeforeTest
    void setup() throws Exception {
        logger.info("Setting up the test...");
        setExcelFile();
        System.setProperty("webdriver.gecko.driver", driverPath); // Set up Firefox driver
        driver = new FirefoxDriver(); // Using Firefox
        driver.get(url);
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.titleContains("Amazon.com. Spend less. Smile more."));
        logger.info("Test setup completed.");
    }
    
    @AfterTest
    void tearDown() {
        logger.info("Tearing down the test...");
        if (driver != null) {
            driver.quit();
        }
        logger.info("Test teardown completed.");
    }

    @Test(priority = 1, enabled = true)
    void searchAndAddItem() throws Exception {
        String formattedDateTime = currentDateTime.format(formatter);
        logger.info("Starting test case: Search and Add Item to Cart");

        logger.info("Step 1: Searching for item...");
        searchItem(formattedDateTime);

        logger.info("Step 2: Selecting the item...");
        selectItem(formattedDateTime);

        logger.info("Step 3: Adding the item to the cart...");
        addToCart(formattedDateTime);

        logger.info("Test case completed successfully.");
    }

    void searchItem(String formattedDateTime) throws Exception {
        driver.findElement(By.id(amazonSearchTextBoxId)).clear();
        driver.findElement(By.id(amazonSearchTextBoxId)).sendKeys(item, type);
        driver.findElement(By.id(amazonSearchButtonId)).click();
        logger.info("Searched for item: " + item + ", type: " + type);

        logger.info("Taking screenshot after search...");
        takeSnapShot(driver, screenshotPath + "searchItem_" + formattedDateTime + ".png");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(type)));

        boolean itemFound = driver.getPageSource().contains(item) || driver.getPageSource().contains(price)
                || driver.getPageSource().contains(color) || driver.getPageSource().contains(type);

        Assert.assertTrue(itemFound, msg);
        logger.info("Search completed successfully for item: " + item);
    }

    void selectItem(String formattedDateTime) throws Exception {
        logger.info("Selecting item with type: " + type+ ", color: " + color);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(type)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
        product.click();
        logger.info("Clicked on the product: " + type);

        try {
            String colorXPath = "//img[@alt='" + color + "']";
            WebElement colorOption = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(colorXPath)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", colorOption);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", colorOption);
            logger.info("Selected color: " + color);
        } catch (TimeoutException e) {
            logger.warn("Color option not found or not clickable: " + color);
        }

        logger.info("Default quantity (1) assumed.");
        takeSnapShot(driver, screenshotPath + "selectItem_" + formattedDateTime + ".png");
    }

    void addToCart(String formattedDateTime) throws Exception {
        logger.info("Adding the item to the cart...");
        WebElement addToCartButton = driver.findElement(By.id("add-to-cart-button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCartButton);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
        try {
            addToCartButton.click();
            logger.info("Clicked 'Add to Cart' button.");
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted. Retrying with JavaScript...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
        }

        takeSnapShot(driver, screenshotPath + "addToCart_" + formattedDateTime + ".png");

        try {
            WebElement noCoverageButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("attachSiNoCoverage")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", noCoverageButton);
            noCoverageButton.click();
            logger.info("Selected 'No Thanks' for warranty.");
        } catch (TimeoutException e) {
            logger.warn("No warranty popup appeared.");
        } catch (ElementNotInteractableException e) {
            logger.warn("Element 'No Thanks' is not interactable. Retrying...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", 
                    driver.findElement(By.id("attachSiNoCoverage")));
            logger.info("Clicked 'No Thanks' using JavaScript.");
        }
    }

    void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {
        TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(fileWithPath);
        FileUtils.copyFile(srcFile, destFile);
        logger.info("Screenshot saved at: " + fileWithPath);
    }

    void setExcelFile() throws Exception {
        try (FileInputStream file = new FileInputStream(testDataFile);
                XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            item = sheet.getRow(1).getCell(0).getStringCellValue();
            type = sheet.getRow(1).getCell(1).getStringCellValue();
            color = sheet.getRow(1).getCell(2).getStringCellValue();
            price = sheet.getRow(1).getCell(3).getStringCellValue();
            logger.info("Loaded test data from Excel: Item=" + item + ", Type=" + type + ", Color=" + color
                    + ", Price=" + price);
        } catch (Exception e) {
            logger.error("Error reading Excel file: ", e);
            throw e;
        }
    }
}





