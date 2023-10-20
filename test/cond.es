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
assert(!(false and (true or false)));
assert(true == !false);
assert(false == !true);
assert(true and !(false == true));
assert(false or (false == !true));
assert(3 < 2 or 4 > 5 or !!true);
assert(!(3 < 2 and !(4 < 3)));

include 'T3.es';

T = !T3;
assert(get(T3, 0) == !get(T, 0));
assert(get(T3, 1) == !get(T, 1));
assert(get(T3, 2) == !get(T, 2));
assert(get(T3, 3) == !get(T, 3));

T = !!T3;
assert(get(T3, 0) == get(T, 0));
assert(get(T3, 1) == get(T, 1));
assert(get(T3, 2) == get(T, 2));
assert(get(T3, 3) == get(T, 3));

print('COND TESTS PASSED');
