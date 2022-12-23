package econ;

import java.util.ArrayList;

public class Panel {
  private String label;
  private ArrayList<Chart> charts = new ArrayList<>();

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public ArrayList<Chart> getCharts() {
    return charts;
  }
}
