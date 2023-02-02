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

S = S1 + S2;
assert(offset(S) == 3);
assert(get(S, 3) == 40.0);
assert(get(S, 4) == 50.0);
assert(get(S, 5) == 60.0);

S = S2 + S1;
assert(offset(S) == 3);
assert(get(S, 3) == 40.0);
assert(get(S, 4) == 50.0);
assert(get(S, 5) == 60.0);

S3 = create('S3');
insert(S3, '2022-12-08', 5);
insert(S3, '2022-12-11', 17);
insert(S3, '2022-12-15', 18);

S = S2 + S3;
print('S2:');
data(S2);
print('S3:');
data(S3);
print('S = S2 + S3:');
data(S);

print("TEST PASSED");
