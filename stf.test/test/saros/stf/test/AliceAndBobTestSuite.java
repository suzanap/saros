package saros.stf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import saros.stf.test.consistency.RecoveryWhileTypingTest2;

/*
import de.fu_berlin.inf.dpp.stf.test.consistency.AddMultipleFilesTest2;
import de.fu_berlin.inf.dpp.stf.test.consistency.ModifyFileWithoutEditorTest2;
import de.fu_berlin.inf.dpp.stf.test.consistency.RecoveryWhileTypingTest2;
import de.fu_berlin.inf.dpp.stf.test.editing.ConcurrentEditingTest2;
import de.fu_berlin.inf.dpp.stf.test.editing.EditDifferentFilesTest2;
import de.fu_berlin.inf.dpp.stf.test.editing.EditWithReadAccessOnlyTest2;
import de.fu_berlin.inf.dpp.stf.test.editing.Editing3ProjectsTest2;
import de.fu_berlin.inf.dpp.stf.test.filefolderoperations.FolderOperationsTest2;
import de.fu_berlin.inf.dpp.stf.test.followmode.FollowModeDisabledInNewSessionTest2;
import de.fu_berlin.inf.dpp.stf.test.followmode.FollowModeTest2;
import de.fu_berlin.inf.dpp.stf.test.followmode.RefactorInFollowModeTest2;
import de.fu_berlin.inf.dpp.stf.test.followmode.SimpleFollowModeIITest2;
import de.fu_berlin.inf.dpp.stf.test.followmode.SimpleFollowModeITest2;
import de.fu_berlin.inf.dpp.stf.test.invitation.HostInvitesBelatedlyTest;
import de.fu_berlin.inf.dpp.stf.test.invitation.InviteWithDifferentVersionsTest;
import de.fu_berlin.inf.dpp.stf.test.invitation.ShareProjectUsingExistingProjectTest;
import de.fu_berlin.inf.dpp.stf.test.partialsharing.ModifyNonSharedFilesTest;
import de.fu_berlin.inf.dpp.stf.test.partialsharing.ShareFilesFromOneProjectToMultipleRemoteProjectsTest;
import de.fu_berlin.inf.dpp.stf.test.partialsharing.ShareFilesToProjectsWithDifferentEncodingTest;
import de.fu_berlin.inf.dpp.stf.test.permissions.WriteAccessChangeAndImmediateWriteTest2;
import de.fu_berlin.inf.dpp.stf.test.roster.HandleContactsTest;
import de.fu_berlin.inf.dpp.stf.test.session.EditFileThatIsNotOpenOnRemoteSideTest;
import de.fu_berlin.inf.dpp.stf.test.session.EstablishSessionWithDifferentTransportModesTest;
*/
@RunWith(Suite.class)
@Suite.SuiteClasses({ SimpleSessionConcurrently.class,
    // SimpleSessionSequentially.class,
    // ChatViewFunctionsTest2.class,

    // AddMultipleFilesTest2.class,
    // ModifyFileWithoutEditorTest2.class,/*
    RecoveryWhileTypingTest2.class /*
                                    * , ConcurrentEditingTest2.class,
                                    * EditDifferentFilesTest2.class,
                                    * Editing3ProjectsTest2.class,
                                    * EditWithReadAccessOnlyTest2.class,
                                    * FolderOperationsTest2.class,
                                    * SimpleFollowModeIITest2.class,
                                    * FollowModeDisabledInNewSessionTest2.
                                    * class, FollowModeTest2.class,
                                    * RefactorInFollowModeTest2.class,
                                    * SimpleFollowModeITest2.class,
                                    * WriteAccessChangeAndImmediateWriteTest2
                                    * .class, HostInvitesBelatedlyTest.class,
                                    * InviteWithDifferentVersionsTest.class,
                                    * ShareProjectUsingExistingProjectTest.
                                    * class, ModifyNonSharedFilesTest.class,
                                    * ShareFilesFromOneProjectToMultipleRemoteProjectsTest
                                    * .class,
                                    * ShareFilesToProjectsWithDifferentEncodingTest.
                                    * class, HandleContactsTest.class,
                                    * EditFileThatIsNotOpenOnRemoteSideTest.
                                    * class,
                                    * EstablishSessionWithDifferentTransportModesTest
                                    * .class
                                    */ })
public class AliceAndBobTestSuite {
    //
}
