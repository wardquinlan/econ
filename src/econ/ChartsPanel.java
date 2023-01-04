package econ;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChartsPanel extends JPanel {
  private static final long serialVersionUID = 8263376302676172047L;
  private static Log log = LogFactory.getFactory().getInstance(ChartsPanel.class);
  private Context ctx;
  private Panel panel;
  private Calendar cal = new GregorianCalendar();
  
  public ChartsPanel(Context ctx, Panel panel) {
    super();
    this.ctx = ctx;
    this.panel = panel;
    JPanel gridPanel = new JPanel();
    gridPanel.setPreferredSize(new Dimension(200, 200));
    gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    setLayout(null);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    final Color PANEL_BACKGROUND = new Color((int) ctx.get("settings.panel.background.color"));
    final Color CHART_BACKGROUND = new Color((int) ctx.get("settings.chart.background.color"));
    final Color CHART_RECT = new Color((int) ctx.get("settings.chart.rect.color"));
    final Color CHART_LINE = new Color((int) ctx.get("settings.chart.line.color"));
    final Color PANEL_FONT_COLOR = new Color((int) ctx.get("settings.panel.font.color"));
    final int CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    final int CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    final int CHART_VPADDING = (int) ctx.get("settings.chart.vpadding");
    final int DXINCR = (int) ctx.get("settings.panel.dxincr");
    final int CHART_GRIDLINES = (int) ctx.get("settings.chart.gridlines");
    
    List<TimeSeries> list = Utils.consolidate(panel);
    TimeSeries timeSeriesCollapsed = Utils.collapse(list);
    log.info("collapsed series=" + timeSeriesCollapsed.toStringVerbose());
    
    UIUtils ut = new UIUtils(g, ctx);
    setBackground(PANEL_BACKGROUND);

    FontMetrics m = g.getFontMetrics(g.getFont());
    Stroke strokeOrig = ((Graphics2D) g).getStroke();
    Stroke strokeGridlines = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1}, 0);
    int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING;
    int y = CHART_SEPARATOR;
    for (int i = 0; i < panel.getCharts().size(); i++) {
      Chart chart = panel.getCharts().get(i);
      int chartHeight = (getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;

      ((Graphics2D) g).setStroke(strokeOrig);
      // Fill the rectangle with the background color
      g.setColor(CHART_BACKGROUND);
      g.fillRect(CHART_HPADDING, y, chartWidth, chartHeight - CHART_SEPARATOR - 1);
      
      // Draw the rectangle
      g.setColor(CHART_RECT);
      g.drawRect(CHART_HPADDING, y, chartWidth, chartHeight - CHART_SEPARATOR - 1);

      // Draw the label
      g.setColor(PANEL_FONT_COLOR);
      g.drawString(chart.getLabel(), CHART_HPADDING, y - CHART_VPADDING);
      
      // Draw the grid lines
      ((Graphics2D) g).setStroke(strokeGridlines);
      for (int idx = 1, x = CHART_HPADDING + DXINCR; idx < timeSeriesCollapsed.size() && x < chartWidth; idx++, x += DXINCR) {
        cal.setTime(timeSeriesCollapsed.get(idx - 1).getDate());
        int monthPrev = cal.get(Calendar.MONTH);

        cal.setTime(timeSeriesCollapsed.get(idx).getDate());
        int month = cal.get(Calendar.MONTH);
        
        if (month != monthPrev) {
          g.setColor(CHART_LINE);
          g.drawLine(x, y + 1, x, y + chartHeight - CHART_SEPARATOR - 1);

          if (i == 0) {
            g.setColor(PANEL_FONT_COLOR);
            g.drawString(Utils.getMonthString(cal), x, getHeight() - CHART_SEPARATOR + m.getHeight());
          }
        }
      }
      
      float valueMin = Float.MAX_VALUE;
      float valueMax = Float.MIN_VALUE;
      for (Series series: chart.getSeries()) {
        TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
        valueMin = minValue(valueMin, timeSeries, timeSeriesCollapsed, chartWidth);
        valueMax = maxValue(valueMax, timeSeries, timeSeriesCollapsed, chartWidth);
      }
      log.info("valueMin=" + valueMin + ", valueMax=" + valueMax);
      float gridLines[] = new float[CHART_GRIDLINES];
      try {
        float dyGridLines = Utils.findDYGridLines(CHART_GRIDLINES, valueMin, valueMax);
        log.info("dyGridLines=" + dyGridLines);
        float yGridLine = (float) Math.ceil(valueMin / dyGridLines) * dyGridLines;
        int count = 0; 
        while (count < CHART_GRIDLINES) {
          log.info("yGridLine=" + yGridLine);
          gridLines[count] = yGridLine;
          yGridLine += dyGridLines;
          count++;
        }
        if (gridLines[CHART_GRIDLINES - 1] > valueMax) {
          valueMax = gridLines[CHART_GRIDLINES - 1];
        }
      } catch(Exception ex) {
        log.error("dyGridLines overflowed, not displaying chart " + chart.getLabel());
        continue;
      }
      
      // Draw the horizontal gridlines
      g.setColor(CHART_LINE);
      for (float gridLine: gridLines) {
        if (gridLine != valueMax) {
          int x1 = CHART_HPADDING + 1;
          int y1 = Utils.transform(gridLine, y + chartHeight - CHART_SEPARATOR - 1, y, valueMin, valueMax);
          int x2 = x1 + chartWidth;
          int y2 = y1;
          g.drawLine(x1, y1, x2, y2);
        }
      }
      
      // Draw the series themselves
      ((Graphics2D) g).setStroke(strokeOrig);
      for (Series series: chart.getSeries()) {
        TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
        g.setColor(series.getColor());
        for (int idx = 1, x = CHART_HPADDING + DXINCR; idx < timeSeriesCollapsed.size() && x < chartWidth; idx++, x += DXINCR) {
          if (timeSeries.get(idx - 1).getValue() != null) {
            int v1 = Utils.transform(timeSeries.get(idx - 1).getValue(), y + chartHeight - CHART_SEPARATOR - 1, y, valueMin, valueMax);
            int v2 = Utils.transform(timeSeries.get(idx).getValue(), y + chartHeight - CHART_SEPARATOR - 1, y, valueMin, valueMax);
            g.drawLine(x - DXINCR, v1, x, v2);
          }
        }
      }
      
      y += chartHeight;
    }
  }
  
  private float minValue(float valueMin, TimeSeries timeSeries, TimeSeries timeSeriesCollapsed, int chartWidth) {
    final int CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    final int DXINCR = (int) ctx.get("settings.panel.dxincr");
    for (int idx = 0, x = CHART_HPADDING; idx < timeSeriesCollapsed.size() && x < chartWidth; idx++, x += DXINCR) {
      if (timeSeries.get(idx).getValue() != null && timeSeries.get(idx).getValue() < valueMin) {
        valueMin = timeSeries.get(idx).getValue();
      }
    }
    return valueMin;
  }
  
  private float maxValue(float valueMax, TimeSeries timeSeries, TimeSeries timeSeriesCollapsed, int chartWidth) {
    final int CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    final int DXINCR = (int) ctx.get("settings.panel.dxincr");
    for (int idx = 0, x = CHART_HPADDING; idx < timeSeriesCollapsed.size() && x < chartWidth; idx++, x += DXINCR) {
      if (timeSeries.get(idx).getValue() != null && timeSeries.get(idx).getValue() > valueMax) {
        valueMax = timeSeries.get(idx).getValue();
      }
    }
    return valueMax;
  }
}
