const RED = #ff0000;
const GREEN = #00ff00;
const BLUE = #0000ff;

-- this is a comment

println("Hello world!");

TBOND2 = loadSeriesByName("TBOND2");
println("TBOND2: " + TBOND2);
CC = TBOND2;
println("CC: " + CC);

-- TBOND10 = loadSeriesByName("TBOND10");
-- DIFFERENTIAL = TBOND10 - TBOND2;
-- INVERTED = DIFFERENTIAL < 0; -- a boolean

-- setSeriesNote(DIFFERENTIAL, "This is a note");
-- println(getSeriesNote(TBOND2));

println(RED);
println(GREEN);
println(BLUE);
