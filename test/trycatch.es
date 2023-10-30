print('RUNNING TRY/CATCH TESTS');

try {
  :Log(DEBUG, 'Test 1');
  :Log(DEBUG, 'Test 1 complete');
} catch(e) {
  assert(false);
}


try {
  :Log(DEBUG, 'Test 2');
  throw 2;
  assert(false);
} catch(e) {
  :Log(DEBUG, 'caught exception: ' + e);
  assert(e == 'invalid: can only throw String exceptions');
  :Log(DEBUG, 'Test 2 complete');
}

try {
  :Log(DEBUG, 'Test 3');
  throw 'exc';
  assert(false);
} catch(e) {
  assert(e == 'exc');
  :Log(DEBUG, 'Test 3 complete');
}

try {
  :Log(DEBUG, 'Test 4');
  try {
    throw '4';
    assert(false);
  } catch(e) {
    assert(e == '4');
    :Log(DEBUG, 'Test 4 continued');
  }
  :Log(DEBUG, 'Test 4 complete');
} catch(e) {
  assert(false);
}

try {
  :Log(DEBUG, 'Test 5');
  throw '5';
  assert(false);
} catch(e) {
  :Log(DEBUG, 'Test 5 continued');
  assert(e == '5');
  try {
    :Log(DEBUG, 'Test 5 continued');
    throw '5.0';
    assert(false);
  } catch(e) {
    :Log(DEBUG, 'Test 5 continued');
    assert(e == '5.0');
  }
  :Log(DEBUG, 'Test 5 complete');
}

print('TRY/CATCH TESTS PASSED');
