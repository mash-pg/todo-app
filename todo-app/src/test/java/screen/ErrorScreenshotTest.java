package screen;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ErrorScreenshotTest {

    private WebDriver driver;
    private final Path screenshotDir = Paths.get("src/test/java/testscreenshot/error");
    private WebDriverWait wait;

    @BeforeAll
    void setupAll() throws IOException {
        WebDriverManager.chromedriver().setup();
        Files.createDirectories(screenshotDir); // スクショ保存フォルダ
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new"); // CI用途などで必要なら有効に
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // --- 404 Not Found テスト ---
    @Test
    void test404Page() throws IOException {
        driver.get("http://localhost:8080/login");

        // ログイン処理
        driver.findElement(By.name("username")).sendKeys("admin");   // ←非オーナーユーザー
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/")); // ログイン完了後の遷移待ち
        
        driver.get("http://localhost:8080/asdf");

        // "存在しません" または "404" を含むまで待機
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
        	    By.tagName("body"),
        	    "お探しのページは見つかりませんでした"
        	));

        takeScreenshot("404_screen.png");

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(
            bodyText.contains("404") || bodyText.contains("Not Found") || bodyText.contains("存在しません"),
            "404ページの内容が確認できません"
        );
    }

    // --- 403 Forbidden テスト ---
    @Test
    void test403Page() throws IOException {
        driver.get("http://localhost:8080/login");

        // ログイン処理
        driver.findElement(By.name("username")).sendKeys("admin");   // ←非オーナーユーザー
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/")); // ログイン完了後の遷移待ち

        driver.get("http://localhost:8080/edit?id=10");  // ←他人のタスクIDに変更すること

        // "403" または "権限がありません" を含むまで待機
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "権限がありません"));

        takeScreenshot("403_screen.png");

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(
            bodyText.contains("403") || bodyText.contains("Forbidden") || bodyText.contains("権限がありません"),
            "403ページの内容が確認できません"
        );
    }

    // --- 400 不正なリクエストが行われました テスト ---
    @Test
    void test400Page() throws IOException {
        driver.get("http://localhost:8080/login");

        // ログイン処理
        driver.findElement(By.name("username")).sendKeys("admin");   // ←非オーナーユーザー
        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/")); // ログイン完了後の遷移待ち
        
    
        
        
        driver.get("http://localhost:8080/edit?id=abc123");

        // "存在しません" または "400" を含むまで待機
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
        	    By.tagName("body"),
        	    "不正なリクエストが行われました。"
        	));

        takeScreenshot("400_screen.png");

        String bodyText = driver.findElement(By.tagName("body")).getText();
        assertTrue(
            bodyText.contains("400") || bodyText.contains("Bad Request") || bodyText.contains("不正なID"),
            "不正なリクエストが行われました。"
        );
    }
    // --- スクリーンショット保存処理 ---
    private void takeScreenshot(String filename) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path dest = screenshotDir.resolve(filename);
        Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("📸 スクリーンショット保存済: " + dest.toAbsolutePath());
    }
}
