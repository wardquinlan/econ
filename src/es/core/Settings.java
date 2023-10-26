package es.core;

import org.apache.commons.cli.CommandLine;

public class Settings {
  private boolean testMode = false;
  private boolean suppressAutoload = false;
  private boolean admin = false;
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
    suppressAutoload = cmd.hasOption("suppress-autoload");
    admin = cmd.hasOption("admin");
    promptSuppressed = cmd.hasOption("suppress-prompt");
  }

  public boolean isTestMode() {
    return testMode;
  }

  public boolean suppressAutoload() {
    return suppressAutoload;
  }

  public boolean isAdmin() {
    return admin;
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
