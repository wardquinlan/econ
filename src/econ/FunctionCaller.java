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

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);

  static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 30};
  static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};

  public static boolean isFunction(String funcName) {
    return funcName.equals("create")        ||
           funcName.equals("delete")        ||
           funcName.equals("exit")          ||
           funcName.equals("help")          ||
           funcName.equals("insert")        ||
           funcName.equals("list")          ||
           funcName.equals("listFonts")     ||
           funcName.equals("load")          ||
           funcName.equals("plot")          ||
           funcName.equals("print")         ||
           funcName.equals("printDetails");
  }
  
  public Object invokeFunction(String funcName, Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    Utils.ASSERT(params.size() >= 1, "params.size() == 0");
    Utils.ASSERT(params.get(params.size() - 1) instanceof File, "params last element is not a File");
    File file = (File) params.remove(params.size() - 1);
    switch(funcName) {
      case "create":
        return create(params);
      case "delete":
        return delete(symbolTable, params);
      case "exit":
        return exit(params);
      case "help":
        return help(params);
      case "insert":
        return insert(params);
      case "list":
        return list(params);
      case "listFonts":
        return listFonts(params);
      case "load":
        return load(params);
      case "plot":
        return plot(symbolTable, file, params);
      case "print":
        return print(params);
      case "printDetails":
        return printDetails(params);
      default:
        throw new Exception("unknown function: " + funcName);
    }
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
  
  private Object help(List<Object> params) throws Exception {
    System.out.println("Econ version 0.10");
    System.out.println("usage:\n");
    System.out.println("int create(int id, String name, String title, String sourceOrg[, String sourceName]);");
    System.out.println("  creates a new series with empty notes, a possibly empty source name, and no series data");
    System.out.println("  returns: 0\n");
    
    System.out.println("int delete(Object object[, String date]);");
    System.out.println("  deletes:");
    System.out.println("  - an entire series using 'object' as either an id or a name");
    System.out.println("  - series data associated with 'date', using 'object' as either an id or a name");
    System.out.println("  note: 'settings.confirm' must == 1 for this command to work");
    System.out.println("  returns: 0\n");
    
    System.out.println("int exit([int code]);");
    System.out.println("  exits the application with status code 'code', or 0 if 'code' not supplied");
    System.out.println("  returns: 'code', or 0 if 'code' not supplied\n");
    
    System.out.println("int help();");
    System.out.println("  prints out this help screen");
    System.out.println("  returns: 0\n");
    
    System.out.println("int insert(Object object, String date, float value);");
    System.out.println("  inserts series data, using 'object' as either an id or a name");
    System.out.println("  returns: 0\n");
    
    System.out.println("int list([Object object]);");
    System.out.println("  lists:");
    System.out.println("  - all series)");
    System.out.println("  - series data associated with 'object' as either an id or a name");
    System.out.println("  returns: 0\n");
    
    System.out.println("int listFonts();");
    System.out.println("  lists all system font names");
    System.out.println("  returns: 0\n");
    
    System.out.println("Series load(Object object);");
    System.out.println("  loads a series using 'object' as either an id or a name");
    System.out.println("  returns: Series, or null if not found\n");
    
    System.out.println("int plot(String filename);");
    System.out.println("  plots series as defined in the context file 'filename'");
    System.out.println("  returns: 0\n");
    
    System.out.println("Object print([Object object]);");
    System.out.println("  prints:");
    System.out.println("  - 'object' (directly from memory)");
    System.out.println("  - an empty line if 'object' supplied");
    System.out.println("  returns: 'object', or 0 if 'object' not supplied\n");
    
    System.out.println("Series printDetails(Series series);");
    System.out.println("  prints series details for 'series' (directly from memory)");
    System.out.println("  returns: 'series'\n");
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
    TimeSeriesDAO.getInstance().insertSeriesData(timeSeries.getId(), date, (Float) params.get(2));
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
  
  private Object printDetails(List<Object> params) throws Exception {
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

  private Object print(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      System.out.println();
      return 0;
    } else {
      System.out.println(params.get(0).toString());
      return params.get(0);
    }
  }
  
  private Object listFonts(List<Object> params) throws Exception {
    if (params.size() != 0) {
      throw new Exception("too many arguments");
    }
    
    Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    for (Font font: fonts) {
      System.out.println(font.getFontName());
    }  
    return 0;
  }
  
  private Object exit(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      System.exit(0);
    }
    
    int value = Integer.parseInt(params.get(0).toString());
    System.exit(value);
    return 0;
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
  
  private Object list(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }

    if (params.size() == 1) {
      List<Object> params2 = new ArrayList<>();
      params2.add(params.get(0));
      TimeSeries timeSeries = (TimeSeries) this.load(params2);
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
