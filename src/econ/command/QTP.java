package econ.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.Symbol;

public class QTP implements Command {
  @Override
  public String getSummary() {
    return "Series qtp(String templateFilePath, String sourceId);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Imports a series from a Quote template file, with:");
    list.add("  'templateFilePath' as the path to the template file");
    list.add("  'sourceId' as the Quote ID string");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Imported series from the Quote template file";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    
    if (!(params.get(0) instanceof String) || !(params.get(1) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    String templateFilePath = (String) params.get(0);
    String name = (String) params.get(1);
    Map<Date, Float> map = new HashMap<>();
    
    TimeSeries timeSeries = new TimeSeries(TimeSeries.TYPE_FLOAT);
    timeSeries.setSource(("QTEMPLATE"));
    timeSeries.setSourceId(name);
    timeSeries.setName(name);
    timeSeries.setTitle(name);
    BufferedReader reader = null;
    try {
      if (Paths.get(templateFilePath).isAbsolute()) {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFilePath)));
      } else {
        String basename = Paths.get(file.getAbsolutePath()).getParent().toString();
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(basename + File.separator + templateFilePath)));
      }

      String line;
      while ((line = reader.readLine()) != null) {
        StringTokenizer st = new StringTokenizer(line, ",");
        if (!st.hasMoreTokens()) {
          continue;
        }
        String date = st.nextToken();
        if (date.length() != 8) {
          log.warn("ignoring invalid date: " + date);
          continue;
        }
        date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        
        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        // ignore scope
        st.nextToken();
        
        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        String label = st.nextToken();
        if (!name.equals(label)) {
          log.debug("ignoring unmatched label: " + label);
          continue;
        }
        
        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        String value = st.nextToken();
        
        if (st.hasMoreTokens()) {
          log.warn("ignoring invalid line: " + line);
          continue;
        }

        try {
          Date d = Utils.DATE_FORMAT.parse(date);
          if (map.get(d) != null) {
            log.warn("value for " + date + " already exists; overwriting");
          }
          Float v = Float.parseFloat(value);
          map.put(d, v);
        } catch(Exception e) {
          log.warn("ignoring invalid line: " + line);
          continue;
        }
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }

    for (Date d: map.keySet()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(d);
      timeSeriesData.setValue(map.get(d));
      timeSeries.add(timeSeriesData);
    }
    Collections.sort(timeSeries.getTimeSeriesDataList());
    return timeSeries;
  }
}
