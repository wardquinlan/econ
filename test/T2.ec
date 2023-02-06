T2 = create('T2', 1);
insert(T2, '2022-12-01', 1);
insert(T2, '2022-12-02', 2);
insert(T2, '2022-12-05', 5);
insert(T2, '2022-12-06', 6);
insert(T2, '2022-12-07', 7);

assert(size(T2) == 5);
assert(offset(T2) == 0);
