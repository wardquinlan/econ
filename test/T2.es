T2 = create('T2', 1);
insert(T2, '2022-12-01', 1);
insert(T2, '2022-12-02', 2);
insert(T2, '2022-12-05', 5);
insert(T2, '2022-12-06', 6);
insert(T2, '2022-12-07', 7);

assert(getSize(T2) == 5);
assert(getOffset(T2) == 0);
