function h() {
  ES:Assert(!:Defined('r'));
  :Print(r);
}

function f() {
  r = 1;
  function g() {
    :Print(r);
    h();
  }
  g();
}

f();
