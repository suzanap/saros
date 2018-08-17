package de.fu_berlin.inf.dpp.stf;

import com.intellij.openapi.project.Project;
import de.fu_berlin.inf.dpp.intellij.SarosComponent;

public class SarosComponentSTF extends SarosComponent {

    public SarosComponentSTF(Project project) {
        super(project);
        StartupSarosSTF startupSarosSTF = new StartupSarosSTF();
        startupSarosSTF.earlyStartup();
    }
}
