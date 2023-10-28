print('RUNNING RETURNS TESTS');

function f() {
}

function g() {
  return;
}

function h() {
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

assert(f() == null);
assert(g() == null);
assert(h() == 7);
assert(outer1() == null);
assert(outer2() == null);
assert(outer3() == 'abc');

print('RETURNS TESTS PASSED');
