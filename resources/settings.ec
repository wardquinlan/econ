settings.chart.legendsize = 14;
settings.chart.separator = 32;
settings.chart.hpadding = 6;
settings.chart.vpadding = 2;
settings.chart.hatches = 10;

settings.tooltips.initialdelay = 200;
settings.tooltips.dismissdelay = 3600 * 1000;
settings.tooltips.maxline = 90;

settings.merge.insert = true;
settings.merge.update = false;
settings.merge.delete = false;

settings.version = "Development Release 0.1";

sources.fred.baseurl = "https://api.stlouisfed.org/fred";

# used to mark that settings have completed loading
settings.loaded = 1;

print("Running Econ '" + settings.version + "'");
print();
print("For color definitions refer to: https://htmlcolorcodes.com/color-names");
print();
print("Type 'help();' to list all commands");
print("Type 'help(\"command\");' to display help for a specific command");
print();
