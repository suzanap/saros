package saros.stf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import saros.stf.test.invitation.ParallelInvitationWithTerminationByHostTest4;
import saros.stf.test.permissions.AllParticipantsFollowUserWithWriteAccessTest4;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AllParticipantsFollowUserWithWriteAccessTest4.class,
    ParallelInvitationWithTerminationByHostTest4.class

})
class AliceAndBobAndCarlAndDaveTestSuite {
    //
}
