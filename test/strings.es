print('RUNNING STRINGS TESTS');

assert(ES:StartsWith('', ''));
assert(ES:StartsWith('hello', ''));
assert(ES:StartsWith('hello', 'h'));
assert(ES:StartsWith('hello', 'hell'));
assert(ES:StartsWith('hello', 'hello'));
assert(!ES:StartsWith('hello', 'x'));
assert(!ES:StartsWith('hello', 'hellox'));
assert(!ES:StartsWith('', 'x'));

assert(ES:EndsWith('', ''));
assert(ES:EndsWith('hello', ''));
assert(ES:EndsWith('hello', 'o'));
assert(ES:EndsWith('hello', 'ello'));
assert(ES:EndsWith('hello', 'hello'));
assert(!ES:EndsWith('hello', 'x'));
assert(!ES:EndsWith('hello', 'hellox'));
assert(!ES:EndsWith('', 'x'));

print('STRINGS TESTS PASSED');
