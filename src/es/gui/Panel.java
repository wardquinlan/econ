package es.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import es.parser.Symbol;

public class Panel extends GUIObject {
  public static final int FREQUENCY_NONE = 0;
  public static final int FREQUENCY_DAYS = 1;
  public static final int FREQUENCY_MONTHS = 2;
  public static final int FREQUENCY_YEARS = 3;
  
  private Color backgroundColor;
  private Color fontColor;
  private int dxIncr;
  private int gridLineTextWidth;
  private String fontName;
  private int fontSize;
  private int frequency;
  private String label;
  private ArrayList<Chart> charts = new ArrayList<>();

  public Panel(Map<String, Symbol> symbolTable) {
    super(symbolTable);
    backgroundColor = new Color((int) symbolTable.get("defaults.panel.backgroundcolor").getValue());
    fontColor = new Color((int) symbolTable.get("defaults.panel.fontcolor").getValue());
    dxIncr = (int) symbolTable.get("defaults.panel.dxincr").getValue();
    gridLineTextWidth = (int) symbolTable.get("defaults.panel.gridlinetextwidth").getValue();
    fontName = (String) symbolTable.get("defaults.panel.fontname").getValue();
    fontSize = (int) symbolTable.get("defaults.panel.fontsize").getValue();
    frequency = (int) symbolTable.get("defaults.panel.frequency").getValue();
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

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
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

  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public Color getFontColor() {
    return fontColor;
  }

  public void setFontColor(Color fontColor) {
    this.fontColor = fontColor;
  }
}
