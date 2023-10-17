function f() {
  if (false) {
  } else {
    print('error');
    return;
  }
}

a = f();
print(a);
return a;

