#################################################################################
# Load functions
#################################################################################
function ES:LoadSeries(series) {
  ES:Log(DEBUG, 'ES:LoadSeries()');
  ES:Log(DEBUG, series);
  if (series == null) {
    ES:Log(WARN, 'series is null, returning null');
    return null;
  }
  if (ES:GetType(series) == 'String') {
    if (ES:Exists(series)) {
      ES:Log(DEBUG, 'series exists in the datastore; loading');
      return ES:Load(series);
    }
    ES:Log(DEBUG, 'attempting to load series from FRED');
    return ES:Fred(series);
  }
  if (ES:GetType(series) == 'int') {
    if (ES:Exists(series)) {
      ES:Log(DEBUG, 'series exists in the datastore; loading');
      return ES:Load(series);
    }
    ES:Log(WARN, 'series not found; returning null');
    return null; 
  }
  if (ES:GetType(series) == 'Series') {
    if (ES:GetId(series) != null and ES:Exists(ES:GetId(series))) {
      ES:Log(DEBUG, 'series exists in the datastore; loading');
      return ES:Load(ES:GetId(series));
    }
    ES:Log(WARN, 'series does not existing in the datastore, returning series as is');
    return series;
  }
  ES:Log(WARN, 'unable to find the series, returning null');
  return null;
}

function ES:AutoLoad(series) {
  ES:Log(DEBUG, 'ES:AutoLoad()');
  ES:Log(DEBUG, series);
  series = ES:LoadSeries(series);
  # don't load the backups...
  if (ES:GetId(series) < 10000) {
    ES:Log(DEBUG, 'id = ' + getId(series) + ' < 10000; putting series into global scope'); 
    name = ES:GetName(series);
    ES:Log(DEBUG, 'name = ' + name);
    ES:GPut(name, series);
  } 
}

loadSeries = ES:LoadSeries;
autoLoad = ES:AutoLoad; 

#################################################################################
# Update functions
#################################################################################
function ES:UpdateSeries(series) {
  ES:Log(DEBUG, 'ES:UpdateSeries()');
  ES:Log(DEBUG, series);
  series = ES:LoadSeries(series);
  if (ES:GetSource(series) == 'FRED' and ES:GetId(series) < 10000) {
    ES:Log(DEBUG, 'series is a candidate for update(s); proceeding');
    id = ES:GetId(series);
    name = ES:GetName(series);
    ES:Log(INFO, 'updating ' + id + ':' + name + '...');
    series = ES:Fred(name);
    ES:SetId(series, id);
    ES:Merge(series, '--with-inserts');
  }
}

function ES:UpdateAll() {
  ES:Log(DEBUG, 'ES:UpdateAll()');
  ES:Ds(updateSeries);
}

updateSeries = ES:UpdateSeries;
updateAll = ES:UpdateAll;

#################################################################################
# Reset functions
#################################################################################
function ES:ResetId(id, idNew) {
  if (!ES:IsAdmin()) {
    throw 'you must be running in administrative mode to reset id\'s';
  }
  if (ES:GetType(id) != 'int' or ES:GetType(idNew) != 'int') {
    throw 'usage: ES:ResetId(int id, int idNew);';
  }
  if (ES:Exists(idNew)) {
    throw 'series already exists: ' + idNew;
  } 
  S = ES:Load(id);
  ES:SetId(S, idNew);
  ES:Drop(id);
  ES:Save(S);
}

function ES:ResetName(name, nameNew) {
  if (!ES:IsAdmin()) {
    throw 'you must be running in administrative mode to reset name\'s';
  }
  if (ES:GetType(name) != 'String' or ES:GetType(nameNew) != 'String') {
    throw 'usage: ES:ResetName(String name, String nameNew);';
  }
  if (ES:Exists(nameNew)) {
    throw 'series already exists: ' + nameNew;
  } 
  S = ES:Load(name);
  ES:SetName(S, nameNew);
  ES:Drop(ES:GetId(S));
  ES:Save(S);
}

resetId = ES:ResetId;
resetName = ES:ResetName;

