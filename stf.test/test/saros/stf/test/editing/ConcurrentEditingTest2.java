package saros.stf.test.editing;

import static org.junit.Assert.assertEquals;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import java.rmi.RemoteException;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;
import saros.stf.test.stf.Constants;
import saros.stf.testwatcher.STFTestWatcherLevelTWOii;

public class ConcurrentEditingTest2 extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {

        Assume.assumeTrue(checkIfLevelONEiSucceeded());
        select(ALICE, BOB);

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

    @Rule
    public STFTestWatcherLevelTWOii watcherLevelTWOii = new STFTestWatcherLevelTWOii();

    static final String FILE = "file.txt";

    /**
     * Test to reproduce bug "Inconsistency when concurrently writing at same
     * position"
     *
     * @throws RemoteException
     * @throws InterruptedException
     *
     * @see <a href="https://sourceforge.net/p/dpp/bugs/419/">Bug tracker entry
     *      419</a>
     */
    @Test
    public void testBugInconsistencyConcurrentEditing()
        throws Exception, InterruptedException {

        ALICE.superBot().internal().createFile(Constants.PROJECT1,
            "src/file.txt", "");
        ALICE.superBot().views().packageExplorerView()
            .selectFile(Constants.PROJECT1, "src", FILE).open();

        ALICE.remoteBot().waitUntilEditorOpen(FILE);
        ALICE.remoteBot().editor(FILE)
            .setTextFromFile("test/resources/lorem.txt");
        ALICE.remoteBot().editor(FILE).navigateTo(0, 6);

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared(Constants.PROJECT1 + "/src/" + FILE);
        BOB.superBot().views().packageExplorerView()
            .selectFile(Constants.PROJECT1, "src", FILE).open();

        BOB.remoteBot().waitUntilEditorOpen(FILE);
        BOB.remoteBot().editor(FILE).navigateTo(0, 30);

        Thread.sleep(1000);

        // Alice goes to 0,6 and hits Delete
        ALICE.remoteBot().activateWorkbench();
        int waitActivate = 100;
        ALICE.remoteBot().editor(FILE).show();

        ALICE.remoteBot().editor(FILE).waitUntilIsActive();
        // at the same time, Bob enters L at 0,30
        BOB.remoteBot().activateWorkbench();
        Thread.sleep(waitActivate);
        BOB.remoteBot().editor(FILE).show();
        BOB.remoteBot().editor(FILE).waitUntilIsActive();

        Thread.sleep(waitActivate);
        ALICE.remoteBot().editor(FILE)
            .pressShortcut(new String[] { IKeyLookup.BACKSPACE_NAME });

        BOB.remoteBot().editor(FILE).typeText("L");
        // both sleep for less than 1000ms

        // Alice hits Delete again
        ALICE.remoteBot().editor(FILE)
            .pressShortcut(new String[] { IKeyLookup.BACKSPACE_NAME });
        // Bob enters o
        BOB.remoteBot().editor(FILE).typeText("o");

        Thread.sleep(3000);
        String ALICEText = ALICE.remoteBot().editor(FILE).getText();
        String BOBText = BOB.remoteBot().editor(FILE).getText();

        ALICE.remoteBot().editor(FILE).closeWithoutSave();
        BOB.remoteBot().editor(FILE).closeWithoutSave();

        assertEquals(ALICEText, BOBText);

    }
}
