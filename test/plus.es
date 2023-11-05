print('RUNNING PLUS TESTS');

assert(1 + 1 == 2);
assert(1 + 1.0 == 2);
assert(1.0 + 1 == 2);
assert(1.0 + 1.0 == 2);
assert("1" + 0 == "10");
assert(1 + 1 + 1 == 3);
assert(1 + 2 - 5 - 7 == -9);

include 'T1.es';

T = T1 + 1;
assert(getSize(T) == getSize(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = 1 + T1;
assert(getSize(T) == getSize(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = T1 + 1.0;
assert(getSize(T) == getSize(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = 1.0 + T1;
assert(getSize(T) == getSize(T1));
assert(get(T, 0) == get(T1, 0) + 1);
assert(get(T, 1) == get(T1, 1) + 1);
assert(get(T, 2) == get(T1, 2) + 1);
assert(get(T, 3) == get(T1, 3) + 1);

T = +T1;
assert(getSize(T) == getSize(T1));
assert(get(T, 0) == get(T1, 0));
assert(get(T, 1) == get(T1, 1));
assert(get(T, 2) == get(T1, 2));
assert(get(T, 3) == get(T1, 3));

T = + +T1;
assert(getSize(T) == getSize(T1));
assert(get(T, 0) == get(T1, 0));
assert(get(T, 1) == get(T1, 1));
assert(get(T, 2) == get(T1, 2));
assert(get(T, 3) == get(T1, 3));

include 'T2.es';

T = T1 + T2;
assert(getSize(T) == 6);
assert(getOffset(T) == 0);
assert(get(T, 0) == 5);
assert(get(T, 1) == 6);
assert(get(T, 2) == 9);
assert(get(T, 3) == 12);
assert(get(T, 4) == 13);
assert(get(T, 5) == 16);

print('PLUS TESTS PASSED');
