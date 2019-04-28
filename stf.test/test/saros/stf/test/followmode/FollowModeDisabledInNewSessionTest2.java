package saros.stf.test.followmode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;

public class FollowModeDisabledInNewSessionTest2 extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {

        Assume.assumeTrue(checkIfLevelONEiandTWOiiiSucceeded());
        select(ALICE, BOB);
        // if for some reason there is no session, build up a new session
        if (isSession() == false) {
            clearWorkspaces();
            ALICE.superBot().internal().createProject("Foo1_Saros");
            Util.buildSessionConcurrently("Foo1_Saros",
                TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);
        }
    }

    @Before
    public void setUp() throws Exception {
        closeAllShells();
        closeAllEditors();
    }

    @After
    public void cleanUpSaros() throws Exception {
        ALICE.superBot().internal().deleteFolder("Foo1_Saros", "src");
        tearDownSaros();
    }

    @Test
    public void testFollowModeDisabledInNewSession() throws Exception {

        ALICE.superBot().internal().createFile("Foo1_Saros", "src/readme.txt",
            "bla bla bla");

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/readme.txt");

        ALICE.superBot().views().packageExplorerView()
            .selectFile("Foo1_Saros", "src", "readme.txt").open();

        BOB.superBot().views().sarosView().selectUser(ALICE.getJID())
            .followParticipant();

        BOB.superBot().views().sarosView().selectUser(ALICE.getJID())
            .waitUntilIsFollowing();

        ALICE.superBot().internal().createFile("Foo1_Saros", "src/info.txt",
            "info");

        ALICE.controlBot().getNetworkManipulator()
            .synchronizeOnActivityQueue(BOB.getJID(), 60 * 1000);

        leaveSessionPeersFirst(ALICE);

        BOB.remoteBot().closeAllEditors();

        BOB.superBot().views().packageExplorerView()
            .selectFile("Foo1_Saros", "src", "readme.txt").open();

        Util.buildSessionSequentially("Foo1_Saros",
            TypeOfCreateProject.EXIST_PROJECT, ALICE, BOB);

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/readme.txt");

        ALICE.superBot().views().packageExplorerView()
            .selectFile("Foo1_Saros", "src", "readme.txt").open();

        ALICE.controlBot().getNetworkManipulator()
            .synchronizeOnActivityQueue(BOB.getJID(), 60 * 1000);

        assertFalse("BOB is following ALICE", BOB.superBot().views().sarosView()
            .selectUser(ALICE.getJID()).isFollowing());

        assertTrue("editor changed",
            BOB.remoteBot().editor("readme.txt").isActive());

    }
}