print("TEST5: collapse 3 overlapping series");
print("------------------------------------");
print();

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
insert(T1, "2020-03-01", 40);
insert(T1, "2020-03-02", 50);
insert(T1, "2020-03-03", 60);

print("inserting into T2...");
insert(T2, "2020-02-01", 20);
insert(T2, "2020-02-02", 30);
insert(T2, "2020-02-03", 40);
insert(T2, "2020-03-01", 50);
insert(T2, "2020-03-02", 60);
insert(T2, "2020-03-03", 70);
insert(T2, "2020-04-01", 80);
insert(T2, "2020-04-02", 90);
insert(T2, "2020-04-03", 100);

print("inserting into T3...");
insert(T3, "2020-05-01", 30);
insert(T3, "2020-05-02", 40);
insert(T3, "2020-05-03", 50);
insert(T3, "2019-12-01", 60);
insert(T3, "2019-12-02", 70);
insert(T3, "2019-12-03", 80);
insert(T3, "2020-04-01", 90);
insert(T3, "2020-04-02", 100);
insert(T3, "2020-04-03", 110);

print("T1:");
data(T1);
print("T2:");
data(T2);
print("T3:");
data(T3);

plot("test20.xml");
