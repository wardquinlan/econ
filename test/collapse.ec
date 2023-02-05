T1 = create('T1', 1);
insert(T1, '2022-12-03', 3);
insert(T1, '2022-12-04', 4);
insert(T1, '2022-12-06', 6);
insert(T1, '2022-12-09', 9);

T2 = create('T2', 1);
insert(T2, '2022-12-01', 1);
insert(T2, '2022-12-02', 2);
insert(T2, '2022-12-05', 5);
insert(T2, '2022-12-06', 6);
insert(T2, '2022-12-07', 7);

C = collapse(T1, T2);
assert(size(C) == 8);
data(C);