#################################################################################
# Backup function
#################################################################################
function ES:Backup(id) {
  if (!ES:IsAdmin()) {
    throw 'you must be running in administrative mode to do backups';
  }
  if (ES:GetType(id) != 'int') {
    throw 'usage: ES:Backup(int id);';
  }
  if (id >= 10000) {
    throw 'id must be < 10000';
  }
  S = ES:Load(id);
  if (ES:Exists(ES:GetId(S) + 10000)) {
    throw 'backup for series already exists: ' + id + '; drop the backup first and try again';
  }
  ES:Print('backing up series ' + id + '...');
  ES:SetName(S, ES:GetName(S) + '.bak');
  ES:SetId(S, ES:GetId(S) + 10000);
  ES:Print('backup series name = ' + ES:GetName(S));
  ES:Print('backup series id = ' + ES:GetId(S));
  ES:Save(S);
}

backup = ES:Backup;

#################################################################################
# Highest / Lowest functions
#################################################################################
function ES:Hh(idx, d, v) {
  if (v > ES:GGet('METRICS.highest')) {
    ES:GPut('METRICS.highest', v);
  }
}

function ES:Ll(idx, d, v) {
  if (v < ES:GGet('METRICS.lowest')) {
    ES:GPut('METRICS.lowest', v);
  }
}

function ES:Highest(series) {
  if (ES:GetType(series) == 'int' or ES:GetType(series) == 'String') {
    series = ES:LoadSeries(series);
  }
  if (ES:GetSize(series) == 0) {
    return null;
  }
  ES:GPut('METRICS.highest', ES:Get(series, 0));
  ES:Data(series, ES:Hh);
  return ES:GGet('METRICS.highest');
}

function ES:Lowest(series) {
  if (ES:GetType(series) == 'int' or ES:GetType(series) == 'String') {
    series = ES:LoadSeries(series);
  }
  if (ES:GetSize(series) == 0) {
    return null;
  }
  ES:GPut('METRICS.lowest', ES:Get(series, 0));
  ES:Data(series, ES:Ll);
  return ES:GGet('METRICS.lowest');
}

hh = ES:Hh;
ll = ES:Ll;
highest = ES:Highest;
lowest = ES:Lowest;

#################################################################################
# Usage functions
#################################################################################
function ES:Metrics(series) {
  series = ES:LoadSeries(series);
  if (ES:GetId(series) < 10000) {
    ES:GPut('METRICS.numberOfSeries', METRICS.numberOfSeries + 1);
    ES:GPut('METRICS.numberOfRecords', METRICS.numberOfRecords + ES:GetSize(series));
    ES:Printf('%-20s%8d\n', ES:GetName(series), ES:GetSize(series));
  }
}

function ES:Usage() {
  ES:GPut('METRICS.numberOfSeries', 0);
  ES:GPut('METRICS.numberOfRecords', 0);
  ES:Print('Series Metrics');
  ES:Print('----------------------------');
  ES:Ds(metrics);
  ES:Print('');
  ES:Print('Series stored in datastore: ' + METRICS.numberOfSeries + ' (excluding backup series)');
  ES:Print('Number of records stored in datastore: ' + METRICS.numberOfRecords);
}

function ES:Defaults() {
  ES:Print('defaults.panel.backgroundcolor = Color');
  ES:Print('defaults.panel.dxincr = int');
  ES:Print('defaults.panel.gridlinetextwidth = int');
  ES:Print('defaults.panel.fontname = String');
  ES:Print('defaults.panel.fontcolor = Color');
  ES:Print('defaults.panel.fontsize = int');
  ES:Print('defaults.panel.frequency = NONE | DAYS | MONTHS | YEARS');
  ES:Print('defaults.panel.label = String');
  ES:Print('defaults.chart.backgroundcolor = Color');
  ES:Print('defaults.chart.linecolor = Color');
  ES:Print('defaults.chart.rectcolor = Color');
  ES:Print('defaults.chart.ngridlines = int');
  ES:Print('defaults.chart.label = String');
  ES:Print('defaults.chart.scaletype = LINEAR | LOG');
  ES:Print('defaults.series.linecolor0 = Color');
  ES:Print('defaults.series.linecolor1 = Color');
  ES:Print('defaults.series.linecolor2 = Color');
  ES:Print('defaults.series.linecolor3 = Color');
}

metrics = ES:Metrics;
usage = ES:Usage;
defaults = ES:Defaults;
