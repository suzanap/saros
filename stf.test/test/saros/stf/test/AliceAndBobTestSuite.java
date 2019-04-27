package saros.stf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import saros.stf.test.chatview.ChatViewFunctionsTest2;
import saros.stf.test.consistency.AddMultipleFilesTest2;
import saros.stf.test.consistency.ModifyFileWithoutEditorTest2;
import saros.stf.test.consistency.RecoveryWhileTypingTest2;
import saros.stf.test.editing.ConcurrentEditingTest2;
import saros.stf.test.editing.EditDifferentFilesTest2;
import saros.stf.test.editing.EditWithReadAccessOnlyTest2;
import saros.stf.test.editing.Editing3ProjectsTest2;
import saros.stf.test.filefolderoperations.FolderOperationsTest2;
import saros.stf.test.followmode.FollowModeDisabledInNewSessionTest2;
import saros.stf.test.followmode.FollowModeTest2;
import saros.stf.test.followmode.RefactorInFollowModeTest2;
import saros.stf.test.followmode.SimpleFollowModeIITest2;
import saros.stf.test.followmode.SimpleFollowModeITest2;
import saros.stf.test.invitation.HostInvitesBelatedlyTest2;
import saros.stf.test.invitation.InviteWithDifferentVersionsTest2;
import saros.stf.test.invitation.ShareProjectUsingExistingProjectTest2;
import saros.stf.test.partialsharing.ModifyNonSharedFilesTest2;
import saros.stf.test.partialsharing.ShareFilesFromOneProjectToMultipleRemoteProjectsTest2;
import saros.stf.test.partialsharing.ShareFilesToProjectsWithDifferentEncodingTest2;
import saros.stf.test.permissions.WriteAccessChangeAndImmediateWriteTest2;
import saros.stf.test.roster.HandleContactsTest2;
import saros.stf.test.session.EditFileThatIsNotOpenOnRemoteSideTest2;
import saros.stf.test.session.EstablishSessionWithDifferentTransportModesTest2;

@RunWith(Suite.class)
@Suite.SuiteClasses({ SimpleSessionConcurrently.class,
    SimpleSessionSequentially.class, ChatViewFunctionsTest2.class,
    AddMultipleFilesTest2.class, ModifyFileWithoutEditorTest2.class,
    RecoveryWhileTypingTest2.class, ConcurrentEditingTest2.class,
    EditDifferentFilesTest2.class, Editing3ProjectsTest2.class,
    EditWithReadAccessOnlyTest2.class, FolderOperationsTest2.class,
    SimpleFollowModeIITest2.class, FollowModeDisabledInNewSessionTest2.class,
    FollowModeTest2.class, RefactorInFollowModeTest2.class,
    SimpleFollowModeITest2.class, WriteAccessChangeAndImmediateWriteTest2.class,
    HostInvitesBelatedlyTest2.class, InviteWithDifferentVersionsTest2.class,
    ShareProjectUsingExistingProjectTest2.class,
    ModifyNonSharedFilesTest2.class,
    ShareFilesFromOneProjectToMultipleRemoteProjectsTest2.class,
    ShareFilesToProjectsWithDifferentEncodingTest2.class,
    HandleContactsTest2.class, EditFileThatIsNotOpenOnRemoteSideTest2.class,
    EstablishSessionWithDifferentTransportModesTest2.class })
public class AliceAndBobTestSuite {
    //
}
