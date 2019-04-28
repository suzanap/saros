package saros.stf.test.followmode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import java.util.List;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;
import saros.stf.testwatcher.STFTestWatcherLevelTWOii;

public class SimpleFollowModeIITest2 extends StfTestCase {

    private final String fileContent = "1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1";
    static long total = 0;

    @BeforeClass
    public static void selectTesters() throws Exception {
        Assume.assumeTrue(checkIfLevelONEiSucceeded());
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
        ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .grantWriteAccess();
        ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .waitUntilHasWriteAccess();
        tearDownSaros();
    }

    @Rule
    public STFTestWatcherLevelTWOii watcherLevelTWOiii = new STFTestWatcherLevelTWOii();

    @Test
    public void testSimpleFollowMode() throws Exception {

        ALICE.superBot().internal().createFile("Foo1_Saros", "src/readme.txt",
            fileContent);

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/readme.txt");

        ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .restrictToReadOnlyAccess();

        ALICE.superBot().views().packageExplorerView()
            .selectFile("Foo1_Saros", "src", "readme.txt").open();

        BOB.superBot().views().sarosView().selectUser(ALICE.getJID())
            .followParticipant();

        int lineCount = ALICE.remoteBot().editor("readme.txt").getLineCount();

        ALICE.remoteBot().editor("readme.txt").navigateTo(lineCount - 1, 0);

        ALICE.remoteBot().editor("readme.txt").typeText("123456789");

        ALICE.controlBot().getNetworkManipulator()
            .synchronizeOnActivityQueue(BOB.getJID(), 60 * 1000);

        List<Integer> viewport = BOB.remoteBot().editor("readme.txt")
            .getViewport();

        assertEquals(lineCount, viewport.get(0) + viewport.get(1));

        BOB.remoteBot().editor("readme.txt").navigateTo(lineCount - 1, 0);
        BOB.remoteBot().editor("readme.txt").typeText("0");

        try {
            BOB.remoteBot().shell("Read-Only Notification");
        } catch (WidgetNotFoundException e) {
            fail(
                "BOB got no notification that he has read only access, balloon window is not present: "
                    + e.getMessage());
        }

        BOB.remoteBot().editor("readme.txt").selectCurrentLine();

        BOB.controlBot().getNetworkManipulator()
            .synchronizeOnActivityQueue(ALICE.getJID(), 60 * 1000);

        assertEquals(BOB.remoteBot().editor("readme.txt").getSelection(),
            ALICE.remoteBot().editor("readme.txt").getSelectionByAnnotation());

    }
}