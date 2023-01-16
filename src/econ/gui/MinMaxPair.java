package econ.gui;

public class MinMaxPair {
  private float minValue = Float.MAX_VALUE;
  private float maxValue = Float.MIN_VALUE;
  
  public float getMinValue() {
    return minValue;
  }
  
  public void setMinValue(float minValue) {
    this.minValue = minValue;
  }
  
  public float getMaxValue() {
    return maxValue;
  }
  
  public void setMaxValue(float maxValue) {
    this.maxValue = maxValue;
  }
  
  @Override
  public String toString() {
    return "[minValue=" + minValue + ", maxValue=" + maxValue + "]";
  }
}
