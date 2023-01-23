package econ.gui;

import java.awt.Color;
import java.util.Map;

import econ.TimeSeries;
import econ.parser.Symbol;

public class Series extends GUIObject {
  public static final int LINE = 1;
  public static final int BACKGROUND = 2;
  
  private TimeSeries timeSeries;
  private int type = LINE;
  private Color color;

  public Series(Map<String, Symbol> symbolTable) {
    super(symbolTable);
    color = new Color((int) symbolTable.get("defaults.series.linecolor").getValue());
  }
  
  public TimeSeries getTimeSeries() {
    return timeSeries;
  }

  public void setTimeSeries(TimeSeries timeSeries) {
    this.timeSeries = timeSeries;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
