function autoload(series) {
  if (getType(series) == 'int' or getType(series) == 'String') {
    # try to load the series
    series = load(series);
  }
  # don't load the backups...
  if (getId(series) < 10000) {
    # need to load data
    series = load(getId(series)); 
    name = getName(series);
    gPut(name, series);
  } 
} 
  
function updateSeries(series) {
  if (getType(series) == 'int' or getType(series) == 'String') {
    # try to load the series
    series = load(series);
  }
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
  if (getType(series) == 'int' or getType(series) == 'String') {
    # try to load the series
    series = load(series);
  }
  if (getId(series) < 10000) {
    # series data may not be loaded
    if (getSize(series) == 0) {
      series = load(getId(series));
    }
    METRICS.numberOfSeries = METRICS.numberOfSeries + 1;
    METRICS.numberOfRecords = METRICS.numberOfRecords + getSize(series);
    print(getName(series) + ': ' + getSize(series));
  }
}

function last(series) {
  if (getType(series) == 'String' or getType(series) == 'int') {
    # try and load the series
    series = load(series);
  }
  if (getSize(series) == 0) {
    # try and load the series
    series = load(getId(series));
    if (getSize(series) == 0) {
      throw getName(series) + ': no data';
    }
  }
  return get(series, getSize(series) - 1);
}

