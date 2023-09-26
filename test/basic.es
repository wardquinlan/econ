function f() {
  if (true) {
    print('f1');
  }
  if (true) {
    print('f2');
  }
}

print('m1');
print(f());
print('m2');

