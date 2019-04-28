package saros.stf.test.session;

import static org.junit.Assert.assertFalse;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import org.junit.After;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants;
import saros.stf.shared.Constants.TypeOfCreateProject;

public class DerivedResourcesTest extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {
        Assume.assumeTrue(checkIfLevelONEiSucceeded());
        // if for some reason there is no session, build up a new session
        select(ALICE, BOB);
        if (!isSession()) {
            clearWorkspaces();
            ALICE.superBot().internal().createProject("Foo1_Saros");
            Util.buildSessionConcurrently("Foo1_Saros",
                TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);
        }

    }

    @After
    public void cleanUpSaros() throws Exception {

        ALICE.superBot().internal().deleteFolder("Foo1_Saros", "src");
        tearDownSaros();

    }

    /*
     * this test is testing the logic, not the attached icons to shared
     * resources !
     */
    @Test
    @Ignore("Does not work under *NIX because the context menu does not contain the Buidl Project option")
    public void testDerivedResourcesMustNotMarkedAsSharedInAFullSharedProject()
        throws Exception {
        ALICE.superBot().internal().createJavaClass("Foo1_Saros", "bar", "Foo");

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/bar/Foo.java");

        ALICE.remoteBot().view(Constants.VIEW_PACKAGE_EXPLORER).show();

        ALICE.remoteBot().view(Constants.VIEW_PACKAGE_EXPLORER).bot().tree()
            .selectTreeItemWithRegex("Foo1_Saros.*")
            .clickContextMenu("Build Project");

        /*
         * the following STF functions does not use the GUI, so we can ignore
         * the current package explorer content
         */

        ALICE.superBot().views().packageExplorerView()
            .waitUntilFileExists("Foo1_Saros/bin/bar/Foo.class");

        assertFalse("compiled class file is marked as shared",
            ALICE.superBot().views().packageExplorerView()
                .isResourceShared("Foo1_Saros/bin/bar/Foo.class"));
    }
}
