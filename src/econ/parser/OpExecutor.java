package econ.parser;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;

public class OpExecutor {
  private Operator operator;
  
  public OpExecutor(Operator operator) {
    this.operator = operator;
  }
  
  public Float exec(Integer val1, Float val2) {
    return operator.exec(val1.floatValue(), val2);
  }
  
  public Float exec(Float val1, Integer val2) {
    return operator.exec(val1, val2.floatValue());
  }
  
  public Float exec(Float val1, Float val2) {
    return operator.exec(val1, val2);
  }
  
  public TimeSeries exec(TimeSeries timeSeries1, Integer val) {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(timeSeriesData1.getValue(), val.floatValue()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }

  public TimeSeries exec(Integer val, TimeSeries timeSeries1) {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(val.floatValue(), timeSeriesData1.getValue()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }

  public TimeSeries exec(TimeSeries timeSeries1, Float val) {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(timeSeriesData1.getValue(), val));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }

  public TimeSeries exec(Float val, TimeSeries timeSeries1) {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(val, timeSeriesData1.getValue()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }
  
  public TimeSeries exec(TimeSeries timeSeries1, TimeSeries timeSeries2) {
    TimeSeries timeSeriesCollapsed = Utils.collapse(timeSeries1, timeSeries2);
    timeSeries1 = Utils.normalize(timeSeriesCollapsed, timeSeries1);
    timeSeries2 = Utils.normalize(timeSeriesCollapsed, timeSeries2);
    TimeSeries timeSeries = new TimeSeries();
    for (int i = 0; i < timeSeriesCollapsed.getTimeSeriesDataList().size(); i++) {
      TimeSeriesData timeSeriesData1 = timeSeries1.getTimeSeriesDataList().get(i);
      TimeSeriesData timeSeriesData2 = timeSeries2.getTimeSeriesDataList().get(i);
      if (timeSeriesData1.getValue() != null && timeSeriesData2.getValue() != null) {
        Utils.ASSERT(timeSeriesData1.getDate().equals(timeSeriesData2.getDate()), "invalid dates after normalize");
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(operator.exec(timeSeriesData1.getValue(), timeSeriesData2.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
}

