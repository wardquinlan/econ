print('RUNNING PLUS TESTS');

assert(1 + 1 == 2);
assert(1 + 1.0 == 2);
assert(1.0 + 1 == 2);
assert(1.0 + 1.0 == 2);
assert("1" + 0 == "10");
assert(1 + 1 + 1 == 3);

include 'T1.ec';

T = T1 + 1;
assert(size(T) == size(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = 1 + T1;
assert(size(T) == size(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = T1 + 1.0;
assert(size(T) == size(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = 1.0 + T1;
assert(size(T) == size(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

include 'T2.ec';

T = T1 + T2;
data(T);

print('PLUS TESTS PASSED');
