const ES:BACKUP_BASE = 10000;
const ES:BACKUP_EXT = '.BAK';

#################################################################################
# Load / Exist functions
#################################################################################
function ES:Load(series, fred) {
  :Log(DEBUG, 'ES:Load(' + series + ', ' + fred + ')');
  if (series == null) {
    :Log(DEBUG, 'series is null, returning null');
    return null;
  }
  if (:GetType(series) == 'String') {
    if (ES:Exists(series)) {
      :Log(DEBUG, 'series exists in the datastore; loading');
      L = :Load(series);
      :Log(DEBUG, 'loaded series = ' + L);
      return L;
    }
    if (fred != null and fred) {
      :Log(DEBUG, 'attempting to load series from FRED');
      F = :Fred(series);
      :Log(DEBUG, 'downloaded series = ' + F);
      return F;
    } else {
      :Log(DEBUG, 'fred flag not set, bypassing :Fred() call');
    }
  }
  if (:GetType(series) == 'int') {
    if (ES:Exists(series)) {
      :Log(DEBUG, 'series exists in the datastore; loading');
      L = :Load(series);
      :Log(DEBUG, 'loaded series = ' + L);
      return L;
    }
    :Log(DEBUG, 'series not found; returning null');
    return null; 
  }
  if (:GetType(series) == 'Series') {
    if (:GetId(series) != null and ES:Exists(:GetId(series))) {
      :Log(DEBUG, 'series exists in the datastore; reloading');
      L = :Load(:GetId(series));
      :Log(DEBUG, 'reloaded series = ' + L);
      return L;
    }
    :Log(DEBUG, 'series does not exist in the datastore, returning series as is: ' + series);
    return series;
  }
  :Log(WARN, 'unable to find the series, returning null');
  return null;
}

function ES:Exists(object) {
  :Log(DEBUG, 'ES:Exists(' + object + ')');
  try {
    return :Load(object) != null;
  } catch(ex) {
    :Log(DEBUG, 'object does not exist: ' + ex);
    return false;
  }
}

