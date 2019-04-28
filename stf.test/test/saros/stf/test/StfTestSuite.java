package saros.stf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ saros.stf.test.AliceTestSuite.class,
    saros.stf.test.AliceAndBobTestSuite.class,
    saros.stf.test.AliceAndBobAndCarlTestSuite.class,
    saros.stf.test.AliceAndBobAndCarlAndDaveTestSuite.class })
public class StfTestSuite {
    // the class remains completely empty,
    // being used only as a holder for the above annotations

}
