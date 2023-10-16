connect('localhost', 'es', 'es');
function f(series) {
  print(series);
}

S = load(500);
ds(f);
print('--------------------------------');
cat(f);
print(S);

S = load(500);
f(S);
