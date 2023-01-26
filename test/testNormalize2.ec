print("testNormalize2: normalizing 3 overlapping series");

print("creating T1...");
T1 = create("MySeries1");

print("creating T2...");
T2 = create("MySeries2");

print("creating T3...");
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

print("collapsing...");
TCollapsed = collapse(T1, T2, T3);
assert(size(TCollapsed) == 18, "size is not 18");

print("normalizing...");
TNormalized1 = normalize(TCollapsed, T1);
TNormalized2 = normalize(TCollapsed, T2);
TNormalized3 = normalize(TCollapsed, T3);
assert(size(TNormalized1) == 18, "size is not 18");
assert(size(TNormalized2) == 18, "size is not 18");
assert(size(TNormalized3) == 18, "size is not 18");
