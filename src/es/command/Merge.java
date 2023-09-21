package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.MergeData;
import es.core.TimeSeries;
import es.core.TimeSeriesDAO;
import es.core.Utils;
import es.parser.SymbolTable;

public class Merge implements Command {
  @Override
  public String getSummary() {
    return "void    merge(Series series, String options...);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Merges 'series' in the catalog into the datastore");
    list.add("The following options are supported as follows:");
    list.add("  --dry-run       - if set, computes the merge but does not actually apply it");
    list.add("  --with-inserts  - if set, merges inserts");
    list.add("  --with-updates  - if set, merges updates (requires administrative mode)");
    list.add("  --with-deletes  - if set, merges deletions (requires administrative mode)");
    list.add("  --with-metadata - if set, merges updates to metadate (requires administrative mode)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 6);
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception(params.get(0) + " is not a Series");
    }
    boolean dryRun = false;
    boolean mergeInserts = false;
    boolean mergeUpdates = false;
    boolean mergeDeletes = false;
    boolean mergeMetaData = false;
    for (int i = 1; i < params.size(); i++) {
      if (params.get(i).equals("--with-inserts")) {
        mergeInserts = true;
      } else if (params.get(i).equals("--with-updates")) {
        mergeUpdates = true;
      } else if (params.get(i).equals("--with-deletes")) {
        mergeDeletes = true;
      } else if (params.get(i).equals("--with-metadata")) {
        mergeMetaData = true;
      } else if (params.get(i).equals("--dry-run")) {
        dryRun = true;
      } else {
        throw new Exception("unrecognized merge option: " + params.get(i));
      }
    }
    if (!mergeInserts && !mergeUpdates && !mergeDeletes && !mergeMetaData) {
      throw new Exception("at least one merge option is required");
    }
    if (!dryRun && (mergeUpdates || mergeDeletes || mergeMetaData)) {
      Utils.validateIsAdmin();
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
    MergeData mergeData = Utils.prepareMerge(timeSeriesCat, timeSeriesDS, mergeInserts, mergeUpdates, mergeDeletes, mergeMetaData);
    if (!mergeMetaData &&
        mergeData.getTimeSeriesInsert().size() == 0 &&
        mergeData.getTimeSeriesUpdate().size() == 0 &&
        mergeData.getTimeSeriesDelete().size() == 0) {
      log.info("nothing to merge: " + timeSeriesCat.getId());
      return null;
    }
    if (dryRun) {
      log.info("suppressing physical merge with --dry-run option");
      return null;
    }
    TimeSeriesDAO.getInstance().merge(mergeData);
    log.info("series merged: " + timeSeriesCat.getId());
    return null;
  }
}
