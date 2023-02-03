S1 = create('S1');
insert(S1, '2022-12-01', 10);
insert(S1, '2022-12-02', 20);
insert(S1, '2022-12-03', 30);

S2 = create('S2');
insert(S2, '2022-12-10', 10);
insert(S2, '2022-12-11', 20);
insert(S2, '2022-12-12', 30);

S = S1 + 5;
assert(offset(S) == 0);
assert(get(S, 0) == 15.0);
assert(get(S, 2) == 35.0);

S = 5 + S2;
assert(offset(S) == 0);
assert(get(S, 0) == 15.0);
assert(get(S, 2) == 35.0);

S3 = create('S3');
insert(S3, '2022-12-03', 3);
insert(S3, '2022-12-04', 4);
insert(S3, '2022-12-06', 6);
insert(S3, '2022-12-09', 9);

S4 = create('S4');
insert(S4, '2022-12-01', 1);
insert(S4, '2022-12-02', 2);
insert(S4, '2022-12-05', 5);
insert(S4, '2022-12-06', 6);
insert(S4, '2022-12-07', 7);

S = S3 + S4;
data(S);
