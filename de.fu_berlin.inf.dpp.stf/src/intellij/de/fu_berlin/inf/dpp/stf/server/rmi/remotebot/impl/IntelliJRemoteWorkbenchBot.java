package de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.impl;

import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.IRemoteWorkbenchBot;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotButton;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotCCombo;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotCLabel;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotCTabItem;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotCheckBox;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotCombo;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotEditor;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotLabel;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotList;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotMenu;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotPerspective;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotRadio;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotShell;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotStyledText;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotTable;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotText;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotToggleButton;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotToolbarButton;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotTree;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotView;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.impl.RemoteBotChatLine;
import org.eclipse.swtbot.swt.finder.waits.ICondition;

import java.rmi.RemoteException;
import java.util.List;

public final class IntelliJRemoteWorkbenchBot implements IRemoteWorkbenchBot {

    private static final IntelliJRemoteWorkbenchBot INSTANCE = new IntelliJRemoteWorkbenchBot();

    public static IntelliJRemoteWorkbenchBot getInstance() {
        return INSTANCE;
    }

    @Override
    public IRemoteBotView view(String viewTitle) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getTitlesOfOpenedViews() throws RemoteException {
        return null;
    }

    @Override
    public boolean isViewOpen(String title) throws RemoteException {
        return false;
    }

    @Override
    public void openViewById(String viewId) throws RemoteException {

    }

    @Override
    public IRemoteBotPerspective perspectiveByLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotPerspective perspectiveById(String id)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotView viewById(String id) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotView activeView() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotEditor editor(String fileName) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotEditor editorById(String id) throws RemoteException {
        return null;
    }

    @Override
    public boolean isEditorOpen(String fileName) throws RemoteException {
        return false;
    }

    @Override
    public IRemoteBotEditor activeEditor() throws RemoteException {
        return null;
    }

    @Override
    public boolean isPerspectiveOpen(String title) throws RemoteException {
        return false;
    }

    @Override
    public boolean isPerspectiveActive(String id) throws RemoteException {
        return false;
    }

    @Override
    public List<String> getPerspectiveTitles() throws RemoteException {
        return null;
    }

    @Override
    public void openPerspectiveWithId(String persID) throws RemoteException {

    }

    @Override
    public IRemoteBotPerspective activePerspective() throws RemoteException {
        return null;
    }

    @Override
    public void resetWorkbench() throws RemoteException {

    }

    @Override
    public void activateWorkbench() throws RemoteException {

    }

    @Override
    public IRemoteBotPerspective defaultPerspective() throws RemoteException {
        return null;
    }

    @Override
    public void closeAllEditors() throws RemoteException {

    }

    @Override
    public void saveAllEditors() throws RemoteException {

    }

    @Override
    public void resetActivePerspective() throws RemoteException {

    }

    @Override
    public void waitUntilEditorOpen(String title) throws RemoteException {

    }

    @Override
    public void waitUntilEditorClosed(String title) throws RemoteException {

    }

    @Override
    public void closeAllShells() throws RemoteException {

    }

    @Override
    public RemoteBotChatLine chatLine() throws RemoteException {
        return null;
    }

    @Override
    public RemoteBotChatLine chatLine(int index) throws RemoteException {
        return null;
    }

    @Override
    public RemoteBotChatLine lastChatLine() throws RemoteException {
        return null;
    }

    @Override
    public RemoteBotChatLine chatLine(String regex) throws RemoteException {
        return null;
    }

    @Override
    public void resetBot() throws RemoteException {

    }

