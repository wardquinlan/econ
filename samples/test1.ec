print("creating T...");
T = create("MySeries");
cat();

print("collapsing T...");
TCollapsed = collapse(T);
cat();

print("inserting into T");
insert(T, "2020-01-01", 10);
insert(T, "2020-01-02", 20);
insert(T, "2020-01-03", 30);
cat();

print("collapsing T");
TCollapsed = collapse(T);
cat();

print("data(T)");
data(T);

print("data(TCollapsed)");
data(TCollapsed);
