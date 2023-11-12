:Print('RUNNING DATE TESTS');

D = :Date('2022-12-01');
assert(:GetType(D) == 'Date');

assert(D == '2022-12-01');
assert(D != '2022-12-02');
assert('2022-12-01' == D);
assert('2022-12-02' != D);
assert(D == :Date('2022-12-01'));
assert(D != :Date('2022-12-02'));
assert(:Date('2022-12-01') == D);
assert(:Date('2022-12-02') != D);

D2 = :Date('2022-12-02');
assert(D < D2);
assert(D < '2022-12-02');
assert(D <= D2);
assert(D <= '2022-12-02');
assert(D2 > D);
assert('2022-12-02' > D);
assert(D2 >= D);
assert('2022-12-02' >= D);

D = create('D', 3);
assert(:GetSeriesType(D) == 'Date');

insert(D, '2022-12-01');
insert(D, '2022-12-02');
insert(D, '2022-12-03');
insert(D, '2022-12-04');
data(D <= '2022-12-03');

:Print('DATE TESTS PASSED');
