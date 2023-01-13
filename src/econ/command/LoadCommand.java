package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;
import econ.TimeSeriesDAO;

public class LoadCommand extends Command {
  public LoadCommand() {
    super("load");
  }
  
  @Override
  public String getSummary() {
    return "Series load(Object object);";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("loads a series from the database into memory, using 'object' as either an id or a name");
    return list;
  }

  @Override
  public String getReturns() {
    return "Series, or null if not found";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (params.get(0) instanceof Integer) {
      return TimeSeriesDAO.getInstance().loadSeriesById(Integer.parseInt(params.get(0).toString()));
    } else if (params.get(0) instanceof String) {
      return TimeSeriesDAO.getInstance().loadSeriesByName(params.get(0).toString());
    } else {
      throw new Exception("unexpected argument: " + params.get(0));
    }
  }
}
