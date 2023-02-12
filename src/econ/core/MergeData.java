package econ.core;

public class MergeData {
  private TimeSeries timeSeriesInsert = new TimeSeries(TimeSeries.TYPE_FLOAT);
  private TimeSeries timeSeriesUpdate = new TimeSeries(TimeSeries.TYPE_FLOAT);
  private TimeSeries timeSeriesDelete = new TimeSeries(TimeSeries.TYPE_FLOAT);
  
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
