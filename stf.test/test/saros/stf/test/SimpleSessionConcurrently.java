package de.fu_berlin.inf.dpp.stf.test;

import static de.fu_berlin.inf.dpp.stf.client.tester.SarosTester.ALICE;
import static de.fu_berlin.inf.dpp.stf.client.tester.SarosTester.BOB;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import de.fu_berlin.inf.dpp.stf.client.StfTestCase;
import de.fu_berlin.inf.dpp.stf.client.util.Util;
import de.fu_berlin.inf.dpp.stf.shared.Constants.TypeOfCreateProject;
import de.fu_berlin.inf.dpp.stf.test.stf.Constants;
import de.fu_berlin.inf.dpp.stf.testwatcher.STFTestWatcherLevelONEi;

public class SimpleSessionConcurrently extends StfTestCase {
    static StopWatch stopwatch = new StopWatch();
    static long total = 0;

    @BeforeClass
    public static void selectTesters() throws Exception {
        stopwatch.start();
        selectFirst(ALICE, BOB);

    }

    @Rule
    public STFTestWatcherLevelONEi watcherONEi = new STFTestWatcherLevelONEi();

    @AfterClass
    public static void cleanUpSaros() throws Exception {

        tearDownSarosLast();
        stopwatch.stop();
        writeToFile("SimpleSessionConcurrently", "Total:", stopwatch.getTime());

        stopwatch.reset();
    }

    @Test
    public void simpleSessionConcurrently() throws Exception,
        InterruptedException {

        ALICE
            .superBot()
            .views()
            .packageExplorerView()
            .tree()
            .newC()
            .javaProjectWithClasses(Constants.PROJECT1, Constants.PKG1,
                Constants.CLS1);

        Util.buildSessionConcurrently(Constants.PROJECT1,
            TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);

        assertTrue(BOB.superBot().views().sarosView().isInSession());
        assertTrue(ALICE.superBot().views().sarosView().isInSession());

        BOB.superBot()
            .views()
            .packageExplorerView()
            .waitUntilClassExists(Constants.PROJECT1, Constants.PKG1,
                Constants.CLS1);

        assertFalse(ALICE.superBot().views().sarosView()
            .selectUser(BOB.getJID()).hasReadOnlyAccess());

        assertTrue(ALICE.superBot().views().sarosView()
            .selectUser(BOB.getJID()).hasWriteAccess());

        leaveSessionPeersFirst(ALICE);

        assertFalse(BOB.superBot().views().sarosView().isInSession());
        assertFalse(ALICE.superBot().views().sarosView().isInSession());

    }

}
