package econ;

import java.util.Date;

public class TimeSeriesData {
  private Date date;
  private Double value;
  
  public int compareTo(TimeSeriesData timeSeriesData) {
    return date.compareTo(timeSeriesData.getDate());
  }
  
  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public Double getValue() {
    return value;
  }
  
  public void setValue(Double value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return "[" + date + ", " + value + "]";
  }
}
