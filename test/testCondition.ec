-- test condition functionality

print("testing integer conditions");
assert(3 == 3, "3 == 3");
assert(3 != 4, "3 != 4");
assert(3 < 4, "3 < 4");
assert(4 > 3, "4 > 3");
assert(3 <= 3, "3 <= 3");
assert(3 >= 3, "3 > 3");

print("testing float conditions");
assert(3.0 == 3.0, "3.0 == 3.0");
assert(3.0 != 4.0, "3.0 != 4.0");
assert(3.0 < 4.0, "3.0 < 4.0");
assert(4.0 > 3.0, "4.0 > 3.0");
assert(3.0 <= 3.0, "3.0 <= 3.0");
assert(3.0 >= 3.0, "3.0 >= 3.0");

print("testing String conditions");
assert("abc" == "abc", "abc == abc");
assert("abc" != "def", "abc != def");

print("testing Boolean conditions");
assert(true == true, "true == true");
assert(true != false, "true != false");
assert(false != true, "false != true");
assert(false == false, "false == false");

print("testing logical conditions");
assert(3 < 4 and 4 < 5);
assert(3 < 4 and true);
assert(3 < 4 or 3 > 4);
assert(true and 3 < 4);
assert(true and true);
assert(true and true or false);
assert(true and false or true);
assert((true and false) or true);
assert(true and (false or true));
assert((true and false) == false);


