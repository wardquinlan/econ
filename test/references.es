print('RUNNING REFERENCES TESTS');

function fn() {
  return 1; 
}

function fn() {
  return 2;
}

f = fn;
assert(f() == 2);

try {
  function fn() {
  }
  print('SHOULD NOT ARRIVE HERE!!!');
} catch(ex) {
  print('Exception thrown, as expected');
}

try {
  fn = null;
  print('SHOULD NOT ARRIVE HERE!!!');
} catch(ex) {
  print('Exception thrown, as expected');
}

print('REFERENCES TESTS PASSED');
