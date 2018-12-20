package de.fu_berlin.inf.dpp.ui.actions;

import de.fu_berlin.inf.dpp.SarosConstants;
import de.fu_berlin.inf.dpp.SarosPluginContext;
import de.fu_berlin.inf.dpp.communication.extensions.JoinSessionRequestExtension;
import de.fu_berlin.inf.dpp.net.ITransmitter;
import de.fu_berlin.inf.dpp.net.xmpp.JID;
import de.fu_berlin.inf.dpp.net.xmpp.discovery.DiscoveryManager;
import de.fu_berlin.inf.dpp.session.ISarosSession;
import de.fu_berlin.inf.dpp.session.ISarosSessionManager;
import de.fu_berlin.inf.dpp.ui.Messages;
import de.fu_berlin.inf.dpp.ui.util.selection.SelectionUtils;
import de.fu_berlin.inf.dpp.ui.util.selection.retriever.SelectionRetrieverFactory;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.picocontainer.annotations.Inject;

/** Action for requesting an invitation to a session from a contact. */
public class RequestSessionInviteAction extends Action implements Disposable {

  @Inject private ISarosSessionManager sessionManager;
  @Inject private DiscoveryManager discoveryManager;
  @Inject private ITransmitter transmitter;

  private ISelectionListener selectionListener =
      new ISelectionListener() {
        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
          updateEnablement();
        }
      };

  public static final String ACTION_ID = RequestSessionInviteAction.class.getName();

  public RequestSessionInviteAction() {
    super(Messages.RequestSessionInviteAction_title);
    setId(ACTION_ID);
    SarosPluginContext.initComponent(this);
    SelectionUtils.getSelectionService().addSelectionListener(selectionListener);
    updateEnablement();
  }

  @Override
  public void run() {
    ISarosSession session = sessionManager.getSession();
    JID jid = getSelectedJID();
    if (session != null || jid == null) {
      return;
    }

    // TODO: Currently only Saros/S is known to have a working JoinSessionRequestHandler,
    //       remove this once the situation changes / change this to it's own feature.
    if (!discoveryManager.isFeatureSupported(jid, SarosConstants.NAMESPACE_SERVER)) {
      return;
    }

    transmitter.sendPacketExtension(
        jid, JoinSessionRequestExtension.PROVIDER.create(new JoinSessionRequestExtension()));
  }

  private JID getSelectedJID() {
    List<JID> selected = SelectionRetrieverFactory.getSelectionRetriever(JID.class).getSelection();

    if (selected.size() != 1) return null;

    return selected.get(0);
  }

  private void updateEnablement() {
    ISarosSession session = sessionManager.getSession();
    setEnabled(session == null && getSelectedJID() != null);
  }

  @Override
  public void dispose() {
    SelectionUtils.getSelectionService().removeSelectionListener(selectionListener);
  }
}
