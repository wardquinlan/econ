package econ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class UIUtils {
  final Color PANEL_BACKGROUND;
  final Color CHART_BACKGROUND;
  final Color CHART_RECT;
  final Color CHART_LINE;
  final Color PANEL_FONT_COLOR;
  final String PANEL_FONT_NAME;
  final Integer PANEL_FONT_SIZE;
  final int CHART_SEPARATOR;
  final int CHART_HPADDING;
  final int CHART_VPADDING;
  final int DXINCR;
  final int CHART_GRIDLINES;
  
  public UIUtils(Graphics g, Context ctx) {
    PANEL_BACKGROUND = new Color((int) ctx.get("settings.panel.background.color"));
    CHART_BACKGROUND = new Color((int) ctx.get("settings.chart.background.color"));
    CHART_RECT = new Color((int) ctx.get("settings.chart.rect.color"));
    CHART_LINE = new Color((int) ctx.get("settings.chart.line.color"));
    PANEL_FONT_COLOR = new Color((int) ctx.get("settings.panel.font.color"));
    PANEL_FONT_NAME = (String) ctx.get("settings.panel.font.name");
    PANEL_FONT_SIZE = (Integer) ctx.get("settings.panel.font.size");
    CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    CHART_VPADDING = (int) ctx.get("settings.chart.vpadding");
    DXINCR = (int) ctx.get("settings.panel.dxincr");
    CHART_GRIDLINES = (int) ctx.get("settings.chart.gridlines");
        
    if (PANEL_FONT_NAME != null && PANEL_FONT_SIZE != null) {
      g.setFont(new Font(PANEL_FONT_NAME, Font.PLAIN, PANEL_FONT_SIZE));
    } else if (PANEL_FONT_NAME != null) {
      g.setFont(new Font(PANEL_FONT_NAME, Font.PLAIN, g.getFont().getSize()));
    } else if (PANEL_FONT_SIZE != null) {
      g.setFont(new Font(g.getFont().getName(), Font.PLAIN, PANEL_FONT_SIZE));
    }
  }
}
