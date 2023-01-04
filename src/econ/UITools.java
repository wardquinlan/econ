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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UITools {
  final private static Log log = LogFactory.getFactory().getInstance(UITools.class);
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
  
  public UITools(Chart chart, JComponent component, TimeSeries timeSeriesCollapsed, Graphics g, Context ctx, int yBase, int gridLineWidth) {
    this.component = component;
    this.timeSeriesCollapsed = timeSeriesCollapsed;
    this.strokeGridlines = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1}, 0);
    this.g = g;
    this.yBase = yBase;
    this.cal = new GregorianCalendar();
    
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
    
    chartWidth = component.getWidth() - 1 - 2 * CHART_HPADDING - gridLineWidth;
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
    
    // Draw the vertical grid lines
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
          g.drawString(Utils.getMonthString(cal), x, component.getHeight() - CHART_SEPARATOR + g.getFontMetrics(g.getFont()).getHeight());
        }
      }
    }
  }
  
  public int getMaximumStringWidth(float[] gridLines, int max) {
    for (float gridLine: gridLines) {
      if (g.getFontMetrics().stringWidth(Float.toString(gridLine)) > max) {
        max = g.getFontMetrics().stringWidth(Float.toString(gridLine));
      }
    }
    return max;
  }
  public float[] calculateGridlines(MinMaxPair pair) throws Exception {
    float gridLines[] = new float[CHART_GRIDLINES];
    float dyGridLines = Utils.findDYGridLines(CHART_GRIDLINES, pair);
    log.debug("dyGridLines=" + dyGridLines);
    float yGridLine = (float) Math.ceil(pair.getMinValue() / dyGridLines) * dyGridLines;
    int count = 0; 
    while (count < CHART_GRIDLINES) {
      log.debug("yGridLine=" + yGridLine);
      gridLines[count] = yGridLine;
      yGridLine += dyGridLines;
      count++;
    }
    if (gridLines[CHART_GRIDLINES - 1] > pair.getMaxValue()) {
      pair.setMaxValue(gridLines[CHART_GRIDLINES - 1]);
    }
    return gridLines;
  }
  
  public void drawHorizontalGridlines(float gridLines[], MinMaxPair pair) {
    g.setColor(CHART_LINE);
    for (float gridLine: gridLines) {
      if (gridLine != pair.getMaxValue()) {
        int x1 = CHART_HPADDING + 1;
        int y1 = Utils.transform(gridLine, yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
        int x2 = x1 + chartWidth - 1;
        int y2 = y1;
        g.drawLine(x1, y1, x2, y2);
      }
    }
  }
  
  public void drawSeries(Chart chart, MinMaxPair pair) {
    for (Series series: chart.getSeries()) {
      TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
      g.setColor(series.getColor());
      for (int idx = 1, x = CHART_HPADDING + DXINCR; idx < timeSeriesCollapsed.size() && x < chartWidth; idx++, x += DXINCR) {
        if (timeSeries.get(idx - 1).getValue() != null) {
          int v1 = Utils.transform(timeSeries.get(idx - 1).getValue(), yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
          int v2 = Utils.transform(timeSeries.get(idx).getValue(), yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
          g.drawLine(x - DXINCR, v1, x, v2);
        }
      }
    }
  }
}