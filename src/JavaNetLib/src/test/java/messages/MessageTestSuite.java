package messages;

import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value = Suite.class)
@SuiteClasses(value = {
	TestGetFlightListResp.class,
	TestGetPassengerInfo.class,
	TestGetPassengerInfoResp.class,
	TestGetPassengerList.class,
	TestGetPassengerListResp.class,
	TestHandshakeResp.class
})
public class MessageTestSuite extends TestSuite {
}
