total = 0;

function count(series) {
  if (getSource(series) == 'FRED') {
    print('count for ' + getName(series) + ' = ' + getSize(series));
    total = total + getSize(series);
  }
}

cat(count);
print('total count for FRED series = ' + total);
