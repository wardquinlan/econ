print("TEST4: collapse 2 overlapping series");
print("------------------------------------");
print();

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

print("data(T1):");
data(T1);
print("data(T2):");
data(T2);

print("collapsing #1...");
TCollapsed1 = collapse(T1, T2);
data(TCollapsed1);

print("collapsing #2...");
TCollapsed2 = collapse(T2, T1);
data(TCollapsed2);
