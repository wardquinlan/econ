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

function h() {
  print('h() called');
  try {
    p = h;
    h = null;
    print('SHOULD NOT ARRIVE HERE!!!');
  } catch(ex) {
    print('Exception thrown, as expected');
  }
  p = null;
  h = 3; # goes into local scope
  p = h;
  h = null;
  print(p);
  print('SHOULD ARRIVE HERE');
}

h();
h = null;

function fn() {
  :Print('hello');
  p = fn;
  fn = null;
  :Print(fn);
}

fn();

print('REFERENCES TESTS PASSED');
