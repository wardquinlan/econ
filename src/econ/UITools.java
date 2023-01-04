package econ;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JComponent;

public class UITools {
  final private Color PANEL_BACKGROUND;
  final private Color CHART_BACKGROUND;
  final private Color CHART_RECT;
  final private Color CHART_LINE;
  final private Color PANEL_FONT_COLOR;
  final private String PANEL_FONT_NAME;
  final private Integer PANEL_FONT_SIZE;
  final private int CHART_SEPARATOR;
  final private int CHART_HPADDING;
  final private int CHART_VPADDING;
  final private int DXINCR;
  final private int CHART_GRIDLINES;
  final private int chartWidth;
  final private int chartHeight;
  final private JComponent component;
  final private TimeSeries timeSeriesCollapsed;
  final private Stroke strokeGridlines;
  final private Graphics g;
  final private Calendar cal;
  final private int yBase;
  final private FontMetrics m;
  
  public UITools(Chart chart, JComponent component, TimeSeries timeSeriesCollapsed, Graphics g, Context ctx, int yBase) {
    this.component = component;
    this.timeSeriesCollapsed = timeSeriesCollapsed;
    this.strokeGridlines = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1}, 0);
    this.g = g;
    this.yBase = yBase;
    this.cal = new GregorianCalendar();
    this.m = g.getFontMetrics(g.getFont());
    
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
    
    chartWidth = component.getWidth() - 1 - 2 * CHART_HPADDING;
    chartHeight = (component.getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;
    
    if (PANEL_FONT_NAME != null && PANEL_FONT_SIZE != null) {
      g.setFont(new Font(PANEL_FONT_NAME, Font.PLAIN, PANEL_FONT_SIZE));
    } else if (PANEL_FONT_NAME != null) {
      g.setFont(new Font(PANEL_FONT_NAME, Font.PLAIN, g.getFont().getSize()));
    } else if (PANEL_FONT_SIZE != null) {
      g.setFont(new Font(g.getFont().getName(), Font.PLAIN, PANEL_FONT_SIZE));
    }
  }
  
  public void drawChartBackground(Chart chart, boolean withMonthLegend) {
    Stroke strokeOrig = ((Graphics2D) g).getStroke();
    ((Graphics2D) g).setStroke(strokeOrig);
    
    // Fill the rectangle with the background color
    g.setColor(CHART_BACKGROUND);
    g.fillRect(CHART_HPADDING, yBase, chartWidth, chartHeight - CHART_SEPARATOR - 1);
    
    // Draw the rectangle
    g.setColor(CHART_RECT);
    g.drawRect(CHART_HPADDING, yBase, chartWidth, chartHeight - CHART_SEPARATOR - 1);

    // Draw the label
    g.setColor(PANEL_FONT_COLOR);
    g.drawString(chart.getLabel(), CHART_HPADDING, yBase - CHART_VPADDING);
    
    // Draw the grid lines
    ((Graphics2D) g).setStroke(strokeGridlines);
    for (int idx = 1, x = CHART_HPADDING + DXINCR; idx < timeSeriesCollapsed.size() && x < chartWidth; idx++, x += DXINCR) {
      cal.setTime(timeSeriesCollapsed.get(idx - 1).getDate());
      int monthPrev = cal.get(Calendar.MONTH);

      cal.setTime(timeSeriesCollapsed.get(idx).getDate());
      int month = cal.get(Calendar.MONTH);
      
      if (month != monthPrev) {
        g.setColor(CHART_LINE);
        g.drawLine(x, yBase + 1, x, yBase + chartHeight - CHART_SEPARATOR - 1);

        if (withMonthLegend) {
          g.setColor(PANEL_FONT_COLOR);
          g.drawString(Utils.getMonthString(cal), x, component.getHeight() - CHART_SEPARATOR + m.getHeight());
        }
      }
    }
  }
}
