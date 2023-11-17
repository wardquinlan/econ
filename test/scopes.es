ES:Print('RUNNING SCOPES TESTS');

function outer() {
  function inner1() {
    # g is defined is global scope, should be visible
    ES:Assert(ES:Defined('g'));
    
    # r is defined in parent scope, should be visible
    ES:Assert(ES:Defined('r'));
    
    x = 10;
    inner2();
  }
  
  function inner2() {
    ES:Assert(ES:Defined('g'));
    ES:Assert(ES:Defined('r'));
    ES:Assert(!ES:Defined('x'));
  }
  
  ES:Assert(ES:Defined('g'));
  r = 5;
  inner1();
}

g = 1;
outer();

ES:Print('SCOPES TESTS PASSED');
