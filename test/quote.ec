SP500 = qdb(db + "/" + "SPX:US.txt");
T10Y3M = qtp(tp, "T10Y3M");
TBOND1 = qtp(tp, "TBOND1");
TBOND2 = qtp(tp, "TBOND2");
WILL5000 = qtp(tp, "WILL5000");
GDP = qtp(tp, "GDP");
NFCI = qtp(tp, "NFCI");

INV = T10Y3M < 0;
setTitle(INV, "3-Month Yield Curve Inversion");
setNotes(INV, "Indicates when the 10-Year / 3-Month yield curve inverts");

FC = (100 + TBOND2)^2 / (100 + TBOND1) - 100;
setTitle(FC, "1-year Forecasted Rate");
setNotes(FC, "1-year Forecasted Rate defined as: (1 + 2-Year Rate)^2 / (1  + 1-Year Rate) - 1");

VIX = qtp(tp, "VIX");
setTitle(VIX, "CBOE Volatility Index");
VOL = VIX > 36;

const E = 2.718280;
const K = 0.5;
const NR = 3.25;

F = (E - E^K) * TBOND2 / NR + E^K;
LOGF = log(F);
MKCAPGDP = 100 * LOGF * WILL5000 / GDP;

setTitle(NFCI, "Chicago Fed National Financial Conditions Index");
setNotes(NFCI, "The Chicago Fed's National Financial Conditions Index (NFCI) provides a comprehensive weekly update on U.S. financial conditions in money markets, debt and equity markets and the traditional and \"shadow\" banking systems. Positive values of the NFCI indicate financial conditions that are tighter than average, while negative values indicate financial conditions that are looser than average.  For further information, please visit the Federal Reserve Bank of Chicago (http://www.chicagofed.org/webpages/publications/nfci/index.cfm).");

