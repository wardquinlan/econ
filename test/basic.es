function iterator(series) {
  if (getSource(series) == 'FRED') {
    print('FRED series found: ' + getName(series));
  }
  return 'x';
}

ds(iterator);
