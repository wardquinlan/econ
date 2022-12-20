package econ;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);
  public boolean isFunction(String funcName) {
    return funcName.equals("println");
  }
  
  public Object invokeFunction(String funcName, List<Object> params) throws Exception {
    switch(funcName) {
      case "println":
        return println(params);
      default:
        throw new Exception("unknown function: " + funcName);
    }
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
