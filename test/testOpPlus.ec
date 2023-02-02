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

print("TEST PASSED");
