:SetLogLevel(0);

function fn() {
  function fn() {}
  
  :GPut('p', fn);
  fn = null;
}
fn();

