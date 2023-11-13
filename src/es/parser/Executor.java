package es.parser;

import java.util.Date;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;

public class Executor {
  private Operator operator;
  
  public Executor(Operator operator) {
    this.operator = operator;
  }
  
  public Object exec(Object val1) throws Exception {
    if (val1 instanceof TimeSeries) {
      TimeSeries timeSeries1 = (TimeSeries) val1;
      TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
      for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
        if (timeSeriesData1.getValue() != null) {
          TimeSeriesData timeSeriesData = new TimeSeriesData();
          timeSeriesData.setDate(timeSeriesData1.getDate());
          timeSeriesData.setValue(((UnaryOperator) operator).exec(timeSeriesData1.getValue()));
          timeSeries.add(timeSeriesData);
        }
      }
      return timeSeries;
    } else {
      return ((UnaryOperator) operator).exec(val1);
    }
  }
  
  public Object exec(Object val1, Object val2) throws Exception {
    if (val1 instanceof TimeSeries && val2 instanceof Integer) {
      return exec((TimeSeries) val1, (Integer) val2);
    } else if (val1 instanceof Integer && val2 instanceof TimeSeries) {
      return exec((Integer) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof Float) {
      return exec((TimeSeries) val1, (Float) val2);
    } else if (val1 instanceof Float && val2 instanceof TimeSeries) {
      return exec((Float) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof TimeSeries) {
      return exec((TimeSeries) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof String) {
      return exec((TimeSeries) val1, (String) val2);
    } else if (val1 instanceof String && val2 instanceof TimeSeries) {
      return exec((String) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof Date) {
      return exec((TimeSeries) val1, (Date) val2);
    } else if (val1 instanceof Date && val2 instanceof TimeSeries) {
      return exec((Date) val1, (TimeSeries) val2);
    } else if (val1 instanceof TimeSeries && val2 instanceof Boolean) {
      return exec((TimeSeries) val1, (Boolean) val2);
    } else if (val1 instanceof Boolean && val2 instanceof TimeSeries) {
      return exec((Boolean) val1, (TimeSeries) val2);
    } else {
      return ((BinaryOperator) operator).exec(val1, val2);
    }
  }

  private Object exec(TimeSeries timeSeries1, Boolean val) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(timeSeriesData1.getValue(), val));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }

  private Object exec(Boolean val, TimeSeries timeSeries1) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(val, timeSeriesData1.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
  
  private Object exec(TimeSeries timeSeries1, String val) throws Exception {
    Date date = Utils.DATE_FORMAT.parse(val);
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(timeSeriesData1.getValue(), date));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
  
  private Object exec(String val, TimeSeries timeSeries1) throws Exception {
    Date date = Utils.DATE_FORMAT.parse(val);
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(date, timeSeriesData1.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }

  private Object exec(TimeSeries timeSeries1, Date val) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(timeSeriesData1.getValue(), val));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
  
  private Object exec(Date val, TimeSeries timeSeries1) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(val, timeSeriesData1.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
  
  private Object exec(TimeSeries timeSeries1, Integer val) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(timeSeriesData1.getValue(), val));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }

  private Object exec(Integer val, TimeSeries timeSeries1) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(val, timeSeriesData1.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }

  private Object exec(TimeSeries timeSeries1, Float val) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(timeSeriesData1.getValue(), val));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }

  private Object exec(Float val, TimeSeries timeSeries1) throws Exception {
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
      if (timeSeriesData1.getValue() != null) {
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(val, timeSeriesData1.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
  
  private Object exec(TimeSeries timeSeries1, TimeSeries timeSeries2) throws Exception {
    TimeSeries timeSeriesCollapsed = Utils.collapse(timeSeries1, timeSeries2);
    timeSeries1 = Utils.normalize(timeSeriesCollapsed, timeSeries1);
    timeSeries2 = Utils.normalize(timeSeriesCollapsed, timeSeries2);
    TimeSeries timeSeries = new TimeSeries(operator.getAssociatedSeriesType());
    for (int i = 0; i < timeSeriesCollapsed.getTimeSeriesDataList().size(); i++) {
      TimeSeriesData timeSeriesData1 = timeSeries1.getTimeSeriesDataList().get(i);
      TimeSeriesData timeSeriesData2 = timeSeries2.getTimeSeriesDataList().get(i);
      if (timeSeriesData1.getValue() != null && timeSeriesData2.getValue() != null) {
        Utils.ASSERT(timeSeriesData1.getDate().equals(timeSeriesData2.getDate()), "invalid dates after normalize");
        TimeSeriesData timeSeriesData = new TimeSeriesData();
        timeSeriesData.setDate(timeSeriesData1.getDate());
        timeSeriesData.setValue(((BinaryOperator) operator).exec(timeSeriesData1.getValue(), timeSeriesData2.getValue()));
        timeSeries.add(timeSeriesData);
      }
    }
    return timeSeries;
  }
}

