package de.fu_berlin.inf.dpp.server.filesystem;

import java.io.IOException;

import de.fu_berlin.inf.dpp.exceptions.OperationCanceledException;
import de.fu_berlin.inf.dpp.filesystem.IPath;
import de.fu_berlin.inf.dpp.filesystem.IProject;
import de.fu_berlin.inf.dpp.filesystem.IReferencePoint;
import de.fu_berlin.inf.dpp.filesystem.IWorkspace;
import de.fu_berlin.inf.dpp.filesystem.IWorkspaceRunnable;
import de.fu_berlin.inf.dpp.monitoring.NullProgressMonitor;
import de.fu_berlin.inf.dpp.session.IReferencePointManager;

/**
 * Server implementation of the {@link IWorkspace} interface.
 */
public class ServerWorkspaceImpl implements IWorkspace {

    private IPath location;

    /**
     * Creates a ServerWorkspaceImpl.
     * 
     * @param location
     *            the workspace's absolute root location in the file system
     */
    public ServerWorkspaceImpl(IPath location) {
        this.location = location;
    }

    @Override
    public IPath getLocation() {
        return location;
    }

    @Override
    public IProject getProject(String name) {
        return new ServerProjectImpl(this, name);
    }

    @Override
    public void run(IWorkspaceRunnable runnable) throws IOException,
        OperationCanceledException {

        run(runnable, null, null);
    }

    @Override
    public void run(IWorkspaceRunnable runnable, IReferencePoint[] referencePoints, IReferencePointManager referencePointManager)
        throws IOException, OperationCanceledException {

        /*
         * TODO Implement a watchdog to interrupt runnables that run too long
         */
        synchronized (this) {
            runnable.run(new NullProgressMonitor());
        }
    }
}
