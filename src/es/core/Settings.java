package es.core;

import org.apache.commons.cli.CommandLine;

public class Settings {
  private boolean testFunctions = false;
  private boolean suppressAutoload = false;
  private boolean admin = false;
  private String version;
  
  private static Settings instance = new Settings();
  
  private Settings() {
  }

  public static Settings getInstance() {
    return instance;
  }
  
  public void setOptions(CommandLine cmd) {
    testFunctions = cmd.hasOption("test");
    suppressAutoload = cmd.hasOption("suppress-autoload");
    admin = cmd.hasOption("admin");
  }

  public boolean testFunctions() {
    return testFunctions;
  }

  public boolean suppressAutoload() {
    return suppressAutoload;
  }

  public boolean isAdmin() {
    return admin;
  }
  
  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }
}
