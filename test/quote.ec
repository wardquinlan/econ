SP500 = qdb(db + "/" + "SPX:US.txt");
T10Y3M = qtp(tp, "T10Y3M");
INV = T10Y3M < 0;

setTitle(INV, "3-Month Yield Curve Inversion");
setNotes(INV, "Indicates when the 10-Year / 3-Month yield curve inverts");
plot("quote.xml");

