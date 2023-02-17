package es.core;

public class LogScaler implements Scaler {
  @Override
  public float scale(float value) throws Exception {
    if (value <= 0) {
      throw new Exception("can't scale zero or negative values");
    }
    return (float) Math.log((float) value);
  }
}
