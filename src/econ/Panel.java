package econ;

import java.util.ArrayList;
import java.util.Map;

import econ.parser.Symbol;

public class Panel extends GUIObject {
  public static final int DATE_FREQUENCY_NONE = 0;
  public static final int DATE_FREQUENCY_DAYS = 1;
  public static final int DATE_FREQUENCY_MONTHS = 2;
  public static final int DATE_FREQUENCY_YEARS = 3;
  
  private int backgroundColor;
  private int dxIncr;
  private int gridLineTextWidth;
  private String fontName;
  private int fontSize;
  private int dateFrequency;
  private String label;
  private ArrayList<Chart> charts = new ArrayList<>();

  public Panel(Map<String, Symbol> symbolTable) {
    super(symbolTable);
    backgroundColor = (int) symbolTable.get("defaults.panel.backgroundcolor").getValue();
    dxIncr = (int) symbolTable.get("defaults.panel.dxincr").getValue();
    gridLineTextWidth = (int) symbolTable.get("defaults.panel.gridlinetextwidth").getValue();
    fontName = (String) symbolTable.get("defaults.panel.fontname").getValue();
    fontSize = (int) symbolTable.get("defaults.panel.fontsize").getValue();
    dateFrequency = (int) symbolTable.get("defaults.panel.datefrequency").getValue();
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

  public int getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public int getDxIncr() {
    return dxIncr;
  }

  public void setDxIncr(int dxIncr) {
    this.dxIncr = dxIncr;
  }

  public int getGridLineTextWidth() {
    return gridLineTextWidth;
  }

  public void setGridLineTextWidth(int gridLineTextWidth) {
    this.gridLineTextWidth = gridLineTextWidth;
  }

  public String getFontName() {
    return fontName;
  }

  public void setFontName(String fontName) {
    this.fontName = fontName;
  }

  public int getFontSize() {
    return fontSize;
  }

  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }

  public int getDateFrequency() {
    return dateFrequency;
  }

  public void setDateFrequency(int dateFrequency) {
    this.dateFrequency = dateFrequency;
  }
}
