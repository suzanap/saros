package de.fu_berlin.inf.dpp.whiteboard.gef.editor;

import java.util.ArrayList;

import org.apache.batik.util.SVGConstants;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SharedImages;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Composite;

import de.fu_berlin.inf.dpp.whiteboard.gef.actions.ChangeBackgroundColorAction;
import de.fu_berlin.inf.dpp.whiteboard.gef.actions.ChangeForegroundColorAction;
import de.fu_berlin.inf.dpp.whiteboard.gef.actions.CopyRecordAction;
import de.fu_berlin.inf.dpp.whiteboard.gef.actions.ExportToImageAction;
import de.fu_berlin.inf.dpp.whiteboard.gef.actions.PasteRecordAction;
import de.fu_berlin.inf.dpp.whiteboard.gef.actions.SXEDeleteAction;
import de.fu_berlin.inf.dpp.whiteboard.gef.commands.ElementRecordCreateCommand;
import de.fu_berlin.inf.dpp.whiteboard.gef.editpolicy.ElementModelLayoutEditPolicy;
import de.fu_berlin.inf.dpp.whiteboard.gef.palette.WhiteboardPaletteRootFactory;
import de.fu_berlin.inf.dpp.whiteboard.gef.part.RecordPartFactory;
import de.fu_berlin.inf.dpp.whiteboard.gef.request.CreateTextBoxRequest;
import de.fu_berlin.inf.dpp.whiteboard.net.WhiteboardManager;
import de.fu_berlin.inf.dpp.whiteboard.standalone.WhiteboardContextMenuProvider;
import de.fu_berlin.inf.dpp.whiteboard.sxe.ISXEMessageHandler.MessageAdapter;
import de.fu_berlin.inf.dpp.whiteboard.sxe.ISXEMessageHandler.NotificationListener;
import de.fu_berlin.inf.dpp.whiteboard.sxe.net.SXEMessage;
import de.fu_berlin.inf.dpp.whiteboard.sxe.records.ElementRecord;

/**
 * <p>
 * The editor creates the GUI using the GEF API and initializes to listen to the
 * WhiteboardManager.
 * </p>
 * 
 * @author jurke
 * 
 */
public class WhiteboardEditor extends SarosPermissionsGraphicalEditor {

    public static final String ID = "de.fu_berlin.inf.dpp.whiteboard.whiteboardeditor";

    private KeyHandler keyHandler;

    /**
     * Creates the editor with a custom command stack
     * 
     * @see SXECommandStack
     */
    public WhiteboardEditor() {
        DefaultEditDomain editDomain = new DefaultEditDomain(this);
        editDomain.setCommandStack(new SXECommandStack());
        setEditDomain(editDomain);
        // initColors();
    }

    /**
     * Initializes the graphical viewer with the root element, from now it
     * listens to applied remote records to update action enablement and to
     * document root changes to update the root.
     */
    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();

        WhiteboardManager.getInstance().getSXEMessageHandler()
            .addMessageListener(new MessageAdapter() {

                // obsolete because of notification listener
                // /*
                // * We have to check the action enablement after receiving
                // a
                // * remove message. I.e. maybe a selected item was deleted
                // * thus copy/delete have to be disabled.
                // */
                // @Override
                // public void sxeRecordMessageApplied(SXEMessage message) {
                // updateActions();
                // }

                @Override
                public void sxeStateMessageApplied(SXEMessage message,
                    ElementRecord root) {
                    updateViewerContents(root);
                }

            });

        viewer.setContents(WhiteboardManager.getInstance()
            .getSXEMessageHandler().getDocumentRecord().getRoot());

        viewer.addDropTargetListener(new TemplateTransferDropTargetListener(
            viewer) {
            /**
             * Overridden by the superclass method because selecting the created
             * object here does not make sense as it differs from the one that
             * will be created by the command (and finally by the DocumentRecord
             * as it should be).
             */
            @Override
            protected void handleDrop() {
                updateTargetRequest();
                updateTargetEditPart();

                if (getTargetEditPart() != null) {
                    Command command = getCommand();

                    // [FIXME] find a better solution to enable a proper
                    // creation of text-based elements via drag & drop
                    if (command instanceof ElementRecordCreateCommand) {
                        String cName = ((ElementRecordCreateCommand) command)
                            .getChildName();
                        if (cName.equals(SVGConstants.SVG_TEXT_VALUE)) {

                            // Open dialog to enter the text
                            InputDialog d = new InputDialog(null, "Enter Text",
                                "Enter the text", "text", null);
                            String val = "";
                            if (d != null) {
                                d.open();

                                val = d.getValue();
                                if (val == null || val.isEmpty())
                                    val = "";
                            }

                            CreateRequest oldReq = ((CreateRequest) getTargetRequest());
                            CreateTextBoxRequest r = new CreateTextBoxRequest();
                            r.setSize(oldReq.getSize());
                            r.setLocation(oldReq.getLocation());
                            r.setFactory(new CombinedTargetRecordCreationFactory(
                                cName));
                            r.setText(val);

                            ElementModelLayoutEditPolicy p = ((ElementModelLayoutEditPolicy) (getTargetEditPart()
                                .getEditPolicy(EditPolicy.LAYOUT_ROLE)));
                            command = p.getCommand(r);

                        }
                    }

                    if (command != null && command.canExecute())
                        getViewer().getEditDomain().getCommandStack()
                            .execute(command);
                    else
                        getCurrentEvent().detail = DND.DROP_NONE;
                } else
                    getCurrentEvent().detail = DND.DROP_NONE;
            }
        });

