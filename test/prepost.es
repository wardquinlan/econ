:Print('RUNNING PRE/POST TESTS');

i = 0;
i++;
assert(i++ == 1);
assert(i == 2);
assert(++i == 3);
assert(i == 3);
assert(i-- == 3);
assert(i == 2);
assert(--i == 1);
assert(i == 1);
assert(i-- == 1);
assert(i == 0);

assert(i++ + 1 == 1);
assert(--i - 1 == -1);
assert(i == 0);

try {
  s = 'hello';
  s++;
  assert(false);
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

try {
  s = 'hello';
  ++s;
  assert(false);
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

const j = 0;
try {
  j++;
  assert(false);
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

:Print('PRE/POST TESTS PASSED');