package econ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EconContext {
  private String script;
  private ArrayList<Chart> charts = new ArrayList<>();
  private Map<String, Symbol> symbolTable = new HashMap<>();

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }
  
  public ArrayList<Chart> getCharts() {
    return charts;
  }

  public Map<String, Symbol> getSymbolTable() {
    return symbolTable;
  }
}
