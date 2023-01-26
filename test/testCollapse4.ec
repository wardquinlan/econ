print("testCollapse4: collapse 2 overlapping series");

print("creating T1...");
T1 = create("MySeries1");

print("creating T2...");
T2 = create("MySeries2");

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

assert(size(T1) == 6, "size is not 6");
assert(size(T2) == 9, "size is not 9");

print("collapsing #1...");
TCollapsed1 = collapse(T1, T2);
assert(size(TCollapsed1) == 12, "size is not 12");

print("collapsing #2...");
TCollapsed2 = collapse(T2, T1);
assert(size(TCollapsed2) == 12, "size is not 12");
