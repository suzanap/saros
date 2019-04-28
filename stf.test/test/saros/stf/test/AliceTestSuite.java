package saros.stf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import saros.stf.test.account.AccountPreferenceTest1;
import saros.stf.test.html.MainViewTest1;
import saros.stf.test.invitation.ShareProjectWizardUITest1;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AccountPreferenceTest1.class,
    ShareProjectWizardUITest1.class, MainViewTest1.class

})
public class AliceTestSuite {

}
