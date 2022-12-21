package econ;

public class TimeSeries {
  private int id;
  private String name;
  private String title;
  private String sourceOrg;
  private String sourceName;
  private String notes;
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getSourceOrg() {
    return sourceOrg;
  }
  
  public void setSourceOrg(String sourceOrg) {
    this.sourceOrg = sourceOrg;
  }
  
  public String getSourceName() {
    return sourceName;
  }
  
  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }
  
  public String getNotes() {
    return notes;
  }
  
  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "[" + id + ", " + name + ", " + title + ", " + sourceOrg + ", " + sourceName + "]"; 
  }
}
