package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Timestamp implements Command {
  @Override
  public String getSummary() {
    return "String  ES:Timestamp();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns the current date and time");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "The current time";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    Date date = new Date();
    return date.toString();
  }
}
