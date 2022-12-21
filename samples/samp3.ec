const RED = #ff0000;
const GREEN = #00ff00;
const BLUE = #0000ff;

-- this is a comment

println("Hello world!");

CC = 3 + BLUE;
println("Value of CC = " + CC);

TBOND10 = loadSeriesByName("TBOND10");
println("TBOND10: " + TBOND10);
CC = TBOND10;
println("CC: " + CC);

-- TBOND10 = loadSeriesByName("TBOND10");
-- DIFFERENTIAL = TBOND10 - TBOND2;
-- INVERTED = DIFFERENTIAL < 0; -- a boolean

-- setSeriesNote(DIFFERENTIAL, "This is a note");
-- println(getSeriesNote(TBOND2));

println(RED);
println(GREEN);
println(BLUE);
