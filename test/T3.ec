T3 = create('T3', BOOLEAN);
insert(T3, '2022-12-03', true);
insert(T3, '2022-12-04', false);
insert(T3, '2022-12-06', true);
insert(T3, '2022-12-09', false);

assert(size(T3) == 4);
assert(offset(T3) == 0);
assert(get(T3, 0));
assert(!get(T3, 1));
assert(get(T3, 2));
assert(!get(T3, 3));
