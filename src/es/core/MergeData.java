package es.core;

public class MergeData {
  private TimeSeries timeSeriesInsert = new TimeSeries(TimeSeries.FLOAT);
  private TimeSeries timeSeriesUpdate = new TimeSeries(TimeSeries.FLOAT);
  private TimeSeries timeSeriesDelete = new TimeSeries(TimeSeries.FLOAT);
  private TimeSeries timeSeriesMerge;
  
  public TimeSeries getTimeSeriesInsert() {
    return timeSeriesInsert;
  }
  
  public TimeSeries getTimeSeriesUpdate() {
    return timeSeriesUpdate;
  }
  
  public TimeSeries getTimeSeriesDelete() {
    return timeSeriesDelete;
  }

  public TimeSeries getTimeSeriesMerge() {
    return timeSeriesMerge;
  }

  public void setTimeSeriesMerge(TimeSeries timeSeriesMerge) {
    this.timeSeriesMerge = timeSeriesMerge;
  }

  public boolean isModified() {
    return timeSeriesInsert.getTimeSeriesDataList().size() > 0 ||
           timeSeriesUpdate.getTimeSeriesDataList().size() > 0 ||
           timeSeriesDelete.getTimeSeriesDataList().size() > 0 ||
           timeSeriesMerge != null;
  }
}
