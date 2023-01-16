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

  public static final float rgnum[] = {
    0.01f, 0.02f, 0.03f, 0.04f, 0.05f, 0.06f, 0.07f, 0.08f, 0.09f,
     0.1f,  0.2f,  0.3f,  0.4f,  0.5f,  0.6f,  0.7f,  0.8f,  0.9f, 
       1f,    2f,    3f,    4f,    5f,    6f,    7f,    8f,    9f,
      10f,   20f,   30f,   40f,   50f,   60f,   70f,   80f,   90f,
     100f,  200f,  300f,  400f,  500f,  600f,  700f,  800f,  900f,
    1000f, 2000f, 3000f, 4000f, 5000f, 6000f, 7000f, 8000f, 900f
  };
  
  public static float findDYGridLines(int nGridLines, MinMaxPair pair) throws Exception {
    int idx = 0;
    while (pair.getMinValue() + nGridLines * rgnum[idx] < pair.getMaxValue() && idx < rgnum.length) {
      idx++;
    }
    
    if (idx == rgnum.length) {
      throw new Exception("gridlines overflowed");
    }
    return rgnum[idx];
  }
  
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
  * s  = series point
  * s1 = series low point
  * s2 = series high point
  * y1 = grid low point
  * y2 = grid high point
  * 
  * Scaling factor S = (y2 - y1) / (s2 - s1)
  * 
  * f(s) = S * (s - s1) + y1
  */
  public static int transform(Float s, int y1, int y2, Float s1, Float s2) {
    log.debug("transform called: s=" + s + ", y1=" + y1 + ", y2=" + y2 + ", s1=" + s1 + ", s2=" + s2);
    Float S = (y2 - y1) / (s2 - s1);
    Float ret =  S * (s - s1) + y1;
    log.debug("transform returning: " + ret);
    return ret.intValue();
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

  public static String getToolTipText(TimeSeries timeSeries) {
    StringBuffer sb = new StringBuffer();
    sb.append("<html>");
    sb.append("<h3><strong>" + timeSeries.getTitle() + "</strong></h3>");
    sb.append("<p><strong>" + "Id:</strong> " + timeSeries.getId() + "</p>");
    sb.append("<p><strong>" + "Name:</strong> " + timeSeries.getName() + "</p>");
    sb.append("<p><strong>" + "Source:</strong> " + timeSeries.getSource() + "</p>");
    if (timeSeries.getSourceId() != null) {
      sb.append("<p><strong>" + "Source Id:</strong> " + timeSeries.getSourceId() + "</p>");
    }
    if (timeSeries.getNotes() != null) {
      sb.append("<p></p>");
      sb.append("<p>" + timeSeries.getNotes() + "</p>");
    }
    sb.append("</html>");
    return sb.toString();
  }
  
  public static String generateFormatString(int colWidths[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < colWidths.length; i++) {
      sb.append("%-" + colWidths[i] + "s");
      if (i < colWidths.length - 1) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  public static String generateUnderlineString(int colWidths[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < colWidths.length; i++) {
      for (int j = 0; j < colWidths[i]; j++) {
        sb.append("-");
      }
      if (i < colWidths.length - 1) {
        sb.append("-");
      }
    }
    return sb.toString();
  }
  
  public static String generateTruncatedData(int colWidths[], int i, String data) {
    if (data == null) {
      return data;
    }
    
    if (data.length() <= colWidths[i]) {
      return data;
    }
    
    return data.substring(0, colWidths[i] - 3) + "...";
  }

  public static TimeSeries load(Object object) throws Exception {
    if (object instanceof Integer) {
      return TimeSeriesDAO.getInstance().loadSeriesById((Integer) object);
    } else if (object instanceof String) {
      return TimeSeriesDAO.getInstance().loadSeriesByName((String) object);
    } else {
      throw new Exception("unexpected argument: " + object);
    }
  }
  
  public static String stringWithNULL(String string) {
    return (string == null ? "NULL" : string);
  }
}
