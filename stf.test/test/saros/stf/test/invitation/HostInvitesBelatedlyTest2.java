package saros.stf.test.invitation;

import static org.junit.Assert.assertEquals;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;
import saros.stf.test.stf.Constants;

public class HostInvitesBelatedlyTest2 extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {

        Assume.assumeTrue(checkIfLevelONEiSucceeded());
        selectFirst(ALICE, BOB);

    }

    @AfterClass
    public static void cleanUpSaros() throws Exception {

        tearDownSarosLast();

    }

    /**
     * Steps:
     *
     * 1. ALICE edits the file CLS but don't saves it.
     *
     * 2. BOB edits the file CLS in the project currently used and saves it.
     *
     * 3. ALICE edits the file CLS2 but don't saves it.
     *
     * 4. BOB edits the file CLS2 in the project currently used and don't saves
     * it.
     *
     * 5. ALICE invites BOB.
     *
     * 6. The question about the changed files at BOB is answered with YES.
     *
     *
     * Expected Results:
     *
     * 7. BOB has the same project like host.
     *
     * FIXME: There are some bugs, if BOB's editors are not closed, BOB has the
     * different project like host.
     *
     * @throws CoreException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizeWithOpenDirtyEditofrsAtInviteesSide()
        throws Exception {

        ALICE.superBot().views().packageExplorerView().tree().newC()
            .javaProjectWithClasses(Constants.PROJECT1, Constants.PKG1,
                Constants.CLS1, Constants.CLS2);

        BOB.superBot().views().packageExplorerView().tree().newC()
            .javaProjectWithClasses(Constants.PROJECT1, Constants.PKG1,
                Constants.CLS1, Constants.CLS2);

        ALICE.superBot().views().packageExplorerView()
            .selectClass(Constants.PROJECT1, Constants.PKG1, Constants.CLS1)
            .open();

        ALICE.remoteBot().editor(Constants.CLS1_SUFFIX)
            .setTextFromFile(Constants.CP1);

        BOB.superBot().views().packageExplorerView()
            .selectClass(Constants.PROJECT1, Constants.PKG1, Constants.CLS1)
            .open();

        BOB.remoteBot().editor(Constants.CLS1_SUFFIX)
            .setTextFromFile(Constants.CP1_CHANGE);

        ALICE.superBot().views().packageExplorerView()
            .selectClass(Constants.PROJECT1, Constants.PKG1, Constants.CLS2)
            .open();

        ALICE.remoteBot().editor(Constants.CLS2_SUFFIX)
            .setTextFromFile(Constants.CP2);

        BOB.superBot().views().packageExplorerView()
            .selectClass(Constants.PROJECT1, Constants.PKG1, Constants.CLS2)
            .open();

        BOB.remoteBot().editor(Constants.CLS2_SUFFIX)
            .setTextFromFile(Constants.CP2_CHANGE);

        Util.buildSessionConcurrently(Constants.PROJECT1,
            TypeOfCreateProject.EXIST_PROJECT, ALICE, BOB);

        Thread.sleep(5000);

        String CLSContentOfAlice = ALICE.remoteBot()
            .editor(Constants.CLS1_SUFFIX).getText();

        String CLS2ContentOfAlice = ALICE.remoteBot()
            .editor(Constants.CLS2_SUFFIX).getText();

        String CLSContentOfBob = BOB.remoteBot().editor(Constants.CLS1_SUFFIX)
            .getText();

        String CLS2ContentOfBob = BOB.remoteBot().editor(Constants.CLS2_SUFFIX)
            .getText();

        assertEquals(CLSContentOfAlice, CLSContentOfBob);
        assertEquals(CLS2ContentOfAlice, CLS2ContentOfBob);

    }
}
