#################################################################################
# Load functions
#################################################################################
function ES:LoadSeries(series) {
  if (series == null) {
    throw 'cannot load series: ' + series;
  }
  if (getType(series) != 'Series') {
    series = load(series);
  }
  if (getSize(series) == 0) {
    series = load(getId(series));
  }
  return series;
}

function ES:AutoLoad(series) {
  series = loadSeries(series);
  # don't load the backups...
  if (getId(series) < 10000) {
    name = getName(series);
    gPut(name, series);
  } 
}

loadSeries = ES:LoadSeries;
autoLoad = ES:AutoLoad; 

#################################################################################
# Update functions
#################################################################################
function ES:UpdateSeries(series) {
  series = loadSeries(series);
  if (getSource(series) == 'FRED' and getId(series) < 10000) {
    id = getId(series);
    name = getName(series);
    log(INFO, 'updating ' + id + ':' + name + '...');
    series = fred(name);
    setId(series, id);
    merge(series, '--with-inserts');
  }
}

function ES:UpdateAll() {
  ds(updateSeries);
}

updateSeries = ES:UpdateSeries;
updateAll = ES:UpdateAll;

#################################################################################
# Reset functions
#################################################################################
function ES:ResetId(id, idNew) {
  if (!isAdmin()) {
    throw 'you must be running in administrative mode to reset id\'s';
  }
  if (getType(id) != 'int' or getType(idNew) != 'int') {
    throw 'usage: resetId(int id, int idNew);';
  }
  if (exists(idNew)) {
    throw 'series already exists: ' + idNew;
  } 
  S = load(id);
  setId(S, idNew);
  drop(id);
  save(S);
}

function ES:ResetName(name, nameNew) {
  if (!isAdmin()) {
    throw 'you must be running in administrative mode to reset name\'s';
  }
  if (getType(name) != 'String' or getType(nameNew) != 'String') {
    throw 'usage: resetName(String name, String nameNew);';
  }
  if (exists(nameNew)) {
    throw 'series already exists: ' + nameNew;
  } 
  S = load(name);
  setName(S, nameNew);
  drop(getId(S));
  save(S);
}

resetId = ES:ResetId;
resetName = ES:ResetName;

#################################################################################
# Backup
#################################################################################
function ES:Backup(id) {
  if (!isAdmin()) {
    throw 'you must be running in administrative mode to do backups';
  }
  if (getType(id) != 'int') {
    throw 'usage: backup(int id);';
  }
  if (id >= 10000) {
    throw 'id must be < 10000';
  }
  S = load(id);
  if (exists(getId(S) + 10000)) {
    throw 'backup for series already exists: ' + id + '; drop the backup first and try again';
  }
  print('backing up series ' + id + '...');
  setName(S, getName(S) + '.bak');
  setId(S, getId(S) + 10000);
  print('backup series name = ' + getName(S));
  print('backup series id = ' + getId(S));
  save(S);
}

backup = ES:Backup;

#################################################################################
# Highest / Lowest functions
#################################################################################
function ES:Hh(idx, d, v) {
  if (v > gGet('METRICS.highest')) {
    gPut('METRICS.highest', v);
  }
}

function ES:Ll(idx, d, v) {
  if (v < gGet('METRICS.lowest')) {
    gPut('METRICS.lowest', v);
  }
}

function ES:Highest(series) {
  series = loadSeries(series);
  gPut('METRICS.highest', get(series, 0));
  data(series, hh);
  return gGet('METRICS.highest');
}

function ES:Lowest(series) {
  series = loadSeries(series);
  gPut('METRICS.lowest', get(series, 0));
  data(series, ll);
  return gGet('METRICS.lowest');
}

hh = ES:Hh;
ll = ES:Ll;
highest = ES:Highest;
lowest = ES:Lowest;

#################################################################################
# Usage functions
#################################################################################
function ES:Metrics(series) {
  series = loadSeries(series);
  if (getId(series) < 10000) {
    gPut('METRICS.numberOfSeries', METRICS.numberOfSeries + 1);
    gPut('METRICS.numberOfRecords', METRICS.numberOfRecords + getSize(series));
    printf('%-20s%8d\n', getName(series), getSize(series));
  }
}

function ES:Usage() {
  gPut('METRICS.numberOfSeries', 0);
  gPut('METRICS.numberOfRecords', 0);
  print('Series Metrics');
  print('----------------------------');
  ds(metrics);
  print('');
  print('Series stored in datastore: ' + METRICS.numberOfSeries + ' (excluding backup series)');
  print('Number of records stored in datastore: ' + METRICS.numberOfRecords);
}

function ES:Defaults() {
  print('defaults.panel.backgroundcolor = Color');
  print('defaults.panel.dxincr = int');
  print('defaults.panel.gridlinetextwidth = int');
  print('defaults.panel.fontname = String');
  print('defaults.panel.fontcolor = Color');
  print('defaults.panel.fontsize = int');
  print('defaults.panel.frequency = NONE | DAYS | MONTHS | YEARS');
  print('defaults.panel.label = String');
  print('defaults.chart.backgroundcolor = Color');
  print('defaults.chart.linecolor = Color');
  print('defaults.chart.rectcolor = Color');
  print('defaults.chart.ngridlines = int');
  print('defaults.chart.label = String');
  print('defaults.chart.scaletype = LINEAR | LOG');
  print('defaults.series.linecolor0 = Color');
  print('defaults.series.linecolor1 = Color');
  print('defaults.series.linecolor2 = Color');
  print('defaults.series.linecolor3 = Color');
}

metrics = ES:Metrics;
usage = ES:Usage;
defaults = ES:Defaults;

