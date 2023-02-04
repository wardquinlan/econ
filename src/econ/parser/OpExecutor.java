package econ.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;

public class OpExecutor {
  private static final Log log = LogFactory.getFactory().getInstance(OpExecutor.class);
  private Operator operator;
  
  public OpExecutor(Operator operator) {
    this.operator = operator;
  }
  
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof String) {
      return exec((String) val1, val2);
    } else if (val1 instanceof Integer && val2 instanceof Integer) {
      return exec((Integer) val1, (Integer) val2);
    } else if (val1 instanceof Integer && val2 instanceof Float) {
      return exec((Integer) val1, (Float) val2);
    } else if (val1 instanceof Float && val2 instanceof Integer) {
      return exec((Float) val1, (Integer) val2);
    } else if (val1 instanceof Float && val2 instanceof Float) {
      return exec((Float) val1, (Float) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof Integer) {
      return exec((TimeSeries) val1, (Integer) val2);
    } else if (val1 instanceof Integer && val2 instanceof TimeSeries) {
      return exec((Integer) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof Float) {
      return exec((TimeSeries) val1, (Float) val2);
    } else if (val1 instanceof Float && val2 instanceof TimeSeries) {
      return exec((Float) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof TimeSeries) {
      return exec((TimeSeries) val1, (TimeSeries) val2);
    } else {
      log.error("invalid expression");
      throw new Exception("syntax error");
    }
  }
  
  private Object exec(String val1, Object val2) throws Exception {
    return operator.exec(val1, val2);
  }
  
  private Object exec(Integer val1, Integer val2) throws Exception {
    return operator.exec(val1, val2);
  }
  
  private Object exec(Integer val1, Float val2) throws Exception {
    return operator.exec(val1.floatValue(), val2);
  }
  
  private Object exec(Float val1, Integer val2) throws Exception {
    return operator.exec(val1, val2.floatValue());
  }
  
  private Object exec(Float val1, Float val2) throws Exception {
    return operator.exec(val1, val2);
  }
  
  private Object exec(TimeSeries timeSeries1, Integer val) throws Exception {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(timeSeriesData1.getValue(), val.floatValue()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }

  private Object exec(Integer val, TimeSeries timeSeries1) throws Exception {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(val.floatValue(), timeSeriesData1.getValue()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }

  private Object exec(TimeSeries timeSeries1, Float val) throws Exception {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(timeSeriesData1.getValue(), val));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }

  private Object exec(Float val, TimeSeries timeSeries1) throws Exception {
    TimeSeries timeSeries = new TimeSeries();
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesData1.getDate());
      timeSeriesData.setValue(operator.exec(val, timeSeriesData1.getValue()));
      timeSeries.add(timeSeriesData);
    }
    return timeSeries;
  }
  
  private Object exec(TimeSeries timeSeries1, TimeSeries timeSeries2) throws Exception {
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

