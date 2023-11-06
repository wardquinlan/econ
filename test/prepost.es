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

function preIncr() {
  assert(++a == 1);
  assert(a == 1);
} 

function postIncr() {
  assert(a++ == 0);
  assert(a == 1);
} 

function preDecr() {
  assert(--a == -1);
  assert(a == -1);
} 

function postDecr() {
  assert(a-- == 0);
  assert(a == -1);
} 

a = 0;
preIncr();
assert(a == 0);
postIncr();
assert(a == 0);
preDecr();
assert(a == 0);
postDecr();
assert(a == 0);

:Print('PRE/POST TESTS PASSED');
