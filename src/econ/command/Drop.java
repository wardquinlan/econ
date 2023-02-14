package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.core.TimeSeriesDAO;
import econ.core.Utils;
import econ.parser.Symbol;

public class Drop implements Command {
  @Override
  public String getSummary() {
    return "int    drop(int id)";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Drops the series identified by 'id' from the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof Integer)) {
      throw new Exception("'id' is not an int");
    }
    int id = (int) params.get(0);
    TimeSeriesDAO.getInstance().drop(id);
    return 0;
  }
}
