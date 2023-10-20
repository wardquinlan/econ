print('RUNNING NE TESTS');

assert(1 != 2);
assert(1 != 2.0);
assert(1.0 != 2);
assert(1.0 != 2.0);
assert(2 + 3 != 7 - 1);
assert("abc" != "abb");
assert(true != false);

print('NE TESTS PASSED');
