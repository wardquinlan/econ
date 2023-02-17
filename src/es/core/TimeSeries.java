package es.core;

import java.util.ArrayList;

public class TimeSeries {
  public static final int NULL = 0;
  public static final int FLOAT = 1;
  public static final int BOOLEAN = 2;
  public static final int DATE = 3;
  
  private int type;
  // base series are created via create(), load(), imports ... that type of thing.
  // They signal that meta data also has to be copied.
  private boolean base = false;
  private Integer id;
  private String name;
  private String title;
  private String source;
  private String sourceId;
  private String notes;
  private ArrayList<TimeSeriesData> timeSeriesDataList = new ArrayList<>();
  
  public TimeSeries(int type) {
    this.type = type;
  }
  
  public int getType() {
    return type;
  }
  
  public boolean isBase() {
    return base;
  }

  public void setBase(boolean base) {
    this.base = base;
  }

  public String getTypeAsString() {
    switch(type) {
    case FLOAT:
      return "Float";
    case BOOLEAN:
      return "Boolean";
    case DATE:
      return "Date";
    case NULL:
      return "";
    default:
      Utils.ASSERT(false, "time series has undefined type: " + type);
      return null;
    }
  }
  
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
