include 'aliases.es';

const ES:BACKUP_BASE = 10000;
const ES:BACKUP_EXT = '.BAK';

#################################################################################
# Load / Exist functions
#################################################################################
const function ES:Load(object, fred) {
  ES:Log(TRACE, 'ES:Load(' + object + ', ' + fred + ')');
  if (ES:GetType(object) == 'String') {
    L = :Load(object);
    if (L != null) {
      ES:Log(TRACE, 'loaded series = ' + L);
      return L;
    }
    if (fred != null and fred) {
      ES:Log(TRACE, 'attempting to load series from FRED');
      F = :Fred(object);
      ES:Log(TRACE, 'downloaded series = ' + F);
      return F;
    }
    ES:Log(TRACE, 'fred flag not set, bypassing :Fred() call; returning null');
    return null;
  } else if (ES:GetType(object) == 'int') {
    L = :Load(object);
    if (L != null) {
      ES:Log(TRACE, 'loaded series = ' + L);
      return L;
    }
    ES:Log(TRACE, 'series not found; returning null');
    return null; 
  } else if (ES:GetType(object) == 'Series') {
    if (:GetId(object) != null) {
      ES:Log(TRACE, 'attempting to reload series from the datastore: ' + object);
      return :Load(:GetId(object));
    }
    ES:Log(TRACE, 'series does not exist in the datastore, returning series as is: ' + object);
    return object;
  } else {
    ES:Log(TRACE, 'unable to load series: unsupported type: ' + ES:GetType(object));
    return null;
  }
}

const function ES:Exists(object) {
  ES:Log(TRACE, 'ES:Exists(' + object + ')');
  try {
    return :Load(object) != null;
  } catch(ex) {
    ES:Log(TRACE, 'object does not exist: ' + ex);
    return false;
  }
}

const function ES:AutoLoad(object) {
  ES:Log(TRACE, 'ES:AutoLoad(' + object + ')');
  series = ES:Load(object);
  if (series == null) {
    ES:Log(TRACE, 'can\'t autoload series: not found (null)');
    return;
  }
  if (:GetId(series) >= ES:BACKUP_BASE) {
    ES:Log(TRACE, 'skipping autoload of series because it is a backup: ' + series);
    return;
  }
  ES:Log(TRACE, 'id = ' + :GetId(series) + ' < ES:BACKUP_BASE; putting series into global scope'); 
  ES:Log(TRACE, 'name = ' + :GetName(series));
  :GPut(:GetName(series), series);
}

#################################################################################
# Update functions
#################################################################################
const function ES:Update(object) {
  ES:Log(TRACE, 'ES:Update(' + object + ')');
  if (object == null) {
    ES:Log(TRACE, 'setting MERGE.modified to false');
    :GPut('MERGE.modified', false);
    ES:Log(TRACE, 'invoking :Ds()');
    :Ds(ES:Update);
    ES:Log(TRACE, 'result of overall merge is: ' + :GGet('MERGE.modified'));
    return :GGet('MERGE.modified');
  }
  series = ES:Load(object);
  if (series == null) {
    ES:Log(TRACE, 'series not found, returning false');
    return false;
  }
  flag = false;
  if (:GetSource(series) == 'FRED' and :GetId(series) < ES:BACKUP_BASE) {
    ES:Log(TRACE, 'series is a candidate for update(s); proceeding');
    id = :GetId(series);
    ES:Log(INFO, 'updating ' + id + ':' + :GetName(series) + '...');
    series = :Fred(:GetName(series));
    :SetId(series, id);
    flag = :Merge(series, '--with-inserts');
    ES:Log(TRACE, 'result of individual merge is: ' + flag);
    :GPut('MERGE.modified', flag);
  }
  ES:Log(TRACE, 'returning individual merge result: ' + flag);
  return flag;
}

