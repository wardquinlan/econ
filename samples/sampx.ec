T1 = load("T1");
T2 = load("T2");
T3 = load("T3");
TBOND2 = load(2);
TBOND10 = load(10);
TBILL3 = load(3);

plot("main.xml");
--createSeries(205, "T4", "T4", "FRED");
T1 = import("QTEMPLATE", "../../../c/quote/template/ng.txt", "TBOND10");
cat();
plot(T1);

