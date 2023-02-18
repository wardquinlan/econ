print("loading quote data at " + timestamp() + "...");

SP500 = qdb(DB  + "/" + "SPX:US.txt");
T10Y3M = qtp(TP, "*", "T10Y3M");
TBOND1 = qtp(TP, "*", "TBOND1");
TBOND2 = qtp(TP, "*", "TBOND2");
WILL5000 = qtp(TP, "*", "WILL5000");
GDP = qtp(TP, "*", "GDP");
NFCI = qtp(TP, "*", "NFCI");
QSALES = qtp(TPS, "^GSPC.TXT", "QSALES");
USD = qtp(TP, "*", "USD");
RRP = qtp(TP, "*", "RRP");

AVG = average(SP500, 200);
setName(AVG, "AVG");
setTitle(AVG, "200-Day Moving Average");

DT = date(SP500);
R1973 = DT >= '1973-11-01' and DT <= '1975-03-31';
R1980 = DT >= '1980-01-01' and DT <= '1980-07-31';
R1981 = DT >= '1981-07-01' and DT <= '1982-11-30';
R1990 = DT >= '1990-07-01' and DT <= '1991-03-31';
R2001 = DT >= '2001-03-01' and DT <= '2001-11-30';
R2007 = DT >= '2007-12-01' and DT <= '2009-06-30';
R2020 = DT >= '2020-02-01' and DT <= '2020-04-30';
RECESSION = R1973 or R1980 or R1981 or R1990 or R2001 or R2007 or R2020;
setName(RECESSION, "RECESSION");

INV = T10Y3M < 0;
setName(INV, "INVERSION");
setTitle(INV, "3-Month Yield Curve Inversion");
setNotes(INV, "Indicates when the 10-Year / 3-Month yield curve inverts");

FC = (100 + TBOND2)^2 / (100 + TBOND1) - 100;
setTitle(FC, "1-year Forecasted Rate");
setNotes(FC, "1-year Forecasted Rate defined as: (1 + 2-Year Rate)^2 / (1  + 1-Year Rate) - 1");

VIX = qtp(TP, "*", "VIX");
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

PS = LOGF * SP500 / QSALES / 4;

setTitle(USD, "Nominal Broad U.S. Dollar Index");

setTitle(RRP, "Overnight Reverse Repurchase Agreements: Treasury Securities Sold by the Federal Reserve in the Temporary Open Market Operations");
setNotes(RRP, "This series is constructed as the aggregated daily amount value of the RRP transactions reported by the New York Fed as part of the Temporary Open Market Operations.  Temporary open market operations involve short-term repurchase and reverse repurchase agreements that are designed to temporarily add or drain reserves available to the banking system and influence day-to-day trading in the federal funds market.  A reverse repurchase agreement (known as reverse repo or RRP) is a transaction in which the New York Fed under the authorization and direction of the Federal Open Market Committee sells a security to an eligible counterparty with an agreement to repurchase that same security at a specified price at a specific time in the future. For these transactions, eligible securities are U.S. Treasury instruments, federal agency debt and the mortgage-backed securities issued or fully guaranteed by federal agencies. For more information, see https://www.newyorkfed.org/markets/rrp_faq.html");

print("quote data loading complete at " + timestamp());

