print('RUNNING PUT TESTS');

function f(C) {
  print('f() called');
  assert(C == 10);
  assert(defined('N'));
  assert(N == 8);
  N = 5;
  assert(defined('N'));
  assert(N == 5);

  function g() {
    print('g() called');
    assert(N == 5);
    N = 6;
    assert(N == 6);
  }

  g();
  assert(N == 5);
}

function h() {
  print('h() called');
  gPut('N', 9);
  assert(N == 9);
}

assert(!defined('N'));
N = 8;
assert(defined('N'));
assert(N == 8);

f(10);
assert(N == 8);

h();
assert(N == 9);

print('PUT TESTS COMPLETE');
