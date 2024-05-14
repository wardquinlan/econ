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
      F = ES:Fred(object);
      ES:Log(TRACE, 'downloaded series = ' + F);
      return F;
    }
    ES:Log(TRACE, 'fred flag not set, bypassing ES:Fred() call; returning null');
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
    if (ES:GetId(object) != null) {
      ES:Log(TRACE, 'attempting to reload series from the datastore: ' + object);
      return :Load(ES:GetId(object));
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
  if (ES:GetId(series) >= ES:BACKUP_BASE) {
    ES:Log(TRACE, 'skipping autoload of series because it is a backup: ' + series);
    return;
  }
  ES:Log(TRACE, 'id = ' + ES:GetId(series) + ' < ES:BACKUP_BASE; putting series into global scope'); 
  ES:Log(TRACE, 'name = ' + ES:GetName(series));
  ES:GPut(ES:GetName(series), series);
}

#################################################################################
# Update functions
#################################################################################
const function ES:Revise(object) {
  ES:Log(TRACE, 'ES:Revise(' + object + ')');
  if (!ES:IsAdmin()) {
    throw 'You must be running in administrative mode to do this';
  }
  if (object == null) {
    ES:Log(TRACE, 'invoking ES:Ds()');
    ES:Ds(ES:Revise);
    return;
  }
  series = ES:Load(object);
  if (series == null) {
    throw 'series not found: ' + object;
  }
  if (ES:GetSource(series) == 'FRED' and ES:GetId(series) < ES:BACKUP_BASE) {
    id = ES:GetId(series);
    ES:Log(INFO, 'revising ' + id + ':' + ES:GetName(series) + '...');
    fseries = :Fred(ES:GetName(series));
    ES:SetId(fseries, id);
    for (i = 0; i < REVISIONS; i++) {
      idx = :GetSize(series) - i - 1;
      fidx = :GetSize(fseries) - i - 1;
      d = :GetDate(series, idx);
      fd = :GetDate(fseries, fidx);
      if (d != fd) {
        throw 'series dates do not match to that in FRED; synchronize the series and try again';
      }
      val = :Get(series, d);
      fval = :Get(fseries, fd);
      if (val == fval) {
        ES:Log(INFO, 'series values match, nothing to do: ' + d);
        continue;
      }
      ES:Log(INFO, 'updating series: ' + ES:GetName(series) + ' :' + d + ': ' + val + ' => ' + fval);
      :Update(series, d, fval);
    }
    ES:Log(INFO, 'merging updated series: ' + ES:GetName(series));
    ES:Merge(series, '--with-updates');
  }
}

const function ES:Update(object) {
  ES:Log(TRACE, 'ES:Update(' + object + ')');
  if (object == null) {
    ES:Log(TRACE, 'setting MERGE.modified to false');
    ES:GPut('MERGE.modified', false);
    ES:Log(TRACE, 'invoking ES:Ds()');
    ES:Ds(ES:Update);
    ES:Log(TRACE, 'result of overall merge is: ' + ES:GGet('MERGE.modified'));
    return ES:GGet('MERGE.modified');
  }
  series = ES:Load(object);
  if (series == null) {
    ES:Log(TRACE, 'series not found, returning false');
    return false;
  }
  flag = false;
  if (ES:GetSource(series) == 'FRED' and ES:GetId(series) < ES:BACKUP_BASE) {
    ES:Log(TRACE, 'series is a candidate for update(s); proceeding');
    id = ES:GetId(series);
    ES:Log(INFO, 'updating ' + id + ':' + ES:GetName(series) + '...');
    series = :Fred(ES:GetName(series));
    ES:SetId(series, id);
    flag = ES:Merge(series, '--with-inserts');
    ES:Log(TRACE, 'result of individual merge is: ' + flag);
    ES:GPut('MERGE.modified', flag);
  }
  ES:Log(TRACE, 'returning individual merge result: ' + flag);
  return flag;
}

const function ES:LastUpdated(object) {
  ES:Log(TRACE, 'ES:LastUpdated(' + object + ')');
  if (object == null) {
    ES:Log(TRACE, 'invoking ES:Ds()');
    ES:Ds(ES:LastUpdated);
    return;
  }
  series = ES:Load(object);
  ES:Printf('%-6d%-22s%-5s%s\n', ES:GetId(series), ES:GetName(series), ES:GetFrequencyShort(series), 
                               ES:GetDate(series, ES:GetSize(series) - 1));
}

