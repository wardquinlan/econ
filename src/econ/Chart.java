package econ;

import java.util.ArrayList;

public class Chart {
  private String label;
  private int span = 100;
  private ArrayList<Float> gridlines = new ArrayList<>();
  private ArrayList<Series> series = new ArrayList<>();
  
  public String getLabel() {
    return label;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public ArrayList<Float> getGridlines() {
    return gridlines;
  }
  
  public ArrayList<Series> getSeries() {
    return series;
  }

  public int getSpan() {
    return span;
  }

  public void setSpan(int span) {
    this.span = span;
  }
}
