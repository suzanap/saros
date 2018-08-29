package de.fu_berlin.inf.dpp.stf.test.stf.html;

import de.fu_berlin.inf.dpp.stf.client.StfTestCase;
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
        select(ALICE);
        assumeTrue(ALICE.usesHtmlGui());
    }

    @Test
    public void openingAndClosingSarosHTMLView() throws RemoteException {
        ALICE.htmlViewBot().openSarosBrowserView();
        assertTrue(ALICE.htmlViewBot().isSarosBrowserViewOpen());

        ALICE.htmlViewBot().closeSarosBrowserView();
        assertFalse(ALICE.htmlViewBot().isSarosBrowserViewOpen());
    }
}
