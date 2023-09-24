function outer() {
  function inner() {
    name = "inner name";
    print("inner: name = " + name);
  }
  name = "outer name";
  print("outer: name = " + name);
  inner();
  print("outer: name = " + name);
}

name = 'global name';
print("global: name = " + name);
outer();
print("global: name = " + name);
