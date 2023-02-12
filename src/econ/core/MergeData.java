package econ.core;

public class MergeData {
  private TimeSeries timeSeriesInsert = new TimeSeries(TimeSeries.FLOAT);
  private TimeSeries timeSeriesUpdate = new TimeSeries(TimeSeries.FLOAT);
  private TimeSeries timeSeriesDelete = new TimeSeries(TimeSeries.FLOAT);
  
  public TimeSeries getTimeSeriesInsert() {
    return timeSeriesInsert;
  }
  
  public TimeSeries getTimeSeriesUpdate() {
    return timeSeriesUpdate;
  }
  
  public TimeSeries getTimeSeriesDelete() {
    return timeSeriesDelete;
  }
}
