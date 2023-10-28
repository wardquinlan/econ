const ES:BACKUP_BASE = 10000;
const ES:BACKUP_EXT = '.BAK';

#################################################################################
# Load functions
#################################################################################
function ES:Load(series) {
  :Log(DEBUG, 'ES:Load(): ' + series);
  if (series == null) {
    :Log(DEBUG, 'series is null, returning null');
    return null;
  }
  if (:GetType(series) == 'String') {
    if (:Exists(series)) {
      :Log(DEBUG, 'series exists in the datastore; loading');
      return :Load(series);
    }
    :Log(DEBUG, 'attempting to load series from FRED');
    return :Fred(series);
  }
  if (:GetType(series) == 'int') {
    if (:Exists(series)) {
      :Log(DEBUG, 'series exists in the datastore; loading');
      return :Load(series);
    }
    :Log(DEBUG, 'series not found; returning null');
    return null; 
  }
  if (:GetType(series) == 'Series') {
    if (:GetId(series) != null and :Exists(:GetId(series))) {
      :Log(DEBUG, 'series exists in the datastore; loading');
      return :Load(:GetId(series));
    }
    :Log(DEBUG, 'series does not exist in the datastore, returning series as is');
    return series;
  }
  :Log(WARN, 'unable to find the series, returning null');
  return null;
}

function ES:AutoLoad(series) {
  :Log(DEBUG, 'ES:AutoLoad(): ' + series);
  series = ES:Load(series);
  # don't load the backups...
  if (:GetId(series) < ES:BACKUP_BASE) {
    :Log(DEBUG, 'id = ' + :GetId(series) + ' < ES:BACKUP_BASE; putting series into global scope'); 
    name = :GetName(series);
    :Log(DEBUG, 'name = ' + name);
    :GPut(name, series);
  } 
}

#################################################################################
# Update functions
#################################################################################
function ES:Update(series) {
  :Log(DEBUG, 'ES:Update(): ' + series);
  series = ES:Load(series);
  if (:GetSource(series) == 'FRED' and :GetId(series) < ES:BACKUP_BASE) {
    :Log(DEBUG, 'series is a candidate for update(s); proceeding');
    id = :GetId(series);
    name = :GetName(series);
    :Log(INFO, 'updating ' + id + ':' + name + '...');
    series = :Fred(name);
    :SetId(series, id);
    :Merge(series, '--with-inserts');
  }
}

function ES:UpdateAll() {
  :Log(DEBUG, 'ES:UpdateAll()');
  :Ds(ES:Update);
}

#################################################################################
# Reset functions
#################################################################################
function ES:ResetId(id, idNew) {
  if (!:IsAdmin()) {
    throw 'you must be running in administrative mode to reset id\'s';
  }
  if (:GetType(id) != 'int' or :GetType(idNew) != 'int') {
    throw 'usage: ES:ResetId(int id, int idNew);';
  }
  if (:Exists(idNew)) {
    throw 'series already exists: ' + idNew;
  } 
  S = ES:Load(id);
  :SetId(S, idNew);
  :Drop(id);
  :Save(S);
}

function ES:ResetName(name, nameNew) {
  if (!:IsAdmin()) {
    throw 'you must be running in administrative mode to reset name\'s';
  }
  if (:GetType(name) != 'String' or :GetType(nameNew) != 'String') {
    throw 'usage: ES:ResetName(String name, String nameNew);';
  }
  if (:Exists(nameNew)) {
    throw 'series already exists: ' + nameNew;
  } 
  S = ES:Load(name);
  :SetName(S, nameNew);
  :Drop(:GetId(S));
  :Save(S);
}

#################################################################################
# Backup function
#################################################################################
function ES:Backup(id) {
  :Log(DEBUG, 'ES:Backup(): ' + id);
  if (!:IsAdmin()) {
    :DlgMessage('You must be running in administrative mode to do this', ERROR);
    return;
  }
  if (id == null) {
    id = :DlgInput('Enter series id');
    if (id == null) {
      return;
    }
    id = :ParseInt(id);
    if (id == null) {
      :DlgMessage('Invalid series id', ERROR);
      return;
    }
  } else {
    if (:GetType(id) != 'int') {
      :DlgMessage('Invalid series id', ERROR);
      return;
    }
  }
  series = ES:Load(id);
  if (series == null) {
    :DlgMessage('Series does not exist', ERROR);
    return;
  }
  :Log(DEBUG, 'backing up series');
  :Log(DEBUG, series);
  if (:GetId(series) >= ES:BACKUP_BASE) {
    :DlgMessage('Cannot backup a series already backed up', ERROR);
    return;
  }
  if (:Exists(:GetId(series) + ES:BACKUP_BASE)) {
    :DlgMessage('Cannot backup series: already exists: ' + :GetId(series) + ES:BACKUP_BASE);
    return;
  }
  if (!:DlgConfirm()) {
    return;
  }
  :Log(DEBUG, 'backing up series ' + id);
  :SetName(series, :GetName(series) + ES:BACKUP_EXT);
  :SetId(series, :GetId(series) + ES:BACKUP_BASE);
  :Save(series);
  :DlgMessage('Backup complete for ' + :GetId(series));
}

