if (!isAdmin()) {
  throw 'you must be running as admin to run this test';
}

const UFID = 12000;

if (exists(UFID)) {
  throw 'id already exists: ' + UFID;
}

UF = create('UF');
setId(UF, UFID);
setTitle(UF, 'UF');
setName(UF, 'UF');
setUnits(UF, 'units');
setFrequency(UF, 'frequency');
setUnitsShort(UF, 'unitsShort');
setFrequencyShort(UF, 'frequencyShort');

meta(UF);
print();
print('units: ' + getUnits(UF));
print('unitsShort: ' + getUnitsShort(UF));
print('frequency: ' + getFrequency(UF));
print('frequencyShort: ' + getFrequencyShort(UF));

assert(getUnits(UF) == 'units');
assert(getUnitsShort(UF) == 'unitsShort');
assert(getFrequency(UF) == 'frequency');
assert(getFrequencyShort(UF) == 'frequencyShort');

save(UF);

print();
print('UNITS / FREQUENCY TESTS COMPLETE');

