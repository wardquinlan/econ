package econ;

import java.util.ArrayList;

public class Chart {
  private String id;
  private String title;
  private ArrayList<Double> gridlines = new ArrayList<>();
  private ArrayList<Series> series = new ArrayList<>();
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public ArrayList<Double> getGridlines() {
    return gridlines;
  }
  
  public void setGridlines(ArrayList<Double> gridlines) {
    this.gridlines = gridlines;
  }
  
  public ArrayList<Series> getSeries() {
    return series;
  }
  
  public void setSeries(ArrayList<Series> series) {
    this.series = series;
  }
}