#################################################################################
# Highest / Lowest functions
#################################################################################
function ES:Highest(series) {
  :Log(DEBUG, 'ES:Highest(): ' + series);
  function fn(idx, d, v) {
    if (v > :GGet('METRICS.highest')) {
      :Log(DEBUG, 'found larger value: ' + v);
      :GPut('METRICS.highest', v);
    }
  }

  :Log(DEBUG, 'loading series');
  series = ES:Load(series);
  if (series == null or :GetSize(series) == 0) {
    :Log(DEBUG, 'series is null or empty; returning null');
    return null;
  }
  :Log(DEBUG, 'initializing with value ' + :Get(series, 0));
  :GPut('METRICS.highest', :Get(series, 0));
  :Data(series, fn);
  :Log(DEBUG, 'found highest value: ' + :GGet('METRICS.highest'));
  return :GGet('METRICS.highest');
}

function ES:Lowest(series) {
  :Log(DEBUG, 'ES:Lowest(): ' + series);
  function fn(idx, d, v) {
    if (v < :GGet('METRICS.lowest')) {
      :Log(DEBUG, 'found smaller value: ' + v);
      :GPut('METRICS.lowest', v);
    }
  }

  :Log(DEBUG, 'loading series');  
  series = ES:Load(series);
  if (series == null or :GetSize(series) == 0) {
    :Log(DEBUG, 'series is null or empty; returning null');
    return null;
  }
  :Log(DEBUG, 'initializing with value ' + :Get(series, 0));
  :GPut('METRICS.lowest', :Get(series, 0));
  :Data(series, fn);
  :Log(DEBUG, 'found lowest value: ' + :GGet('METRICS.lowest'));
  return :GGet('METRICS.lowest');
}

#################################################################################
# Usage functions
#################################################################################
function ES:Usage() {
  function m(series) {
    :Log(DEBUG, 'm(): ' + series);
    :Assert(series != null, 'series is unexpectedly null');
    :Log(DEBUG, series);
    series = ES:Load(series);
    :Assert(series != null, 'series is unexpectedly null');
    if (:GetId(series) < ES:BACKUP_BASE) {
      :Log(DEBUG, 'series id < ES:BACKUP_BASE; is a candidate for metrics');
      :GPut('METRICS.numberOfSeries', METRICS.numberOfSeries + 1);
      :GPut('METRICS.numberOfRecords', METRICS.numberOfRecords + :GetSize(series));
      :Printf('%-20s%8d\n', :GetName(series), :GetSize(series));
    }
  }
  
  :Log(DEBUG, 'ES:Usage()');
  :GPut('METRICS.numberOfSeries', 0);
  :GPut('METRICS.numberOfRecords', 0);
  :Print('Series Metrics');
  :Print('----------------------------');
  :Ds(m);
  :Print();
  :Print('Series stored in datastore: ' + METRICS.numberOfSeries + ' (excluding backup series)');
  :Print('Number of records stored in datastore: ' + METRICS.numberOfRecords);
}

function ES:Defaults() {
  :Print('defaults.panel.backgroundcolor = Color');
  :Print('defaults.panel.dxincr = int');
  :Print('defaults.panel.gridlinetextwidth = int');
  :Print('defaults.panel.fontname = String');
  :Print('defaults.panel.fontcolor = Color');
  :Print('defaults.panel.fontsize = int');
  :Print('defaults.panel.frequency = NONE | DAYS | MONTHS | YEARS');
  :Print('defaults.panel.label = String');
  :Print('defaults.chart.backgroundcolor = Color');
  :Print('defaults.chart.linecolor = Color');
  :Print('defaults.chart.rectcolor = Color');
  :Print('defaults.chart.ngridlines = int');
  :Print('defaults.chart.label = String');
  :Print('defaults.chart.scaletype = LINEAR | LOG');
  :Print('defaults.series.linecolor0 = Color');
  :Print('defaults.series.linecolor1 = Color');
  :Print('defaults.series.linecolor2 = Color');
  :Print('defaults.series.linecolor3 = Color');
}