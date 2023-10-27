package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Today implements Command {
  @Override
  public String getSummary() {
    return "String  " + Utils.ROOT_NAMESPACE + "Today();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns a string representation of today's date");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Today's date as a string";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    return Utils.DATE_FORMAT.format(new Date());
  }
}
