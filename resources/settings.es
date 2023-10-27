settings.chart.legendsize = 14;
settings.chart.separator = 32;
settings.chart.hpadding = 6;
settings.chart.vpadding = 2;
settings.chart.hatches = 10;

settings.tooltips.initialdelay = 200;
settings.tooltips.dismissdelay = 3600 * 1000;
settings.tooltips.maxline = 90;

sources.fred.baseurl = "https://api.stlouisfed.org/fred";

# used to mark that settings have completed loading
settings.loaded = true;

:Print("Running '" + :Version() + "'");
:Print();
:Print("For color definitions refer to: https://htmlcolorcodes.com/color-names");
:Print();
:Print("Type 'help();' to list all commands");
:Print("Type 'help(\"command\");' to display help for a specific command");
:Print();
