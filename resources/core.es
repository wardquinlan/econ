const ES:BACKUP_BASE = 10000;
const ES:BACKUP_EXT = '.BAK';

#################################################################################
# Load / Exist functions
#################################################################################
const function ES:Load(object, fred) {
  :Log(DEBUG, 'ES:Load(' + object + ', ' + fred + ')');
  if (:GetType(object) == 'String') {
    L = :Load(object);
    if (L != null) {
      :Log(DEBUG, 'loaded series = ' + L);
      return L;
    }
    if (fred != null and fred) {
      :Log(DEBUG, 'attempting to load series from FRED');
      F = :Fred(object);
      :Log(DEBUG, 'downloaded series = ' + F);
      return F;
    }
    :Log(DEBUG, 'fred flag not set, bypassing :Fred() call; returning null');
    return null;
  } else if (:GetType(object) == 'int') {
    L = :Load(object);
    if (L != null) {
      :Log(DEBUG, 'loaded series = ' + L);
      return L;
    }
    :Log(DEBUG, 'series not found; returning null');
    return null; 
  } else if (:GetType(object) == 'Series') {
    if (:GetId(object) != null) {
      :Log(DEBUG, 'attempting to reload series from the datastore: ' + object);
      return :Load(:GetId(object));
    }
    :Log(DEBUG, 'series does not exist in the datastore, returning series as is: ' + object);
    return object;
  } else {
    :Log(DEBUG, 'unable to load series: unsupported type: ' + :GetType(object));
    return null;
  }
}

const function ES:Exists(object) {
  :Log(DEBUG, 'ES:Exists(' + object + ')');
  try {
    return :Load(object) != null;
  } catch(ex) {
    :Log(DEBUG, 'object does not exist: ' + ex);
    return false;
  }
}

const function ES:AutoLoad(object) {
  :Log(DEBUG, 'ES:AutoLoad(' + object + ')');
  series = ES:Load(object);
  if (series == null) {
    :Log(DEBUG, 'can\'t autoload series: not found (null)');
    return;
  }
  if (:GetId(series) >= ES:BACKUP_BASE) {
    :Log(DEBUG, 'skipping autoload of series because it is a backup: ' + series);
    return;
  }
  :Log(DEBUG, 'id = ' + :GetId(series) + ' < ES:BACKUP_BASE; putting series into global scope'); 
  :Log(DEBUG, 'name = ' + :GetName(series));
  :GPut(:GetName(series), series);
}

#################################################################################
# Update functions
#################################################################################
const function ES:Update(object) {
  :Log(DEBUG, 'ES:Update(' + object + ')');
  if (object == null) {
    :Log(DEBUG, 'setting MERGE.modified to false');
    :GPut('MERGE.modified', false);
    :Log(DEBUG, 'invoking :Ds()');
    :Ds(ES:Update);
    :Log(DEBUG, 'result of overall merge is: ' + :GGet('MERGE.modified'));
    return :GGet('MERGE.modified');
  }
  series = ES:Load(object);
  if (series == null) {
    :Log(DEBUG, 'series not found, returning false');
    return false;
  }
  flag = false;
  if (:GetSource(series) == 'FRED' and :GetId(series) < ES:BACKUP_BASE) {
    :Log(DEBUG, 'series is a candidate for update(s); proceeding');
    id = :GetId(series);
    :Log(INFO, 'updating ' + id + ':' + :GetName(series) + '...');
    series = :Fred(:GetName(series));
    :SetId(series, id);
    flag = :Merge(series, '--with-inserts');
    :Log(DEBUG, 'result of individual merge is: ' + flag);
    :GPut('MERGE.modified', flag);
  }
  :Log(DEBUG, 'returning individual merge result: ' + flag);
  return flag;
}

const function ES:LastUpdated(object) {
  :Log(DEBUG, 'ES:LastUpdated(' + object + ')');
  if (object == null) {
    :Log(DEBUG, 'invoking :Ds()');
    :Ds(ES:LastUpdated);
    return;
  }
  series = ES:Load(object);
  D = :Date(series);
  :Printf('%-6d%-22s%-5s%s\n', :GetId(series), :GetName(series), :GetFrequencyShort(series), 
                               :Get(D, :GetSize(D) - 1));
}

const function ES:Refresh(object) {
  :Log(DEBUG, 'ES:Refresh(' + object + ')');
  if (!:IsAdmin()) {
    throw 'you must be running in administrative mode to refresh';
  }
  if (object == null) {
    :Log(DEBUG, 'invoking :Ds()');
    :Ds(ES:Refresh);
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
    return :Merge(series, '--with-metadata');
  } else {
    :Log(INFO, 'series metadata has not changed, nothing to do');
  }
  return false;
}

