package screen;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupValidationTest {

    private WebDriver driver;
    private String toSafeFilename(String displayName) {
        return switch (displayName) {
            case "空のフォームでバリデーションエラー" -> "empty_form_validation";
            case "メール形式不正でバリデーションエラー" -> "invalid_email_format";
            case "パスワード不一致でエラー" -> "password_mismatch";
            case "既存ユーザー名でエラー（DBに依存するためモックが必要）" -> "duplicate_username";
            case "正しい入力で登録成功（/loginにリダイレクト）" -> "successful_signup";
            case "サインアップ後にログインできること" -> "signup_then_login";
            default -> "screenshot";
        };
    }

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("http://localhost:8080/signup");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        if (driver != null) {
            try {
                takeScreenshot(testInfo.getDisplayName());  // 各テスト名で保存
            } catch (IOException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }

    private void takeScreenshot(String testName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // 保存先ディレクトリ
        Path outputDir = Paths.get("src/test/java/testscreenshot/signup");
        Files.createDirectories(outputDir);

        // タイムスタンプ付きファイル名で保存
        String safeName = toSafeFilename(testName);  // ★ ここを追加
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path outputPath = outputDir.resolve(safeName + "_" + timestamp + ".png");

        Files.copy(screenshot.toPath(), outputPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("スクリーンショット保存済み: " + outputPath.toAbsolutePath());
    }

    @Test
    @DisplayName("空のフォームでバリデーションエラー")
    void testEmptyFormValidation() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String body = driver.getPageSource();
        assertTrue(body.contains("ユーザー名"));
        assertTrue(body.contains("パスワード"));
    }

    @Test
    @DisplayName("メール形式不正でバリデーションエラー")
    void testInvalidEmailFormat() {
        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("email")).sendKeys("invalid-email");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("confirmPassword")).sendKeys("password123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String body = driver.getPageSource();
        assertTrue(body.contains("メール"));
    }

    @Test
    @DisplayName("パスワード不一致でエラー")
    void testPasswordMismatch() {
        driver.findElement(By.name("username")).sendKeys("user");
        driver.findElement(By.name("email")).sendKeys("test@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("confirmPassword")).sendKeys("different123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String body = driver.getPageSource();
        assertTrue(body.contains("確認用パスワード"));
    }

    @Test
    @DisplayName("既存ユーザー名でエラー（DBに依存するためモックが必要）")
    void testDuplicateUsername() {
        driver.findElement(By.name("username")).sendKeys("existingUser");
        driver.findElement(By.name("email")).sendKeys("new@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("confirmPassword")).sendKeys("password123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        String body = driver.getPageSource();
        assertTrue(body.contains("既に使われています"));
    }

    @Test
    @DisplayName("正しい入力で登録成功（/loginにリダイレクト）")
    void testSuccessfulSignup() {
        driver.findElement(By.name("username")).sendKeys("newUser" + System.currentTimeMillis());
        driver.findElement(By.name("email")).sendKeys("new" + System.currentTimeMillis() + "@example.com");
        driver.findElement(By.name("password")).sendKeys("password123");
        driver.findElement(By.name("confirmPassword")).sendKeys("password123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
    @Test
    @DisplayName("サインアップ後にログインできること")
    void testSignupAndLogin() {
        String username = "testuser" + System.currentTimeMillis();
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String password = "password123";

        // 1. サインアップ
        driver.get("http://localhost:8080/signup");
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("confirmPassword")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 2. ログインページにリダイレクトされたことを確認
        assertTrue(driver.getCurrentUrl().contains("/login"), "サインアップ後に /login にリダイレクトされるべき");

        // 3. ログイン情報を入力してログイン
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 4. ログイン後の遷移先確認（例: /dashboard, / やユーザー名の表示など）
        String body = driver.getPageSource();
        assertTrue(body.contains(username), "ログイン後にユーザー名が表示されているべき");
    }

}
