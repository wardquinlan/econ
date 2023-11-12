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

d = :Date('2023-11-17');
assert(d == '2023-11-17');
assert(d++ == '2023-11-17');
assert(d == '2023-11-18');
assert(++d == '2023-11-19');
assert(d == '2023-11-19');

d = :Date('2023-11-17');
assert(d == '2023-11-17');
assert(d-- == '2023-11-17');
assert(d == '2023-11-16');
assert(--d == '2023-11-15');
assert(d == '2023-11-15');

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
  assert(++d == '2023-11-18');
  assert(d == '2023-11-18');
} 

function postIncr() {
  assert(a++ == 0);
  assert(a == 1);
  assert(d++ == '2023-11-17');
  assert(d == '2023-11-18');
} 

function preDecr() {
  assert(--a == -1);
  assert(a == -1);
  assert(--d == '2023-11-16');
  assert(d == '2023-11-16');
} 

function postDecr() {
  assert(a-- == 0);
  assert(a == -1);
  assert(d-- == '2023-11-17');
  assert(d == '2023-11-16');
} 

a = 0;
d = :Date('2023-11-17');
preIncr();
assert(a == 0);
assert(d == '2023-11-17');
postIncr();
assert(a == 0);
assert(d == '2023-11-17');
preDecr();
assert(a == 0);
assert(d == '2023-11-17');
postDecr();
assert(a == 0);
assert(d == '2023-11-17');

:Print('PRE/POST TESTS PASSED');
