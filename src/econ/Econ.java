package econ;

import java.util.ArrayList;

public class Econ {
  private String script;
  private ArrayList<Chart> charts = new ArrayList<>();

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }
  
  public ArrayList<Chart> getCharts() {
    return charts;
  }
}
