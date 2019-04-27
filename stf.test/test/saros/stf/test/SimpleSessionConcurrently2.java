package saros.stf.test;

import static org.junit.Assert.assertTrue;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;
import static saros.stf.client.tester.SarosTester.CARL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;
import saros.stf.test.stf.Constants;

public class SimpleSessionConcurrently2 extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {

        selectFirst(ALICE, BOB, CARL);

    }

    // @Rule
    // public STFTestWatcherLevelONEi watcherONEi = new
    // STFTestWatcherLevelONEi();

    @AfterClass
    public static void cleanUpSaros() throws Exception {

        // tearDownSarosLast();

    }

    @Test
    public void simpleSessionConcurrently()
        throws Exception, InterruptedException {
        ALICE.superBot().views().packageExplorerView().tree().newC()
            .javaProjectWithClasses(Constants.PROJECT1, Constants.PKG1,
                Constants.CLS1);

        Util.buildSessionConcurrently(Constants.PROJECT1,
            TypeOfCreateProject.NEW_PROJECT, ALICE, BOB, CARL);

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/MyClass.java");

        CARL.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/MyClass.java");
        assertTrue(true);
        /*
         * assertTrue(BOB.superBot().views().sarosView().isInSession());
         * assertTrue(ALICE.superBot().views().sarosView().isInSession());
         * assertTrue(CARL.superBot().views().sarosView().isInSession());
         *
         * BOB.superBot().views().packageExplorerView().waitUntilClassExists(
         * Constants.PROJECT1, Constants.PKG1, Constants.CLS1);
         * CARL.superBot().views().packageExplorerView().waitUntilClassExists(
         * Constants.PROJECT1, Constants.PKG1, Constants.CLS1);
         * assertFalse(ALICE.superBot().views().sarosView()
         * .selectUser(BOB.getJID()).hasReadOnlyAccess());
         *
         * assertTrue(ALICE.superBot().views().sarosView().selectUser(BOB.getJID
         * ()) .hasWriteAccess());
         *
         * leaveSessionPeersFirst(ALICE);
         *
         * assertFalse(BOB.superBot().views().sarosView().isInSession());
         * assertFalse(ALICE.superBot().views().sarosView().isInSession());
         */
    }

}
