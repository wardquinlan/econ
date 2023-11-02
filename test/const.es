:Print('RUNNING CONST/REF TESTS');

:Print('----------------');
function fn1() {
  pfn1 = fn1;
  :Print('pfn1=' + pfn1);
  fn1 = null;
}
fn1();
:Print('fn1=' + fn1);

:Print('----------------');

function fn2() {
  function fn2() {}
  pfn2 = fn2;
  :Print('fn2=' + fn2);
  :Print('pfn2=' + pfn2);
  try {
    fn2 = null;
  } catch (ex) {
    :Print('exception thrown, as expected: ' + ex);
  }
}
fn2();
:Print('fn2=' + fn2);

:Print('----------------');

function fn3() {}
pfn3 = fn3;
:Print('pfn3=' + pfn3);
function fn4() {
  :GPut('fn3', 10);
}
try {
  fn4();
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}
:Print('pfn3=' + pfn3);
:Print('fn3=' + fn3);

const function fn3b() {}
function fn4b() {
  try {
    :GPut('fn3b', 'abc');
    assert(false);
  } catch (ex) {
    :Print('exception thrown, as expected: ' + ex);
  }
}
fn4b();

:Print('----------------');

const function fn5() {}
try {
  function fn5() {}
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

function fn5b() {
  try {
    function fn5() {}
    :Print('exception not thrown, as expected');
  } catch (ex) {
    assert(false);
  }
}
fn5b();

:Print('----------------');

function fn6() {}
try {
  const function fn6() {}
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

:Print('----------------');

const f7 = 7;
try {
  f7 = 7;
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

:Print('----------------');

f8 = 8;
try {
  const f8 = 8;
} catch (ex) {
  :Print('exception thrown, as expected: ' + ex);
}

:Print('----------------');
:Print('CONST/REF TESTS PASSED');
