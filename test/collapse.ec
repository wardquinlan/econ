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
assert(offset(C) == -1);

N1 = normalize(C, T1);
assert(size(N1) == 8);
assert(offset(N1) == 2);
assert(get(N1, 2) == 3);
assert(get(N1, 3) == 4);
assert(get(N1, 4) == 4);
assert(get(N1, 5) == 6);
assert(get(N1, 6) == 6);
assert(get(N1, 7) == 9);

N2 = normalize(C, T2);
assert(size(N2) == 8);
assert(offset(N2) == 0);
assert(get(N2, 0) == 1);
assert(get(N2, 1) == 2);
assert(get(N2, 2) == 2);
assert(get(N2, 3) == 2);
assert(get(N2, 4) == 5);
assert(get(N2, 5) == 6);
assert(get(N2, 6) == 7);
assert(get(N2, 7) == 7);

