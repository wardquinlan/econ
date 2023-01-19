package econ.importers;

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

import econ.TimeSeries;
import econ.TimeSeriesData;
import econ.Utils;
import econ.parser.Symbol;

public class QDBImporter implements Importer {
  @Override
  public TimeSeries run(File file, List<Object> params) throws Exception {
    if (params.size() < 1) {
      throw new Exception("missing argument(s)");
    }
    
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    String dbFilePath = (String) params.get(0);
    File fileDB = new File(dbFilePath);
    Set<String> duplicateCheckSet = new HashSet<>();
    
    String fileBase = Utils.getFileBaseName(Paths.get(fileDB.getAbsolutePath()).getFileName().toString()).toUpperCase();
    TimeSeries timeSeries = new TimeSeries();
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

        if (duplicateCheckSet.contains(date)) {
          log.warn("ignoring duplicate line: " + line);
          continue;
        }
        
        try {
          TimeSeriesData timeSeriesData = new TimeSeriesData();
          timeSeriesData.setDate(Utils.DATE_FORMAT.parse(date));
          timeSeriesData.setValue(Float.parseFloat(close));
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
