SP500 = qdb(DB + "/" + "SPX:US.txt");
T10Y3M = qtp(TP, "T10Y3M");
INV = T10Y3M < 0;
plot("testQuote.xml");

