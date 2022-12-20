include "constants.ec"

const RED = #ff0000;
const GREEN = #00ff00;
const BLUE = #0000ff;

-- I believe properties will be a primary.  As such, properties can in turn contain other properties.
props = {
  color: RED;
};

println("loading series...");

TBOND2 = load("TBOND2");
decorate(TBOND2, props);
TBOND10 = load("TBOND10");
T10T2 = TBOND10 - TBOND2;
INVERTED = T10T2 < 0; -- a boolean
decorate(INVERTED, props);

-- define a property called chart1
chart1 = {
  label: "My Chart";
  series: TBOND2, TBOND10, INVERTED;
  grids: 0, 5, 10, 15, 20; 
}

tab1 = {
  charts: chart1;
  padding-left: 5;
  padding-right: 5;
  padding-top: 5;
  padding-bottom: 5;
  background-color: GREY;
};

plot(tab1);


