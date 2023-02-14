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

public class QDB implements Command {
  @Override
  public String getSummary() {
    return "Series qdb(String dbFilePath);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Imports a series from a Quote database file, with:");
    list.add("  'dbFilePath' as the path to the database file");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Imported series from the Quote database file";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    String dbFilePath = (String) params.get(0);
    File fileDB = new File(dbFilePath);
    Map<Date, Float> map = new HashMap<>();
    
    String fileBase = Utils.getFileBaseName(Paths.get(fileDB.getAbsolutePath()).getFileName().toString()).toUpperCase();
    TimeSeries timeSeries = new TimeSeries(TimeSeries.FLOAT);
    timeSeries.setSource(("QDB"));
    timeSeries.setSourceId(fileBase);
    timeSeries.setName(fileBase);
    timeSeries.setTitle(fileBase);
    BufferedReader reader = null;
    try {
      if (Paths.get(dbFilePath).isAbsolute()) {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(dbFilePath)));
      } else {
        String basename = Paths.get(file.getAbsolutePath()).getParent().toString();
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(basename + File.separator + dbFilePath)));
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
        st.nextToken();

        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        st.nextToken();
        
        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        st.nextToken();

        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        String close = st.nextToken();
        
        if (!st.hasMoreTokens()) {
          log.warn("ignoring incomplete line: " + line);
          continue;
        }
        st.nextToken();
        
        if (st.hasMoreTokens()) {
          log.warn("ignoring invalid line: " + line);
          continue;
        }

        try {
          Date d = Utils.DATE_FORMAT.parse(date);
          if (map.get(d) != null) {
            log.warn("value for " + date + " already exists; overwriting");
          }
          Float v = Float.parseFloat(close);
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
    timeSeries.setBase(true);
    return timeSeries;
  }
}
