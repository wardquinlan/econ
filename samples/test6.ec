print("TEST6: normalize 2 non-overlapping series");
print("-----------------------------------------");
print();

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

print("normalizing...");
TNormalized1 = normalize(TCollapsed, T1);
TNormalized2 = normalize(TCollapsed, T2);

cat();
print("data(T1):");
data(T1);
print("data(T2):");
data(T2);
print("data(TCollapsed):");
data(TCollapsed);
print("data(TNormalized1):");
data(TNormalized1);
print("data(TNormalized2):");
data(TNormalized2);

