print("testNormalize1: normalize 2 non-overlapping series");

print("creating T1...");
T1 = create("MySeries1");

print("creating T2...");
T2 = create("MySeries2");

print("inserting into T1...");
insert(T1, "2020-01-01", 10);
insert(T1, "2020-01-02", 20);
insert(T1, "2020-01-03", 30);

print("inserting into T2...");
insert(T2, "2020-02-01", 10);
insert(T2, "2020-02-02", 20);
insert(T2, "2020-02-03", 30);

print("collapsing...");
TCollapsed = collapse(T1, T2);
assert(size(TCollapsed) == 6, "size is not 6");

print("normalizing...");
TNormalized1 = normalize(TCollapsed, T1);
assert(size(TNormalized1) == 6, "size is not 6");
TNormalized2 = normalize(TCollapsed, T2);
assert(size(TNormalized2) == 6, "size is not 6");
