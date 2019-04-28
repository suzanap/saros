package saros.stf.test.consistency;

import static org.junit.Assert.assertEquals;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;
import static saros.stf.client.tester.SarosTester.CARL;
import static saros.stf.shared.Constants.ACCEPT;
import static saros.stf.shared.Constants.SHELL_SESSION_INVITATION;

import java.rmi.RemoteException;
import java.util.Random;
import org.junit.After;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.tester.SarosTester;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants.TypeOfCreateProject;
import saros.stf.test.stf.Constants;
import saros.test.util.EclipseTestThread;

public class EditDuringInvitationStressTest3 extends StfTestCase {

    private EclipseTestThread bobIsWriting;
    private final String[] CLASS_NAMES = { "ClassA", "ClassB", "ClassC",
        "ClassD", "ClassE" };

    @BeforeClass
    public static void selectTesters() throws Exception {
        Assume.assumeTrue(checkIfLevelONEiiiandTWOivSucceeded());
        select(ALICE, BOB, CARL);
        // if for some reason there is no session, build up a new session
        if (ALICE.superBot().views().sarosView().isInSession() == false
            && BOB.superBot().views().sarosView().isInSession() == false) {
            clearWorkspaces();
            ALICE.superBot().internal().createProject("Foo1_Saros");
            Util.buildSessionConcurrently("Foo1_Saros",
                TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);
        }

    }

    @After
    public void restoreNetwork() throws Exception {
        ALICE.superBot().internal().deleteFolder("Foo1_Saros", "src");

        tearDownSarosLast();
    }

    @Test
    public void testEditMultipleClassesDuringInvitation() throws Exception {
        ALICE.superBot().internal().createJavaClass(Constants.PROJECT1,
            Constants.PKG1, "ClassA");
        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/ClassA.java");

        ALICE.superBot().internal().createJavaClass(Constants.PROJECT1,
            Constants.PKG1, "ClassB");
        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/ClassB.java");
        ALICE.superBot().internal().createJavaClass(Constants.PROJECT1,
            Constants.PKG1, "ClassC");
        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/ClassC.java");
        ALICE.superBot().internal().createJavaClass(Constants.PROJECT1,
            Constants.PKG1, "ClassD");
        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/ClassD.java");
        ALICE.superBot().internal().createJavaClass(Constants.PROJECT1,
            Constants.PKG1, "ClassE");
        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/ClassE.java");

        openTestClasses(BOB);

        ALICE.superBot().views().sarosView().selectContact(CARL.getJID())
            .addToSarosSession();

        CARL.remoteBot().shell(SHELL_SESSION_INVITATION).confirm(ACCEPT);

        bobIsWriting = createTestThread(new EclipseTestThread.Runnable() {

            private final Random random = new Random();

            @Override
            public void run() throws Exception {
                for (int i = 0; i < 100; i++) {
                    String nextClass = CLASS_NAMES[i % 5] + ".java";
                    BOB.remoteBot().editor(nextClass)
                        .typeText(String.valueOf(generateCharacter()));
                }
            }

            // ([A-Z] union [a-z])
            private char generateCharacter() {
                return (char) (((random.nextInt() & 0x7FFFFFFF) % 26) | 0x41
                    | ((random.nextInt() & 1) << 5));
            }
        });

        bobIsWriting.start();

        CARL.superBot()
            .confirmShellAddProjectWithNewProject(Constants.PROJECT1);

        bobIsWriting.join();
        bobIsWriting.verify();

        CARL.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/ClassA.java");
        openTestClasses(ALICE);
        openTestClasses(CARL);

        compareFilesOfTesters(BOB, ALICE);
        compareFilesOfTesters(BOB, CARL);
    }

    private void openTestClasses(SarosTester tester) throws RemoteException {
        for (String className : CLASS_NAMES) {
            className = className + ".java";
            tester.superBot().views().packageExplorerView()
                .selectFile("Foo1_Saros", "src", "my", "pkg", className).open();
        }
    }

    private void compareFilesOfTesters(SarosTester testerA, SarosTester testerB)
        throws RemoteException {
        for (String className : CLASS_NAMES) {
            String textByA = testerA.remoteBot().editor(className + ".java")
                .getText();
            String textByB = testerB.remoteBot().editor(className + ".java")
                .getText();
            assertEquals(textByA, textByB);
        }
    }
}
