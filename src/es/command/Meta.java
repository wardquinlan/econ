package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;

public class Meta implements Command {
  @Override
  public String getSummary() {
    return "int    meta(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Shows series metadata associated with 'series'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    System.out.println("Id       : " + (timeSeries.getId() == null ? "" : timeSeries.getId()));
    System.out.println("Name     : " + Utils.stringWithNULL(timeSeries.getName()));
    System.out.println("Title    : " + Utils.stringWithNULL(timeSeries.getTitle()));
    System.out.println("Source   : " + Utils.stringWithNULL(timeSeries.getSource()));
    System.out.println("Source Id: " + Utils.stringWithNULL(timeSeries.getSourceId()));
    System.out.println("Size     : " + timeSeries.size());
    if (timeSeries.size() > 0) {
      System.out.println("Last     : " + timeSeries.get(timeSeries.size() - 1).getValue());
    }
    String change = Utils.change(timeSeries);
    if (change != null) {
      System.out.println("Change   : " + change);
    }
    if (timeSeries.getNotes() != null) {
      System.out.println();
      System.out.println(timeSeries.getNotes());
    }
    return 0;
  }
}
