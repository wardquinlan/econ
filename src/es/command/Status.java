package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.Settings;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.Symbol;

public class Status implements Command {
  @Override
  public String getSummary() {
    return "int    status();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Displays ES status information");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    System.out.println("version   : " + Settings.getInstance().getVersion());
    System.out.println("datastore : " + (TimeSeriesDAO.getInstance().getDatastore() == null ? "not connected" : TimeSeriesDAO.getInstance().getDatastore()));
    System.out.println("admin mode: " + Settings.getInstance().isAdmin());
    return 0;
  }
}
