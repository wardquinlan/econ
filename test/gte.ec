print('RUNNING GTE TESTS');

assert(1 >= 1);
assert(2 >= 1);
assert(1 >= 1.0);
assert(1.0 >= 1);
assert(1.0 >= 1.0);
assert(2 + 3 >= 7 -2);
assert("abc" >= "abc");
assert("abc" >= "abb");

print('GTE TESTS PASSED');
