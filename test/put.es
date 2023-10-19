print('RUNNING PUT TESTS');

function f() {
  assert(defined('N'));
  assert(N == 8);
  N = 5;
  assert(defined('N'));
  assert(N == 5);

  function g() {
    assert(N == 5);
    N = 6;
    assert(N == 6);
  }

  g();
  assert(N == 5);
}

function h() {
  gPut('N', 9);
  assert(N == 9);
}

assert(!defined('N'));
N = 8;
assert(defined('N'));
assert(N == 8);

h();
assert(N == 9);

print('PUT TESTS COMPLETE');
