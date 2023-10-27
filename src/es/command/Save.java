package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.SymbolTable;

public class Save implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Save(Series series);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Saves a series into the datastore");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    if (timeSeries.getType() != TimeSeries.FLOAT) {
      throw new Exception("cannot save a series which is not of type float");
    }
    if (timeSeries.getId() == null) {
      throw new Exception("series id is null");
    }
    if (timeSeries.getTitle() == null) {
      throw new Exception("series title is null");
    }
    TimeSeries timeSeriesTmp = Utils.load(timeSeries.getId());
    if (timeSeriesTmp != null) {
      throw new Exception("series already exists in the datastore: " + timeSeriesTmp.getId());
    }
    timeSeriesTmp = Utils.load(timeSeries.getName());
    if (timeSeriesTmp != null) {
      throw new Exception("series already exists in the datastore: " + timeSeriesTmp.getName());
    }
    TimeSeriesDAO.getInstance().saveSeries(timeSeries);
    log.info("series saved");
    return null;
  }
}
