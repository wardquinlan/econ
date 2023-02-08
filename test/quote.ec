SP500 = qdb(db + "/" + "SPX:US.txt");
T10Y3M = qtp(tp, "T10Y3M");
TBOND1 = qtp(tp, "TBOND1");
TBOND2 = qtp(tp, "TBOND2");

INV = T10Y3M < 0;
setTitle(INV, "3-Month Yield Curve Inversion");
setNotes(INV, "Indicates when the 10-Year / 3-Month yield curve inverts");

FC = (100 + TBOND2)^2 / (100 + TBOND1) - 100;
setTitle(FC, "1-year Forecasted Rate");
setNotes(FC, "1-year Forecasted Rate defined as: (1 + 2-Year Rate)^2 / (1  + 1-Year Rate) - 1");

plot("quote.xml");

