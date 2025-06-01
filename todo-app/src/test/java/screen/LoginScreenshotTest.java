package screen;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import org.openqa.selenium.chrome.ChromeOptions;


public class LoginScreenshotTest {

    private static WebDriver driver;

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // 必要に応じて headless モードも使えます
        // options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testLoginAndCaptureScreenshots() throws IOException {
        // フォルダ作成
        Path folderPath = Paths.get("src/test/java/testscreenshot/login");
        Files.createDirectories(folderPath);

        // ① ログイン画面へアクセス
        driver.get("http://localhost:8080/login");
        takeScreenshot(folderPath.resolve("login_screen.png"));

        // ② フォーム入力（name属性がusername/passwordであることが前提）
//        driver.findElement(By.name("username")).sendKeys("admin");
//        driver.findElement(By.name("password")).sendKeys("password");
        driver.findElement(By.name("username")).sendKeys("test1");
        driver.findElement(By.name("password")).sendKeys("test");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 少し待つ（遷移完了のため）
        try {
            Thread.sleep(1000); // 必要に応じて WebDriverWait に変更可能
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ③ ログイン後トップ画面キャプチャ
        takeScreenshot(folderPath.resolve("home_screen.png"));
    }

    private void takeScreenshot(Path filePath) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(src.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("スクリーンショット保存: " + filePath);
    }
}
