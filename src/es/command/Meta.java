package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.SymbolTable;

public class Meta implements Command {
  @Override
  public String getSummary() {
    return "void    meta(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Shows series metadata associated with 'object', where 'object' is one of:");
    list.add("  - a series");
    list.add("  - an id");
    list.add("");
    list.add("If 'object' is an id, series metadata is displayed directly from the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    TimeSeries timeSeries;
    if (params.get(0) instanceof TimeSeries) {
      timeSeries = (TimeSeries) params.get(0);
    } else if (params.get(0) instanceof Integer) {
      timeSeries = TimeSeriesDAO.getInstance().loadSeriesById((Integer) params.get(0));
      if (timeSeries == null) {
        throw new Exception(params.get(0) + " not found");
      }
    } else {
      throw new Exception(params.get(0) + " is neither a Series nor an int");
    }

    System.out.println("Id       : " + (timeSeries.getId() == null ? "" : timeSeries.getId()));
    System.out.println("Name     : " + Utils.stringWithNULL(timeSeries.getName()));
    System.out.println("Title    : " + Utils.stringWithNULL(timeSeries.getTitle()));
    System.out.println("Source   : " + Utils.stringWithNULL(timeSeries.getSource()));
    System.out.println("Source Id: " + Utils.stringWithNULL(timeSeries.getSourceId()));
    System.out.println("Size     : " + timeSeries.size());
    if (timeSeries.size() > 0) {
      System.out.println("Last     : " + timeSeries.get(timeSeries.size() - 1).getValue() + " (" +
        Utils.DATE_FORMAT.format(timeSeries.get(timeSeries.size() - 1).getDate()) + ")");
    }
    String change = Utils.change(timeSeries);
    if (change != null) {
      System.out.println("Change   : " + change);
    }
    if (timeSeries.getNotes() != null) {
      System.out.println();
      System.out.println(timeSeries.getNotes());
    }
    return null;
  }
}
