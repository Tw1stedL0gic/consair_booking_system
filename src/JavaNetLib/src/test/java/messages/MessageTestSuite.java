package messages;

import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
	TestLogin.class,
	TestDisconnect.class,
	TestRequestAirports.class,
	TestRequestAirportsResp.class
})
public class MessageTestSuite extends TestSuite {
}
