import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"com.liaverg.textgram.app.usecases.users.domain.commands",
                "com.liaverg.textgram.app.usecases.users.application.services",
                "com.liaverg.textgram.app.usecases.users.adapters.in.web",
                "com.liaverg.textgram.app.usecases.users.adapters.out.persistence",
                "com.liaverg.textgram.app.usecases.users.system"})
public class TestSuite {
}
