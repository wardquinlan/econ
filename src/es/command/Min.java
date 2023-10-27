package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Executor;
import es.parser.SymbolTable;

public class Min implements Command {
  @Override
  public String getSummary() {
    return "Object  " + Utils.ROOT_NAMESPACE + "Min(Object object1, Object object2);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Computes and returns the minimum between 'object1' and 'object2', where:");
    list.add("  - 'object1' is a Series, an int, or a float");
    list.add("  - 'object2' is a Series, an int, or a float");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Minimum Series, int, or float, depending on the context";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    Object val1 = params.get(0);
    if (!(val1 instanceof TimeSeries) && !(val1 instanceof Float) && !(val1 instanceof Integer)) {
      throw new Exception(val1 + " is not a Series, an int, or a float");
    }
    Object val2 = params.get(1);
    if (!(val2 instanceof TimeSeries) && !(val2 instanceof Float) && !(val2 instanceof Integer)) {
      throw new Exception(val2 + " is not a Series, an int, or a float");
    }
    Executor executor = new Executor(new es.parser.Min());
    return executor.exec(val1, val2);
  }
}