    @Override
    public IRemoteBotTree tree() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithLabel(String label) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeInGroup(String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree tree(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithLabelInGroup(String label, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTree treeWithLabelInGroup(String label, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotShell shell(String title) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getOpenShellNames() throws RemoteException {
        return null;
    }

    @Override
    public boolean isShellOpen(String title) throws RemoteException {
        return false;
    }

    @Override
    public void waitUntilShellIsClosed(String title) throws RemoteException {

    }

    @Override
    public void waitLongUntilShellIsClosed(String title)
        throws RemoteException {

    }

    @Override
    public void waitUntilShellIsOpen(String title) throws RemoteException {

    }

    @Override
    public void waitLongUntilShellIsOpen(String title) throws RemoteException {

    }

    @Override
    public IRemoteBotButton buttonWithLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton button(String mnemonicText) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton button(String mnemonicText, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithTooltip(String tooltip)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithTooltip(String tooltip, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton button() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton button(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithLabelInGroup(String label, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithLabelInGroup(String label, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonInGroup(String mnemonicText, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonInGroup(String mnemonicText, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithTooltipInGroup(String tooltip,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotButton buttonWithTooltipInGroup(String tooltip,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel label() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel label(String mnemonicText) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel label(String mnemonicText, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelInGroup(String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel label(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelInGroup(String mnemonicText, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotLabel labelInGroup(String mnemonicText, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public boolean existsStyledText() throws RemoteException {
        return false;
    }

    @Override
    public boolean existsLabel() throws RemoteException {
        return false;
    }

    @Override
    public boolean existsLabelInGroup(String groupName) throws RemoteException {
        return false;
    }

    @Override
    public boolean existsLabel(String text) throws RemoteException {
        return false;
    }

    @Override
    public boolean existsCLabel() throws RemoteException {
        return false;
    }

    @Override
    public IRemoteBotCLabel clabel() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCLabel clabel(String text) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledText(String text) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledText(String text, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithId(String key, String value,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithId(String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledText() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledText(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithLabelInGroup(String label,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextWithLabelInGroup(String label,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextInGroup(String text, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotStyledText styledTextInGroup(String text, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBox(String text) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBox(String text, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBox() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBox(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithLabelInGroup(String label,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxWithLabelInGroup(String label,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxInGroup(String text, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCombo comboBoxInGroup(String text, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBox(String text) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBox(String text, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithId(String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBox() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBox(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxInGroup(String text, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxInGroup(String text, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithLabelInGroup(String label,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCCombo ccomboBoxWithLabelInGroup(String label,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButton() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButton(int index)
        throws RemoteException {
        return null;
    }

    @Override
    public boolean existsToolbarButton() throws RemoteException {
        return false;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButton(String mnemonicText)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButton(String mnemonicText, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithTooltip(String tooltip)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithTooltip(String tooltip,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithId(String key, String value,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithId(String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonInGroup(String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonInGroup(String mnemonicText,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonInGroup(String mnemonicText,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithTooltipInGroup(
        String tooltip, String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToolbarButton toolbarButtonWithTooltipInGroup(
        String tooltip, String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithLabel(String label) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText text(String text) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText text(String text, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithTooltip(String tooltip)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithTooltip(String tooltip, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithMessage(String message)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithMessage(String message, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textInGroup(String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText text() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText text(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithLabelInGroup(String label, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithLabelInGroup(String label, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textInGroup(String text, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textInGroup(String text, String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithTooltipInGroup(String tooltip, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotText textWithTooltipInGroup(String tooltip, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public boolean existsTable() throws RemoteException {
        return false;
    }

    @Override
    public boolean existsTableInGroup(String groupName) throws RemoteException {
        return false;
    }

    @Override
    public IRemoteBotTable tableWithLabel(String label) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableInGroup(String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable table() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable table(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotTable tableWithLabelInGroup(String label, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotMenu menu(String text) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotMenu menu(String text, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotMenu menuWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotMenu menuWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotMenu menuWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotMenu menuWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithLabel(String label) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listInGroup(String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList list() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList list(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithLabelInGroup(String label, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotList listWithLabelInGroup(String label, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public String getTextOfActiveShell() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotShell activeShell() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBox(String mnemonicText)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBox(String mnemonicText, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithTooltip(String tooltip)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithTooltip(String tooltip, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithId(String key, String value,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithId(String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBox() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBox(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithLabelInGroup(String label,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithLabelInGroup(String label,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxInGroup(String mnemonicText,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxInGroup(String mnemonicText,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithTooltipInGroup(String tooltip,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCheckBox checkBoxWithTooltipInGroup(String tooltip,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithLabel(String label) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radio(String mnemonicText) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radio(String mnemonicText, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithTooltip(String tooltip)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithTooltip(String tooltip, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithId(String key, String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithId(String value) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioInGroup(String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radio() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radio(int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithLabelInGroup(String label, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithLabelInGroup(String label, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioInGroup(String mnemonicText, String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioInGroup(String mnemonicText, String inGroup,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithTooltipInGroup(String tooltip,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotRadio radioWithTooltipInGroup(String tooltip,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithLabel(String label)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithLabel(String label, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButton(String mnemonicText)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButton(String mnemonicText, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithTooltip(String tooltip)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithTooltip(String tooltip,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithId(String key, String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithId(String key, String value,
        int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithId(String value)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithId(String value, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonInGroup(String inGroup)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonInGroup(String inGroup, int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButton() throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButton(int index)
        throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithLabelInGroup(String label,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithLabelInGroup(String label,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonInGroup(String mnemonicText,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonInGroup(String mnemonicText,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithTooltipInGroup(String tooltip,
        String inGroup) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotToggleButton toggleButtonWithTooltipInGroup(String tooltip,
        String inGroup, int index) throws RemoteException {
        return null;
    }

    @Override
    public IRemoteBotCTabItem cTabItem() throws RemoteException {
        return null;
    }

    @Override
    public void waitUntil(ICondition condition) throws RemoteException {

    }

    @Override
    public void waitLongUntil(ICondition condition) throws RemoteException {

    }

    @Override
    public void waitShortUntil(ICondition condition) throws RemoteException {

    }

    @Override
    public void logMessage(String message) throws RemoteException {

    }

    @Override
    public void captureScreenshot(String fileName) throws RemoteException {

    }
}