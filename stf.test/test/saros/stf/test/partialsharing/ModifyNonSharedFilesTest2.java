package saros.stf.test.partialsharing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;

public class ModifyNonSharedFilesTest2 extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {

        Assume.assumeTrue(checkIfLevelONEiSucceeded());
        selectFirst(ALICE, BOB);

    }

    @AfterClass
    public static void cleanUpSaros() throws Exception {

        tearDownSarosLast();

    }

    @Test
    public void testModifyNonSharedFilesTest() throws Exception {

        ALICE.superBot().internal().createProject("A");
        ALICE.superBot().internal().createFile("A", "a/a.txt", "a");
        ALICE.superBot().internal().createFile("A", "b/b.txt", "b");

        BOB.superBot().internal().createProject("A");
        BOB.superBot().internal().createFile("A", "a/a.txt", "a");
        BOB.superBot().internal().createFile("A", "b/b.txt", "b");

        Util.buildFileSessionConcurrently("A", new String[] { "a/a.txt" },
            TypeOfCreateProject.EXIST_PROJECT, ALICE, BOB);

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("A/a/a.txt");

        assertFalse("a non shared resource is marked as shared", BOB.superBot()
            .views().packageExplorerView().isResourceShared("A/b/b.txt"));

        ALICE.superBot().internal().append("A", "b/b.txt",
            " this should not be appended as the file is not shared");

        ALICE.controlBot().getNetworkManipulator()
            .synchronizeOnActivityQueue(BOB.getJID(), 10000);

        byte[] content = BOB.superBot().internal().getFileContent("A",
            "b/b.txt");

        assertEquals("file was changed", "b", new String(content));

    }
}
