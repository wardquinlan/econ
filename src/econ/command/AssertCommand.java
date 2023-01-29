package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeries;
import econ.core.Utils;
import econ.parser.Symbol;

public class AssertCommand implements Command {
  @Override
  public String getSummary() {
    return "int    assert(Boolean condition[, String message]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Asserts 'condition' is true.  If not, throws an exception (with 'message' as text, if given)");
    list.add("(Note that this is primarily a test function)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    if (params.size() < 1) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof Boolean)) {
      throw new Exception("'condition' is not a Boolean");
    }

    String message;
    if (params.size() == 2) {
      if (!(params.get(1) instanceof String)) {
        throw new Exception("'message' is not a String");
      }
      message = (String) params.get(1);
    } else {
      message = "assertion failed";
    }
    
    Utils.ASSERT((Boolean) params.get(0), message);
    return 0;
  }
}
