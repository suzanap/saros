package saros.ui.wizards;

import org.bitlet.weupnp.GatewayDevice;
import org.eclipse.jface.preference.IPreferenceStore;
import org.picocontainer.annotations.Inject;
import saros.SarosPluginContext;
import saros.editor.colorstorage.UserColorID;
import saros.feedback.ErrorLogManager;
import saros.feedback.FeedbackManager;
import saros.feedback.StatisticManagerConfiguration;
import saros.preferences.PreferenceConstants;
import saros.ui.ImageManager;
import saros.ui.Messages;
import saros.ui.wizards.pages.ColorChooserWizardPage;
import saros.ui.wizards.pages.ConfigurationSettingsWizardPage;
import saros.ui.wizards.pages.ConfigurationSummaryWizardPage;

/**
 * A wizard to configure Saros (XMPP account, network settings, statistic submission).
 *
 * @author bkahlert
 */
public class ConfigurationWizard extends AddXMPPAccountWizard {

  private final ConfigurationSettingsWizardPage configurationSettingsWizardPage =
      new ConfigurationSettingsWizardPage();

  private final ConfigurationSummaryWizardPage configurationSummaryWizardPage =
      new ConfigurationSummaryWizardPage(
          enterXMPPAccountWizardPage, configurationSettingsWizardPage);

  private final ColorChooserWizardPage colorChooserWizardPage = new ColorChooserWizardPage(false);

  @Inject private IPreferenceStore preferences;

  public ConfigurationWizard() {
    SarosPluginContext.initComponent(this);

    setWindowTitle("Saros Configuration");
    setDefaultPageImageDescriptor(ImageManager.WIZBAN_CONFIGURATION);
    colorChooserWizardPage.setTitle(Messages.ChangeColorWizardPage_configuration_mode_title);

    colorChooserWizardPage.setDescription(
        Messages.ChangeColorWizardPage_configuration_mode_description);
  }

  @Override
  public void addPages() {
    super.addPages();
    addPage(configurationSettingsWizardPage);
    addPage(colorChooserWizardPage);
    addPage(configurationSummaryWizardPage);
  }

  @Override
  public boolean performFinish() {
    setConfiguration();
    return super.performFinish();
  }

  @Override
  public boolean canFinish() {
    return getContainer().getCurrentPage() == configurationSummaryWizardPage;
  }

  /**
   * Stores the Saros configuration on the base of the {@link ConfigurationSettingsWizardPage} and
   * {@link ColorChooserWizardPage}into the PreferenceStore.
   */
  protected void setConfiguration() {

    String skypeUsername =
        (configurationSettingsWizardPage.isSkypeUsage())
            ? configurationSettingsWizardPage.getSkypeUsername()
            : "";

    preferences.setValue(
        PreferenceConstants.AUTO_CONNECT, configurationSettingsWizardPage.isAutoConnect());

    preferences.setValue(PreferenceConstants.SKYPE_USERNAME, skypeUsername);

    int colorID = colorChooserWizardPage.getSelectedColor();

    if (UserColorID.isValid(colorID))
      preferences.setValue(PreferenceConstants.FAVORITE_SESSION_COLOR_ID, colorID);

    if (FeedbackManager.isFeedbackFeatureRequired()) {
      boolean statisticSubmissionAllowed =
          configurationSettingsWizardPage.isStatisticSubmissionAllowed();
      boolean errorLogSubmissionAllowed =
          configurationSettingsWizardPage.isErrorLogSubmissionAllowed();

      StatisticManagerConfiguration.setStatisticSubmissionAllowed(statisticSubmissionAllowed);
      ErrorLogManager.setErrorLogSubmissionAllowed(errorLogSubmissionAllowed);
    }

    GatewayDevice gatewayDevice = configurationSettingsWizardPage.getPortmappingDevice();

    if (gatewayDevice != null)
      preferences.setValue(PreferenceConstants.AUTO_PORTMAPPING_DEVICEID, gatewayDevice.getUSN());
  }
}
