print('RUNNING STDEV TESTS');

M = create('myseries');

insert(M, '2023-01-01', 2);
#data(stdev(M, 1));
insert(M, '2023-01-02', 4);
#data(stdev(M, 1));
data(stdev(M, 2));
assert(getSize(stdev(M, 2)) == 1);
assert(get(stdev(M, 2), 0) == 1.4142135);

insert(M, '2023-01-03', 7);
insert(M, '2023-01-04', 3);
insert(M, '2023-01-05', 9);

data(stdev(M, 2));
assert(getSize(stdev(M, 2)) == 4);
assert(get(stdev(M, 2), 0) == 1.4142135);
assert(get(stdev(M, 2), 1) == 2.1213202);
assert(get(stdev(M, 2), 2) == 2.828427);
assert(get(stdev(M, 2), 3) == 4.2426405);

data(stdev(M, 5));
assert(getSize(stdev(M, 5)) == 1);
assert(get(stdev(M, 5), 0) == 2.9154758);
