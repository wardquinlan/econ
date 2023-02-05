print('RUNNING PLUS TESTS');

assert(1 + 1 == 2);
assert(1 + 1.0 == 2);
assert(1.0 + 1 == 2);
assert(1.0 + 1.0 == 2);
assert("1" + 0 == "10");
assert(1 + 1 + 1 == 3);

include 'T1.ec';

T2 = T1 + 1;
assert(size(T2) == size(T1));
assert(get(T2, 0) == get(T1, 0) + 1);
assert(get(T2, 1) == get(T1, 1) + 1);
assert(get(T2, 2) == get(T1, 2) + 1);
assert(get(T2, 3) == get(T1, 3) + 1);

T2 = 1 + T1;
assert(size(T2) == size(T1));
assert(get(T2, 0) == get(T1, 0) + 1);
assert(get(T2, 1) == get(T1, 1) + 1);
assert(get(T2, 2) == get(T1, 2) + 1);
assert(get(T2, 3) == get(T1, 3) + 1);

T2 = T1 + 1.0;
assert(size(T2) == size(T1));
assert(get(T2, 0) == get(T1, 0) + 1);
assert(get(T2, 1) == get(T1, 1) + 1);
assert(get(T2, 2) == get(T1, 2) + 1);
assert(get(T2, 3) == get(T1, 3) + 1);

T2 = 1.0 + T1;
assert(size(T2) == size(T1));
assert(get(T2, 0) == get(T1, 0) + 1);
assert(get(T2, 1) == get(T1, 1) + 1);
assert(get(T2, 2) == get(T1, 2) + 1);
assert(get(T2, 3) == get(T1, 3) + 1);

print('PLUS TESTS PASSED');
