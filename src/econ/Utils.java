package econ;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {
  public static int parseHex(String string) throws Exception {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      if (Character.isDigit(ch) || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
        sb.append(ch);
      } else {
        throw new Exception("invalid hex value: " + string);
      }
    }
    return Integer.parseInt(sb.toString(), 16);
  }
  
  public static List<TimeSeries> consolidate(Panel panel) {
    ArrayList<TimeSeries> list = new ArrayList<>();
    for (Chart chart: panel.getCharts()) {
      for (Series series: chart.getSeries()) {
        list.add(series.getTimeSeries());
      }
    }
    return list;
  }
  
  public static TimeSeries collapse(List<TimeSeries> timeSeriesList) throws Exception {
    if (timeSeriesList.size() == 0) {
      throw new Exception("unexpected timeSeriesList size: 0");
    }
    
    if (timeSeriesList.size() == 1) {
      return timeSeriesList.get(0);
    }
    
    if (timeSeriesList.size() == 2) {
      return collapse(timeSeriesList.get(0), timeSeriesList.get(1));
    }
    
    return collapse(timeSeriesList.subList(1, timeSeriesList.size()));
  }
  
  private static TimeSeries collapse(TimeSeries timeSeries1, TimeSeries timeSeries2) {
    TimeSeries timeSeries = new TimeSeries();
    int index1 = 0;
    int index2 = 0;
    while (index1 < timeSeries1.getTimeSeriesData().size() && index2 < timeSeries1.getTimeSeriesData().size()) {
      Date date1 = timeSeries1.getTimeSeriesData().get(index1).getDate();
      Date date2 = timeSeries2.getTimeSeriesData().get(index2).getDate();
      TimeSeriesData data = new TimeSeriesData();
      if (date1.compareTo(date2) < 0) {
        data.setDate(date1);
        timeSeries.getTimeSeriesData().add(data);
        index1++;
      } else if (date1.compareTo(date2) > 0) {
        data.setDate(date2);
        timeSeries.getTimeSeriesData().add(data);
        index2++;
      } else {
        data.setDate(date1);
        timeSeries.getTimeSeriesData().add(data);
        index1++;
        index2++;
      }
    }
    
    while (index1 < timeSeries1.getTimeSeriesData().size()) {
      TimeSeriesData data = new TimeSeriesData();
      data.setDate(timeSeries1.getTimeSeriesData().get(index1).getDate());
      timeSeries.getTimeSeriesData().add(data);
      index1++;
    }

    while (index2 < timeSeries2.getTimeSeriesData().size()) {
      TimeSeriesData data = new TimeSeriesData();
      data.setDate(timeSeries2.getTimeSeriesData().get(index2).getDate());
      timeSeries.getTimeSeriesData().add(data);
      index2++;
    }
    
    return timeSeries;
  }
}
