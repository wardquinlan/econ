package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.Symbol;

public class Load implements Command {
  @Override
  public String getSummary() {
    return "Series load(Object object);";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Loads a series from the database into memory, using 'object' as either an id or a name");
    return list;
  }

  @Override
  public String getReturns() {
    return "Series, or null if not found";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    return Utils.load(params.get(0));
  }
}
