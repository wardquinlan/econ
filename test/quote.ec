print("loading quote data at " + timestamp() + "...");

SP500 = qdb(db + "/" + "SPX:US.txt");
T10Y3M = qtp(tp, "T10Y3M");
TBOND1 = qtp(tp, "TBOND1");
TBOND2 = qtp(tp, "TBOND2");
WILL5000 = qtp(tp, "WILL5000");
GDP = qtp(tp, "GDP");
NFCI = qtp(tp, "NFCI");
QSALES = qtp(tps, "QSALES");
USD = qtp(tp, "USD");
RRP = qtp(tp, "RRP");

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

PS = LOGF * SP500 / QSALES / 4;

setTitle(USD, "Nominal Broad U.S. Dollar Index");

setTitle(RRP, "Overnight Reverse Repurchase Agreements: Treasury Securities Sold by the Federal Reserve in the Temporary Open Market Operations");
setNotes(RRP, "This series is constructed as the aggregated daily amount value of the RRP transactions reported by the New York Fed as part of the Temporary Open Market Operations.  Temporary open market operations involve short-term repurchase and reverse repurchase agreements that are designed to temporarily add or drain reserves available to the banking system and influence day-to-day trading in the federal funds market.  A reverse repurchase agreement (known as reverse repo or RRP) is a transaction in which the New York Fed under the authorization and direction of the Federal Open Market Committee sells a security to an eligible counterparty with an agreement to repurchase that same security at a specified price at a specific time in the future. For these transactions, eligible securities are U.S. Treasury instruments, federal agency debt and the mortgage-backed securities issued or fully guaranteed by federal agencies. For more information, see https://www.newyorkfed.org/markets/rrp_faq.html");

print("quote data loading complete at " + timestamp());
