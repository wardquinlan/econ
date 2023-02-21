package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Assert implements Command {
  @Override
  public String getSummary() {
    return "void    assert(boolean condition[, String message]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Asserts 'condition' is true.  If not, throws an exception (with 'message' as text, if given)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 2);
    
    if (!(params.get(0) instanceof Boolean)) {
      throw new Exception(params.get(0) + " is not a boolean");
    }

    String message;
    if (params.size() == 2) {
      if (!(params.get(1) instanceof String)) {
        throw new Exception(params.get(1) + " is not a String");
      }
      message = (String) params.get(1);
    } else {
      message = "assertion failed";
    }
    
    Utils.ASSERT((Boolean) params.get(0), message);
    return null;
  }
}
