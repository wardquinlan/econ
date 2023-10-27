package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.SymbolTable;

public class Connect implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Connect(String host, String database, String username[, String password]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates a connection to the database");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 3, 4);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a String");
    }
    if (!(params.get(2) instanceof String)) {
      throw new Exception(params.get(2) + " is not a String");
    }
    if (params.size() == 4 && !(params.get(3) instanceof String)) {
      throw new Exception(params.get(3) + " is not a String");
    }
    
    TimeSeriesDAO.getInstance().connect((String) params.get(0), (String) params.get(1), (String) params.get(2), params.size() == 3 ? null : (String) params.get(3));
    log.info("connected to datastore '" + params.get(1) + "'");
    return null;
  }
}
