#function f() {
  try {
    if (true) {
      return 7;
      throw 'crap';
    }
    assert(false);
  } catch(ex) {
    print('an exception occurred: ' + ex);
    return;
    assert(false);
  }
#}

#f();
print('done');
