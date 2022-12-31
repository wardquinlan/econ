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
           funcName.equals("printFontNames")   ||
           funcName.equals("getSeriesDetails") ||
           funcName.equals("usage")            ||
           funcName.equals("plot")             ||
           funcName.equals("exit");
  }
  
  public Object invokeFunction(String funcName, Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    switch(funcName) {
      case "println":
        return println(params);
      case "loadSeriesByName":
        return loadSeriesByName(params);
      case "printFontNames":
        return printFontNames(params);
      case "exit":
        return exit(params);
      case "getSeriesDetails":
        return getSeriesDetails(params);
      case "usage":
        return usage(params);
      case "plot":
        return plot(symbolTable, params);
      default:
        throw new Exception("unknown function: " + funcName);
    }
  }
  
  private Object plot(Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    if (params.size() > 2) {
      throw new Exception("plot: too many arguments");
    }
    
    if (params.size() == 1) {
      throw new Exception("plot: missing argument");
    }
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("plot: argument not a string");
    }
    
    String filename = (String) params.get(0);
    File file = (File) params.get(1);
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
  
  private Object usage(List<Object> params) throws Exception {
    System.out.println("usage:");
    System.out.println();
    System.out.println("exit()");
    System.out.println("  exits with status code of 0");
    System.out.println("exit(CODE)");
    System.out.println("  exits with status code of CODE");
    System.out.println("println(OBJECT)");
    System.out.println("  prints OBJECT");
    System.out.println("  returns OBJECT");
    System.out.println("getSeriesDetails(SERIES)");
    System.out.println("  gets SERIES details (including values) as a string");
    System.out.println("  returns SERIES details as a string");
    System.out.println("loadSeriesByName(SERIES)");
    System.out.println("  loads SERIES by its name");
    System.out.println("  returns SERIES");
    System.out.println("plot(DESCRIPTOR-FILE)");
    System.out.println("  plots series as defined in DESCRIPTOR-FILE");
    System.out.println("  returns 0");
    System.out.println("printFontNames()");
    System.out.println("  prints a list of all system font names");
    System.out.println("  returns 0");
    System.out.println("println()");
    System.out.println("  prints a carriage return");
    System.out.println("  returns 0");
    System.out.println("println(OBJECT)");
    System.out.println("  prints OBJECT");
    System.out.println("  returns OBJECT");
    System.out.println("usage()");
    System.out.println("  prints out this screen");
    System.out.println("  returns 0");
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
  
  private Object printFontNames(List<Object> params) throws Exception {
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
      TimeSeriesDAO.getInstance().close();
      System.exit(0);
    }
    
    int value = Integer.parseInt(params.get(0).toString());
    TimeSeriesDAO.getInstance().close();
    System.exit(value);
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
