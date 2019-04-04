import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.String;

//import java.awt.Toolkit;
//import java.awt.datatransfer.DataFlavor;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EnumTestClass {
	private WebDriver driver;

	private String baseUrl;

	@BeforeClass
	public void setupClass() {
		// Notwendig wenn geckodriver nicht im Pfad ist
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
		driver = new FirefoxDriver(options);

		baseUrl = System.getProperty("containerURL", "http://127.0.0.1:8080/enum.html")+"/enum.html";

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@BeforeMethod
	public void setupMethod() {
		driver.get(baseUrl);
	}

    @Test
    public void nameTest() {
        WebElement name = driver.findElement(By.id("enumName"));
        WebElement nameDesc = driver.findElement(By.id("enumDescIn"));

        name.sendKeys("Test");
        nameDesc .sendKeys("Test Beschreibung");
        
        String generatedEnum = driver.findElement(By.id("src")).getText();
        try {
            String expectedEnum = new String(Files.readAllBytes(Paths.get("testFiles/enumName.c")));
            Assert.assertTrue(expectedEnum.contains(generatedEnum));
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            System.out.println("Generated Enumeration:");
            System.out.println(generatedEnum);
            Assert.fail("Generated enumeration did not match expected result");
        }
    }

    @Test
    public void addConstTest() {
        this.createConsts();
        String generatedEnum = driver.findElement(By.id("src")).getText();
        try {
            String expectedEnum = new String(Files.readAllBytes(Paths.get("testFiles/enumAddConsts.c")));
            Assert.assertTrue(expectedEnum.contains(generatedEnum));
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            System.out.println("Generated Enumeration:");
            System.out.println(generatedEnum);
            Assert.fail("Generated enumeration did not match expected result");
        }
    }

    @Test
    public void rmConstTest() {
        this.createConsts();

        WebElement rm = driver.findElement(By.id("rm_const"));

        rm.click();

        String generatedEnum = driver.findElement(By.id("src")).getText();
        try {
            String expectedEnum = new String(Files.readAllBytes(Paths.get("testFiles/enumRmConst.c")));
            Assert.assertTrue(expectedEnum.contains(generatedEnum));
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            System.out.println("Generated Enumeration:");
            System.out.println(generatedEnum);
            Assert.fail("Generated enumeration did not match expected result");
        }
    }

	@AfterMethod
	public void sleep() throws InterruptedException {
		Thread.sleep(500);
	}

	@AfterClass
	public void teardown() throws InterruptedException {
		driver.close();
	}

    /**
     * Helper Methods
     */

    public void createConsts() {
        WebElement name = driver.findElement(By.id("constName"));
        WebElement value = driver.findElement(By.id("constValue"));
        WebElement add = driver.findElement(By.id("add_const"));

        name.sendKeys("Foo");
        add.click();
        name.sendKeys("Bar");
        value.sendKeys("42");
        add.click();
    }
}
