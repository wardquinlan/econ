try {
  if (true) {
    throw 'crap';
  }
} catch(ex) {
  print('an exception occurred: ' + ex);
  return;
  assert(false);
}

print('done');
