package es.gui;

import java.awt.Color;

import es.core.TimeSeries;
import es.parser.SymbolTable;

public class Series extends GUIObject {
  public static final int LINE = 1;
  public static final int BACKGROUND = 2;
  
  private TimeSeries timeSeries;
  private int type = LINE;
  private Color color;

  public Series(SymbolTable symbolTable) {
    super(symbolTable);
    color = new Color((int) symbolTable.get("defaults.series.linecolor0").getValue());
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
