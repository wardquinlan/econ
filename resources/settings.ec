settings.panel.background.color = LightGray;
settings.panel.dxincr=5;
settings.chart.background.color = Ivory;
settings.chart.line.color = LightGray;
settings.chart.rect.color = Red;
settings.chart.gridlines=12;
settings.chart.legend.size=15;
settings.panel.font.name = "Courier";
--settings.panel.font.name = "Source Code Pro Black";
--settings.panel.font.size = 12;
settings.panel.font.color = DimGray;
settings.chart.separator = 32;
settings.chart.hpadding = 6;
settings.chart.vpadding = 3;
settings.tooltips.initialdelay = 200;
settings.tooltips.dismissdelay = 3600 * 1000;

-- used to mark that settings have completed loading
settings.loaded = 1;

print("settings loaded");
