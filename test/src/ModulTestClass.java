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

public class ModulTestClass {
	private WebDriver driver;

	private String baseUrl;

	@BeforeClass
	public void setupClass() {
		// Notwendig wenn geckodriver nicht im Pfad ist
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
		driver = new FirefoxDriver(options);

		baseUrl = System.getProperty("containerURL", "http://127.0.0.1:8080/modul.html")+"/modules.html";

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@BeforeMethod
	public void setupMethod() {
		driver.get(baseUrl);
	}

    @Test
    public void nameTest() {
        WebElement name = driver.findElement(By.id("modulName"));
        WebElement nameDesc = driver.findElement(By.id("modulDescIn"));

        name.sendKeys("Foo");
        nameDesc.sendKeys("Foo Beschreibung");

        String generatedHFile = driver.findElement(By.id("h-file")).getText();
        String generatedCFile = driver.findElement(By.id("c-file")).getText();
        try {
            String expectedHFile = new String(Files.readAllBytes(Paths.get("testFiles/testModulName.h")));
            String expectedCFile = new String(Files.readAllBytes(Paths.get("testFiles/testModulName.c")));
            boolean firstCondition = expectedHFile.contains(generatedHFile);
            boolean secondCondition = expectedCFile.contains(generatedCFile);
            Assert.assertTrue(firstCondition && secondCondition);
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            Assert.fail("Generated module did not match expected result");
        }
    }

    @Test
    public void createUnittestTest() {
        WebElement button = driver.findElement(By.id("unit"));

        button.click();

        String generatedCFile = driver.findElement(By.id("c-file")).getText();
        try {
            String expectedCFile = new String(Files.readAllBytes(Paths.get("testFiles/testModulUnit.c")));
            boolean secondCondition = expectedCFile.contains(generatedCFile);
            Assert.assertTrue(secondCondition);
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            Assert.fail("Generated module did not match expected result");
        }
    }

    @Test
    public void createWithIoTest() {
        WebElement button = driver.findElement(By.id("io"));

        button.click();

        String generatedCFile = driver.findElement(By.id("c-file")).getText();
        try {
            String expectedCFile = new String(Files.readAllBytes(Paths.get("testFiles/testModulIO.c")));
            boolean secondCondition = expectedCFile.contains(generatedCFile);
            Assert.assertTrue(secondCondition);
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            Assert.fail("Generated module did not match expected result");
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
}
