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
  if (exists(nameNew)) {
    throw 'series already exists: ' + nameNew;
  } 
  S = load(name);
  setName(S, nameNew);
  drop(getId(S));
  save(S);
}

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

function last(series) {
  if (getType(series) == 'String' or getType(series) == 'int') {
    # try and load the series
    series = load(series);
  }
  if (getSize(series) == 0) {
    throw getName(series) + ': no data';
  }
  return get(series, getSize(series) - 1);
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

