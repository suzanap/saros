package saros.stf.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;
import saros.stf.test.stf.Constants;
import saros.stf.testwatcher.STFTestWatcherLevelONEii;

public class SimpleSessionSequentially extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {

        selectFirst(ALICE, BOB);

    }

    @AfterClass
    public static void cleanUpSaros() throws Exception {

        tearDownSarosLast();

    }

    @Rule
    public STFTestWatcherLevelONEii watcherONEii = new STFTestWatcherLevelONEii();

    @Test
    public void simpleSessionSequentially()
        throws Exception, InterruptedException {

        ALICE.superBot().views().packageExplorerView().tree().newC()
            .javaProjectWithClasses(Constants.PROJECT1, Constants.PKG1,
                Constants.CLS1);

        Util.buildSessionSequentially(Constants.PROJECT1,
            TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);

        assertTrue(BOB.superBot().views().sarosView().isInSession());
        assertTrue(ALICE.superBot().views().sarosView().isInSession());

        BOB.superBot().views().packageExplorerView().waitUntilClassExists(
            Constants.PROJECT1, Constants.PKG1, Constants.CLS1);

        assertFalse(ALICE.superBot().views().sarosView()
            .selectUser(BOB.getJID()).hasReadOnlyAccess());

        assertTrue(ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .hasWriteAccess());

        leaveSessionPeersFirst(ALICE);

        assertFalse(BOB.superBot().views().sarosView().isInSession());
        assertFalse(ALICE.superBot().views().sarosView().isInSession());

    }

}