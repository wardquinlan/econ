print('RUNNING EXP TESTS');

assert(2 ^ 3 == 8);
assert(2 ^ 3.0 == 8);
assert(2.0 ^ 3 == 8);
assert(2.0 ^ 3.0 == 8);
assert(2 ^ 3 ^ 2 == 64);
assert(2 ^ (3 ^ 2) == 512); 

print('EXP TESTS PASSED');
