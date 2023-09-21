package es.command;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.parser.SymbolTable;

/*
  TEMPLATE:
  
  @Override
  public String getSummary() {
    return "";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
  }
  
*/

public interface Command {
  public static Log log = LogFactory.getFactory().getInstance(Command.class);
  
  public abstract String getSummary();
  public abstract List<String> getDetails();
  public abstract String getReturns();
  public abstract Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception;
}
