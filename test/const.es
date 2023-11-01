:Print('RUNNING CONST/REF TESTS');

function fn1() {
  pfn1 = fn1;
  :Print(pfn1);
  fn1 = null;
}
fn1();
:Print(fn1);

function fn2() {
  function fn2() {}
  pfn2 = fn2;
  :Print(pfn2);
  try {
    fn2 = null;
  } catch (ex) {
    :Print('exception thrown, as expected: ' + ex);
  }
}
fn2();
:Print(fn2);

:Print('CONST/REF TESTS PASSED');
