function f(s) {
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

f(500);
