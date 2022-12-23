package econ;

import java.util.ArrayList;

public class Chart {
  private String label;
  private ArrayList<Double> gridlines = new ArrayList<>();
  private ArrayList<Series> series = new ArrayList<>();
  
  public String getLabel() {
    return label;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public ArrayList<Double> getGridlines() {
    return gridlines;
  }
  
  public ArrayList<Series> getSeries() {
    return series;
  }
}
