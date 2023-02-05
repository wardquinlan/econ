T1 = create('T1', 1);
insert(T1, '2022-12-03', 3);
insert(T1, '2022-12-04', 4);
insert(T1, '2022-12-06', 6);
insert(T1, '2022-12-09', 9);

assert(size(T1) == 4);
assert(offset(T1) == 0);
