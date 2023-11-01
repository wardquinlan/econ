:SetLogLevel(0);

function fn() {
  function fn() {}
  
  p = fn;
  #p = null;
  fn = null;
}
fn();

