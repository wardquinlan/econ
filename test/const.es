print('RUNNING CONST TESTS');

const c = 1;

function f() {
  try {
    c = 2;
    print('SHOULD NOT ARRIVE HERE!!!');
  } catch(ex) {
    print('Exception thrown, as expected');
  }
}

function g() {
  const c2 = 3;
}
g();
const c2 = 5;
print(c2);

f();

print('CONST TESTS PASSED');
