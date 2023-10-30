print('RUNNING TRY/CATCH TESTS');

try {
  :Log(DEBUG, 'test 1a');
  :Log(DEBUG, 'test 1b');
} catch(e) {
  assert(false);
}


try {
  :Log(DEBUG, 'test 2');
  throw 2;
  assert(false);
} catch(e) {
  assert(e == '2');
}

try {
  :Log(DEBUG, 'test 3a');
  try {
    throw 3;
  } catch(e) {
    assert(e == '3');
  }
  :Log(DEBUG, 'test 3b');
} catch(e) {
  assert(false);
}

try {
  :Log(DEBUG, 'test 4a');
  throw 4;
  assert(false);
} catch(e) {
  :Log(DEBUG, 'test 4b');
  assert(e == '4');
  try {
    :Log(DEBUG, 'test 4c');
    throw 4.0;
    assert(false);
  } catch(e) {
    :Log(DEBUG, 'test 4d');
    assert(e == '4.0');
  }
  assert(true);
  :Log(DEBUG, 'test 4e');
}

print('TRY/CATCH TESTS PASSED');
