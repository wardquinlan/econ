package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.SymbolTable;

public class GetSeriesType implements Command {
  @Override
  public String getSummary() {
    return "String  ES:GetSeriesType(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Returns series type of 'series' represented as a String:");
    list.add("  - 'null'");
    list.add("  - 'float'");
    list.add("  - 'boolean'");
    list.add("  - 'Date'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series type of 'series', represented as a String";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a series");
    }
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    switch(timeSeries.getType()) {
      case TimeSeries.NULL:
        return "null";
      case TimeSeries.FLOAT:
        return "float";
      case TimeSeries.BOOLEAN:
        return "boolean";
      case TimeSeries.DATE:
        return "date";
      default:
        throw new Exception("unknown type: " + timeSeries.getType());
    }
  }
}
