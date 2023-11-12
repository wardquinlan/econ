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

print('1 - true true true false:');
data(D <= '2022-12-03');
print('2 - true true false false:');
data(D < '2022-12-03');
print('3 - false false true true:');
data(D >= '2022-12-03');
print('4 - false false false true:');
data(D > '2022-12-03');

print('5 - true true true false:');
data(D <= :Date('2022-12-03'));
print('6 - true true false false:');
data(D < :Date('2022-12-03'));
print('7 - false false true true:');
data(D >= :Date('2022-12-03'));
print('8 - false false false true:');
data(D > :Date('2022-12-03'));

###

print('9 - true true true false:');
data('2022-12-03' >= D);
print('10 - true true false false:');
data('2022-12-03' > D);
print('11 - false false true true:');
data('2022-12-03' <= D);
print('12 - false false false true:');
data('2022-12-03' < D);

print('13 - true true true false:');
data(:Date('2022-12-03') >= D);
print('14 - true true false false:');
data(:Date('2022-12-03') > D);
print('15 - false false true true:');
data(:Date('2022-12-03') <= D);
print('16 - false false false true:');
data(:Date('2022-12-03') < D);

:Print('DATE TESTS PASSED');