const function ES:Refresh(object) {
  ES:Log(TRACE, 'ES:Refresh(' + object + ')');
  if (!ES:IsAdmin()) {
    throw 'you must be running in administrative mode to refresh';
  }
  if (object == null) {
    ES:Log(TRACE, 'invoking :Ds()');
    ES:Ds(ES:Refresh);
    return;
  }
  series = ES:Load(object);
  if (series == null) {
    throw 'Series does not exist: ' + object;
  }
  if (ES:GetId(series) == null) {
    throw 'Series id does not exist: ' + object;
  }
  if (ES:GetId(series) >= ES:BACKUP_BASE) {
    ES:Log(TRACE, 'ignoring series with id ' + ES:GetId(series));
    return;
  }
  if (ES:GetSource(series) != 'FRED') {
    ES:Log(TRACE, 'Series source is not FRED, nothing to do');
    return;
  }
  F = ES:Fred(ES:GetSourceId(series));
  if (F == null) {
    throw 'Series not found in FRED database: ' + series;
  }
  changed = false;
  if (ES:GetTitle(series) != ES:GetTitle(F)) {
    ES:Log(INFO, 'series ' + ES:GetId(series) + ' title has changed to: ' + ES:GetTitle(F));
    ES:SetTitle(series, ES:GetTitle(F));
    changed = true;
  }
  if (ES:GetNotes(series) != ES:GetNotes(F)) {
    ES:Log(INFO, 'series ' + ES:GetId(series) + ' notes has changed to: ' + ES:GetNotes(F));
    ES:SetNotes(series, ES:GetNotes(F));
    changed = true;
  }
  if (changed) {
    ES:Log(TRACE, 'merging series: ' + series);
    #:Meta(series);
    return ES:Merge(series, '--with-metadata');
  } else {
    ES:Log(INFO, 'series metadata has not changed, nothing to do');
  }
  return false;
}

const function ES:CheckMetaData(object) {
  ES:Log(TRACE, 'ES:CheckMetaData(' + object + ')');
  if (object == null) {
    ES:Log(TRACE, 'invoking ES:Ds()');
    ES:Ds(ES:CheckMetaData);
    return;
  }
  series = ES:Load(object);
  if (series == null) {
    throw 'Series does not exist: ' + object;
  }
  if (ES:GetId(series) == null) {
    throw 'Series id does not exist: ' + object;
  }
  if (ES:GetId(series) >= ES:BACKUP_BASE) {
    ES:Log(TRACE, 'ignoring series with id ' + ES:GetId(series));
    return;
  }
  if (ES:GetSource(series) != 'FRED') {
    ES:Log(TRACE, 'Series source is not FRED, nothing to do');
    return;
  }
  F = ES:Fred(ES:GetSourceId(series));
  if (F == null) {
    throw 'Series not found in FRED database: ' + series;
  }
  changed = false;
  if (ES:GetTitle(series) != ES:GetTitle(F)) {
    ES:Log(INFO, 'series ' + ES:GetId(series) + ' title has changed to: ' + ES:GetTitle(F));
    changed = true;
  }
  if (ES:GetNotes(series) != ES:GetNotes(F)) {
    ES:Log(INFO, 'series ' + ES:GetId(series) + ' notes has changed to: ' + ES:GetNotes(F));
    changed = true;
  }
  if (!changed) {
    ES:Log(INFO, 'series ' + ES:GetId(series) + ' has not changed');
  }
}

