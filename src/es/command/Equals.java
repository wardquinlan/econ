package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Executor;
import es.parser.SymbolTable;

public class Equals implements Command {
  @Override
  public String getSummary() {
    return "Object  equals(Object object1, Object object2);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Performs a deep equality comparison between 'object1' and 'object2'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "A Series or a Boolean representing the equality between 'object1' and 'object2'";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    Executor executor = new Executor(new es.parser.Eq());
    return executor.exec(params.get(0), params.get(1));
  }
}