const function ES:LastUpdated(object) {
  ES:Log(TRACE, 'ES:LastUpdated(' + object + ')');
  if (object == null) {
    ES:Log(TRACE, 'invoking :Ds()');
    :Ds(ES:LastUpdated);
    return;
  }
  series = ES:Load(object);
  :Printf('%-6d%-22s%-5s%s\n', :GetId(series), :GetName(series), :GetFrequencyShort(series), 
                               :GetDate(series, ES:GetSize(series) - 1));
}

const function ES:Refresh(object) {
  ES:Log(TRACE, 'ES:Refresh(' + object + ')');
  if (!:IsAdmin()) {
    throw 'you must be running in administrative mode to refresh';
  }
  if (object == null) {
    ES:Log(TRACE, 'invoking :Ds()');
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
    ES:Log(TRACE, 'ignoring series with id ' + :GetId(series));
    return;
  }
  if (:GetSource(series) != 'FRED') {
    ES:Log(TRACE, 'Series source is not FRED, nothing to do');
    return;
  }
  F = :Fred(:GetSourceId(series));
  if (F == null) {
    throw 'Series not found in FRED database: ' + series;
  }
  changed = false;
  if (:GetTitle(series) != :GetTitle(F)) {
    ES:Log(INFO, 'series ' + :GetId(series) + ' title has changed to: ' + :GetTitle(F));
    :SetTitle(series, :GetTitle(F));
    changed = true;
  }
  if (:GetNotes(series) != :GetNotes(F)) {
    ES:Log(INFO, 'series ' + :GetId(series) + ' notes has changed to: ' + :GetNotes(F));
    :SetNotes(series, :GetNotes(F));
    changed = true;
  }
  if (changed) {
    ES:Log(TRACE, 'merging series: ' + series);
    #:Meta(series);
    return :Merge(series, '--with-metadata');
  } else {
    ES:Log(INFO, 'series metadata has not changed, nothing to do');
  }
  return false;
}

const function ES:CheckMetaData(object) {
  ES:Log(TRACE, 'ES:CheckMetaData(' + object + ')');
  if (object == null) {
    ES:Log(TRACE, 'invoking :Ds()');
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
    ES:Log(TRACE, 'ignoring series with id ' + :GetId(series));
    return;
  }
  if (:GetSource(series) != 'FRED') {
    ES:Log(TRACE, 'Series source is not FRED, nothing to do');
    return;
  }
  F = :Fred(:GetSourceId(series));
  if (F == null) {
    throw 'Series not found in FRED database: ' + series;
  }
  changed = false;
  if (:GetTitle(series) != :GetTitle(F)) {
    ES:Log(INFO, 'series ' + :GetId(series) + ' title has changed to: ' + :GetTitle(F));
    changed = true;
  }
  if (:GetNotes(series) != :GetNotes(F)) {
    ES:Log(INFO, 'series ' + :GetId(series) + ' notes has changed to: ' + :GetNotes(F));
    changed = true;
  }
  if (!changed) {
    ES:Log(INFO, 'series ' + :GetId(series) + ' has not changed');
  }
}

