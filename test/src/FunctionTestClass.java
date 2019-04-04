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

public class FunctionTestClass {
	private WebDriver driver;

	private String baseUrl;

	@BeforeClass
	public void setupClass() {
		// Notwendig wenn geckodriver nicht im Pfad ist
		System.setProperty("webdriver.gecko.driver", "lib/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
		driver = new FirefoxDriver(options);

		baseUrl = System.getProperty("containerURL", "http://127.0.0.1:8080/function.html")+"/function.html";

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	@BeforeMethod
	public void setupMethod() {
		driver.get(baseUrl);
	}

//    Does not work in headless mode ie in a container
//    @Test
//    public void copyTest() {
//        WebElement copyButton = driver.findElement(By.id("copy-btn"));
//        copyButton.click();
//
//        try {
//            String generatedFunction = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor); 
//            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/empty.c")));
//            Assert.assertTrue(expectedFunction.contains(generatedFunction));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

    @Test (priority=1)
    public void staticTest() {
        WebElement staticRadio = driver.findElement(By.id("static"));
        staticRadio.click();

        String generatedFunction = driver.findElement(By.id("src")).getText();
        try {
            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/testStatic.c")));
            Assert.assertTrue(expectedFunction.contains(generatedFunction));
        } catch (Exception e) {
            System.out.println(e);
        } catch (AssertionError e) {
            System.out.println("Generated Function:");
            System.out.println(generatedFunction);
            Assert.fail("Generated function did not match expected result");
        }
    }

    @Test (priority=2)
    public void normalTest() {
        WebElement normalRadio = driver.findElement(By.id("normal"));
        normalRadio.click();

        String generatedFunction = driver.findElement(By.id("src")).getText();
        try {
            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/empty.c")));
            Assert.assertTrue(expectedFunction.contains(generatedFunction));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Function:");
            System.out.println(generatedFunction);
            Assert.fail("Generated function did not match expected result");
        }

    }

    @Test (priority=3)
    public void createStruct() {
        WebElement typeButton = driver.findElement(By.id("struct"));
        WebElement typeName = driver.findElement(By.id("typeName"));
        WebElement paramSelect = driver.findElement(By.id("paramType"));
        WebElement customSelect = driver.findElement(By.id("typeList"));
        WebElement addButton = driver.findElement(By.id("add_type"));
        WebElement paramRadio = driver.findElement(By.id("Parameter"));

        paramRadio.click();
        typeButton.click();
        typeName.sendKeys("TestStruct");
        addButton.click();

        boolean isTypeInParamSelect = paramSelect.getText().contains("TestStruct"); 
        boolean isTypeInCustomSelect = customSelect.getText().contains("TestStruct");
        Assert.assertTrue(isTypeInParamSelect && isTypeInCustomSelect);
    }

    @Test (priority=3)
    public void createEnum() {
        WebElement typeButton = driver.findElement(By.id("enum"));
        WebElement typeName = driver.findElement(By.id("typeName"));
        WebElement paramSelect = driver.findElement(By.id("paramType"));
        WebElement customSelect = driver.findElement(By.id("typeList"));
        WebElement addButton = driver.findElement(By.id("add_type"));
        WebElement paramRadio = driver.findElement(By.id("Parameter"));

        paramRadio.click();
        typeButton.click();
        typeName.sendKeys("TestEnum");
        addButton.click();

        boolean isTypeInParamSelect = paramSelect.getText().contains("TestEnum"); 
        boolean isTypeInCustomSelect = customSelect.getText().contains("TestEnum");
        Assert.assertTrue(isTypeInParamSelect && isTypeInCustomSelect);
    }

    @Test (priority=4)
    public void deleteDatatypes() {
        WebElement paramSelect = driver.findElement(By.id("paramType"));
        WebElement customSelect = driver.findElement(By.id("typeList"));
        Select customSelector = new Select(customSelect);
        WebElement rmButton = driver.findElement(By.id("rm_type"));
        WebElement paramRadio = driver.findElement(By.id("Parameter"));

        driver.findElement(By.id("enum")).click();
        driver.findElement(By.id("typeName")).sendKeys("TestEnum");
        driver.findElement(By.id("add_type")).click();
        driver.findElement(By.id("struct")).click();
        driver.findElement(By.id("typeName")).clear();
        driver.findElement(By.id("typeName")).sendKeys("TestStruct");
        driver.findElement(By.id("add_type")).click();

        paramRadio.click();
        customSelector.selectByVisibleText("TestStruct");
        rmButton.click();
        customSelector.selectByVisibleText("TestEnum");
        rmButton.click();

        boolean isTypeInParamSelect = paramSelect.getText().contains("TestEnum"); 
        boolean isTypeInCustomSelect = customSelect.getText().contains("TestEnum");
        Assert.assertTrue(!isTypeInParamSelect && !isTypeInCustomSelect);
    }

    @Test (priority=3)
    public void createParamsTest() {
        this.createParams();
        
        String generatedFunction = driver.findElement(By.id("src")).getText();
        try {
            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/testParams.c")));
            Assert.assertTrue(expectedFunction.contains(generatedFunction));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Function:");
            System.out.println(generatedFunction);
            Assert.fail("Generated function did not match expected result");
        }

    }

    @Test (priority=4)
    public void removeParam1Test() {
        this.createParams();
        WebElement rmParam = driver.findElement(By.id("rm_param"));
        rmParam.click();

        String generatedFunction = driver.findElement(By.id("src")).getText();
        try {
            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/testRemoveParams1.c")));
            Assert.assertTrue(expectedFunction.contains(generatedFunction));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Function:");
            System.out.println(generatedFunction);
            Assert.fail("Generated function did not match expected result");
        }

    }

    @Test (priority=4)
    public void removeParam2Test() {
        this.createParams();
        WebElement rmParam = driver.findElement(By.id("rm_param"));
        rmParam.click();
        rmParam.click();

        String generatedFunction = driver.findElement(By.id("src")).getText();
        try {
            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/empty.c")));
            Assert.assertTrue(expectedFunction.contains(generatedFunction));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Function:");
            System.out.println(generatedFunction);
            Assert.fail("Generated function did not match expected result");
        }

    }

    @Test (priority=5)
    public void returnTypeTest() {
        Select returnTypeList = new Select(driver.findElement(By.id("returnType")));
        WebElement returnTypeDesc = driver.findElement(By.id("returnDescIn"));

        returnTypeList.selectByValue("void");
        returnTypeDesc.sendKeys("void");
        String generatedFunction = driver.findElement(By.id("src")).getText();
        try {
            String expectedFunction = new String(Files.readAllBytes(Paths.get("testFiles/testReturnType.c")));
            Assert.assertTrue(expectedFunction.contains(generatedFunction));
        } catch (Exception e) {
            System.out.println(e);
        }catch (AssertionError e) {
            System.out.println("Generated Function:");
            System.out.println(generatedFunction);
            Assert.fail("Generated function did not match expected result");
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

    public void createParams() {
        WebElement radio = driver.findElement(By.id("Parameter"));
        WebElement name = driver.findElement(By.id("paramName"));
        Select types = new Select(driver.findElement(By.id("paramType")));
        WebElement desc = driver.findElement(By.id("paramsDescIn"));
        WebElement list = driver.findElement(By.id("list"));
        WebElement add = driver.findElement(By.id("add_param"));

        radio.click();

        name.sendKeys("Test1");
        types.selectByVisibleText("Zeichen");
        desc.sendKeys("test1");
        add.click();

        name.clear();
        desc.clear();

        name.sendKeys("Test2");
        types.selectByVisibleText("Zeichen");
        desc.sendKeys("test2");
        list.click();
        add.click();
    }
}
