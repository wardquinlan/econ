package econ;

import java.awt.Color;

public class Series {
  public static final int LINE = 1;
  public static final int BACKGROUND = 2;
  
  private TimeSeries timeSeries;
  private int type;
  private Color color;

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
