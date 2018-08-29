package de.fu_berlin.inf.dpp.stf;

import com.intellij.openapi.project.Project;
import de.fu_berlin.inf.dpp.context.IContainerContext;
import de.fu_berlin.inf.dpp.intellij.IntellijProjectLifecycle;
import de.fu_berlin.inf.dpp.stf.server.STFController;
import de.fu_berlin.inf.dpp.util.ThreadUtils;
import org.apache.log4j.Logger;

/**
 * Component to start the Saros-Test-Framework
 */
public class SarosComponentSTF implements
    com.intellij.openapi.components.ProjectComponent {

    private static final Logger LOG = Logger.getLogger(SarosComponentSTF.class);

    public SarosComponentSTF(final Project project) {
        Integer port = Integer.getInteger("de.fu_berlin.inf.dpp.testmode");
        IContainerContext context = IntellijProjectLifecycle.getInstance(
            project).getSarosContext();

        startSTFController(port, context);
    }

    private void startSTFController(final Integer port,
        final IContainerContext context) {
        if (port != null && port > 0 && port <= 65535) {
            LOG.info("starting STF controller on port " + port);
            ThreadUtils.runSafeAsync("dpp-stf-startup", LOG, new Runnable() {
                @Override
                public void run() {
                    try {
                        STFController.start(port, context);
                    } catch (Exception e) {
                        LOG.error("starting STF controller failed", e);
                    }
                }
            });

        } else if (port != null) {
            LOG.error("could not start STF controller: port " + port
                + " is not a valid port number");
        }
    }
}
