const TEMPLATE_PATH = "/home/ward/c/quote/template/ng.txt";
const DB_PATH = "/home/ward/c/quote/db";

SPX = import("QDB", DB_PATH + "/SPX:US.txt");
TBILL3 = import("QTEMPLATE", TEMPLATE_PATH, "TBILL3");
TBOND2 = import("QTEMPLATE", TEMPLATE_PATH, "TBOND2");
TBOND10 = import("QTEMPLATE", TEMPLATE_PATH, "TBOND10");

plot("main.xml");
