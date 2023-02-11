print('RUNNING AVERAGE TESTS');

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
assert(size(A) == 10, "size(A) != 10");

B = average(A, 4);
assert(size(B) == 7, "size(B) != 7");

assert(get(B, 0) == 4.5,  "index 0");
assert(get(B, 1) == 5.75, "index 1");
assert(get(B, 6) == 6,    "index 6");

assert(get(date(B), 0) == '2022-12-04');
assert(get(date(B), 1) == '2022-12-05');
assert(get(date(B), 6) == '2022-12-10');

print('AVERAGE TESTS PASSED');
