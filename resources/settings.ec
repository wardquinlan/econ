settings.panel.background.color = LightGray;
settings.panel.dxincr=5;
settings.panel.gridlinetextwidth = 40;
settings.panel.font.name = "Courier";
settings.panel.font.color = DimGray;

settings.chart.background.color = Ivory;
settings.chart.line.color = DimGray;
settings.chart.rect.color = Red;
settings.chart.gridlines=12;
settings.chart.legend.size=14;
settings.chart.separator = 32;
settings.chart.hpadding = 6;
settings.chart.vpadding = 2;

settings.tooltips.initialdelay = 200;
settings.tooltips.dismissdelay = 3600 * 1000;
settings.tooltips.maxline = 150;

settings.version = "Development Release 0.1";

-- used to mark that settings have completed loading
settings.loaded = 1;

print("Running Econ '" + settings.version + "'");
print();
print("Type 'help();' to list all commands");
print("Type 'help(\"command\");' to display help for a specific command");
print();
