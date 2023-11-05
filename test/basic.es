function digits(arg) {
  if (arg < 10) {
    return '0' + arg;
  } 
  return arg;
}

function printDays(m) {
  d = 1;
  while (d <= 30) {
    :Print('2023-' + digits(m) + '-' + digits(d));
    d++;
  }
}

m = 1;
while (m <= 12) {
  printDays(m);
  m++;
}

