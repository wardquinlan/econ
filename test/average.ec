print('RUNNING AVERAGE / SUM TESTS');

A = create("A", 1);
insert(A, "2022-12-01", 2);
insert(A, "2022-12-02", 3);
insert(A, "2022-12-03", 5);
insert(A, "2022-12-04", 8);
insert(A, "2022-12-05", 7);
insert(A, "2022-12-06", 1);
insert(A, "2022-12-07", 8);
insert(A, "2022-12-08", 4);
insert(A, "2022-12-09", 3);
insert(A, "2022-12-10", 9);
assert(getSize(A) == 10, "getSize(A) != 10");

B = average(A, 4);
assert(getSize(B) == 7, "getSize(B) != 7");
B1 = sum(A, 4);
assert(getSize(B) == getSize(B1));

assert(get(B, 0) == 4.5,  "index 0");
assert(get(B, 1) == 5.75, "index 1");
assert(get(B, 6) == 6,    "index 6");

assert(get(B1, 0) == 4 * 4.5,  "index 0");
assert(get(B1, 1) == 4 * 5.75, "index 1");
assert(get(B1, 6) == 4 * 6,    "index 6");

assert(get(date(B), 0) == '2022-12-04');
assert(get(date(B), 1) == '2022-12-05');
assert(get(date(B), 6) == '2022-12-10');

assert(get(date(B1), 0) == '2022-12-04');
assert(get(date(B1), 1) == '2022-12-05');
assert(get(date(B1), 6) == '2022-12-10');

print('AVERAGE / SUM TESTS PASSED');
