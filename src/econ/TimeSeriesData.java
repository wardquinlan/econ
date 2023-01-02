package econ;

import java.util.Date;

public class TimeSeriesData {
  private Integer id;
  private Date date;
  private Float value;
  
  public int compareTo(TimeSeriesData timeSeriesData) {
    return date.compareTo(timeSeriesData.getDate());
  }
  
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public Float getValue() {
    return value;
  }
  
  public void setValue(Float value) {
    this.value = value;
  }
  
  @Override
  public String toString() {
    return "[" + date + ", " + value + "]";
  }
}
