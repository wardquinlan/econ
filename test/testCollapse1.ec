print("testCollapse1: collapse a single series");

print("creating T...");
T = create("MySeries");

print("collapsing T...");
TCollapsed = collapse(T);

assert(size(TCollapsed) == 0, "size is not 0");

print("inserting into T...");
insert(T, "2020-01-01", 10);
insert(T, "2020-01-02", 20);
insert(T, "2020-01-03", 30);

TCollapsed = collapse(T);
assert(size(TCollapsed) == 3, "size is not 3");
