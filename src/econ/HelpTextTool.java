package econ;

import java.util.List;

public class HelpTextTool {
  public void showHelp(List<Object> params) {
    System.out.println("Econ version 0.10");
    System.out.println("usage:\n");
    System.out.println("int create(int id, String name, String title, String sourceOrg[, String sourceName]);");
    System.out.println("  creates a new series in the database with empty notes, a possibly empty source name, and no series data");
    System.out.println("  returns: 0\n");
    
    System.out.println("int delete(Object object[, String date]);");
    System.out.println("  deletes from the database:");
    System.out.println("  - an entire series, using 'object' as either an id or a name");
    System.out.println("  - the single series data point associated with 'date', using 'object' as either an id or a name");
    System.out.println("  note: 'settings.confirm' must == 1 for this command to work");
    System.out.println("  returns: 0\n");
    
    System.out.println("String getNotes(Series series);");
    System.out.println("  gets notes from 'series' in memory");
    System.out.println("  returns: String, or null if no notes attached to 'series'\n");
    
    System.out.println("int import(String path[, String name]);");
    System.out.println("  imports Quote data from 'path' into the database:");
    System.out.println("  - filters on 'name' if present");
    System.out.println("  - otherwise imports all data");
    System.out.println("  note: 'settings.confirm' must == 1 for this command to work");
    System.out.println("  returns: 0\n");
    
    System.out.println("int insert(Object object, String date, float value);");
    System.out.println("  inserts series data into the database, using 'object' as either an id or a name");
    System.out.println("  returns: 0\n");
    
    System.out.println("int list([Object object]);");
    System.out.println("  list from the database:");
    System.out.println("  - all series)");
    System.out.println("  - series data associated with 'object' as either an id or a name");
    System.out.println("  returns: 0\n");
    
    System.out.println("Series load(Object object);");
    System.out.println("  loads a series from the database into memory, using 'object' as either an id or a name");
    System.out.println("  returns: Series, or null if not found\n");
    
    System.out.println("int notes(Object object);");
    System.out.println("  prints a series' notes from the database, using 'object' as either an id or a name");
    System.out.println("  returns: 0\n");
    
    System.out.println("int plot(String filename);");
    System.out.println("  plots series as defined in the context file 'filename'");
    System.out.println("  returns: 0\n");
    
    System.out.println("Series printData(Series series);");
    System.out.println("  prints series data for 'series' in memory");
    System.out.println("  returns: 'series'\n");
  }
}
