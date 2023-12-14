import com.liaverg.textgram.app.usecases.users.application.services.RegisterServiceTest;
import com.liaverg.textgram.app.usecases.users.domain.commands.RegisterCommandTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({RegisterCommandTest.class, RegisterServiceTest.class})
public class TestSuite {
}
