package saros.stf.test.chatview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static saros.stf.client.tester.SarosTester.ALICE;
import static saros.stf.client.tester.SarosTester.BOB;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import saros.stf.client.StfTestCase;
import saros.stf.client.util.Util;
import saros.stf.shared.Constants;
import saros.stf.shared.Constants.TypeOfCreateProject;

public class ChatViewFunctionsTest2 extends StfTestCase {

    String messageBob = "Hello Bob";
    String messageAlice = "Hello Alice";
    String messageUnseen1 = "first unseen message";
    String messageUnseen2 = "second unseen message";
    String messageUnseen3 = "third unseen message";

    @BeforeClass
    public static void selectTesters() throws Exception {

        Assume.assumeTrue(checkIfLevelONEiSucceeded());
        selectFirst(ALICE, BOB);

        ALICE.superBot().internal().createProject("Foo1_Saros");

        Util.buildSessionConcurrently("Foo1_Saros",
            TypeOfCreateProject.NEW_PROJECT, ALICE, BOB);

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
    public void testChat() throws Exception {

        ALICE.superBot().internal().createJavaClass("Foo1_Saros", "my.pkg",
            "MyClass");

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/MyClass.java");

        ALICE.superBot().views().sarosView()
            .selectChatroom(Constants.CHATROOM_TAB_LABEL)
            .sendChatMessage(messageBob);

        /*
         * Wait so that the message is received on the other side
         */

        Thread.sleep(5000);

        assertEquals(messageBob,
            BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL)
                .getTextOfLastChatLine());

        BOB.superBot().views().sarosView()
            .selectChatroom(Constants.CHATROOM_TAB_LABEL)
            .sendChatMessage(messageAlice);

        /*
         * Wait so that the message is received on the other side
         */

        Thread.sleep(5000);

        assertEquals(messageAlice,
            ALICE.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL)
                .getTextOfLastChatLine());

    }

    /*
     * Test for misbehavior on file opening in follow mode as described by bug
     * #3455372.
     */
    @Test
    public void testUnintentionalCursorMove() throws Exception {

        ALICE.superBot().internal().createJavaClass("Foo1_Saros", "my.pkg",
            "MyClass");

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/MyClass.java");

        ALICE.superBot().internal().createFile("Foo1_Saros",
            "src/my/pkg/MyClass2.java", "");

        BOB.superBot().views().packageExplorerView()
            .waitUntilResourceIsShared("Foo1_Saros/src/my/pkg/MyClass2.java");

        BOB.superBot().views().sarosView().selectUser(ALICE.getJID())
            .followParticipant();

        BOB.superBot().views().sarosView()
            .selectChatroom(Constants.CHATROOM_TAB_LABEL)
            .enterChatMessage("Hello how");

        ALICE.superBot().views().packageExplorerView()
            .selectClass("Foo1_Saros", "my.pkg", "MyClass2").open();

        /* Wait a short time so the editor change is executed on Bobs side */
        Thread.sleep(5000);

        BOB.superBot().views().sarosView()
            .selectChatroom(Constants.CHATROOM_TAB_LABEL)
            .enterChatMessage(" are you?");

        assertEquals(
            "cursor position changed during focus lost and focus regain",
            "Hello how are you?", BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).getChatMessage());

    }

    @Test
    public void testVisualChatNotification() throws Exception {

        // Assume.assumeTrue(checkIfLevelONEiSucceeded());
        // Initial state: session chat open and with focus, two message
        // exchanged, no notification shown
        ALICE.superBot().views().sarosView()
            .selectChatroom(Constants.CHATROOM_TAB_LABEL)
            .sendChatMessage(messageBob);

        BOB.superBot().views().sarosView()
            .selectChatroom(Constants.CHATROOM_TAB_LABEL)
            .sendChatMessage(messageAlice);

        assertTrue("Session Chat should be the active chat tab at ALICE",
            ALICE.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).isActive());

        assertTrue("Session Chat should be the active chat tab at BOB",
            BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).isActive());

        assertEquals("Visual chat notification should not appear at ALICE",
            Constants.CHATROOM_TAB_LABEL, ALICE.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).getTitle());

        assertEquals("Visual chat notification should not appear at BOB",
            Constants.CHATROOM_TAB_LABEL, BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).getTitle());

        // Alice opens new single user chat with Bob and sends a message
        ALICE.superBot().views().sarosView().selectUser(BOB.getJID())
            .openChat();

        assertTrue("Session Chat should still be the active chat tab at BOB",
            BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).isActive());

        ALICE.superBot().views().sarosView()
            .selectChatroomWithRegex(BOB.getBaseJid() + ".*")
            .sendChatMessage(messageUnseen1);

        Thread.sleep(5000);

        assertTrue("Session Chat should still be the active chat tab at BOB",
            BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).isActive());

        // Chat notification is not shown if the chat tab was opened because of
        // this message
        assertEquals("Visual chat notification should not appear at BOB",
            ALICE.getBaseJid(), BOB.superBot().views().sarosView()
                .selectChatroomWithRegex(ALICE.getBaseJid() + ".*").getTitle());

        assertTrue("Session Chat should still be the active chat tab at BOB",
            BOB.superBot().views().sarosView()
                .selectChatroom(Constants.CHATROOM_TAB_LABEL).isActive());

        ALICE.superBot().views().sarosView()
            .selectChatroomWithRegex(BOB.getBaseJid() + ".*")
            .sendChatMessage(messageUnseen2);

        Thread.sleep(5000);

        assertEquals("Visual chat notification should appear at BOB",
            "(1) " + ALICE.getBaseJid(), BOB.superBot().views().sarosView()
                .selectChatroomWithRegex(ALICE.getBaseJid() + ".*").getTitle());

        ALICE.superBot().views().sarosView()
            .selectChatroomWithRegex(BOB.getBaseJid() + ".*")
            .sendChatMessage(messageUnseen3);

        Thread.sleep(5000);

        assertEquals("Visual chat notification should appear at BOB",
            "(2) " + ALICE.getBaseJid(), BOB.superBot().views().sarosView()
                .selectChatroomWithRegex(ALICE.getBaseJid() + ".*").getTitle());

        BOB.superBot().views().sarosView()
            .selectChatroomWithRegex(ALICE.getBaseJid() + ".*").activate();

        assertEquals("Visual chat notification should disappear at BOB",
            ALICE.getBaseJid(), BOB.superBot().views().sarosView()
                .selectChatroomWithRegex(ALICE.getBaseJid() + ".*").getTitle());

        ALICE.superBot().views().sarosView()
            .closeChatroomWithRegex(BOB.getBaseJid() + ".*");

        BOB.superBot().views().sarosView()
            .closeChatroomWithRegex(ALICE.getBaseJid() + ".*");

    }
}