package saros.stf.test.editing;

import static org.junit.Assert.assertEquals;
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

public class EditWithReadAccessOnlyTest extends StfTestCase {

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

    @Test
    public void testEditingWithReadOnlyAccess() throws Exception {

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros");
        ALICE.superBot().internal().createJavaClass("Foo1_Saros", "bar",
            "HelloWorld");
        ALICE.superBot().views().packageExplorerView()
            .selectClass("Foo1_Saros", "bar", "HelloWorld").open();

        BOB.superBot().views().packageExplorerView()
            .selectClass("Foo1_Saros", "bar", "HelloWorld").open();

        ALICE.remoteBot().waitUntilEditorOpen("HelloWorld.java");
        BOB.remoteBot().waitUntilEditorOpen("HelloWorld.java");

        String aliceEditorText = ALICE.remoteBot().editor("HelloWorld.java")
            .getText();

        ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .restrictToReadOnlyAccess();

        ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .waitUntilHasReadOnlyAccess();

        BOB.remoteBot().editor("HelloWorld.java").typeText("1234567890");
        String bobEditorText = BOB.remoteBot().editor("HelloWorld.java")
            .getText();

        assertEquals(aliceEditorText, bobEditorText);

    }
}
