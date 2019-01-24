package de.fu_berlin.inf.dpp.server.session;

import de.fu_berlin.inf.dpp.filesystem.IProject;
import de.fu_berlin.inf.dpp.filesystem.IWorkspace;
import de.fu_berlin.inf.dpp.monitoring.NullProgressMonitor;
import de.fu_berlin.inf.dpp.negotiation.AbstractIncomingProjectNegotiation;
import de.fu_berlin.inf.dpp.negotiation.AbstractOutgoingProjectNegotiation;
import de.fu_berlin.inf.dpp.negotiation.IncomingSessionNegotiation;
import de.fu_berlin.inf.dpp.negotiation.NegotiationTools;
import de.fu_berlin.inf.dpp.negotiation.OutgoingSessionNegotiation;
import de.fu_berlin.inf.dpp.negotiation.ProjectNegotiation;
import de.fu_berlin.inf.dpp.negotiation.ProjectNegotiationData;
import de.fu_berlin.inf.dpp.negotiation.SessionNegotiation;
import de.fu_berlin.inf.dpp.net.xmpp.JID;
import de.fu_berlin.inf.dpp.server.ServerConfig;
import de.fu_berlin.inf.dpp.server.filesystem.ServerProjectImpl;
import de.fu_berlin.inf.dpp.server.progress.ConsoleProgressIndicator;
import de.fu_berlin.inf.dpp.session.INegotiationHandler;
import de.fu_berlin.inf.dpp.session.ISarosSessionManager;
import de.fu_berlin.inf.dpp.util.NamedThreadFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class NegotiationHandler implements INegotiationHandler {

  private static final Logger log = Logger.getLogger(NegotiationHandler.class);

  private final ISarosSessionManager sessionManager;
  private final IWorkspace workspace;
  private final ThreadPoolExecutor sessionExecutor =
      new ThreadPoolExecutor(
          0,
          4,
          60,
          TimeUnit.SECONDS,
          new LinkedBlockingQueue<Runnable>(),
          new NamedThreadFactory("ServerSessionNegotiation-"));
  private final ThreadPoolExecutor projectExecutor =
      new ThreadPoolExecutor(
          0,
          10,
          60,
          TimeUnit.SECONDS,
          new LinkedBlockingQueue<Runnable>(),
          new NamedThreadFactory("ServerProjectNegotiation-"));

  public NegotiationHandler(ISarosSessionManager sessionManager, IWorkspace workspace) {
    sessionManager.setNegotiationHandler(this);
    this.sessionManager = sessionManager;
    this.workspace = workspace;
  }

  @Override
  public void handleOutgoingSessionNegotiation(final OutgoingSessionNegotiation negotiation) {

    sessionExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            SessionNegotiation.Status status;
            if (ServerConfig.isInteractive()) {
              status = negotiation.start(new ConsoleProgressIndicator(System.out));
            } else {
              status = negotiation.start(new NullProgressMonitor());
            }

            switch (status) {
              case OK:
                sessionManager.startSharingProjects(negotiation.getPeer());
                break;
              case ERROR:
                log.error("ERROR running session negotiation: " + negotiation.getErrorMessage());
                break;
              case REMOTE_ERROR:
                log.error(
                    "REMOTE_ERROR running session negotiation: "
                        + negotiation.getErrorMessage()
                        + " at remote: "
                        + negotiation.getPeer().toString());
                break;
              case CANCEL:
                log.info("Session negotiation was cancelled locally");
                break;
              case REMOTE_CANCEL:
                log.info(
                    "Session negotiation was cancelled by remote: "
                        + negotiation.getPeer().toString());
                break;
            }
          }
        });
  }

  @Override
  public void handleIncomingSessionNegotiation(final IncomingSessionNegotiation negotiation) {

    sessionExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            negotiation.localCancel(
                "Server does not accept session invites",
                NegotiationTools.CancelOption.NOTIFY_PEER);
            negotiation.accept(new NullProgressMonitor());
          }
        });
  }

  @Override
  public void handleOutgoingProjectNegotiation(
      final AbstractOutgoingProjectNegotiation negotiation) {

    projectExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            ProjectNegotiation.Status status;
            if (ServerConfig.isInteractive()) {
              status = negotiation.run(new ConsoleProgressIndicator(System.out));
            } else {
              status = negotiation.run(new NullProgressMonitor());
            }

            if (status != ProjectNegotiation.Status.OK)
              handleErrorStatus(status, negotiation.getErrorMessage(), negotiation.getPeer());
          }
        });
  }

  @Override
  public void handleIncomingProjectNegotiation(
      final AbstractIncomingProjectNegotiation negotiation) {

    Map<String, IProject> projectMapping = new HashMap<>();

    for (ProjectNegotiationData data : negotiation.getProjectNegotiationData()) {
      String projectName = data.getProjectName();
      IProject project = workspace.getProject(projectName);

      if (!project.exists()) {
        try {
          ((ServerProjectImpl) project).create();
        } catch (IOException e) {
          projectMapping.clear();
          negotiation.localCancel(
              "Error creating project folder", NegotiationTools.CancelOption.NOTIFY_PEER);
          break;
        }
      }

      projectMapping.put(data.getProjectID(), project);
    }

    projectExecutor.execute(
        new Runnable() {
          @Override
          public void run() {
            ProjectNegotiation.Status status;
            if (ServerConfig.isInteractive()) {
              status = negotiation.run(projectMapping, new ConsoleProgressIndicator(System.out));
            } else {
              status = negotiation.run(projectMapping, new NullProgressMonitor());
            }

            if (status != ProjectNegotiation.Status.OK)
              handleErrorStatus(status, negotiation.getErrorMessage(), negotiation.getPeer());
          }
        });
  }

  private void handleErrorStatus(ProjectNegotiation.Status status, String errorMessage, JID peer) {
    switch (status) {
      case ERROR:
        log.error("ERROR running project negotiation: " + errorMessage);
        break;
      case REMOTE_ERROR:
        log.error(
            "REMOTE_ERROR running project negotiation: "
                + errorMessage
                + " at remote: "
                + peer.toString());
        break;
      case CANCEL:
        log.info("Project negotiation was cancelled locally");
        break;
      case REMOTE_CANCEL:
        log.info("Project negotiation was cancelled by remote: " + peer.toString());
        break;
    }
  }
}
