package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Random implements Command {
  @Override
  public String getSummary() {
    return "int     " + Utils.ROOT_NAMESPACE + "Random(int bound);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Generates an integral random number between 0 (inclusive) and 'bound' (exclusive)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Generated random number";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof Integer)) {
      throw new Exception(params.get(0) + " is not an int");
    }
    int bound = (Integer) params.get(0);
    java.util.Random random = new java.util.Random();
    return random.nextInt(bound); 
  }
}
