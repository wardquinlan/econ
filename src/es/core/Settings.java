package es.core;

import org.apache.commons.cli.CommandLine;

public class Settings {
  private boolean testMode = false;
  private boolean autoloadSuppressed = false;
  private boolean adminMode = false;
  private String version;
  private boolean promptSuppressed = false;
  
  private static Settings instance = new Settings();
  
  private Settings() {
  }

  public static Settings getInstance() {
    return instance;
  }
  
  public void setOptions(CommandLine cmd) {
    testMode = cmd.hasOption("test");
    autoloadSuppressed = cmd.hasOption("suppress-autoload");
    adminMode = cmd.hasOption("admin");
    promptSuppressed = cmd.hasOption("suppress-prompt");
  }

  public boolean isTestMode() {
    return testMode;
  }

  public boolean isAutoloadSuppressed() {
    return autoloadSuppressed;
  }

  public boolean isAdminMode() {
    return adminMode;
  }
  
  public boolean isPromptSuppressed() {
    return promptSuppressed;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }
}
