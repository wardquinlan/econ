print('RUNNING NULL TESTS');

function f() {
  assert(defined('a'));
  assert(a == null);
  a = 5;
  assert(a == 5);
}

a = null;
assert(defined('a'));
assert(a == null);
assert(!(a != null));

S = create('myseries');
assert(S != null);
assert(3 != null);
assert(true != null);
assert('abc' != null);

f();
assert(a == null);
