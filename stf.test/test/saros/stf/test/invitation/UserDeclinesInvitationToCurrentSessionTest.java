package saros.stf.test.invitation;

import static org.junit.Assert.assertTrue;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;
import static saros.stf.client.tester.SarosTester.CARL;
import static saros.stf.shared.Constants.CANCEL;
import static saros.stf.shared.Constants.SHELL_SESSION_INVITATION;

import org.junit.After;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.annotation.TestLink;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.server.rmi.remotebot.widget.IRemoteBotShell;
import saros.stf.shared.Constants.TypeOfCreateProject;

@TestLink(id = "Saros-133_user_declines_invitation_to_an_already_established_session")
public class UserDeclinesInvitationToCurrentSessionTest extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {
        Assume.assumeTrue(checkIfLevelONEiiiSucceeded());
        select(ALICE, BOB, CARL);
        // if for some reason there is no session, build up a new session
        if (ALICE.superBot().views().sarosView().isInSession() == false
            && BOB.superBot().views().sarosView().isInSession() == false) {
            clearWorkspaces();
            ALICE.superBot().internal().createProject("Foo1_Saros");
            Util.buildSessionConcurrently("Foo1_Saros",
                TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);
        }

    }

    @After
    public void restoreNetwork() throws Exception {
        ALICE.superBot().internal().deleteFolder("Foo1_Saros", "src");

        tearDownSarosLast();
    }

    @Test
    public void testUserDeclinesInvitationToCurrentSession() throws Exception {

        ALICE.superBot().internal().createFile("Foo1_Saros", "src/readme.txt",
            "1234/1234=1");
        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/readme.txt");

        ALICE.superBot().views().sarosView().selectContact(CARL.getJID())
            .addToSarosSession();

        IRemoteBotShell shell = CARL.remoteBot()
            .shell(SHELL_SESSION_INVITATION);

        shell.bot().button(CANCEL).click();

        Thread.sleep(5000);

        assertTrue(
            "Alice closed the session because Carl cancels the invitation but Bob was in the session",
            ALICE.superBot().views().sarosView().isInSession());
    }
}
