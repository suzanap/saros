package saros.stf.test.invitation;

import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;
import static saros.stf.client.tester.SarosTester.CARL;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;

public class InviteAndLeaveStressTest extends StfTestCase {

    private static final int MAX_PROJECTS = 10;

    @BeforeClass
    public static void selectTesters() throws Exception {
        Assume.assumeTrue(checkIfLevelONEiiiSucceeded());
        selectFirst(ALICE, BOB, CARL);
    }

    @AfterClass
    public static void cleanUpSaros() throws Exception {
        tearDownSarosLast();
    }

    @Test
    public void testInviteAndLeaveStress() throws Exception {

        for (int i = 0; i < MAX_PROJECTS; i++) {
            ALICE.superBot().internal().createProject("foo" + i);
            ALICE.superBot().internal().createFile("foo" + i, "foo.txt", "foo");
        }

        for (int i = 0; i < MAX_PROJECTS; i++) {
            Util.buildSessionConcurrently("foo" + i,
                TypeOfCreateProject.NEW_PROJECT, ALICE, BOB, CARL);

            leaveSessionHostFirst(ALICE);
        }
    }
}
