package econ;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
  public static final Map<Integer, String> MONTHS = new HashMap<Integer, String>();
  static {
    MONTHS.put(1, "F");
    MONTHS.put(2, "M");
    MONTHS.put(3, "A");
    MONTHS.put(4, "M");
    MONTHS.put(5, "J");
    MONTHS.put(6, "J");
    MONTHS.put(7, "A");
    MONTHS.put(8, "S");
    MONTHS.put(9, "O");
    MONTHS.put(10, "N");
    MONTHS.put(11, "D");
  }
  
  public static String getMonthString(Calendar cal) {
    int month = cal.get(Calendar.MONTH);
    if (month > 0) {
      return MONTHS.get(month);
    }
    return Integer.toString(cal.get(Calendar.YEAR)).substring(2);
  }
  
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
  
  public static TimeSeries collapse(List<TimeSeries> timeSeriesList) {
    if (timeSeriesList.size() == 0) {
      return new TimeSeries();
    }
    
    if (timeSeriesList.size() == 1) {
      return timeSeriesList.get(0);
    }
    
    TimeSeries timeSeries1 = timeSeriesList.remove(0);
    TimeSeries timeSeries2 = timeSeriesList.remove(0);
    TimeSeries timeSeries = collapse(timeSeries1, timeSeries2);
    timeSeriesList.add(0, timeSeries);
    return collapse(timeSeriesList);
  }
  
  private static TimeSeries collapse(TimeSeries timeSeries1, TimeSeries timeSeries2) {
    TimeSeries timeSeries = new TimeSeries();
    int index1 = 0;
    int index2 = 0;
    while (index1 < timeSeries1.getTimeSeriesData().size() && index2 < timeSeries2.getTimeSeriesData().size()) {
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
