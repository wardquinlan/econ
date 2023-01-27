package econ.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import econ.parser.Symbol;

public class Chart extends GUIObject {
  public static final int SCALE_LINEAR = 0;
  public static final int SCALE_LOG = 1;
  
  private Color backgroundColor;
  private Color lineColor;
  private Color rectColor;
  private String label;
  private int nGridLines;
  private int span = 100;
  private int scale = SCALE_LINEAR;
  private ArrayList<Float> gridlines = new ArrayList<>();
  private ArrayList<Series> series = new ArrayList<>();
  
  public Chart(Map<String, Symbol> symbolTable) {
    super(symbolTable);
    backgroundColor = new Color((int) symbolTable.get("defaults.chart.backgroundcolor").getValue());
    lineColor = new Color((int) symbolTable.get("defaults.chart.linecolor").getValue());
    rectColor = new Color((int) symbolTable.get("defaults.chart.rectcolor").getValue());
    nGridLines = (int) symbolTable.get("defaults.chart.ngridlines").getValue();
  }
  
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

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Color getLineColor() {
    return lineColor;
  }

  public void setLineColor(Color lineColor) {
    this.lineColor = lineColor;
  }

  public Color getRectColor() {
    return rectColor;
  }

  public void setRectColor(Color rectColor) {
    this.rectColor = rectColor;
  }

  public int getNGridLines() {
    return nGridLines;
  }

  public void setNGridLines(int nGridLines) {
    this.nGridLines = nGridLines;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }
}
