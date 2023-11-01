
function fn() {
  :Print('hello');
  #function fn() {}
  p = fn;
  fn = null;
  :Print(fn);
}

fn();