function ES:AutoLoad(series) {
  :Log(DEBUG, 'ES:AutoLoad(' + series + ')');
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
function ES:Update(object) {
  :Log(DEBUG, 'ES:Update(' + object + ')');
  series = ES:Load(object);
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

function ES:LastUpdated(object) {
  :Log(DEBUG, 'ES:LastUpdated(' + object + ')');
  series = ES:Load(object);
  D = :Date(series);
  :Printf('%-6d%-22s%-5s%s\n', :GetId(series), :GetName(series), :GetFrequencyShort(series), 
                               :Get(D, :GetSize(D) - 1));
}

function ES:LastUpdatedReport() {
  :Log(DEBUG, 'ES:LastUpdaetdReport()');
  :Ds(ES:LastUpdated);
}

function ES:UpdateAll() {
  :Log(DEBUG, 'ES:UpdateAll()');
  :Ds(ES:Update);
}

function ES:RefreshMetaData(object) {
  :Log(DEBUG, 'ES:RefreshMetaData(' + object + ')');
  if (!:IsAdmin()) {
    throw 'you must be running in administrative mode to refresh metadata';
  }
  series = ES:Load(object);
  if (series == null) {
    throw 'Series does not exist: ' + object;
  }
  if (:GetId(series) == null) {
    throw 'Series id does not exist: ' + object;
  }
  if (:GetId(series) >= ES:BACKUP_BASE) {
    :Log(DEBUG, 'ignoring series with id ' + :GetId(series));
    return;
  }
  if (:GetSource(series) != 'FRED') {
    :Log(DEBUG, 'Series source is not FRED, nothing to do');
    return;
  }
  F = :Fred(:GetSourceId(series));
  if (F == null) {
    throw 'Series not found in FRED database: ' + series;
  }
  changed = false;
  if (:GetTitle(series) != :GetTitle(F)) {
    :Log(INFO, 'series ' + :GetId(series) + ' title has changed to: ' + :GetTitle(F));
    :SetTitle(series, :GetTitle(F));
    changed = true;
  }
  if (:GetNotes(series) != :GetNotes(F)) {
    :Log(INFO, 'series ' + :GetId(series) + ' notes has changed to: ' + :GetNotes(F));
    :SetNotes(series, :GetNotes(F));
    changed = true;
  }
  if (changed) {
    :Log(DEBUG, 'merging series: ' + series);
    #:Meta(series);
    :Merge(series, '--with-metadata');
  } else {
    :Log(INFO, 'series metadata has not changed, nothing to do');
  }
}

function ES:RefreshAll() {
  :Log(DEBUG, 'ES:RefreshAll()');
  :Ds(ES:RefreshMetaData);
}

function ES:CheckMetaData(object) {
  :Log(DEBUG, 'ES:CheckMetaData(' + object + ')');
  if (object == null) {
    :Log(DEBUG, 'invoking :Ds()');
    :Ds(ES:CheckMetaData);
    return;
  }
  series = ES:Load(object);
  if (series == null) {
    throw 'Series does not exist: ' + object;
  }
  if (:GetId(series) == null) {
    throw 'Series id does not exist: ' + object;
  }
  if (:GetId(series) >= ES:BACKUP_BASE) {
    :Log(DEBUG, 'ignoring series with id ' + :GetId(series));
    return;
  }
  if (:GetSource(series) != 'FRED') {
    :Log(DEBUG, 'Series source is not FRED, nothing to do');
    return;
  }
  F = :Fred(:GetSourceId(series));
  if (F == null) {
    throw 'Series not found in FRED database: ' + series;
  }
  changed = false;
  if (:GetTitle(series) != :GetTitle(F)) {
    :Log(INFO, 'series ' + :GetId(series) + ' title has changed to: ' + :GetTitle(F));
    changed = true;
  }
  if (:GetNotes(series) != :GetNotes(F)) {
    :Log(INFO, 'series ' + :GetId(series) + ' notes has changed to: ' + :GetNotes(F));
    changed = true;
  }
  if (!changed) {
    :Log(INFO, 'series ' + :GetId(series) + ' has not changed');
  }
}

#################################################################################
# Reset functions
#################################################################################
function ES:ResetId(id, idNew) {
  :Log(DEBUG, 'ES:ResetId(' + id + ', ' + idNew + ')');
  try {
    if (!:IsAdmin()) {
      throw 'you must be running in administrative mode to reset ids';
    }
    if (:GetType(id) != 'int' or :GetType(idNew) != 'int') {
      throw 'id and idNew must be int\'s';
    }
    if (ES:Exists(idNew)) {
      throw 'id already exists: ' + idNew;
    } 
    S = :Load(id);
    if (S == null) {
      throw 'unable to load id: ' + id;
    }
    :SetId(S, idNew);
    :Drop(id);
    :Save(S);
  } catch(ex) {
    :DlgMessage('Unable to Reset ID: ' + ex, ERROR);
  }
}

function ES:ResetName(name, nameNew) {
  :Log(DEBUG, 'ES:ResetName(' + name + ', ' + nameNew + ')');
  try {
    if (!:IsAdmin()) {
      throw 'you must be running in administrative mode to reset names';
    }
    if (:GetType(name) != 'String' or :GetType(nameNew) != 'String') {
      throw 'name and nameNew must be Strings';
    }
    if (ES:Exists(nameNew)) {
      throw 'series already exists: ' + nameNew;
    } 
    S = :Load(name);
    if (S == null) {
      throw 'unable to load name: ' + name;
    }
    :SetName(S, nameNew);
    :Drop(:GetId(S));
    :Save(S);
  } catch(ex) {
    :DlgMessage('Unable to Reset Name: ' + ex, ERROR);
  }
}

#################################################################################
# Backup function
#################################################################################
function ES:Backup(id) {
  :Log(DEBUG, 'ES:Backup(' + id + ')');
  try {
    if (!:IsAdmin()) {
      throw 'You must be running in administrative mode to do this';
    }
    if (id == null) {
      id = :DlgInput('Enter series id');
      if (id == null) {
        throw 'Cancelled by user';
      }
      id = :ParseInt(id);
      if (id == null) {
        throw 'Invalid series id';
      }
    } else {
      if (:GetType(id) != 'int') {
        throw 'Invalid series id';
      }
    }
    series = ES:Load(id);
    if (series == null) {
      throw 'Series does not exist';
    }
    if (:GetId(series) >= ES:BACKUP_BASE) {
      throw 'Cannot backup a series already backed up';
    }
    if (ES:Exists(:GetId(series) + ES:BACKUP_BASE)) {
      throw 'Cannot backup series: already exists: ' + (:GetId(series) + ES:BACKUP_BASE);
    }
    if (!:DlgConfirm()) {
      throw 'Cancelled by user';
    }
    :Log(DEBUG, 'backing up series: ' + series);
    :SetName(series, :GetName(series) + ES:BACKUP_EXT);
    :SetId(series, :GetId(series) + ES:BACKUP_BASE);
    :Save(series);
    :DlgMessage('Backup complete for ' + id);
  } catch(ex) {
    :DlgMessage('Unable to perform backup: ' + ex, ERROR);
  }
}

#################################################################################
# Highest / Lowest functions
#################################################################################
function ES:Highest(object) {
  :Log(DEBUG, 'ES:Highest(' + object + ')');
  function fn(idx, d, v) {
    if (v > :GGet('METRICS.highest')) {
      :Log(DEBUG, 'found larger value: ' + v);
      :GPut('METRICS.highest', v);
    }
  }

  :Log(DEBUG, 'loading series');
  series = ES:Load(object);
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

function ES:Lowest(object) {
  :Log(DEBUG, 'ES:Lowest(' + object + ')');
  function fn(idx, d, v) {
    if (v < :GGet('METRICS.lowest')) {
      :Log(DEBUG, 'found smaller value: ' + v);
      :GPut('METRICS.lowest', v);
    }
  }

  :Log(DEBUG, 'loading series');  
  series = ES:Load(object);
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
  function m(object) {
    ES:Assert(object != null, 'object is unexpectedly null');
    series = ES:Load(object);
    ES:Assert(series != null, 'series is unexpectedly null');
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

#################################################################################
# StartsWith / EndsWith functions
#################################################################################
function ES:StartsWith(string, prefix) {
  :Log(DEBUG, 'ES:StartsWith(' + string + ', ' + prefix + ')');
  if (:GetLength(prefix) > :GetLength(string)) {
    return false;
  }
  ss = :SubString(string, 0, :GetLength(prefix));
  :Log(DEBUG, 'substring = ' + ss);
  return ss == prefix;
}

function ES:EndsWith(string, suffix) {
  :Log(DEBUG, 'ES:EndsWith(' + string + ', ' + suffix + ')');
  if (:GetLength(suffix) > :GetLength(string)) {
    return false;
  }
  if (:GetLength(suffix) == 0) {
    :Log(DEBUG, 'suffix length = 0, returning true');
    return true;
  }
  ss = :SubString(string, :GetLength(string) - :GetLength(suffix), :GetLength(string));
  :Log(DEBUG, 'substring = ' + ss);
  return ss == suffix;
}

#################################################################################
# Assert
#################################################################################
function ES:Assert(condition, message) {
  :Log(DEBUG, 'ES:Assert(' + condition + ', ' + message + ')');
  if (!condition) {
    if (message == null) {
      throw '*** ASSERTION FAILED ***';
    } else {
      throw '*** ASSERTION FAILED *** : ' + message;
    }
  }
}
