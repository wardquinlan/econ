package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.core.MergeData;
import es.core.Settings;
import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.Symbol;

public class Merge implements Command {
  @Override
  public String getSummary() {
    return "void    merge(Series series[, String option[, String option[, String option]]]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Merges 'series' in the catalog into the datastore");
    list.add("3 options are supported as follows:");
    list.add("  --with-updates - if set, also merges updates to data");
    list.add("  --with-deletes - if set, also merges deletions to data (requires administrative mode)");
    list.add("  --with-metadata - if set, also merges updates to metadate (requires administrative mode)");
    list.add("");
    list.add("(Note that inserts are automatically merged)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 4);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    boolean mergeUpdates = false;
    boolean mergeDeletes = false;
    boolean mergeMetaData = false;
    for (int i = 1; i < params.size(); i++) {
      if (params.get(i).equals("--with-updates")) {
        mergeUpdates = true;
      } else if (params.get(i).equals("--with-deletes")) {
        Utils.validateIsAdmin();
        mergeDeletes = true;
      } else if (params.get(i).equals("--with-metadata")) {
        Utils.validateIsAdmin();
        mergeMetaData = true;
      } else {
        throw new Exception("unrecognized merge option: " + params.get(i));
      }
    }
    TimeSeries timeSeriesCat = (TimeSeries) params.get(0);
    if (timeSeriesCat.getId() == null) {
      throw new Exception("series id is null");
    }
    if (timeSeriesCat.getName() == null) {
      throw new Exception("series name is null");
    }
    if (timeSeriesCat.getType() != TimeSeries.FLOAT) {
      throw new Exception("can only merge float series");
    }
    if (timeSeriesCat.size() > 0 && Utils.offset(timeSeriesCat) != 0) {
      throw new Exception("cannot merge series containing null values");
    }
    TimeSeries timeSeriesDS = Utils.load(timeSeriesCat.getId());
    if (timeSeriesDS == null) {
      throw new Exception("series not found");
    }
    if (!timeSeriesCat.getName().equals(timeSeriesDS.getName())) {
      throw new Exception("series names not equal: DS=" + timeSeriesDS.getName() + ", CAT=" + timeSeriesCat.getName());
    }
    if (mergeMetaData && timeSeriesCat.getTitle() == null) {
      throw new Exception("series title is null");
    }
    MergeData mergeData = Utils.prepareMerge(timeSeriesCat, timeSeriesDS, mergeUpdates, mergeDeletes, mergeMetaData);
    TimeSeriesDAO.getInstance().merge(mergeData);
    log.info("series merged");
    return null;
  }
}
