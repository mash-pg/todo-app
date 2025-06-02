package alltest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import com.example.todo.controller.SignupControllerTest;
import com.example.todo.controller.TodoControllerTest;
import com.example.todo.TodoServiceIntegrationTest;
import com.example.todo.TodoServiceTest;
import com.example.todo.UserAuthIntegrationTest;
import screen.ErrorScreenshotTest;
import screen.LoginScreenshotTest;
import screen.SignupValidationTest;

@Suite
@SelectClasses({
	// 単体テスト
	SignupControllerTest.class,  
	TodoControllerTest.class,
	TodoServiceIntegrationTest.class,
	TodoServiceTest.class,
	UserAuthIntegrationTest.class,
	ErrorScreenshotTest.class,
	LoginScreenshotTest.class,
	SignupValidationTest.class,
	
})
public class AllTestsSuite {
}