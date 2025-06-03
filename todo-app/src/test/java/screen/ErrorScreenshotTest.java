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
    private WebDriverWait wait;
    private final Path screenshotDir = Paths.get("src/test/java/testscreenshot/error");

    @BeforeAll
    void setupAll() throws IOException {
        WebDriverManager.chromedriver().setup();
        Files.createDirectories(screenshotDir); // スクショ保存先を作成
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless=new"); // 必要ならヘッドレス化
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

    // ========================
    // ▼ 各ステータス別テスト
    // ========================
    @Test
    void test200Page() throws IOException {
        loginAs("admin", "password");

        driver.get("http://localhost:8080/");

        // 正常表示の指標となる要素を待機（ToDo一覧の見出しなど）
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.tagName("h1")
        ));

        // 見出しが「ToDo一覧」やユーザー名を含むか確認（必要に応じて調整）
        String headingText = heading.getText();
        assertTrue(
            headingText.contains("ログイン") || headingText.contains("ToDo") || headingText.contains("さんがログインしています"),
            "200ページが正しく表示されていません"
        );

        takeScreenshot("success_200.png");
    }

    @Test
    void test400Page() throws IOException {
        loginAs("admin", "password");
        driver.get("http://localhost:8080/edit?id=abc123"); // 不正なIDで400エラー発生

        String statusCode = waitForStatusCodeAndCapture("400");
        assertEquals("400", statusCode, "ステータスコードが 400 ではありません");
    }

    @Test
    void test403Page() throws IOException {
        loginAs("admin", "password");
        driver.get("http://localhost:8080/edit?id=10"); // 他人のタスクID（OWNERでないID）を指定

        String statusCode = waitForStatusCodeAndCapture("403");
        assertEquals("403", statusCode, "ステータスコードが 403 ではありません");
    }

    @Test
    void test404Page() throws IOException {
        loginAs("admin", "password");
        driver.get("http://localhost:8080/asdf"); // 存在しないURLにアクセス

        String statusCode = waitForStatusCodeAndCapture("404");
        assertEquals("404", statusCode, "ステータスコードが 404 ではありません");
    }

    @Test
    void test500Page() throws IOException {
        loginAs("admin", "password");
        driver.get("http://localhost:8080/cause-error"); // RuntimeExceptionを明示的に発生

        String statusCode = waitForStatusCodeAndCapture("500");
        assertEquals("500", statusCode, "ステータスコードが 500 ではありません");
    }

    // ========================
    // ▼ 共通処理
    // ========================

    private void loginAs(String username, String password) {
        driver.get("http://localhost:8080/login");
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/"));
    }

    private String waitForStatusCodeAndCapture(String expectedStatusCode) throws IOException {
        WebElement statusCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("h1.display-4.text-danger")
        ));

        String actualStatusCode = statusCodeElement.getText();
        takeScreenshot("error_" + actualStatusCode + ".png");

        return actualStatusCode;
    }

    private void takeScreenshot(String filename) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path dest = screenshotDir.resolve(filename);
        Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("📸 スクリーンショット保存済: " + dest.toAbsolutePath());
    }
}
