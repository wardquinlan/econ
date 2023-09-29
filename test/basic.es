function f(a, b) {
  if (defined('a')) {
    print(a);
  }
  if (defined('b')) {
    print(b);
  }
}


f(1, 2);
f(3);
f('a', 'b', 'c');
f();
