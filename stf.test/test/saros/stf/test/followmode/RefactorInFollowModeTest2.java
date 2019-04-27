package saros.stf.test.followmode;

import static org.junit.Assert.assertEquals;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import java.util.List;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;

public class RefactorInFollowModeTest2 extends StfTestCase {

    static long total = 0;

    @BeforeClass
    public static void selectTesters() throws Exception {

        Assume.assumeTrue(checkIfLevelONEiandTWOiiiSucceeded());
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

    @Test
    public void testRefactorInFollowMode() throws Exception {

        // ALICE.superBot().internal().createJavaProject("foo");
        ALICE.superBot().internal().createJavaClass("Foo1_Saros", "bar",
            "HelloWorld");

        ALICE.superBot().views().packageExplorerView()
            .selectClass("Foo1_Saros", "bar", "HelloWorld").open();

        StringBuilder builder = new StringBuilder();
        builder.append(ALICE.remoteBot().editor("HelloWorld.java").getText());
        builder.setLength(builder.length() - 1);
        builder.append("\n\nint myfoobar = 0;\n");
        for (int i = 0; i < 10; i++)
            builder.append("public void test").append(i)
                .append("()\n{myfoobar = ").append(i).append(
                    ";\n}\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        builder.append("}");

        ALICE.remoteBot().editor("HelloWorld.java").closeWithSave();

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/bar/HelloWorld.java");

        ALICE.superBot().views().packageExplorerView()
            .selectClass("Foo1_Saros", "bar", "HelloWorld").open();

        BOB.superBot().views().sarosView().selectUser(ALICE.getJID())
            .followParticipant();

        List<Integer> viewPortBeforeRefactor = BOB.remoteBot()
            .editor("HelloWorld.java").getViewport();

        ALICE.remoteBot().editor("HelloWorld.java").setText(builder.toString());

        ALICE.remoteBot().editor("HelloWorld.java").navigateTo(4, 8);

        // wait for the text to be selected
        Thread.sleep(10000);

        ALICE.remoteBot().activateWorkbench();
        ALICE.remoteBot().menu("Refactor").menu("Rename...").click();
        ALICE.remoteBot().editor("HelloWorld.java").typeText("bar\n");

        // refactor might take a while
        Thread.sleep(10000);

        List<Integer> viewPortAfterRefactor = BOB.remoteBot()
            .editor("HelloWorld.java").getViewport();

        assertEquals("viewport changed", viewPortBeforeRefactor.get(0),
            viewPortAfterRefactor.get(0));

        assertEquals("file content is not the same",
            ALICE.remoteBot().editor("HelloWorld.java").getText(),
            BOB.remoteBot().editor("HelloWorld.java").getText());

    }
}