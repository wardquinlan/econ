package econ.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import econ.Symbol;
import econ.TimeSeries;
import econ.TimeSeriesData;
import econ.Utils;

public class QTemplateImporter implements Importer {
  @Override
  public TimeSeries run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
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
    timeSeries.setSourceId(name);
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
