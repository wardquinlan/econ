package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeriesDAO;
import econ.parser.Symbol;

public class ConnectCommand implements Command {
  @Override
  public String getSummary() {
    return "int    connect(String host, String database, String username[, String password])";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates a connection to the database");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 4) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() < 3) {
      throw new Exception("missing arguments");
    }
    
    if (!(params.get(0) instanceof String) ||
        !(params.get(1) instanceof String) ||
        !(params.get(2) instanceof String) ||
        (params.size() == 4 && !(params.get(3) instanceof String))) {
      throw new Exception("argument(s) is/are not of type String");
    }
    
    TimeSeriesDAO.getInstance().connect((String) params.get(0), (String) params.get(1), (String) params.get(2), params.size() == 3 ? null : (String) params.get(3));
    System.out.println("connected to datastore '" + params.get(1) + "'");
    return 0;
  }
}
