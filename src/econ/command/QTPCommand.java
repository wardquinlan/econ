package econ.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import econ.core.TimeSeries;
import econ.core.TimeSeriesData;
import econ.core.Utils;
import econ.parser.Symbol;

public class QTPCommand implements Command {
  @Override
  public String getSummary() {
    return "Series qtp(String templateFilePath, String sourceId);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Imports a series from the Quote template file, with:");
    list.add("  'templateFilePath' as the path to Quote's template file");
    list.add("  'sourceId' as the Quote ID string");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Imported series from the Quote template file";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() < 2) {
      throw new Exception("missing argument(s)");
    }
    
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    
    if (!(params.get(0) instanceof String) || !(params.get(1) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    String templateFilePath = (String) params.get(0);
    String name = (String) params.get(1);
    Set<String> duplicateCheckSet = new HashSet<>();
    
    TimeSeries timeSeries = new TimeSeries();
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

        if (duplicateCheckSet.contains(date)) {
          log.warn("ignoring duplicate line: " + line);
          continue;
        }
        
        try {
          TimeSeriesData timeSeriesData = new TimeSeriesData();
          timeSeriesData.setDate(Utils.DATE_FORMAT.parse(date));
          timeSeriesData.setValue(Float.parseFloat(value));
          timeSeries.add(timeSeriesData);
          duplicateCheckSet.add(date);
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
    Collections.sort(timeSeries.getTimeSeriesDataList());
    return timeSeries;
  }
}