const function ES:CheckMetaData(object) {
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
const function ES:ResetId(id, idNew) {
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

const function ES:ResetName(name, nameNew) {
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
const function ES:Backup(id) {
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
const function ES:Highest(object, withDate) {
  :Log(DEBUG, 'ES:Highest(' + object + ', ' + withDate + ')');
  function fn(idx, d, v) {
    if (v > :GGet('METRICS.highest')) {
      :Log(DEBUG, 'found larger value: ' + d + ' => ' + v);
      :GPut('METRICS.date.highest', d);
      :GPut('METRICS.highest', v);
    }
  }

  :Log(DEBUG, 'loading series');
  series = ES:Load(object);
  if (series == null or :GetSize(series) == 0) {
    :Log(DEBUG, 'series is null or empty; returning null');
    return null;
  }
  if (withDate == true) {
    :Log(DEBUG, 'initializing with date ' + :Get(:Date(series), 0));
    :GPut('METRICS.date.highest', :Get(:Date(series), 0));
  }
  :Log(DEBUG, 'initializing with value ' + :Get(series, 0));
  :GPut('METRICS.highest', :Get(series, 0));
  :Data(series, fn);
  if (withDate == true) {
    :Log(DEBUG, 'found highest value: ' + :GGet('METRICS.date.highest') + ' => ' + :GGet('METRICS.highest'));
    return '' + :GGet('METRICS.date.highest') + ' => ' + :GGet('METRICS.highest');
  } else {
    :Log(DEBUG, 'found highest value: ' + :GGet('METRICS.highest'));
    return :GGet('METRICS.highest');
  }
}

const function ES:Lowest(object, withDate) {
  :Log(DEBUG, 'ES:Lowest(' + object + ', ' + withDate + ')');
  function fn(idx, d, v) {
    if (v < :GGet('METRICS.lowest')) {
      :Log(DEBUG, 'found smaller value: ' + d + ' => ' + v);
      :GPut('METRICS.date.lowest', d);
      :GPut('METRICS.lowest', v);
    }
  }

  :Log(DEBUG, 'loading series');
  series = ES:Load(object);
  if (series == null or :GetSize(series) == 0) {
    :Log(DEBUG, 'series is null or empty; returning null');
    return null;
  }
  if (withDate == true) {
    :Log(DEBUG, 'initializing with date ' + :Get(:Date(series), 0));
    :GPut('METRICS.date.lowest', :Get(:Date(series), 0));
  }
  :Log(DEBUG, 'initializing with value ' + :Get(series, 0));
  :GPut('METRICS.lowest', :Get(series, 0));
  :Data(series, fn);
  if (withDate == true) {
    :Log(DEBUG, 'found lowest value: ' + :GGet('METRICS.date.lowest') + ' => ' + :GGet('METRICS.lowest'));
    return '' + :GGet('METRICS.date.lowest') + ' => ' + :GGet('METRICS.lowest');
  } else {
    :Log(DEBUG, 'found lowest value: ' + :GGet('METRICS.lowest'));
    return :GGet('METRICS.lowest');
  }
}

#################################################################################
# Usage functions
#################################################################################
const function ES:Usage() {
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

const function ES:Defaults() {
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
  :Print('defaults.panel.decorations.color = Color');
  :Print('defaults.panel.decorations.stroke = int');
}

#################################################################################
# String functions
#################################################################################
const function ES:StartsWith(string, prefix) {
  :Log(DEBUG, 'ES:StartsWith(' + string + ', ' + prefix + ')');
  if (:GetLength(prefix) > :GetLength(string)) {
    return false;
  }
  ss = :SubString(string, 0, :GetLength(prefix));
  :Log(DEBUG, 'substring = ' + ss);
  return ss == prefix;
}

const function ES:EndsWith(string, suffix) {
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

const function ES:ToString(object) {
  return '' + object;
}

#################################################################################
# Assert
#################################################################################
const function ES:Assert(condition, message) {
  :Log(DEBUG, 'ES:Assert(' + condition + ', ' + message + ')');
  if (!condition) {
    if (message == null) {
      throw '*** ASSERTION FAILED ***';
    } else {
      throw '*** ASSERTION FAILED *** : ' + message;
    }
  }
}

#################################################################################
# ES:Chop(series, date1, date2)
#
# Chops series so that its contents ranges from date1 to date2 (inclusive) only
#
# series: the series to chop
# date1 : the start date
# date2 : the end date
#
# Returns: the chopped series
#################################################################################
const function ES:Chop(series, date1, date2) {
  :Log(DEBUG, 'ES:Chop(' + series + ', ' + date1 + ', ' + date2 + ')');
  if (:GetType(series) != 'Series') {
    throw 'ES:Chop: not a Series: ' + series;
  }
  if (:GetSeriesType(series) != 'float') {
    throw 'ES:Chop: unsupported series type: ' + :GetSeriesType(series);
  }
  if (:GetSize(series) == 0) {
    throw 'ES:Chop: cannot chop an empty series: ' + series;
  }
  S = :Create(:GetName(series) + '-chopped');
  D = :Date(series);
  if (date1 == null) {
    date1 = :Get(D, 0);
  }
  if (date2 == null) {
    date2 = :Get(D, :GetSize(D) - 1);
  }
  for (i = 0; i < :GetSize(D); i++) {
    if (:Get(D, i) >= date1 and :Get(D, i) <= date2) {
      :Insert(S, :Get(D, i), :Get(series, i));
    }
  }
  return S;
}

#################################################################################
# ES:Scale(series, scale)
#
# Scales a series by a factor of 'scale'
#
# series: the series to scale
# scale : the scaling factor
#
# Returns: the scaled series
#################################################################################
const function ES:Scale(series, scale) {
  :Log(DEBUG, 'ES:Scale(' + series + ', ' + scale + ')');
  if (:GetType(series) != 'Series') {
    throw 'ES:Scale: not a Series: ' + series;
  }
  if (:GetSeriesType(series) != 'float') {
    throw 'ES:Scale: unsupported series type: ' + :GetSeriesType(series);
  }
  if (:GetSize(series) == 0) {
    throw 'ES:Scale: cannot scale an empty series: ' + series;
  }
  if (:GetType(scale) != 'int' and :GetType(scale) != 'float') {
    throw 'ES:Scale: unsupported scaling type: ' + scale;
  }
  S = :Create(:GetName(series) + '-scaled');
  D = :Date(series);
  :Insert(S, :Get(D, 0), :Get(series, 0));
}
