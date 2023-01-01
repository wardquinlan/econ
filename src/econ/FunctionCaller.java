package econ;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);

  static final int COL_WIDTHS[] = {5, 20, 30, 12, 30};

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
      case "print":
      case "println":
        return println(params);
      case "loadSeriesByName":
        return loadSeriesByName(params);
      case "loadSeriesById":
        return loadSeriesById(params);
      case "listFontNames":
        return listFontNames(params);
      case "exit":
      case "quit":
        return exit(params);
      case "listSeries":
        return listSeries(params);
      case "printSeries":
        return printSeries(params);
      case "help":
        return help(params);
      case "plot":
        return plot(symbolTable, file, params);
      case "createSeries":
        return createSeries(params);
      case "deleteSeries":
        return deleteSeries(symbolTable, params);
      case "insertSeriesData":
        return insertSeriesData(params);
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
  
  private Object insertSeriesData(List<Object> params) throws Exception {
    if (params.size() > 3) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() < 3) {
      throw new Exception("missing argument(s)");
    }
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    TimeSeriesDAO.getInstance().insertSeriesData((Integer) params.get(0), date, (Float) params.get(2));
    return 0;
  }
  
  private Object deleteSeries(Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    Symbol symbol = symbolTable.get("settings.confirm");
    if (symbol == null || !symbol.getValue().equals(1)) {
      throw new Exception("settings.confirm != 1");
    }
    
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    TimeSeriesDAO.getInstance().deleteSeries((Integer) params.get(0));
    return 0;
  }
  
  private Object createSeries(List<Object> params) throws Exception {
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
  
  private Object printSeries(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("not a series");
    }
    
    TimeSeries timeSeries  = (TimeSeries) params.get(0);
    System.out.println(timeSeries.toStringVerbose());
    return timeSeries;
  }
  
  private Object listFontNames(List<Object> params) throws Exception {
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
  
  private static String generateTitleFormatString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < COL_WIDTHS.length; i++) {
      switch (i) {
        case 0:
        case 1:
        case 2:
        case 3:
          sb.append("%" + COL_WIDTHS[i] + "s ");
          break;
        case 4:
          sb.append("%" + COL_WIDTHS[i] + "s");
          break;
      }
    }
    return sb.toString();
  }

  private static String generateDataFormatString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < COL_WIDTHS.length; i++) {
      switch (i) {
        case 0:
          sb.append("%" + COL_WIDTHS[i] + "d ");
          break;
        case 1:
        case 2:
        case 3:
          sb.append("%" + COL_WIDTHS[i] + "s ");
          break;
        case 4:
          sb.append("%" + COL_WIDTHS[i] + "s");
          break;
      }
    }
    return sb.toString();
  }
  
  private static String generateUnderlineString() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < COL_WIDTHS.length; i++) {
      for (int j = 0; j < COL_WIDTHS[i]; j++) {
        sb.append("-");
      }
      if (i != COL_WIDTHS.length - 1) {
        sb.append("-");
      }
    }
    return sb.toString();
  }
  
  private static String generateTruncatedData(int i, String data) {
    if (data == null) {
      return data;
    }
    
    if (data.length() <= COL_WIDTHS[i]) {
      return data;
    }
    
    return data.substring(0, COL_WIDTHS[i] - 3) + "...";
  }
  
  private Object listSeries(List<Object> params) throws Exception {
    if (params.size() > 0) {
      throw new Exception("too many arguments");
    }

    System.out.printf(generateTitleFormatString() + "\n", "Id", "Name", "Title", "Source Org", "Source Name");
    System.out.printf(generateUnderlineString() + "\n");
    List<TimeSeries> list = TimeSeriesDAO.getInstance().listSeries();
    for (TimeSeries timeSeries: list) {
      System.out.printf(generateDataFormatString() + "\n", 
        timeSeries.getId(), 
        generateTruncatedData(1, timeSeries.getName()), 
        generateTruncatedData(2, timeSeries.getTitle()), 
        generateTruncatedData(3, timeSeries.getSourceOrg()), 
        generateTruncatedData(4, timeSeries.getSourceName() == null ? "NULL" : timeSeries.getSourceName()));
    }
    return 0;
  }
  
  private Object loadSeriesByName(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    return TimeSeriesDAO.getInstance().loadSeriesByName(params.get(0).toString());
  }

  private Object loadSeriesById(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    return TimeSeriesDAO.getInstance().loadSeriesById(Integer.parseInt(params.get(0).toString()));
  }
  
  private Object println(List<Object> params) throws Exception {
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
}
