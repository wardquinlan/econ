print('RUNNING COND TESTS');

assert(true);
assert(true != false);
assert((true == true) != false);
assert((true == false) == false);
assert(true == (false == false));
assert(false == (false == true));
assert(true and true);
assert((true and false) == (false and true));
assert(true and (false or true) == false or (true and true));
assert(true == !false);
assert(false == !true);
assert(true and !(false == true));
assert(false or (false == !true));
assert(3 < 2 or 4 > 5 or !!true);

print('COND TESTS PASSED');