        super.initializeGraphicalViewer();
    }

    protected void updateViewerContents(ElementRecord root) {
        if (root == null)
            return;
        getGraphicalViewer().setContents(root);
    }

    /**
     * <p>
     * Creates a custom <code>ScrollingGraphicalViewer</code> that does not
     * update actions while editparts are notified about applied records to
     * recreate their figures.
     * </p>
     * 
     * <p>
     * This was added because performance decreases a lot if the whole selection
     * infrastructure plus every <code>SelectionAction</code> is recalculated on
     * every selection handle removal during
     * <code>EditPart.refreshChildren()</code>.
     * </p>
     * 
     */
    /*
     * A delete of 7000 selected objects - they have to be selected to be
     * deleted - took more than 10 minutes, with this fix it is done in less
     * than half a minute. An updateActions() is now forced after processing a
     * bunch of records.
     */
    @Override
    protected void createGraphicalViewer(Composite parent) {

        final GraphicalViewer viewer = new ScrollingGraphicalViewer() {

            protected boolean isNotifying = false;

            {
                WhiteboardManager.getInstance().getSXEMessageHandler()
                    .addNotificationListener(new NotificationListener() {

                        @Override
                        public void beforeNotification() {
                            isNotifying = true;
                        }

                        @Override
                        public void afterNotificaion() {
                            isNotifying = false;
                            fireSelectionChanged();
                            updateActions();
                        }

                    });
            }

            @Override
            protected void fireSelectionChanged() {
                if (isNotifying)
                    return;
                super.fireSelectionChanged();
            }
        };

        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }

    @Override
    protected void initializePaletteViewer() {
        super.initializePaletteViewer();
        getPaletteViewer().addDragSourceListener(
            new TemplateTransferDragSourceListener(getPaletteViewer()));
    }

    /*
     * Initializes/adds the EditPartFactory, RootEditPart, ZoomManager,
     * KeyHandler and ContextMenuProvider
     * 
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
     */
    @Override
    protected void configureGraphicalViewer() {
        double[] zoomLevels;

        super.configureGraphicalViewer();

        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new RecordPartFactory());

        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootEditPart);

        ZoomManager manager = rootEditPart.getZoomManager();
        getActionRegistry().registerAction(new ZoomInAction(manager));
        getActionRegistry().registerAction(new ZoomOutAction(manager));

        zoomLevels = new double[] { 0.1, 0.25, 0.5, 0.75, 1, 1.5, 2.0, 2.5, 3,
            4, 5, 10 };
        manager.setZoomLevels(zoomLevels);
        manager.setZoom(1);
        ArrayList<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);

        keyHandler = new KeyHandler();
        keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0),
            getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),
            getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE),
            MouseWheelZoomHandler.SINGLETON);

        viewer.setKeyHandler(keyHandler);

        ContextMenuProvider provider = new WhiteboardContextMenuProvider(
            viewer, getActionRegistry());
        viewer.setContextMenu(provider);

    }

    @Override
    @SuppressWarnings("unchecked")
    public void createActions() {
        ActionRegistry registry = getActionRegistry();
        IAction action;

        action = new UndoAction(this);
        registry.registerAction(action);
        getStackActions().add(action.getId());

        action = new RedoAction(this);
        registry.registerAction(action);
        getStackActions().add(action.getId());

        action = new ChangeBackgroundColorAction();
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new ChangeForegroundColorAction();
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new SelectAllAction(this) {
            {
                /*
                 * Somehow it did not work to set a transparent color, let's use
                 * the standard marquee icon for the moment
                 */
                setHoverImageDescriptor(SharedImages.DESC_MARQUEE_TOOL_16);
                setImageDescriptor(SharedImages.DESC_MARQUEE_TOOL_16);
            }
        };
        registry.registerAction(action);

        action = new SXEDeleteAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new SaveAction(this);
        registry.registerAction(action);
        getPropertyActions().add(action.getId());

        registry.registerAction(new PrintAction(this));

        action = new CopyRecordAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new PasteRecordAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new ExportToImageAction(this);
        registry.registerAction(action);
        getStackActions().add(action.getId());
    }

    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
        if (type == ZoomManager.class)
            return ((ScalableFreeformRootEditPart) getGraphicalViewer()
                .getRootEditPart()).getZoomManager();
        return super.getAdapter(type);
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        return WhiteboardPaletteRootFactory.createPaletteRoot();
    }

    // TODO Saving
    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    /*
     * Updates the registered selection actions, needed for the copy command
     */
    public void updateSelectionActions() {
        updateActions(getSelectionActions());
    }

}
