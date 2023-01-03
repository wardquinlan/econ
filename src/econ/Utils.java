package econ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Utils {
  private static Log log = LogFactory.getFactory().getInstance(Utils.class);
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
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  
  public static void ASSERT(boolean condition, String message) {
    if (!condition) {
      log.fatal("***ASSERTION FAILED: " + message);
      System.exit(1);
    }
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
  
  /* Transformation Function:
  *
  * s1 = series low point
  * s2 = series high point
  * y1 = grid low point
  * y2 = grid high point
  * 
  * f(y) = y1 + y * (y2 - y1) / (s2 - s1)
  */
  public static int transform(Float y1) {
    return 0;
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
  
  public static TimeSeries normalize(TimeSeries timeSeriesCollapsed, TimeSeries timeSeries) {
    TimeSeries timeSeriesNormalized = new TimeSeries();
    int indexCollapsed = 0;
    int index = 0;
    
    if (timeSeries.size() == 0) {
      return timeSeriesCollapsed;
    }
    
    while (timeSeriesCollapsed.get(indexCollapsed).compareTo(timeSeries.get(index)) < 0) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesCollapsed.get(indexCollapsed).getDate());
      timeSeriesNormalized.add(timeSeriesData);
      indexCollapsed++;
    }
    
    Float valueLast = null;
    while (indexCollapsed < timeSeriesCollapsed.size()) {
      if (index == timeSeries.size()) {
        Utils.ASSERT(valueLast != null, "valueLast not expected to be NULL");
      } else if (timeSeriesCollapsed.get(indexCollapsed).compareTo(timeSeries.get(index)) == 0) {
        valueLast = timeSeries.get(index).getValue();
        index++;
      }
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesCollapsed.get(indexCollapsed).getDate());
      timeSeriesData.setValue(valueLast);
      timeSeriesNormalized.add(timeSeriesData);
      indexCollapsed++;
    }
    
    return timeSeriesNormalized;
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
    while (index1 < timeSeries1.size() && index2 < timeSeries2.size()) {
      Date date1 = timeSeries1.get(index1).getDate();
      Date date2 = timeSeries2.get(index2).getDate();
      TimeSeriesData data = new TimeSeriesData();
      if (date1.compareTo(date2) < 0) {
        data.setDate(date1);
        timeSeries.add(data);
        index1++;
      } else if (date1.compareTo(date2) > 0) {
        data.setDate(date2);
        timeSeries.add(data);
        index2++;
      } else {
        data.setDate(date1);
        timeSeries.add(data);
        index1++;
        index2++;
      }
    }
    
    while (index1 < timeSeries1.size()) {
      TimeSeriesData data = new TimeSeriesData();
      data.setDate(timeSeries1.get(index1).getDate());
      timeSeries.add(data);
      index1++;
    }

    while (index2 < timeSeries2.size()) {
      TimeSeriesData data = new TimeSeriesData();
      data.setDate(timeSeries2.get(index2).getDate());
      timeSeries.add(data);
      index2++;
    }
    
    return timeSeries;
  }
}
