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

public class StructTestClass {
	private WebDriver driver;

	private String baseUrl;

	@BeforeClass
	public void setupClass() {
		// Notwendig wenn geckodriver nicht im Pfad ist
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
		driver = new FirefoxDriver(options);

		baseUrl = System.getProperty("containerURL", "http://127.0.0.1:8080/struct.html")+"/struct.html";

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@BeforeMethod
	public void setupMethod() {
		driver.get(baseUrl);
	}

    @Test (priority=1)
    public void createStruct() {
        WebElement typeButton = driver.findElement(By.id("struct"));
        WebElement typeName = driver.findElement(By.id("typeName"));
        WebElement paramSelect = driver.findElement(By.id("memberType"));
        WebElement customSelect = driver.findElement(By.id("typeList"));
        WebElement addButton = driver.findElement(By.id("add_type"));

        typeButton.click();
        typeName.sendKeys("TestStruct");
        addButton.click();

        boolean isTypeInParamSelect = paramSelect.getText().contains("TestStruct"); 
        boolean isTypeInCustomSelect = customSelect.getText().contains("TestStruct");
        Assert.assertTrue(isTypeInParamSelect && isTypeInCustomSelect);
    }

    @Test (priority=1)
    public void createEnum() {
        WebElement typeButton = driver.findElement(By.id("enum"));
        WebElement typeName = driver.findElement(By.id("typeName"));
        WebElement paramSelect = driver.findElement(By.id("memberType"));
        WebElement customSelect = driver.findElement(By.id("typeList"));
        WebElement addButton = driver.findElement(By.id("add_type"));

        typeButton.click();
        typeName.sendKeys("TestEnum");
        addButton.click();

        boolean isTypeInParamSelect = paramSelect.getText().contains("TestEnum"); 
        boolean isTypeInCustomSelect = customSelect.getText().contains("TestEnum");
        Assert.assertTrue(isTypeInParamSelect && isTypeInCustomSelect);
    }

    @Test (priority=2)
    public void deleteDatatypes() {
        WebElement paramSelect = driver.findElement(By.id("memberType"));
        WebElement customSelect = driver.findElement(By.id("typeList"));
        Select customSelector = new Select(customSelect);
        WebElement rmButton = driver.findElement(By.id("rm_type"));

        driver.findElement(By.id("enum")).click();
        driver.findElement(By.id("typeName")).sendKeys("TestEnum");
        driver.findElement(By.id("add_type")).click();
        driver.findElement(By.id("struct")).click();
        driver.findElement(By.id("typeName")).clear();
        driver.findElement(By.id("typeName")).sendKeys("TestStruct");
        driver.findElement(By.id("add_type")).click();

        customSelector.selectByVisibleText("TestStruct");
        rmButton.click();
        customSelector.selectByVisibleText("TestEnum");
        rmButton.click();

        boolean isTypeInParamSelect = paramSelect.getText().contains("TestEnum"); 
        boolean isTypeInCustomSelect = customSelect.getText().contains("TestEnum");
        Assert.assertTrue(!isTypeInParamSelect && !isTypeInCustomSelect);
    }

    @Test 
    public void createMembersTest() {
        this.createMembers();
        
        String generatedStructure = driver.findElement(By.id("src")).getText();
        try {
            String expectedStructure = new String(Files.readAllBytes(Paths.get("testFiles/testAddMembers.c")));
            Assert.assertTrue(expectedStructure.contains(generatedStructure));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Structure:");
            System.out.println(generatedStructure);
            Assert.fail("Generated structure did not match expected result");
        }

    }

    @Test 
    public void removeMemberTest() {
        this.createMembers();
        WebElement rmParam = driver.findElement(By.id("rm_member"));
        rmParam.click();

        String generatedStructure = driver.findElement(By.id("src")).getText();
        try {
            String expectedStructure = new String(Files.readAllBytes(Paths.get("testFiles/testRemoveMember.c")));
            Assert.assertTrue(expectedStructure.contains(generatedStructure));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Structure:");
            System.out.println(generatedStructure);
            Assert.fail("Generated structure did not match expected result");
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

    public void createMembers() {
        WebElement name = driver.findElement(By.id("memberName"));
        Select types = new Select(driver.findElement(By.id("memberType")));
        WebElement desc = driver.findElement(By.id("membersDescIn"));
        WebElement list = driver.findElement(By.id("list"));
        WebElement add = driver.findElement(By.id("add_member"));

        name.sendKeys("Foo");
        types.selectByVisibleText("Zeichen");
        desc.sendKeys("Foo Beschreibung");
        add.click();

        name.clear();
        desc.clear();

        name.sendKeys("Bar");
        types.selectByVisibleText("Zeichen");
        desc.sendKeys("Bar Beschreibung");
        list.click();
        add.click();
    }
}
