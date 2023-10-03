function autoload(series) {
  # don't load the backups...
  if (getId(series) < 10000) {
    # need to load data
    series = load(getId(series)); 
    name = getName(series);
    gPut(name, series);
  } 
} 
  
function updateSeries(series) {
  if (getSource(series) == 'FRED' and getId(series) < 10000) {
    id = getId(series);
    name = getName(series);
    log(INFO, 'updating ' + id + ':' + name + '...');
    series = fred(name);
    setId(series, id);
    merge(series, '--with-inserts');
  }
}

function metrics(series) {
  if (getId(series) < 10000) {
    S = load(getId(series));
    METRICS.numberOfSeries = METRICS.numberOfSeries + 1;
    METRICS.numberOfRecords = METRICS.numberOfRecords + getSize(S);
    print(getName(S) + ': ' + getSize(S));
  }
}

function last(series) {
  if (getSize(series) == 0) {
    throw getName(series) + ': no data';
  }
  D = date(series);
  C = change(series);
  assert(getSize(series) == getSize(D), 'original series and date series are not consistent');
  assert(getSize(series) == getSize(C) + 1, 'original series and change series are not consistent');
  print(get(D, getSize(D) - 1) + ': ' + get(series, getSize(series) - 1) + ' (change = ' + get(C, getSize(C) - 1) + ')');
}

