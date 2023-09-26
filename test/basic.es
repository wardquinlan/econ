function test(flag) {
  if (!flag) {
    print('validation failed');
    return false;
  }
  print('validation passed');
  return true;
}

result = test(false);
print('result=' + result);
