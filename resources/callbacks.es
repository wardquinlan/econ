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

