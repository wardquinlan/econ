include "colors.ec";

settings.panel.background.color = LIGHT_GRAY;
settings.panel.dxincr=4;
settings.chart.background.color = WHITE;
settings.chart.line.color = LIGHT_GRAY;
settings.chart.rect.color = RED;
--settings.panel.font.name = "Courier 10 Pitch";
settings.panel.font.name = "Monospaced.plain";
settings.panel.font.size = 11;
settings.chart.font.color = DARK_GRAY;
settings.chart.separator = 32;
settings.chart.hpadding = 6;
settings.chart.vpadding = 2;

-- used to mark that settings have completed loading
settings.loaded = 1;
