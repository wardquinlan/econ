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
    return funcName.equals("println")            ||
           funcName.equals("print")              ||
           funcName.equals("loadSeriesByName")   ||
           funcName.equals("loadSeriesById")     ||
           funcName.equals("listFontNames")      ||
           funcName.equals("printSeries")        ||
           funcName.equals("listSeries")         ||
           funcName.equals("help")               ||
           funcName.equals("plot")               ||
           funcName.equals("quit")               ||
           funcName.equals("exit");
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
    System.out.println("int exit([int code]);");
    System.out.println("int quit([int code]);");
    System.out.println("  exits");
    System.out.println("  returns code (0 if not supplied)\n");
    System.out.println("int help();");
    System.out.println("  prints out this screen");
    System.out.println("  returns 0\n");
    System.out.println("int listSeries();");
    System.out.println("  list series");
    System.out.println("  returns 0\n");
    System.out.println("Series loadSeriesById(int id);");
    System.out.println("  loads series by id");
    System.out.println("  returns Series (null if not found)\n");
    System.out.println("Series loadSeriesByName(String seriesName);");
    System.out.println("  loads series by name");
    System.out.println("  returns Series (null if not found)\n");
    System.out.println("int plot(String fileName);");
    System.out.println("  plots series as defined in fileName");
    System.out.println("  returns 0\n");
    System.out.println("int listFontNames();");
    System.out.println("  lists system font names");
    System.out.println("  returns 0\n");
    System.out.println("Object println([Object object]);");
    System.out.println("Object print([Object object]);");
    System.out.println("  prints object (empty line if not supplied)");
    System.out.println("  returns Object (0 if not supplied)\n");
    System.out.println("Series printSeries(Series series);");
    System.out.println("  print series details (including data)");
    System.out.println("  returns series\n");
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
  
  private Object listSeries(List<Object> params) throws Exception {
    if (params.size() > 0) {
      throw new Exception("too many arguments");
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
