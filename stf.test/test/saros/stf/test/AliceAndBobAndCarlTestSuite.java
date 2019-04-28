package saros.stf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import saros.stf.test.consistency.CreateSameFileAtOnceTest3;
import saros.stf.test.consistency.EditDuringInvitationStressTest3;
import saros.stf.test.consistency.EditDuringInvitationTest3;
import saros.stf.test.consistency.ModifyDocumentBeforeProjectNegotiationTest3;
import saros.stf.test.editing.ConcurrentEditingInsert100CharactersTest3;
import saros.stf.test.editing.ConcurrentEditingWith3UsersTest3;
import saros.stf.test.invitation.AwarenessInformationVisibleAfterInvitationTest3;
import saros.stf.test.invitation.InviteAndLeaveStressInstantSessionTest3;
import saros.stf.test.invitation.InviteAndLeaveStressTest3;
import saros.stf.test.invitation.NonHostInvitesContactTest3;
import saros.stf.test.invitation.Share3UsersConcurrentlyTest3;
import saros.stf.test.invitation.Share3UsersLeavingSessionTest3;
import saros.stf.test.invitation.Share3UsersSequentiallyTest3;
import saros.stf.test.invitation.UserDeclinesInvitationToCurrentSessionTest3;
import saros.stf.test.roster.SortContactsOnlineOverOfflineTest3;
import saros.stf.test.session.ShareMultipleProjectsTest3;

@RunWith(Suite.class)
@Suite.SuiteClasses({ Share3UsersConcurrentlyTest3.class, // watcher not
                                                          // implemented yet
    CreateSameFileAtOnceTest3.class, // -- selecFirst Alice&Bob&Carl
    // EditDuringNonHostAddsProjectTest3.class,
    ConcurrentEditingInsert100CharactersTest3.class, // -- condition from
                                                     // ConcurrentEditingTest2
    ConcurrentEditingWith3UsersTest3.class, // -- condition from
                                            // ConcurrentEditingTest2
    // TO FIX : FileOperationsTest3.class,
    Share3UsersLeavingSessionTest3.class,

    EditDuringInvitationTest3.class, // --watcher for stress test --selecFirst
                                     // Alice&Bob
    UserDeclinesInvitationToCurrentSessionTest3.class,
    // EditDuringNonHostInvitationTest3.class,
    EditDuringInvitationStressTest3.class,

    Share3UsersSequentiallyTest3.class, // independent done
    ModifyDocumentBeforeProjectNegotiationTest3.class, // done
    AwarenessInformationVisibleAfterInvitationTest3.class, // done
    InviteAndLeaveStressTest3.class,
    InviteAndLeaveStressInstantSessionTest3.class, // done

    NonHostInvitesContactTest3.class, // done
    SortContactsOnlineOverOfflineTest3.class, // done
    ShareMultipleProjectsTest3.class// dine

})
public class AliceAndBobAndCarlTestSuite {
    //
}
