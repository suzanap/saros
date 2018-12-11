package de.fu_berlin.inf.dpp.intellij.project.filesystem;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.LocalFileSystem;
import de.fu_berlin.inf.dpp.exceptions.OperationCanceledException;
import de.fu_berlin.inf.dpp.filesystem.IPath;
import de.fu_berlin.inf.dpp.filesystem.IProject;
import de.fu_berlin.inf.dpp.filesystem.IReferencePoint;
import de.fu_berlin.inf.dpp.filesystem.IWorkspace;
import de.fu_berlin.inf.dpp.filesystem.IWorkspaceRunnable;
import de.fu_berlin.inf.dpp.intellij.filesystem.Filesystem;
import de.fu_berlin.inf.dpp.intellij.filesystem.IntelliJProjectImpl;
import de.fu_berlin.inf.dpp.intellij.project.FileSystemChangeListener;
import de.fu_berlin.inf.dpp.monitoring.NullProgressMonitor;
import de.fu_berlin.inf.dpp.session.IReferencePointManager;
import java.io.IOException;
import org.apache.log4j.Logger;

public class IntelliJWorkspaceImpl implements IWorkspace {
  public static final Logger LOG = Logger.getLogger(IntelliJWorkspaceImpl.class);

  private LocalFileSystem fileSystem;

  private Project project;

  public IntelliJWorkspaceImpl(Project project) {
    this.project = project;
    fileSystem = LocalFileSystem.getInstance();
    fileSystem.addRootToWatch(project.getBasePath(), true);
  }

  @Override
  public void run(IWorkspaceRunnable procedure, IReferencePointManager referencePointManager)
      throws IOException, OperationCanceledException {
    procedure.run(new NullProgressMonitor());
  }

  @Override
  public void run(
      IWorkspaceRunnable runnable,
      IReferencePoint[] resources,
      IReferencePointManager referencePointManager)
      throws IOException, OperationCanceledException {
    run(runnable, referencePointManager);
  }

  @Override
  public IProject getProject(final String moduleName) {
    Module module =
        Filesystem.runReadAction(
            new Computable<Module>() {
              @Override
              public Module compute() {
                return ModuleManager.getInstance(project).findModuleByName(moduleName);
              }
            });

    return module != null ? new IntelliJProjectImpl(module) : null;
  }

  @Override
  public IPath getLocation() {
    return IntelliJPathImpl.fromString(project.getBasePath());
  }

  public void addResourceListener(FileSystemChangeListener listener) {
    fileSystem.addVirtualFileListener(listener);
  }

  public void removeResourceListener(FileSystemChangeListener listener) {
    fileSystem.removeVirtualFileListener(listener);
  }
}
