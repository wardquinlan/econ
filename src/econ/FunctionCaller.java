package econ;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);
  
  public static boolean isFunction(String funcName) {
    return funcName.equals("println")          || 
           funcName.equals("loadSeriesByName") ||
           funcName.equals("listFontNames")    ||
           funcName.equals("getSeriesDetails") ||
           funcName.equals("listSeries")       ||
           funcName.equals("help")             ||
           funcName.equals("plot")             ||
           funcName.equals("quit")             ||
           funcName.equals("exit");
  }
  
  public Object invokeFunction(String funcName, Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    Utils.ASSERT(params.size() >= 1, "params.size() == 0");
    Utils.ASSERT(params.get(params.size() - 1) instanceof File, "params last element is not a File");
    File file = (File) params.remove(params.size() - 1);
    switch(funcName) {
      case "println":
        return println(params);
      case "loadSeriesByName":
        return loadSeriesByName(params);
      case "listFontNames":
        return listFontNames(params);
      case "exit":
      case "quit":
        return exit(params);
      case "listSeries":
        return listSeries(params);
      case "getSeriesDetails":
        return getSeriesDetails(params);
      case "help":
        return help(params);
      case "plot":
        return plot(symbolTable, file, params);
      default:
        throw new Exception("unknown function: " + funcName);
    }
  }
  
  private Object plot(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("plot: too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("plot: missing argument");
    }
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("plot: argument not a string");
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
    System.out.println("int exit();");
    System.out.println("  exits");
    System.out.println("  returns 0\n");
    System.out.println("int exit(int code);");
    System.out.println("  exits");
    System.out.println("  returns code\n");
    System.out.println("String getSeriesDetails(String seriesName);");
    System.out.println("  gets series details (including data) as a string");
    System.out.println("  returns series details\n");
    System.out.println("int help();");
    System.out.println("  prints out this screen");
    System.out.println("  returns 0\n");
    System.out.println("int listSeries();");
    System.out.println("  list series");
    System.out.println("  returns 0\n");
    System.out.println("Series loadSeriesByName(String seriesName);");
    System.out.println("  loads series by name");
    System.out.println("  returns Series\n");
    System.out.println("int plot(String fileName);");
    System.out.println("  plots series as defined in fileName");
    System.out.println("  returns 0\n");
    System.out.println("int listFontNames();");
    System.out.println("  lists system font names");
    System.out.println("  returns 0\n");
    System.out.println("int println();");
    System.out.println("  prints a carriage return");
    System.out.println("  returns 0\n");
    System.out.println("Object println(Object object);");
    System.out.println("  prints object");
    System.out.println("  returns Object\n");
    System.out.println("int quit();");
    System.out.println("  exits");
    System.out.println("  returns 0\n");
    System.out.println("int quit(int code);");
    System.out.println("  exits");
    System.out.println("  returns code\n");
    return 0;
  }
  
  private Object getSeriesDetails(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("getSeriesDetails: too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("getSeriesDetails: missing argument");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("getSeriesDetails: not a series");
    }
    
    TimeSeries timeSeries  = (TimeSeries) params.get(0);
    return timeSeries.toStringVerbose();
  }
  
  private Object listFontNames(List<Object> params) throws Exception {
    if (params.size() != 0) {
      throw new Exception("printFontNames: too many arguments");
    }
    
    Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    for (Font font: fonts) {
      System.out.println(font.getFontName());
    }  
    return 0;
  }
  
  private Object exit(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("exit: too many arguments");
    }
    
    if (params.size() == 0) {
      System.exit(0);
    }
    
    int value = Integer.parseInt(params.get(0).toString());
    System.exit(value);
    return 0;
  }
  
  private Object listSeries(List<Object> params) throws Exception {
    if (params.size() > 0) {
      throw new Exception("listSeries: too many arguments");
    }
    
    List<TimeSeries> list = TimeSeriesDAO.getInstance().listSeries();
    System.out.println("   Id                                    Name");
    System.out.println("---------------------------------------------");
    for (TimeSeries timeSeries: list) {
      System.out.printf("%5d%40s\n", timeSeries.getId(), timeSeries.getName());
    }
    return 0;
  }
  
  private Object loadSeriesByName(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("loadSeriesByName: too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("loadSeriesByName: missing argument");
    }
    
    return TimeSeriesDAO.getInstance().loadSeriesByName(params.get(0).toString());
  }
  
  private Object println(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("println: too many arguments");
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
