include "constants.ec"

const RED = #ff0000;
const GREEN = #00ff00;
const BLUE = #0000ff;

-- I believe properties will be a primary.  As such, properties can in turn contain other properties.
-- Arrays will also be primaries
props = {
  color: RED;
};

println("loading series...");

TBOND2 = load("TBOND2");
TBOND2Props = { 
  color: RED;
}
TBOND10 = load("TBOND10");
TBOND10Props = {
  color: BLUE;
};
INVERTED = T10T2 < 0; -- a boolean
INVERTEDProps = {
  color: GREEN;
};

-- define a property called chart1
chart1 = {
  title: "My Chart";
  grids: [-2, -1, 0, 1, 2];
  serieslist: [
    {
      series:  TBOND2, 
      props: TBOND2Props;
    },
    {
      series: TBOND10,
      props: TBOND10Props;
    },
    {
      series: INVERTED,
      props: INVERTEDProps
    }
  ];
}

panel = {
  charts: chart1;
  padding-left: 5;
  padding-right: 5;
  padding-top: 5;
  padding-bottom: 5;
  background-color: GREY;
};

plot(tab1);


