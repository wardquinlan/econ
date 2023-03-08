package es.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.core.Scaler;
import es.core.TimeSeries;
import es.core.Utils;

public class ChartRenderer {
  final private static Log log = LogFactory.getFactory().getInstance(ChartRenderer.class);
  final private static DecimalFormat df = new DecimalFormat("#.###");
  final private Panel panel;
  final private Chart chart;
  final private int CHART_SEPARATOR;
  final private int CHART_HPADDING;
  final private int CHART_VPADDING;
  final private int CHART_LEGEND_SIZE;
  final private int CHART_HATCHES;
  final private int chartWidth;
  final private int chartHeight;
  final private JComponent component;
  final private TimeSeries timeSeriesCollapsed;
  final private Stroke strokeGridlines;
  final private Stroke strokeMain;
  final private Graphics2D g;
  final private Calendar cal;
  final private int yBase;
  
  public ChartRenderer(Panel panel, Chart chart, JComponent component, TimeSeries timeSeriesCollapsed, Graphics2D g, Context ctx, int yBase) {
    this.panel = panel;
    this.chart = chart;
    this.component = component;
    this.timeSeriesCollapsed = timeSeriesCollapsed;
    this.strokeGridlines = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1f, 2f}, 0);
    this.strokeMain = new BasicStroke(1);
    this.g = g;
    this.yBase = yBase;
    this.cal = new GregorianCalendar();
    
    CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    CHART_VPADDING = (int) ctx.get("settings.chart.vpadding");
    CHART_LEGEND_SIZE = (int) ctx.get("settings.chart.legendsize");
    CHART_HATCHES = (int) ctx.get("settings.chart.hatches");
    
    chartWidth = component.getWidth() - 1 - 2 * CHART_HPADDING - panel.getGridLineTextWidth();
    chartHeight = (component.getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;
    
    // uncomment to reveal system font name and size
    // System.out.println("****" + g.getFont().getFontName());
    // System.out.println("****" + g.getFont().getSize());
    g.setFont(new Font(panel.getFontName(), Font.PLAIN, panel.getFontSize()));
  }
  
  public void drawUnavailableChart() {
    // Fill the rectangle with the background color
    g.setColor(chart.getBackgroundColor());
    g.fillRect(CHART_HPADDING, yBase, chartWidth, chartHeight - CHART_SEPARATOR - 1);
    
    // Draw the rectangle
    g.setStroke(strokeMain);
    g.setColor(chart.getRectColor());
    g.drawRect(CHART_HPADDING, yBase, chartWidth, chartHeight - CHART_SEPARATOR - 1);
    
    // Draw the label
    g.setColor(panel.getFontColor());
    g.drawString(chart.getLabel(), CHART_HPADDING, yBase - CHART_VPADDING);
    
    g.setColor(chart.getRectColor());
    for (int y = yBase + CHART_HATCHES; y < yBase + chartHeight - CHART_SEPARATOR - 1; y += CHART_HATCHES) {
      g.drawLine(CHART_HPADDING, y, CHART_HPADDING + chartWidth, y);
    }
    
    for (int x = CHART_HPADDING + CHART_HATCHES; x < CHART_HPADDING + chartWidth; x += CHART_HATCHES) {
      g.drawLine(x, yBase, x, yBase + chartHeight - CHART_SEPARATOR - 1);
    }
  }
  
  public void drawChartBackground(int idxBase) {
    // Fill the rectangle with the background color
    g.setColor(chart.getBackgroundColor());
    g.fillRect(CHART_HPADDING, yBase, chartWidth, chartHeight - CHART_SEPARATOR - 1);
    
    // Draw the rectangle
    g.setStroke(strokeMain);
    g.setColor(chart.getRectColor());
    g.drawRect(CHART_HPADDING, yBase, chartWidth, chartHeight - CHART_SEPARATOR - 1);
    
    // Draw the label
    g.setColor(panel.getFontColor());
    g.drawString(chart.getLabel(), CHART_HPADDING, yBase - CHART_VPADDING);
  }
  
  public void drawVerticalGridlines(boolean withMonthLegend, int idxBase) {
    g.setStroke(strokeGridlines);
    int idxMax = Math.min(idxBase + chartWidth / panel.getDxIncr(), timeSeriesCollapsed.size());
    for (int idx = idxBase + 1; idx < idxMax; idx++) {
      int x = CHART_HPADDING + (idx - idxBase) * panel.getDxIncr();
      cal.setTime(timeSeriesCollapsed.get(idx - 1).getDate());
      int monthPrev = cal.get(Calendar.MONTH);
      int yearPrev = cal.get(Calendar.YEAR);

      cal.setTime(timeSeriesCollapsed.get(idx).getDate());
      int month = cal.get(Calendar.MONTH);
      int year = cal.get(Calendar.YEAR);
      
      switch(panel.getFrequency()) {
      case Panel.FREQUENCY_DAYS:
        g.setColor(chart.getLineColor());
        g.drawLine(x, yBase + 1, x, yBase + chartHeight - CHART_SEPARATOR - 1);

        if (withMonthLegend) {
          g.setColor(panel.getFontColor());
          if (month != monthPrev) {
            g.drawString(Utils.getMonthString(cal, false), x, component.getHeight() - CHART_SEPARATOR + g.getFontMetrics(g.getFont()).getHeight());
          } else {
            g.drawString(Utils.getDayString(cal), x, component.getHeight() - CHART_SEPARATOR + g.getFontMetrics(g.getFont()).getHeight());
          }
        }
        break;

      case Panel.FREQUENCY_MONTHS:
        if (month != monthPrev) {
          g.setColor(chart.getLineColor());
          g.drawLine(x, yBase + 1, x, yBase + chartHeight - CHART_SEPARATOR - 1);

          if (withMonthLegend) {
            g.setColor(panel.getFontColor());
            g.drawString(Utils.getMonthString(cal, true), x, component.getHeight() - CHART_SEPARATOR + g.getFontMetrics(g.getFont()).getHeight());
          }
        }
        break;
        
      case Panel.FREQUENCY_YEARS:
        if (year != yearPrev) {
          g.setColor(chart.getLineColor());
          g.drawLine(x, yBase + 1, x, yBase + chartHeight - CHART_SEPARATOR - 1);

          if (withMonthLegend) {
            g.setColor(panel.getFontColor());
            g.drawString(Utils.getYearString(cal), x, component.getHeight() - CHART_SEPARATOR + g.getFontMetrics(g.getFont()).getHeight());
          }
        }
        break;
        
      case Panel.FREQUENCY_NONE:
        break;
        
      default:
        Utils.ASSERT(false, "invalid date frequency: " + panel.getFrequency());
      }
    }
  }
  
  public void drawLegend(Map<Point, Series> mapLegend) {
    for (int i = 0; i < chart.getSeries().size(); i++) {
      Series series = chart.getSeries().get(i);
      int x = 2 * CHART_HPADDING;
      int y = yBase + 2 * CHART_VPADDING + i * (CHART_LEGEND_SIZE + 2 * CHART_VPADDING);
      Point point = new Point(x, y);
      mapLegend.put(point, series);
      g.setColor(series.getColor());
      g.fillRect(x, y, CHART_LEGEND_SIZE, CHART_LEGEND_SIZE);
      g.setColor(chart.getLineColor());
      g.drawRect(x, y, CHART_LEGEND_SIZE, CHART_LEGEND_SIZE);
      if (series.getTimeSeries().getName() != null) {
        g.setColor(panel.getFontColor());
        g.drawString(series.getTimeSeries().getName(), x + CHART_LEGEND_SIZE + CHART_HPADDING, y + CHART_LEGEND_SIZE);
      }
    }
  }
  
  public float[] calculateGridlines(MinMaxPair pair) throws Exception {
    float gridLines[] = new float[chart.getNGridLines()];
    float dyGridLines = Utils.findDYGridLines(chart.getNGridLines(), pair);
    log.debug("dyGridLines=" + dyGridLines);
    float yGridLine = (float) Math.ceil(pair.getMinValue() / dyGridLines) * dyGridLines;
    int count = 0; 
    while (count < chart.getNGridLines()) {
      log.debug("yGridLine=" + yGridLine);
      gridLines[count] = yGridLine;
      yGridLine += dyGridLines;
      count++;
    }
    if (gridLines[chart.getNGridLines() - 1] > pair.getMaxValue()) {
      pair.setMaxValue(gridLines[chart.getNGridLines() - 1]);
    }
    return gridLines;
  }

  public MinMaxPair calculateMinMax(MinMaxPair pair, Context ctx, TimeSeries timeSeries, TimeSeries timeSeriesCollapsed, int idxBase) {
    Utils.ASSERT(timeSeries.getType() == TimeSeries.FLOAT, "series must be of type float");
    int idxMax = Math.min(idxBase + chartWidth / panel.getDxIncr(), timeSeriesCollapsed.size());
    for (int idx = idxBase; idx < idxMax; idx++) {
      if (timeSeries.get(idx).getValue() != null && (Float) timeSeries.get(idx).getValue() < pair.getMinValue()) {
        pair.setMinValue((Float) timeSeries.get(idx).getValue());
      }
      if (timeSeries.get(idx).getValue() != null && (Float) timeSeries.get(idx).getValue() > pair.getMaxValue()) {
        pair.setMaxValue((Float) timeSeries.get(idx).getValue());
      }
    }
    return pair;
  }
  
  public void drawHorizontalGridlines(Scaler scaler, float gridLines[], MinMaxPair pair) throws Exception {
    g.setStroke(strokeGridlines);
    for (float gridLine: gridLines) {
      int x1 = CHART_HPADDING + 1;
      int y1 = Utils.transform(scaler, gridLine, yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
      int x2 = x1 + chartWidth - 1;
      int y2 = y1;
      g.setColor(chart.getLineColor());
      if (gridLine < pair.getMaxValue()) {
        // don't draw top gridline because the RECT will take care of it
        g.drawLine(x1, y1, x2, y2);
      }
      g.setColor(panel.getFontColor());
      g.drawString(df.format(gridLine), x2 + CHART_HPADDING, y2);
    }
    // draw the unlabelled gridlines.  Note: don't try and draw a label (obviously)
    for (float uGridline: chart.getGridlines()) {
      if (gridLines[0] < uGridline && gridLines[gridLines.length - 1] > uGridline) {
        int x1 = CHART_HPADDING + 1;
        int y1 = Utils.transform(scaler, uGridline, yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
        int x2 = x1 + chartWidth - 1;
        int y2 = y1;
        g.setColor(chart.getLineColor());
        g.setStroke(strokeMain);
        g.drawLine(x1, y1, x2, y2);
      }
    }
  }
  
  public void drawFloatSeries(Scaler scaler, MinMaxPair pair, int idxBase) throws Exception {
    g.setStroke(strokeMain);
    for (Series series: chart.getSeries()) {
      TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
      g.setColor(series.getColor());
      if (timeSeries.getType() == TimeSeries.FLOAT) {
        int idxMax = Math.min(idxBase + chartWidth / panel.getDxIncr(), timeSeriesCollapsed.size());
        for (int idx = idxBase + 1; idx < idxMax; idx++) {
          int x = CHART_HPADDING + (idx - idxBase) * panel.getDxIncr();
          if (timeSeries.get(idx - 1).getValue() != null) {
            int v1 = Utils.transform(scaler, (Float) timeSeries.get(idx - 1).getValue(), yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
            int v2 = Utils.transform(scaler, (Float) timeSeries.get(idx).getValue(), yBase + chartHeight - CHART_SEPARATOR - 1, yBase, pair.getMinValue(), pair.getMaxValue());
            g.drawLine(x - panel.getDxIncr(), v1, x, v2);
          }
        }
      }
    }
  }
  
  public void drawBooleanSeries(int idxBase) {
    for (Series series: chart.getSeries()) {
      TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
      g.setColor(series.getColor());
      if (timeSeries.getType() == TimeSeries.BOOLEAN) {
        int idxMax = Math.min(idxBase + chartWidth / panel.getDxIncr(), timeSeriesCollapsed.size());
        for (int idx = idxBase + 1; idx < idxMax; idx++) {
          int x = CHART_HPADDING + (idx - idxBase) * panel.getDxIncr();
          if ((Boolean) timeSeries.get(idx - 1).getValue() != null && (Boolean) timeSeries.get(idx - 1).getValue()) {
            g.fillRect(x - panel.getDxIncr() + 1, yBase + 1, panel.getDxIncr(), chartHeight - CHART_SEPARATOR - 2);
          }
        }
      }
    }
  }
}
