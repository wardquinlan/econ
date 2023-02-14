package econ.core;

import org.apache.commons.cli.CommandLine;

public class Settings {
  private boolean mergeDeletes = false;
  private boolean mergeUpdates = false;
  private boolean mergeMetaData = false;
  private boolean testFunctions = false;
  private boolean suppressAutoload = false;
  
  private static Settings instance = new Settings();
  
  private Settings() {
  }

  public static Settings getInstance() {
    return instance;
  }
  
  public void setOptions(CommandLine cmd) {
    mergeDeletes = cmd.hasOption("merge-deletes");
    mergeUpdates = cmd.hasOption("merge-updates");
    mergeMetaData = cmd.hasOption("merge-metadata");
    testFunctions = cmd.hasOption("test");
    suppressAutoload = cmd.hasOption("suppress-autoload");
  }

  public boolean mergeDeletes() {
    return mergeDeletes;
  }

  public boolean mergeUpdates() {
    return mergeUpdates;
  }

  public boolean mergeMetaData() {
    return mergeMetaData;
  }

  public boolean testFunctions() {
    return testFunctions;
  }

  public boolean suppressAutoload() {
    return suppressAutoload;
  }
}
