print("testCollapse5: collapse 3 overlapping series");

print("creating T1...");
T1 = create("MySeries1");

print("creating T2...");
T2 = create("MySeries2");

print("creating T2...");
T3 = create("MySeries3");

print("inserting into T1...");
insert(T1, "2020-01-01", 10);
insert(T1, "2020-01-02", 20);
insert(T1, "2020-01-03", 30);
insert(T1, "2020-03-01", 10);
insert(T1, "2020-03-02", 20);
insert(T1, "2020-03-03", 30);

print("inserting into T2...");
insert(T2, "2020-02-01", 10);
insert(T2, "2020-02-02", 20);
insert(T2, "2020-02-03", 30);
insert(T2, "2020-03-01", 10);
insert(T2, "2020-03-02", 20);
insert(T2, "2020-03-03", 30);
insert(T2, "2020-04-01", 10);
insert(T2, "2020-04-02", 20);
insert(T2, "2020-04-03", 30);

print("inserting into T3...");
insert(T3, "2020-05-01", 10);
insert(T3, "2020-05-02", 20);
insert(T3, "2020-05-03", 30);
insert(T3, "2019-12-01", 10);
insert(T3, "2019-12-02", 20);
insert(T3, "2019-12-03", 30);
insert(T3, "2020-04-01", 10);
insert(T3, "2020-04-02", 20);
insert(T3, "2020-04-03", 30);

print("collapsing #1...");
TCollapsed1 = collapse(T1, T2, T3);
assert(size(TCollapsed1) == 18, "size is not 18");

print("collapsing #2...");
TCollapsed2 = collapse(T3, T2, T1);
assert(size(TCollapsed2) == 18, "size is not 18");

print("collapsing #3...");
TCollapsed3 = collapse(T3, T1, T2);
assert(size(TCollapsed3) == 18, "size is not 18");
