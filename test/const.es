print('RUNNING CONST/REF TESTS');

const C:A = 5;
try {
  C:A = 3;
  :Log(ERROR, 'able to overwrite a constant');
  :Exit(1);
} catch (ex) {
  :Log(INFO, 'exception thrown as expected: ' + ex); 
}

function C:FN1() {
  C:PFN = C:FN1;
  C:FN1 = null;
}
C:FN1();
C:FN1 = null;

const function C:FN2() {
  C:PFN = C:FN2;
  C:FN2 = null;
}
C:FN2();
try {
  C:FN2 = null;
} catch (ex) {
  :Log(INFO, 'exception thrown as expected: ' + ex);
}

function C:FN3() {
  function inner() {
  }
  pfn = inner;
  try {
    inner = null;
  } catch (ex) {
    :Log(INFO, 'exception thrown as expected: ' + ex);
    pfn = null;
    inner = null;
    :Log(INFO, 'now inner has been set to null');
  }
}
C:FN3();

#function fn() {
#  :Print('hello');
#  #function fn() {}
#  p = fn;
#  fn = null;
#  :Print(fn);
#}
#fn();

print('CONST/REF TESTS PASSED');
