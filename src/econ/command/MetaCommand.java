package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.TimeSeries;
import econ.Utils;
import econ.parser.Symbol;

public class MetaCommand implements Command {
  @Override
  public String getSummary() {
    return "int meta(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Shows series metadata associated with 'object', where 'object' is:");
    list.add("  - an id");
    list.add("  - a name");
    list.add("  - a Series");
    list.add("");
    list.add("Note that if 'object' is a series, meta() shows series metadata from the catalog; otherwise, meta() shows");
    list.add("series metadata from the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }

    TimeSeries timeSeries;
    Object object = params.get(0);
    if (object instanceof TimeSeries) {
      timeSeries = (TimeSeries) object;
    } else {
      timeSeries = Utils.load(params.get(0));
      if (timeSeries == null) {
        throw new Exception("time series not found: " + params.get(0));
      }
    }

    System.out.println("Id       : " + (timeSeries.getId() == null ? "NULL" : timeSeries.getId()));
    System.out.println("Name     : " + Utils.stringWithNULL(timeSeries.getName()));
    System.out.println("Title    : " + Utils.stringWithNULL(timeSeries.getTitle()));
    System.out.println("Source   : " + Utils.stringWithNULL(timeSeries.getSource()));
    System.out.println("Source Id: " + Utils.stringWithNULL(timeSeries.getSourceId()));
    System.out.println("Size     : " + timeSeries.size());
    if (timeSeries.getNotes() != null) {
      System.out.println();
      System.out.println(timeSeries.getNotes());
    }
    return 0;
  }
}
