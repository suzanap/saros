package de.fu_berlin.inf.dpp.stf.test.stf.html;

import de.fu_berlin.inf.dpp.stf.client.StfTestCase;
import de.fu_berlin.inf.dpp.ui.View;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.RemoteException;

import static de.fu_berlin.inf.dpp.stf.client.tester.SarosTester.ALICE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class HtmlViewTest extends StfTestCase {

    @BeforeClass
    public static void selectTesters() throws Exception {
        initTesters(ALICE);
        assumeTrue(ALICE.usesHtmlGui());
    }

    @Test
    public void openingAndClosingSarosHTMLView() throws RemoteException {
        ALICE.htmlBot().view(View.MAIN_VIEW).button("add-contact").click();
        assertTrue(ALICE.htmlBot().view(View.ADD_CONTACT).isOpen());

        ALICE.htmlViewBot().openSarosBrowserView();
        assertTrue(ALICE.htmlViewBot().isSarosBrowserViewOpen());

        ALICE.htmlViewBot().closeSarosBrowserView();
        assertFalse(ALICE.htmlViewBot().isSarosBrowserViewOpen());
    }
}
