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
    
    setBackground(PANEL_BACKGROUND);

    Stroke strokeOrig = ((Graphics2D) g).getStroke();
    int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING;
    int yBase = CHART_SEPARATOR;
    for (int i = 0; i < panel.getCharts().size(); i++) {
      Chart chart = panel.getCharts().get(i);
      int chartHeight = (getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;
      UITools ut = new UITools(chart, this, timeSeriesCollapsed, g, ctx, yBase);
      ut.drawChartBackground(chart, i == 0);
      
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
          int y1 = Utils.transform(gridLine, yBase + chartHeight - CHART_SEPARATOR - 1, yBase, valueMin, valueMax);
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
            int v1 = Utils.transform(timeSeries.get(idx - 1).getValue(), yBase + chartHeight - CHART_SEPARATOR - 1, yBase, valueMin, valueMax);
            int v2 = Utils.transform(timeSeries.get(idx).getValue(), yBase + chartHeight - CHART_SEPARATOR - 1, yBase, valueMin, valueMax);
            g.drawLine(x - DXINCR, v1, x, v2);
          }
        }
      }
      
      yBase += chartHeight;
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
