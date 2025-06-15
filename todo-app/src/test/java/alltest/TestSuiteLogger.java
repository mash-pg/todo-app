package alltest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

public class TestSuiteLogger {

    private static final Logger logger = Logger.getLogger(TestSuiteLogger.class.getName());

    @AfterAll
    static void endMessage() {
        logger.info("✅ テスト終了です！");
    }

    @Test
    void dummyTest() {
        // 空テスト（JUnitがクラスを無視しないように）
    }
}
