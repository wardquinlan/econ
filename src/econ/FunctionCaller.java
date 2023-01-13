package econ;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.command.Command;
import econ.command.ExitCommand;
import econ.command.HelpCommand;
import econ.command.ListFontsCommand;
import econ.command.PrintCommand;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);
  private Map<String, Command> commandMap = new TreeMap<>();

  static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 30};
  static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};

  public FunctionCaller() {
    commandMap.put("exit", new ExitCommand());
    commandMap.put("print", new PrintCommand());
    commandMap.put("lsfonts", new ListFontsCommand());
    commandMap.put("help", new HelpCommand());
  }
  
  public boolean isFunction(String funcName) {
    return funcName.equals("help") || commandMap.keySet().contains(funcName);
  }
  
  public Object invokeFunction(String funcName, Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (funcName.equals("help")) {
      if (params.size() == 1) {
        Command command = commandMap.get(params.get(0));
        if (command != null) {
          System.out.println(command.getSummary());
          System.out.println();
          for (String detail: command.getDetails()) {
            System.out.println(detail);
          }
          System.out.println();
          System.out.println("returns: " + command.getReturns());
          return 0;
        }
      }
      for (String name: commandMap.keySet()) {
        Command command = commandMap.get(name);
        System.out.println(command.getSummary());
      }
      return 0;
    }
    return commandMap.get(funcName).run(symbolTable, file, params);
  }
  
  private Object plot(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    String filename = (String) params.get(0);
    XMLParser xmlParser;
    if (Paths.get(filename).isAbsolute()) {
      xmlParser = new XMLParser(new File(filename), 0, symbolTable);
    } else {
      String basename = Paths.get(file.getAbsolutePath()).getParent().toString();
      xmlParser = new XMLParser(new File(basename + File.separator + filename), 0, symbolTable);
    }
    
    Context ctx = xmlParser.parse();
    JFrame frame = new Frame(ctx);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
          log.info("closing");
          synchronized (Lock.instance()) {
            Lock.instance().notify();
          }
      }
    });

    synchronized (Lock.instance()) {
      Lock.instance().wait();
    }
    
    return 0;
  }
  
  private Object insert(List<Object> params) throws Exception {
    if (params.size() > 3) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() < 3) {
      throw new Exception("missing argument(s)");
    }

    List<Object> params2 = new ArrayList<>();
    params2.add(params.get(0));
    TimeSeries timeSeries = (TimeSeries) this.load(params2);
    if (timeSeries == null) {
      throw new Exception("time series not found: " + params.get(0));
    }
    
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    float value;
    if (params.get(2) instanceof Integer) {
      value = ((Integer) params.get(2)).floatValue();
    } else if (params.get(2) instanceof Float) {
      value = (Float) params.get(2);
    } else {
      throw new Exception("invalid value: " + params.get(2));
    }
    TimeSeriesDAO.getInstance().insertSeriesData(timeSeries.getId(), date, value);
    return 0;
  }
  
  private Object delete(Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    Symbol symbol = symbolTable.get("settings.confirm");
    if (symbol == null || !symbol.getValue().equals(1)) {
      throw new Exception("settings.confirm != 1");
    }
    
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument(s)");
    }

    List<Object> params2 = new ArrayList<>();
    params2.add(params.get(0));
    TimeSeries timeSeries = (TimeSeries) this.load(params2);
    if (timeSeries == null) {
      log.warn("time series not found: " + params.get(0));
      return 0;
    }
    
    Date date = null;
    if (params.size() == 2) {
      date = Utils.DATE_FORMAT.parse((String) params.get(1));
      TimeSeriesDAO.getInstance().deleteSeriesData(timeSeries.getId(), date);
    } else {
      TimeSeriesDAO.getInstance().deleteSeries(timeSeries.getId());
    }
    
    return 0;
  }
  
  private Object create(List<Object> params) throws Exception {
    if (params.size() < 4) {
      throw new Exception("missing argument(s)");
    }

    if (params.size() > 5) {
      throw new Exception("too many arguments");
    }
    
    TimeSeries timeSeries = new TimeSeries();
    timeSeries.setId((Integer) params.get(0));
    timeSeries.setName((String) params.get(1));
    timeSeries.setTitle((String) params.get(2));
    timeSeries.setSourceOrg((String) params.get(3));
    if (params.size() == 5) {
      timeSeries.setSourceName((String) params.get(4));
    }
    TimeSeriesDAO.getInstance().createSeries(timeSeries);
    return 0;
  }
  
  private Object printData(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("not a series: " + params.get(0));
    }
    
    TimeSeries timeSeries  = (TimeSeries) params.get(0);
    System.out.println(timeSeries.toStringVerbose());
    return timeSeries;
  }

  private static String generateFormatString(int colWidths[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < colWidths.length; i++) {
      sb.append("%" + colWidths[i] + "s");
      if (i < colWidths.length - 1) {
        sb.append(" ");
      }
    }
    return sb.toString();
  }

  private static String generateUnderlineString(int colWidths[]) {
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
  
  private static String generateTruncatedData(int colWidths[], int i, String data) {
    if (data == null) {
      return data;
    }
    
    if (data.length() <= colWidths[i]) {
      return data;
    }
    
    return data.substring(0, colWidths[i] - 3) + "...";
  }
  
  private Object importData(Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    System.out.println("TODO: IMPORT DATA");
    Symbol symbol = symbolTable.get("settings.confirm");
    if (symbol == null || !symbol.getValue().equals(1)) {
      throw new Exception("settings.confirm != 1");
    }
    return 0;
  }
  
  private Object getNotes(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("not a series: " + params.get(0));
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    return timeSeries.getNotes();
  }
  
  private Object notes(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    TimeSeries timeSeries = (TimeSeries) this.load(params);
    if (timeSeries == null) {
      throw new Exception("time series not found: " + params.get(0));
    }

    System.out.printf(generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Id", "Name", "Title", "Source Org", "Source Name");
    System.out.printf(generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
    System.out.printf(generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
        timeSeries.getId().toString(), 
        generateTruncatedData(TIME_SERIES_COL_WIDTHS, 1, timeSeries.getName()), 
        generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, timeSeries.getTitle()), 
        generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, timeSeries.getSourceOrg()), 
        generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, timeSeries.getSourceName() == null ? "NULL" : timeSeries.getSourceName()));
    System.out.println();
    System.out.println(timeSeries.getNotes() == null ? "NULL" : timeSeries.getNotes());
    return 0;
  }
  
  private Object list(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }

    if (params.size() == 1) {
      TimeSeries timeSeries = (TimeSeries) this.load(params);
      if (timeSeries == null) {
        throw new Exception("time series not found: " + params.get(0));
      }

      System.out.printf(generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Id", "Name", "Title", "Source Org", "Source Name");
      System.out.printf(generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
      System.out.printf(generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
          timeSeries.getId().toString(), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 1, timeSeries.getName()), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, timeSeries.getTitle()), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, timeSeries.getSourceOrg()), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, timeSeries.getSourceName() == null ? "NULL" : timeSeries.getSourceName()));
      System.out.println();
      System.out.printf(generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n", "Id", "Date", "Value");
      System.out.printf(generateUnderlineString(TIME_SERIES_DATA_COL_WIDTHS) + "\n");
      for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
        System.out.printf(generateFormatString(TIME_SERIES_DATA_COL_WIDTHS) + "\n", 
          timeSeriesData.getId().toString(), 
          generateTruncatedData(TIME_SERIES_DATA_COL_WIDTHS, 1, Utils.DATE_FORMAT.format(timeSeriesData.getDate())), 
          generateTruncatedData(TIME_SERIES_DATA_COL_WIDTHS, 2, timeSeriesData.getValue().toString())); 
      }
    } else {
      System.out.printf(generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Id", "Name", "Title", "Source Org", "Source Name");
      System.out.printf(generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
      List<TimeSeries> list = TimeSeriesDAO.getInstance().listSeries();
      for (TimeSeries timeSeries: list) {
        System.out.printf(generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
          timeSeries.getId().toString(), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 1, timeSeries.getName()), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, timeSeries.getTitle()), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, timeSeries.getSourceOrg()), 
          generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, timeSeries.getSourceName() == null ? "NULL" : timeSeries.getSourceName()));
      }
    }
    return 0;
  }
  
  private Object load(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (params.get(0) instanceof Integer) {
      return TimeSeriesDAO.getInstance().loadSeriesById(Integer.parseInt(params.get(0).toString()));
    } else if (params.get(0) instanceof String) {
      return TimeSeriesDAO.getInstance().loadSeriesByName(params.get(0).toString());
    } else {
      throw new Exception("unexpected argument: " + params.get(0));
    }
  }
}
