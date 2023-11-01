print('RUNNING RETURNS TESTS');

function ff() {
}

function gg() {
  return;
}

function hh() {
  return 7;
}

function outer1() {
  function inner1() {
    return;
  }
  return inner1();
}

function outer2() {
  function inner2() {
  }
  return inner2();
}

function outer3() {
  function inner3() {
    return 'abc';
  }
  return inner3();
}

assert(ff() == null);
assert(gg() == null);
assert(hh() == 7);
assert(outer1() == null);
assert(outer2() == null);
assert(outer3() == 'abc');

print('RETURNS TESTS PASSED');
