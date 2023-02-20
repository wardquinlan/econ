print("RUNNING MERGE TESTS");
print("THESE TESTS REQUIRE ADMIN MODE");

drop(99);
include "T1.ec";
setId(T1, 99);
setTitle(T1, "My Title");

save(T1);

insert(T1, "2022-12-01", 1);
insert(T1, "2022-12-05", 5);
insert(T1, "2022-12-10", 10);

merge(T1);

T2 = load(99);

assert(size(T1) == size(T2), "T1 != T2");
assert(get(T1, 0) == get(T2, 0));
assert(get(T1, 3) == get(T2, 3));
assert(get(T1, 6) == get(T2, 6));

print("MERGE TESTS PASSED");

