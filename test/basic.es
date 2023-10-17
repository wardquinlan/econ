function f(s) {
  if (getType(s) == 'String' and s == 'undefined') {
    print('defaulting s to DFF...');
    s = load('DFF');
  }
  if (getType(s) == 'int') {
    if (!exists(s)) {
      throw 'series does not exist: ' + s;
    }
    s = load(s);
  }
  if (getType(s) == 'Series' and getId(s) < 10000) {
    print(getName(s));
  }
}

f();
f(500);
