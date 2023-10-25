package es.gui;

import java.awt.Color;
import java.util.ArrayList;

import es.parser.SymbolTable;

public class Chart extends GUIObject {
  public static final int SCALE_TYPE_LINEAR = 0;
  public static final int SCALE_TYPE_LOG = 1;
  private Color backgroundColor;
  private Color lineColor;
  private Color rectColor;
  private String label;
  private int nGridLines;
  private int span = 100;
  private Scaler scaler;
  private ArrayList<Float> gridlines = new ArrayList<>();
  private ArrayList<Series> series = new ArrayList<>();
  
  public Chart(SymbolTable symbolTable) throws Exception {
    super(symbolTable);
    backgroundColor = new Color((int) symbolTable.get("defaults.chart.backgroundcolor").getValue());
    lineColor = new Color((int) symbolTable.get("defaults.chart.linecolor").getValue());
    rectColor = new Color((int) symbolTable.get("defaults.chart.rectcolor").getValue());
    nGridLines = (int) symbolTable.get("defaults.chart.ngridlines").getValue();
    int type = (int) symbolTable.get("defaults.chart.scaletype").getValue();
    if (type == SCALE_TYPE_LINEAR) {
      scaler = new LinearScaler();
    } else if (type == SCALE_TYPE_LOG) {
      scaler = new LogScaler();
    } else {
      throw new Exception("invalid scale type: " + type);
    }
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

  public Scaler getScaler() {
    return scaler;
  }

  public void setScaler(Scaler scaler) {
    this.scaler = scaler;
  }
}
