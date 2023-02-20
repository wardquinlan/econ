print("RUNNING MERGE TESTS");
print("THESE TESTS REQUIRE ADMIN MODE");

drop(99);
include "T1.ec";
setId(T1, 99);
setTitle(T1, "My Title");

save(T1);

print("MERGE TESTS PASSED");