#################################################################################
# Reset functions
#################################################################################
const function ES:ResetId(id, idNew) {
  ES:Log(TRACE, 'ES:ResetId(' + id + ', ' + idNew + ')');
  try {
    if (!ES:IsAdmin()) {
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
    ES:SetId(S, idNew);
    ES:Drop(id);
    ES:Save(S);
  } catch(ex) {
    ES:DlgMessage('Unable to Reset ID: ' + ex, ERROR);
  }
}

const function ES:ResetName(name, nameNew) {
  ES:Log(TRACE, 'ES:ResetName(' + name + ', ' + nameNew + ')');
  try {
    if (!ES:IsAdmin()) {
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
    ES:SetName(S, nameNew);
    ES:Drop(ES:GetId(S));
    ES:Save(S);
  } catch(ex) {
    ES:DlgMessage('Unable to Reset Name: ' + ex, ERROR);
  }
}

#################################################################################
# Backup function
#################################################################################
const function ES:Backup(id) {
  ES:Log(TRACE, 'ES:Backup(' + id + ')');
  try {
    if (!ES:IsAdmin()) {
      throw 'You must be running in administrative mode to do this';
    }
    if (id == null) {
      id = ES:DlgInput('Enter series id');
      if (id == null) {
        throw 'Cancelled by user';
      }
      id = ES:ParseInt(id);
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
    if (ES:GetId(series) >= ES:BACKUP_BASE) {
      throw 'Cannot backup a series already backed up';
    }
    if (ES:Exists(ES:GetId(series) + ES:BACKUP_BASE)) {
      throw 'Cannot backup series: already exists: ' + (ES:GetId(series) + ES:BACKUP_BASE);
    }
    if (!ES:DlgConfirm()) {
      throw 'Cancelled by user';
    }
    ES:Log(TRACE, 'backing up series: ' + series);
    ES:SetName(series, ES:GetName(series) + ES:BACKUP_EXT);
    ES:SetId(series, ES:GetId(series) + ES:BACKUP_BASE);
    ES:Save(series);
    ES:DlgMessage('Backup complete for ' + id);
  } catch(ex) {
    ES:DlgMessage('Unable to perform backup: ' + ex, ERROR);
  }
}

#################################################################################
# Highest / Lowest functions
#################################################################################
const function ES:Highest(object, withDate) {
  ES:Log(TRACE, 'ES:Highest(' + object + ', ' + withDate + ')');
  function fn(idx, d, v) {
    if (v > ES:GGet('METRICS.highest')) {
      ES:Log(TRACE, 'found larger value: ' + d + ' => ' + v);
      ES:GPut('METRICS.date.highest', d);
      ES:GPut('METRICS.highest', v);
    }
  }

  ES:Log(TRACE, 'loading series');
  series = ES:Load(object);
  if (series == null or ES:GetSize(series) == 0) {
    ES:Log(TRACE, 'series is null or empty; returning null');
    return null;
  }
  if (withDate == true) {
    ES:Log(TRACE, 'initializing with date ' + ES:GetDate(series, 0));
    ES:GPut('METRICS.date.highest', ES:GetDate(series, 0));
  }
  ES:Log(TRACE, 'initializing with value ' + ES:Get(series, 0));
  ES:GPut('METRICS.highest', ES:Get(series, 0));
  ES:Data(series, fn);
  if (withDate == true) {
    ES:Log(TRACE, 'found highest value: ' + ES:GGet('METRICS.date.highest') + ' => ' + ES:GGet('METRICS.highest'));
    return '' + ES:GGet('METRICS.date.highest') + ' => ' + ES:GGet('METRICS.highest');
  } else {
    ES:Log(TRACE, 'found highest value: ' + ES:GGet('METRICS.highest'));
    return ES:GGet('METRICS.highest');
  }
}

const function ES:Lowest(object, withDate) {
  ES:Log(TRACE, 'ES:Lowest(' + object + ', ' + withDate + ')');
  function fn(idx, d, v) {
    if (v < ES:GGet('METRICS.lowest')) {
      ES:Log(TRACE, 'found smaller value: ' + d + ' => ' + v);
      ES:GPut('METRICS.date.lowest', d);
      ES:GPut('METRICS.lowest', v);
    }
  }

  ES:Log(TRACE, 'loading series');
  series = ES:Load(object);
  if (series == null or ES:GetSize(series) == 0) {
    ES:Log(TRACE, 'series is null or empty; returning null');
    return null;
  }
  if (withDate == true) {
    ES:Log(TRACE, 'initializing with date ' + ES:GetDate(series, 0));
    ES:GPut('METRICS.date.lowest', ES:GetDate(series, 0));
  }
  ES:Log(TRACE, 'initializing with value ' + ES:Get(series, 0));
  ES:GPut('METRICS.lowest', ES:Get(series, 0));
  ES:Data(series, fn);
  if (withDate == true) {
    ES:Log(TRACE, 'found lowest value: ' + ES:GGet('METRICS.date.lowest') + ' => ' + ES:GGet('METRICS.lowest'));
    return '' + ES:GGet('METRICS.date.lowest') + ' => ' + ES:GGet('METRICS.lowest');
  } else {
    ES:Log(TRACE, 'found lowest value: ' + ES:GGet('METRICS.lowest'));
    return ES:GGet('METRICS.lowest');
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
    if (ES:GetId(series) < ES:BACKUP_BASE) {
      ES:Log(TRACE, 'series id < ES:BACKUP_BASE; is a candidate for metrics');
      ES:GPut('METRICS.numberOfSeries', METRICS.numberOfSeries + 1);
      ES:GPut('METRICS.numberOfRecords', METRICS.numberOfRecords + ES:GetSize(series));
      ES:Printf('%-20s%8d\n', ES:GetName(series), ES:GetSize(series));
    }
  }
  
  ES:Log(TRACE, 'ES:Usage()');
  ES:GPut('METRICS.numberOfSeries', 0);
  ES:GPut('METRICS.numberOfRecords', 0);
  ES:Print('Series Metrics');
  ES:Print('----------------------------');
  ES:Ds(m);
  ES:Print();
  ES:Print('Series stored in datastore: ' + METRICS.numberOfSeries + ' (excluding backup series)');
  ES:Print('Number of records stored in datastore: ' + METRICS.numberOfRecords);
}

const function ES:Defaults() {
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
  ES:Print('defaults.panel.decorations.color = Color');
  ES:Print('defaults.panel.decorations.stroke = int');
}

#################################################################################
# String functions
#################################################################################
const function ES:StartsWith(string, prefix) {
  ES:Log(TRACE, 'ES:StartsWith(' + string + ', ' + prefix + ')');
  if (ES:GetLength(prefix) > ES:GetLength(string)) {
    return false;
  }
  ss = ES:SubString(string, 0, ES:GetLength(prefix));
  ES:Log(TRACE, 'substring = ' + ss);
  return ss == prefix;
}

const function ES:EndsWith(string, suffix) {
  ES:Log(TRACE, 'ES:EndsWith(' + string + ', ' + suffix + ')');
  if (ES:GetLength(suffix) > ES:GetLength(string)) {
    return false;
  }
  if (ES:GetLength(suffix) == 0) {
    ES:Log(TRACE, 'suffix length = 0, returning true');
    return true;
  }
  ss = ES:SubString(string, ES:GetLength(string) - ES:GetLength(suffix), ES:GetLength(string));
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
  S = ES:Create(ES:ToString(ES:GetName(series)) + '-chopped');
  if (date1 == null) {
    date1 = ES:GetDate(series, 0);
  }
  if (date2 == null) {
    date2 = ES:GetDate(series, ES:GetSize(series) - 1);
  }
  for (i = 0; i < ES:GetSize(series); i++) {
    if (ES:GetDate(series, i) >= date1 and ES:GetDate(series, i) <= date2) {
      ES:Insert(S, ES:GetDate(series, i), ES:Get(series, i));
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
  S = ES:Create(ES:GetName(series) + '-scaled [x' + scale + ']');
  ES:Insert(S, ES:GetDate(series, 0), ES:Get(series, 0));
  for (i = 1; i < ES:GetSize(series); i++) {
    totalScale = scale * (ES:Get(series, i) - ES:Get(series, i - 1)) / ES:Get(series, i - 1);
    ES:Insert(S, ES:GetDate(series, i), (1 + totalScale) * ES:Get(S, i - 1));
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
# ES:AnnualYield(periodYield, period)
#
# Calculates the annual yield of 'periodYield'
#
# periodYield : the period yield, expressed as a percentage (e.g. 3.25)
# period      : the period, in days (e.g. 90)
#
# Returns: the annual yield, expressed as a percentage
#################################################################################
const function ES:AnnualYield(periodYield, period) {
  ES:Log(TRACE, 'ES:AnnualYield(' + periodYield + ', ' + period + ')');
  yield = (1 + (periodYield / 100)) ^ (365 / period) - 1;
  return yield * 100; 
}

#################################################################################
# ES:PeriodYield(annualYield, period)
#
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

#################################################################################
# ES:Transform(s, s1, s2, y1, y2)
#
# Transforms a s1..s2 space into a y1..y2 space.
# Note that s is force-bounded into s1..s2.
#
# s  : value to transform from s1..s2 into y1..y2
# s1 : lower bound of s1..s2 space
# s2 : upper bound of s1..s2 space
# y1 : lower bound of y1..y2 space
# y2 : upper bound of y1..y2 space
#
# Returns: transformation of s into y1..y2 space
#################################################################################
const function ES:Transform(s, s1, s2, y1, y2) {
  # bound s to within s1..s2
  s = :Min(s, s2); 
  s = :Max(s, s1);

  # transformation constant
  S = (y2 - y1) / (s2 - s1);
  return S * (s - s1) + y1;
} 

#################################################################################
# ES:PercentChange(value1, value2)
#
# Calculates the Percent Change between 'value1' and 'value2'.
#
# value1 : the first value
# value2 : the second value
#
# Returns: the percent change between value1 and value2
#################################################################################
const function ES:PercentChange(value1, value2) {
  return 100 * (value2 - value1) / value1;
}

#################################################################################
# ES:PercentInverse(value)
#
# Calculates the Inverse Percent Change of 'value'
#
# value : the value to take the inverse of, as a percentage
#
# Returns: the inverse percent change
#################################################################################
const function ES:PercentInverse(value) {
  return 100 * (100 / (100 + value) - 1);
}

#################################################################################
# ES:GenerateId()
#
# Generates a new id which does not conflict with an existing id
#
# Returns: the new id
#################################################################################
const function ES:GenerateId() {
  while (true) {
    id = ES:Random(1000) + 1;
    if (!ES:Exists(id)) {
      break;
    }
    ES:Log(DEBUG, 'id already exists: ' + id);
  }
  ES:Log(DEBUG, 'generated id=' + id);
  return id;
}

#################################################################################
# ES:Plot(arg1, arg2, arg3, arg4)
#
# Plots up to 4 series, passed as arg1, arg2, arg3, and arg4.  Uses ES:Load()
# and as such, these arguments may be integers, Strings, or actual series.
# Also, attempts to scale the series logarithmically, though falls back to
# linear scaling if needed.  Finally, automatically determines the frequency
# and the chart label.
#
# arg1 : the first series
# arg2 : the second series (may be null)
# arg3 : the third series (may be null)
# arg4 : the fourth series (may be null)
#################################################################################
const function ES:Plot(arg1, arg2, arg3, arg4) {
  function dxincr(series) {
    if (series == null) {
      return 18;
    }
    if (:GetFrequencyShort(series) == 'D') {
      return 1;
    } else if (:GetFrequencyShort(series) == 'W') {
      return 8;
    } else {
      return 18;
    }
  }

  if (arg1 == null) {
    :Log(INFO, 'no series passed, nothing to do');
    return;
  }

  ES:Log(DEBUG, 'loading series...');
  arg1 = ES:Load(arg1);
  arg2 = ES:Load(arg2);
  arg3 = ES:Load(arg3);
  arg4 = ES:Load(arg4);

  ES:Log(DEBUG, 'computing lowest values...');
  l1 = l2 = l3 = l4 = 0.01;
  if (arg1 != null and :GetSeriesType(arg1) == 'float') {
    l1 = ES:Lowest(arg1);
  }
  if (arg2 != null and :GetSeriesType(arg2) == 'float') {
    l2 = ES:Lowest(arg2);
  }
  if (arg3 != null and :GetSeriesType(arg3) == 'float') {
    l3 = ES:Lowest(arg3);
  }
  if (arg4 != null and :GetSeriesType(arg4) == 'float') {
    l4 = ES:Lowest(arg4);
  }

  ES:Log(DEBUG, 'computing scaling type...');
  if (l1 > 0 and l2 > 0 and l3 > 0 and l4 > 0) {
    :Log(DEBUG, 'LOG scaling detected');
    defaults.chart.scaletype = LOG;
  } else {
    :Log(DEBUG, 'LINEAR scaling detected');
    defaults.chart.scaletype = LINEAR;
  }

  ES:Log(DEBUG, 'computing dxincr...');
  defaults.panel.frequency = MONTHS;
  dx = dxincr(arg1);
  dx = :Min(dx, dxincr(arg2));
  dx = :Min(dx, dxincr(arg3));
  dx = :Min(dx, dxincr(arg4));
  :Log(DEBUG, 'detected dxincr = ' + dx);
  defaults.panel.dxincr = dx;

  defaults.panel.label = 'Consolidated Panel';

  if (:GetTitle(arg1) != null) {
    defaults.chart.label = :GetTitle(arg1);
  }
  if (arg2 != null and :GetTitle(arg2) != null) {
    defaults.chart.label = defaults.chart.label + ' / ' + :GetTitle(arg2);
  }
  if (arg3 != null and :GetTitle(arg3) != null) {
    defaults.chart.label = defaults.chart.label + ' / ' + :GetTitle(arg3);
  }
  if (arg4 != null and :GetTitle(arg4) != null) {
    defaults.chart.label = defaults.chart.label + ' / ' + :GetTitle(arg4);
  }
  :Plot(arg1, arg2, arg3, arg4);
}

