package econ;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);
  public boolean isFunction(String funcName) {
    return funcName.equals("println")          || 
           funcName.equals("loadSeriesByName") ||
           funcName.equals("listFontNames")    ||
           funcName.equals("exit");
  }
  
  public Object invokeFunction(String funcName, List<Object> params) throws Exception {
    switch(funcName) {
      case "println":
        return println(params);
      case "loadSeriesByName":
        return loadSeriesByName(params);
      case "listFontNames":
        return listFontNames(params);
      case "exit":
        return exit(params);
      default:
        throw new Exception("unknown function: " + funcName);
    }
  }
  
  private Object listFontNames(List<Object> params) throws Exception {
    if (params.size() != 0) {
      throw new Exception("listFontNames: too many arguments");
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