#################################################################################
# Reset functions
#################################################################################
const function ES:ResetId(id, idNew) {
  ES:Log(TRACE, 'ES:ResetId(' + id + ', ' + idNew + ')');
  try {
    if (!:IsAdmin()) {
      throw 'you must be running in administrative mode to reset ids';
    }
    if (ES:GetType(id) != 'int' or ES:GetType(idNew) != 'int') {
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
  ES:Log(TRACE, 'ES:ResetName(' + name + ', ' + nameNew + ')');
  try {
    if (!:IsAdmin()) {
      throw 'you must be running in administrative mode to reset names';
    }
    if (ES:GetType(name) != 'String' or ES:GetType(nameNew) != 'String') {
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
  ES:Log(TRACE, 'ES:Backup(' + id + ')');
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
      if (ES:GetType(id) != 'int') {
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
    ES:Log(TRACE, 'backing up series: ' + series);
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
  ES:Log(TRACE, 'ES:Highest(' + object + ', ' + withDate + ')');
  function fn(idx, d, v) {
    if (v > :GGet('METRICS.highest')) {
      ES:Log(TRACE, 'found larger value: ' + d + ' => ' + v);
      :GPut('METRICS.date.highest', d);
      :GPut('METRICS.highest', v);
    }
  }

  ES:Log(TRACE, 'loading series');
  series = ES:Load(object);
  if (series == null or ES:GetSize(series) == 0) {
    ES:Log(TRACE, 'series is null or empty; returning null');
    return null;
  }
  if (withDate == true) {
    ES:Log(TRACE, 'initializing with date ' + :GetDate(series, 0));
    :GPut('METRICS.date.highest', :GetDate(series, 0));
  }
  ES:Log(TRACE, 'initializing with value ' + :Get(series, 0));
  :GPut('METRICS.highest', :Get(series, 0));
  :Data(series, fn);
  if (withDate == true) {
    ES:Log(TRACE, 'found highest value: ' + :GGet('METRICS.date.highest') + ' => ' + :GGet('METRICS.highest'));
    return '' + :GGet('METRICS.date.highest') + ' => ' + :GGet('METRICS.highest');
  } else {
    ES:Log(TRACE, 'found highest value: ' + :GGet('METRICS.highest'));
    return :GGet('METRICS.highest');
  }
}

const function ES:Lowest(object, withDate) {
  ES:Log(TRACE, 'ES:Lowest(' + object + ', ' + withDate + ')');
  function fn(idx, d, v) {
    if (v < :GGet('METRICS.lowest')) {
      ES:Log(TRACE, 'found smaller value: ' + d + ' => ' + v);
      :GPut('METRICS.date.lowest', d);
      :GPut('METRICS.lowest', v);
    }
  }

  ES:Log(TRACE, 'loading series');
  series = ES:Load(object);
  if (series == null or ES:GetSize(series) == 0) {
    ES:Log(TRACE, 'series is null or empty; returning null');
    return null;
  }
  if (withDate == true) {
    ES:Log(TRACE, 'initializing with date ' + :GetDate(series, 0));
    :GPut('METRICS.date.lowest', :GetDate(series, 0));
  }
  ES:Log(TRACE, 'initializing with value ' + :Get(series, 0));
  :GPut('METRICS.lowest', :Get(series, 0));
  :Data(series, fn);
  if (withDate == true) {
    ES:Log(TRACE, 'found lowest value: ' + :GGet('METRICS.date.lowest') + ' => ' + :GGet('METRICS.lowest'));
    return '' + :GGet('METRICS.date.lowest') + ' => ' + :GGet('METRICS.lowest');
  } else {
    ES:Log(TRACE, 'found lowest value: ' + :GGet('METRICS.lowest'));
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
      ES:Log(TRACE, 'series id < ES:BACKUP_BASE; is a candidate for metrics');
      :GPut('METRICS.numberOfSeries', METRICS.numberOfSeries + 1);
      :GPut('METRICS.numberOfRecords', METRICS.numberOfRecords + ES:GetSize(series));
      :Printf('%-20s%8d\n', :GetName(series), ES:GetSize(series));
    }
  }
  
  ES:Log(TRACE, 'ES:Usage()');
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
  ES:Log(TRACE, 'ES:StartsWith(' + string + ', ' + prefix + ')');
  if (:GetLength(prefix) > :GetLength(string)) {
    return false;
  }
  ss = :SubString(string, 0, :GetLength(prefix));
  ES:Log(TRACE, 'substring = ' + ss);
  return ss == prefix;
}

const function ES:EndsWith(string, suffix) {
  ES:Log(TRACE, 'ES:EndsWith(' + string + ', ' + suffix + ')');
  if (:GetLength(suffix) > :GetLength(string)) {
    return false;
  }
  if (:GetLength(suffix) == 0) {
    ES:Log(TRACE, 'suffix length = 0, returning true');
    return true;
  }
  ss = :SubString(string, :GetLength(string) - :GetLength(suffix), :GetLength(string));
  ES:Log(TRACE, 'substring = ' + ss);
  return ss == suffix;
}

const function ES:ToString(object) {
  return '' + object;
}

#################################################################################
# Assert
#################################################################################
const function ES:Assert(condition, message) {
  ES:Log(TRACE, 'ES:Assert(' + condition + ', ' + message + ')');
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
  ES:Log(TRACE, 'ES:Chop(' + series + ', ' + date1 + ', ' + date2 + ')');
  if (ES:GetType(series) != 'Series') {
    throw 'ES:Chop: not a Series: ' + series;
  }
  if (ES:GetSeriesType(series) != 'float') {
    throw 'ES:Chop: unsupported series type: ' + ES:GetSeriesType(series);
  }
  if (ES:GetSize(series) == 0) {
    throw 'ES:Chop: cannot chop an empty series: ' + series;
  }
  S = :Create(:GetName(series) + '-chopped');
  if (date1 == null) {
    date1 = :GetDate(series, 0);
  }
  if (date2 == null) {
    date2 = :GetDate(series, ES:GetSize(series) - 1);
  }
  for (i = 0; i < ES:GetSize(series); i++) {
    if (:GetDate(series, i) >= date1 and :GetDate(series, i) <= date2) {
      :Insert(S, :GetDate(series, i), :Get(series, i));
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
  ES:Log(TRACE, 'ES:Scale(' + series + ', ' + scale + ')');
  if (ES:GetType(series) != 'Series') {
    throw 'ES:Scale: not a Series: ' + series;
  }
  if (ES:GetSeriesType(series) != 'float') {
    throw 'ES:Scale: unsupported series type: ' + ES:GetSeriesType(series);
  }
  if (ES:GetSize(series) == 0) {
    throw 'ES:Scale: cannot scale an empty series: ' + series;
  }
  if (ES:GetType(scale) != 'int' and ES:GetType(scale) != 'float') {
    throw 'ES:Scale: unsupported scaling type: ' + scale;
  }
  S = :Create(:GetName(series) + '-scaled [x' + scale + ']');
  :Insert(S, :GetDate(series, 0), :Get(series, 0));
  for (i = 1; i < ES:GetSize(series); i++) {
    totalScale = scale * (:Get(series, i) - :Get(series, i - 1)) / :Get(series, i - 1);
    :Insert(S, :GetDate(series, i), (1 + totalScale) * :Get(S, i - 1));
  }
  return S;
}

#################################################################################
# ES:Sqrt(value)
#
# Calculates the Square Root of 'value'
#
# value: the value to take the square root of
#
# Returns: the square root of 'value'
#################################################################################
const function ES:Sqrt(value) {
  ES:Log(TRACE, 'ES:Sqrt(' + value + ')');
  if (value < 0) {
    throw 'cannot take the square root of a negative value: ' + value;
  }
  return value ^ 0.5;
}

#################################################################################
# Calculates the annualized yield of 'periodYield'
#
# periodYield : the period yield, expressed as a percentage (e.g. 3.25)
# period      : the period, in days (e.g. 90)
#
# Returns: the annualized yield, expressed as a percentage
#################################################################################
const function ES:AnnualizedYield(periodYield, period) {
  ES:Log(TRACE, 'ES:AnnualizedYield(' + periodYield + ', ' + period + ')');
  yield = (1 + (periodYield / 100)) ^ (365 / period) - 1;
  return yield * 100; 
}

#################################################################################
# Calculates the period yield of 'annualYield'
#
# annualYield : the annual yield, expressed as a percentage (e.g. 3.25)
# period      : the period, in days (e.g. 90)
#
# Returns: the period yield, expressed as a percentage
#################################################################################
const function ES:PeriodYield(annualYield, period) {
  ES:Log(TRACE, 'ES:PeriodYield(' + annualYield + ', ' + period + ')');
  yield = (1 + (annualYield / 100)) ^ (period / 365) - 1;
  return yield * 100; 
}
