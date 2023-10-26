package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.SymbolTable;

public class IsConnected implements Command {
  @Override
  public String getSummary() {
    return "boolean ES:IsConnected();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Determines if a database connection is established");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "true if a database connection is established; false otherwise";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    return (TimeSeriesDAO.getInstance().getDatastore() != null);
  }
}
