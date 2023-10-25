package es.core;

import java.util.ArrayList;
import java.util.Objects;

public class TimeSeries {
  public static final int NULL = 0;
  public static final int FLOAT = 1;
  public static final int BOOLEAN = 2;
  public static final int DATE = 3;
  
  private int type;
  private Integer id;
  private String name;
  private String title;
  private String source;
  private String sourceId;
  private String notes;
  private String units;
  private String unitsShort;
  private String frequency;
  private String frequencyShort;
  private ArrayList<TimeSeriesData> timeSeriesDataList = new ArrayList<>();
  
  public TimeSeries(int type) {
    this.type = type;
  }
  
  public int getType() {
    return type;
  }
  
  public String getTypeAsString() {
    switch(type) {
    case FLOAT:
      return "float";
    case BOOLEAN:
      return "boolean";
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
  
  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public String getFrequencyShort() {
    return frequencyShort;
  }

  public void setFrequencyShort(String frequencyShort) {
    this.frequencyShort = frequencyShort;
  }

  public String getUnitsShort() {
    return unitsShort;
  }

  public void setUnitsShort(String unitsShort) {
    this.unitsShort = unitsShort;
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
  
  @Override
  public int hashCode() {
    return Objects.hash(frequency, frequencyShort, id, name, notes, source, sourceId, timeSeriesDataList, title, type,
        units, unitsShort);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TimeSeries other = (TimeSeries) obj;
    return Objects.equals(frequency, other.frequency) && Objects.equals(frequencyShort, other.frequencyShort)
        && Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(notes, other.notes)
        && Objects.equals(source, other.source) && Objects.equals(sourceId, other.sourceId)
        && Objects.equals(timeSeriesDataList, other.timeSeriesDataList) && Objects.equals(title, other.title)
        && type == other.type && Objects.equals(units, other.units) && Objects.equals(unitsShort, other.unitsShort);
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
