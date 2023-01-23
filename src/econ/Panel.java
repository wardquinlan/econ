package econ;

import java.util.ArrayList;
import java.util.Map;

import econ.parser.Symbol;

public class Panel extends GUIObject {
  private String label;
  private ArrayList<Chart> charts = new ArrayList<>();

  public Panel(Map<String, Symbol> symbolTable) {
    super(symbolTable);
  }
  
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
