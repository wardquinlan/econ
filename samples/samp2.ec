const RED = #ff0000;
const GREEN = #00ff00;
const BLUE = #0000ff;

println("loading series...");

TBOND2 = loadSeriesByName("TBOND2");
TBOND10 = loadSeriesByName("TBOND10");
DIFFERENTIAL = TBOND10 - TBOND2;
INVERTED = DIFFERENTIAL < 0; -- a boolean

setSeriesNote(DIFFERENTIAL, "This is a note");
println(getSeriesNote(TBOND2));

