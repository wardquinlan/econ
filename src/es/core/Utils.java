package es.core;

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

import es.gui.Chart;
import es.gui.MinMaxPair;
import es.gui.Panel;
import es.gui.Series;

public class Utils {
  private static Log log = LogFactory.getFactory().getInstance(Utils.class);
  public static final Map<Integer, String> MONTHS = new HashMap<Integer, String>();
  static {
    MONTHS.put(0, "J");
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
    1000f, 2000f, 3000f, 4000f, 5000f, 6000f, 7000f, 8000f, 9000f
  };
  
  public static final String UNDEFINED = "[[[UNDEFINED]]]";
  
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
      if (message != null) {
        log.error("*** ASSERTION FAILED ***: " + message);
      } else {
        log.error("*** ASSERTION FAILED ***");
      }
    }
  }
  
  public static String getMonthString(Calendar cal, boolean withYear) {
    int month = cal.get(Calendar.MONTH);
    if (!withYear || month > 0) {
      return MONTHS.get(month);
    }
    return Integer.toString(cal.get(Calendar.YEAR)).substring(2);
  }
  
  public static String getDayString(Calendar cal) {
    return Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
  }
  
  public static String getYearString(Calendar cal) {
    return Integer.toString(cal.get(Calendar.YEAR));
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
  public static int transform(Scaler scaler, Float s, int y1, int y2, Float s1, Float s2) throws Exception {
    log.debug("transform called: s=" + s + ", y1=" + y1 + ", y2=" + y2 + ", s1=" + s1 + ", s2=" + s2);
    Float S = (y2 - y1) / (scaler.scale(s2) - scaler.scale(s1));
    Float ret =  S * (scaler.scale(s) - scaler.scale(s1)) + y1;
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
    TimeSeries timeSeriesNormalized = new TimeSeries(timeSeries.getType());
    int indexCollapsed = 0;
    int index = 0;
    
    if (timeSeries.size() == 0) {
      for (TimeSeriesData timeSeriesData: timeSeriesCollapsed.getTimeSeriesDataList()) {
        timeSeriesNormalized.add(timeSeriesData);
      }
      return timeSeriesNormalized;
    }
    
    // Note: can consider setDate(new Date(oldDate.getTime()));
    while (timeSeriesCollapsed.get(indexCollapsed).compareTo(timeSeries.get(index)) < 0) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(timeSeriesCollapsed.get(indexCollapsed).getDate());
      timeSeriesNormalized.add(timeSeriesData);
      indexCollapsed++;
    }
    
    Object valueLast = null;
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
      return new TimeSeries(TimeSeries.NULL);
    }
    
    if (timeSeriesList.size() == 1) {
      TimeSeries timeSeries = new TimeSeries(TimeSeries.NULL);
      for (TimeSeriesData timeSeriesData: timeSeriesList.get(0).getTimeSeriesDataList()) {
        TimeSeriesData data = new TimeSeriesData();
        data.setDate(timeSeriesData.getDate());
        timeSeries.add(data);
      }
      return timeSeries;
    }
    
    TimeSeries timeSeries1 = timeSeriesList.remove(0);
    TimeSeries timeSeries2 = timeSeriesList.remove(0);
    TimeSeries timeSeries = collapse(timeSeries1, timeSeries2);
    timeSeriesList.add(0, timeSeries);
    return collapse(timeSeriesList);
  }
  
  public static int offset(TimeSeries timeSeries) {
    for (int i = 0; i < timeSeries.size(); i++) {
      if (timeSeries.get(i).getValue() != null) {
        return i;
      }
    }
    return -1;
  }
  
  public static MergeData prepareMerge(TimeSeries timeSeriesCat, TimeSeries timeSeriesDS, boolean mergeInserts, boolean mergeUpdates, boolean mergeDeletes, boolean mergeMetaData) throws Exception {
    MergeData mergeData = new MergeData();
    mergeData.getTimeSeriesInsert().setId(timeSeriesCat.getId());
    mergeData.getTimeSeriesUpdate().setId(timeSeriesCat.getId());
    mergeData.getTimeSeriesDelete().setId(timeSeriesCat.getId());
    if (mergeMetaData) {
      TimeSeries timeSeriesMerge = new TimeSeries(TimeSeries.NULL);
      timeSeriesMerge.setId(timeSeriesCat.getId());
      timeSeriesMerge.setName(timeSeriesCat.getName());
      timeSeriesMerge.setTitle(timeSeriesCat.getTitle());
      timeSeriesMerge.setSource(timeSeriesCat.getSource());
      timeSeriesMerge.setSourceId(timeSeriesCat.getSourceId());
      timeSeriesMerge.setNotes(timeSeriesCat.getNotes());
      mergeData.setTimeSeriesMerge(timeSeriesMerge);
      log.info("updating metadata");
    }
    
    int indexCat = 0;
    int indexDS = 0;
    while (indexCat < timeSeriesCat.size() && indexDS < timeSeriesDS.size()) {
      Date dateCat = timeSeriesCat.get(indexCat).getDate();
      float valueCat = (float) timeSeriesCat.get(indexCat).getValue();
      Date dateDS = timeSeriesDS.get(indexDS).getDate();
      float valueDS = (float) timeSeriesDS.get(indexDS).getValue();
      TimeSeriesData data = new TimeSeriesData();
      if (dateCat.compareTo(dateDS) < 0) {
        if (mergeInserts) {
          log.info("[1] inserting data at " + Utils.DATE_FORMAT.format(dateCat));
          data.setDate(dateCat);
          data.setValue(valueCat);
          mergeData.getTimeSeriesInsert().add(data);
        }
        indexCat++;
      } else if (dateCat.compareTo(dateDS) > 0) {
        if (mergeDeletes) {
          log.info("[1] deleting data at " + Utils.DATE_FORMAT.format(dateDS));
          data.setDate(dateDS);
          data.setValue(valueDS);
          mergeData.getTimeSeriesDelete().add(data);
        }
        indexDS++;
      } else {
        if (valueCat != valueDS) {
          if (mergeUpdates) {
            log.info("updating data at " + Utils.DATE_FORMAT.format(dateCat));
            data.setDate(dateCat);
            data.setValue(valueCat);
            mergeData.getTimeSeriesUpdate().add(data);
          }
        }
        indexCat++;
        indexDS++;
      }
    }
    
    while (indexCat < timeSeriesCat.size()) {
      if (mergeInserts) {
        Date dateCat = timeSeriesCat.get(indexCat).getDate();
        float valueCat = (float) timeSeriesCat.get(indexCat).getValue();
        log.info("[2] inserting data at " + Utils.DATE_FORMAT.format(dateCat));
        TimeSeriesData data = new TimeSeriesData();
        data.setDate(dateCat);
        data.setValue(valueCat);
        mergeData.getTimeSeriesInsert().add(data);
      }
      indexCat++;
    }

    while (indexDS < timeSeriesDS.size()) {
      if (mergeDeletes) {
        Date dateDS = timeSeriesDS.get(indexDS).getDate();
        float valueDS = (float) timeSeriesDS.get(indexDS).getValue();
        log.info("[2] deleting data at " + Utils.DATE_FORMAT.format(dateDS));
        TimeSeriesData data = new TimeSeriesData();
        data.setDate(dateDS);
        data.setValue(valueDS);
        mergeData.getTimeSeriesDelete().add(data);
      }
      indexDS++;
    }
    
    return mergeData;
  }
  
  public static TimeSeries collapse(TimeSeries timeSeries1, TimeSeries timeSeries2) {
    TimeSeries timeSeries = new TimeSeries(TimeSeries.NULL);
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

  public static String getToolTipText(TimeSeries timeSeries, int maxline) {
    StringBuffer sb = new StringBuffer();
    sb.append("<html>");
    if (timeSeries.getTitle() != null) {
      sb.append("<h3><strong>" + splitHTML(timeSeries.getTitle(), maxline) + "</strong></h3>");
    }
    if (timeSeries.getId() != null) {
      sb.append("<p><strong>" + "Id:</strong> " + timeSeries.getId() + "</p>");
    }
    if (timeSeries.getName() != null) {
      sb.append("<p><strong>" + "Name:</strong> " + timeSeries.getName() + "</p>");
    }
    if (timeSeries.getSource() != null) {
      sb.append("<p><strong>" + "Source:</strong> " + timeSeries.getSource() + "</p>");
    }
    if (timeSeries.getSourceId() != null) {
      sb.append("<p><strong>" + "Source Id:</strong> " + timeSeries.getSourceId() + "</p>");
    }
    sb.append("<p><strong>" + "Size:</strong> " + timeSeries.size() + "</p>");
    if (timeSeries.getTimeSeriesDataList().size() > 0) {
      sb.append("<p><strong>" + "Last:</strong> " + timeSeries.getTimeSeriesDataList().get(timeSeries.getTimeSeriesDataList().size() - 1).getValue() +
        " (" + Utils.DATE_FORMAT.format(timeSeries.get(timeSeries.size() - 1).getDate()) + ")" + "</p>");
    }
    String change = Utils.change(timeSeries);
    if (change != null) {
      sb.append("<p><strong>Change:</strong>" + change + "</p>");
    }
    if (timeSeries.getNotes() != null) {
      sb.append("<p></p>");
      sb.append("<p>" + splitHTML(timeSeries.getNotes(), maxline) + "</p>");
    }
    sb.append("</html>");
    return sb.toString();
  }

  private static String splitHTML(String text, int maxline) {
    String array[] = text.split("\n");
    StringBuffer splitHTML = new StringBuffer();
    for (int i = 0; i < array.length; i++) {
      splitHTML.append(splitHTML2(array[i], maxline));
      if (i != array.length - 1) {
        splitHTML.append("<br/>");
      }
    }
    return splitHTML.toString();
  }
  
  private static String splitHTML2(String text, int maxline) {
    String array[] = text.split(" ");
    int idx = 0;
    StringBuffer splitHTML = new StringBuffer();
    StringBuffer curr = new StringBuffer();
    while (idx < array.length) {
      if (curr.length() == 0 || curr.length() + array[idx].length() + 1 <= maxline) {
        curr.append(array[idx]);
        curr.append(" ");
      } else {
        splitHTML.append(curr);
        splitHTML.append("<br/>");
        curr = new StringBuffer();
        curr.append(array[idx]);
        curr.append(" ");
      }
      idx++;
    }
    if (curr.length() > 0) {
      splitHTML.append(curr);
    }
    return splitHTML.toString();
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
    return (string == null ? "" : string);
  }
  
  public static String getFileBaseName(String fileName) {
    int index = fileName.indexOf('.');
    if (index >= 0) {
      return fileName.substring(0, index);
    }
    return fileName;
  }
  
  public static String change(TimeSeries timeSeries) {
    if (timeSeries.getType() == TimeSeries.FLOAT && timeSeries.size() > 1) {
      Float val1 = (Float) timeSeries.get(timeSeries.size() - 1).getValue();
      Float val2 = (Float) timeSeries.get(timeSeries.size() - 2).getValue();
      if (val1 != null && val2 != null) {
        float change = val1 - val2;
        if (val1 > 0 && val2 > 0) {
          float percentChange = change / val2 * 100f;
          return String.format("%f (%.3f%%)", change, percentChange);
        } else {
          return String.format("%f", change);
        }
      }
    }
    return null;
  }
  
  public static void validateIsAdmin() throws Exception {
    if (!Settings.getInstance().isAdmin()) {
      throw new Exception("you must be running in administrative mode to do this");
    }
  }
  
  public static void validate(List<Object> params, int min, int max) throws Exception {
    if (params.size() < min) {
      throw new Exception("missing argument(s)");
    }
    
    if (params.size() > max) {
      throw new Exception("too many arguments");
    }
  }
}
