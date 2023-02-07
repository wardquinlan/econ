print('RUNNING MINUS TESTS');

assert(1 - 1 == 0);
assert(1 - 1.0 == 0);
assert(1.0 - 1 == 0);
assert(1.0 - 1.0 == 0);
assert(1 - 1 - 1 == -1);
assert(1 + 2 - 5 - 7 == -9);

include 'T1.ec';

T = -T1;
assert(size(T) == size(T1));
assert(get(T, 0) == -get(T1, 0));
assert(get(T, 1) == -get(T1, 1));
assert(get(T, 2) == -get(T1, 2));
assert(get(T, 3) == -get(T1, 3));

T = - -T1; -- be careful of comments
assert(size(T) == size(T1));
assert(get(T, 0) == get(T1, 0));
assert(get(T, 1) == get(T1, 1));
assert(get(T, 2) == get(T1, 2));
assert(get(T, 3) == get(T1, 3));

print('MINUS TESTS PASSED');
