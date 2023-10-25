#################################################################################
# load() functions
#################################################################################
function loadSeries(series) {
  if (series == null or series == 'undefined') {
    throw 'cannot load empty series: ' + series;
  }
  if (getType(series) != 'Series') {
    series = load(series);
  }
  if (getSize(series) == 0) {
    series = load(getId(series));
  }
  return series;
}

function autoload(series) {
  series = loadSeries(series);
  # don't load the backups...
  if (getId(series) < 10000) {
    name = getName(series);
    gPut(name, series);
  } 
} 

#################################################################################
# update() functions
#################################################################################
function updateSeries(series) {
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

function updateAll() {
  ds(updateSeries);
}

#################################################################################
# reset() functions
#################################################################################
function resetId(id, idNew) {
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

function resetName(name, nameNew) {
  if (!isAdmin()) {
    throw 'you must be running in administrative mode to reset name\'s';
  }
  if (getType(name) != 'String' or getType(nameNew) != 'String') {
    throw 'usage: resetName(String name, String nameNew);';
  }
  if (name == 'undefined' or nameNew == 'undefined') {
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

#################################################################################
# backup()
#################################################################################
function backup(id) {
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

#################################################################################
# highest() / lowest() functions
#################################################################################
function hh(idx, d, v) {
  if (v > gGet('METRICS.highest')) {
    gPut('METRICS.highest', v);
  }
}

function ll(idx, d, v) {
  if (v < gGet('METRICS.lowest')) {
    gPut('METRICS.lowest', v);
  }
}

function highest(series) {
  series = loadSeries(series);
  gPut('METRICS.highest', get(series, 0));
  data(series, hh);
  return gGet('METRICS.highest');
}

function lowest(series) {
  series = loadSeries(series);
  gPut('METRICS.lowest', get(series, 0));
  data(series, ll);
  return gGet('METRICS.lowest');
}

#################################################################################
# usage() functions
#################################################################################
function metrics(series) {
  series = loadSeries(series);
  if (getId(series) < 10000) {
    gPut('METRICS.numberOfSeries', METRICS.numberOfSeries + 1);
    gPut('METRICS.numberOfRecords', METRICS.numberOfRecords + getSize(series));
    print(getName(series) + ': ' + getSize(series));
  }
}

function usage() {
  gPut('METRICS.numberOfSeries', 0);
  gPut('METRICS.numberOfRecords', 0);
  print('Series Metrics');
  print('--------------');
  ds(metrics);
  print('');
  print('Series stored in datastore: ' + METRICS.numberOfSeries + ' (excluding backup series)');
  print('Number of records stored in datastore: ' + METRICS.numberOfRecords);
}
