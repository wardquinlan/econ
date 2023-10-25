package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.parser.SymbolTable;

public class Equals implements Command {
  @Override
  public String getSummary() {
    return "Series  equals(Series series1, Series series2);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Generates a boolean series with values representing the equality between 'series1' and 'series2'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "A new series containing boolean values representing the equality between 'series1' and 'series2'";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    return null;
  }
}
