:Print('RUNNING LOOP TESTS');

while (false) {
  ES:Assert(false);
}

while (true) {
  break;
  ES:Assert(false);
}

i = 0;
while (i < 10) {
  i++;
  if (i >= 5) continue;
  :Print(i);
}
ES:Assert(i == 10);

i = 0;
while (i < 10) {
  i++;
  if (i >= 5) break;
  :Print(i);
}
ES:Assert(i == 5);

function fn1() {
  if (true)
    break;
}
try {
  fn1();
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

function fn2() {
  break;
}
try {
  fn2();
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

function fn3(N) {
  i = 0;
  while (i < N) {
    if (i == 10) break;
    i++;
    :Print('fn3: ' + i);
  }
  return i;
}

ES:Assert(fn3(100) == 10);

:Print('LOOP TESTS PASSED');