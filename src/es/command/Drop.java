package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.Symbol;

public class Drop implements Command {
  @Override
  public String getSummary() {
    return "void   drop(int id)";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Drops the series identified by 'id' from the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validateIsAdmin();
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof Integer)) {
      throw new Exception(params.get(0) + " is not an int");
    }
    int id = (int) params.get(0);
    TimeSeriesDAO.getInstance().drop(id);
    log.info("series dropped");
    return null;
  }
}
