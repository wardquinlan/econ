package econ.core;

import java.util.ArrayList;

public class TimeSeries {
  private Integer id;
  private String name;
  private String title;
  private String source;
  private String sourceId;
  private String notes;
  private ArrayList<TimeSeriesData> timeSeriesDataList = new ArrayList<>();
  
  public int size() {
    return timeSeriesDataList.size();
  }
  
  public TimeSeriesData get(int index) {
    return timeSeriesDataList.get(index);
  }
  
  public void add(TimeSeriesData timeSeriesData) {
    timeSeriesDataList.add(timeSeriesData);
  }
  
  public Integer getId() {
    return id;
  }
  
  public void setId(Integer id) {
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
  
  public String getSource() {
    return source;
  }
  
  public void setSource(String source) {
    this.source = source;
  }
  
  public String getSourceId() {
    return sourceId;
  }
  
  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }
  
  public String getNotes() {
    return notes;
  }
  
  public void setNotes(String notes) {
    this.notes = notes;
  }

  public ArrayList<TimeSeriesData> getTimeSeriesDataList() {
    return timeSeriesDataList;
  }
  
  @Override
  public String toString() {
    return "[" + id + ", " + name + ", " + title + ", " + source + ", " + Utils.stringWithNULL(sourceId) + " (count=" + timeSeriesDataList.size() + ")]"; 
  }
  
  public String toStringVerbose() {
    StringBuffer sb = new StringBuffer(toString() + "\n");
    int i = 0;
    for (TimeSeriesData data: timeSeriesDataList) {
      sb.append(i);
      sb.append(": " + data + "\n");
      i++;
    }
    return sb.toString();
  }
}