package es.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.core.TimeSeries;
import es.core.Utils;

public class ChartsPanel extends JPanel {
  private static final long serialVersionUID = 8263376302676172047L;
  private static Log log = LogFactory.getFactory().getInstance(ChartsPanel.class);
  private final Context ctx;
  private final Panel panel;
  private final Map<Point, Series> mapLegend = new HashMap<>();
  private final int CHART_LEGEND_SIZE;
  private final int CHART_SEPARATOR;
  private final int CHART_HPADDING;
  private final int TOOLTIPS_MAXLINE;
  private final TimeSeries timeSeriesCollapsed;
  private int idxBase;
  private boolean firstInvokation = true;
  private List<Point> points = new ArrayList<>();
  private List<Line> lines = new ArrayList<>();
  private Point src;
  private Point dest;
  private Color colorDecorations;
  private Stroke strokeDecorations;
  private int widthOrig = 0;
  private int heightOrig = 0;
  
  public ChartsPanel(Context ctx, Panel panel, JTabbedPane tabbedPane) {
    super();
    this.ctx = ctx;
    this.panel = panel;
    JPanel gridPanel = new JPanel();
    gridPanel.setPreferredSize(new Dimension(200, 200));
    gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    setLayout(null);
    setFocusable(true);
    colorDecorations = new Color((int) ctx.get("defaults.panel.decorations.color"));
    strokeDecorations = new BasicStroke((int) ctx.get("defaults.panel.decorations.stroke"));
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        requestFocus();
        if (event.getButton() != MouseEvent.BUTTON1) {
          points.add(new Point(event.getX(), event.getY()));
        }
        repaint();
      }
      
      @Override
      public void mousePressed(MouseEvent event) {
        src = event.getPoint();
      }
      
      @Override
      public void mouseReleased(MouseEvent event) {
        dest = event.getPoint();
        if (src.getX() != dest.getX() || src.getY() != dest.getY()) {
          lines.add(new Line(src, dest));
        }
        repaint();
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseDragged(MouseEvent event) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setColor(colorDecorations);
        g.setStroke(strokeDecorations);
        g.drawLine(src.x, src.y, event.getX(), event.getY());
        repaint();
      }
    });
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          clearDecorations();
          keyLeft();
          return;
        case KeyEvent.VK_RIGHT:
          clearDecorations();
          keyRight();
          return;
        case KeyEvent.VK_HOME:
          clearDecorations();
          keyHome();
          return;
        case KeyEvent.VK_END:
          clearDecorations();
          keyEnd();
          return;
        case KeyEvent.VK_SPACE:
          if (tabbedPane.getTabCount() > 0) {
            int index;
            if (event.isShiftDown()) {
              index = tabbedPane.getSelectedIndex() - 1;
              if (index == -1) {
                index = tabbedPane.getTabCount() - 1;
              }
            } else {
              index = tabbedPane.getSelectedIndex() + 1;
              if (index == tabbedPane.getTabCount()) {
                index = 0;
              }
            }
            tabbedPane.setSelectedIndex(index);
          }
          return;
        case KeyEvent.VK_F5:
          clearDecorations();
          repaint();
          return;
        }
      }
    });
    CHART_LEGEND_SIZE = (int) ctx.get("settings.chart.legendsize");
    CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    TOOLTIPS_MAXLINE = (int) ctx.get("settings.tooltips.maxline");

    // consolidate all time series into a single list
    List<TimeSeries> list = Utils.consolidate(panel);
    
    // collapse all time series into a single, consolidated time series
    timeSeriesCollapsed = Utils.collapse(list);
    log.trace("collapsed series=" + timeSeriesCollapsed.toStringVerbose());
  }

  private void clearDecorations() {
    lines.clear();
    points.clear();
  }
  
  private void drawDecorations(Graphics2D g) {
    // handle panel resizing
    if (widthOrig != getWidth() || heightOrig != getHeight()) {
      clearDecorations();
      widthOrig = getWidth();
      heightOrig = getHeight();
      return;
    }
    g.setColor(colorDecorations);
    g.setStroke(strokeDecorations);
    for (Point point: points) {
      g.drawLine(0, point.y, getWidth(), point.y);
      g.drawLine(point.x, 0, point.x, getHeight());
    }
    for (Line line: lines) {
      g.drawLine(line.getSrc().x, line.getSrc().y, line.getDest().x, line.getDest().y);
    }
  }
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // go to end on first invokation
    if (firstInvokation) {
      firstInvokation = false;
      int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING - panel.getGridLineTextWidth();
      idxBase = Math.max(timeSeriesCollapsed.size() - chartWidth / panel.getDxIncr(), 0);    
    }
    
    // set up the background panel
    setBackground(panel.getBackgroundColor());

    // iterate through the charts
    int yBase = CHART_SEPARATOR;
    mapLegend.clear();
    for (int i = 0; i < panel.getCharts().size(); i++) {
      Chart chart = panel.getCharts().get(i);
      
      // create the renderer
      int chartHeight = (getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;
      ChartRenderer r = new ChartRenderer(panel, chart, this, timeSeriesCollapsed, (Graphics2D) g, ctx, yBase);
      
      // calculate the minimum and maximum
      MinMaxPair pair = new MinMaxPair();
      for (Series series: chart.getSeries()) {
        TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
        if (timeSeries.getType() == TimeSeries.FLOAT) {
          pair = r.calculateMinMax(pair, ctx, timeSeries, timeSeriesCollapsed, idxBase);
        }
      }
      
      float gridLines[];
      try {
        gridLines = r.calculateGridlines(pair);
        r.drawChartBackground(idxBase);
        r.drawBooleanSeries(idxBase);
        r.drawVerticalGridlines(i == 0, idxBase);
        r.drawHorizontalGridlines(chart.getScaler(), gridLines, pair);
        r.drawFloatSeries(chart.getScaler(), pair, idxBase);
        r.drawLegend(mapLegend);
      } catch(Exception ex) {
        log.error("exception occurred while displaying panel " + panel.getLabel() + ": " +  ex);
        r.drawUnavailableChart();
        
        // advance to the next chart
        yBase += chartHeight;
        continue;
      }
      
      // advance to the next chart
      yBase += chartHeight;
    }
    drawDecorations((Graphics2D) g);
  }

  private void keyLeft() {
    int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING - panel.getGridLineTextWidth();
    int idxIncr = (chartWidth / panel.getDxIncr()) / 8;
    idxBase = Math.max(idxBase - idxIncr, 0);
    repaint();
  }
  
  private void keyRight() {
    int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING - panel.getGridLineTextWidth();
    int idxIncr = (chartWidth / panel.getDxIncr()) / 8;
    idxBase = Math.min(idxBase + idxIncr, timeSeriesCollapsed.size() - 1);
    repaint();
  }
   
  private void keyHome() {
    idxBase = 0;
    repaint();
  }
  
  private void keyEnd() {
    int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING - panel.getGridLineTextWidth();
    idxBase = Math.max(timeSeriesCollapsed.size() - chartWidth / panel.getDxIncr(), 0);
    repaint();
  }

  @Override
  public String getToolTipText(MouseEvent event) {
    for (Point point: mapLegend.keySet()) {
      if (event.getX() >= point.getX() && event.getX() <= point.getX() + CHART_LEGEND_SIZE &&
          event.getY() >= point.getY() && event.getY() <= point.getY() + CHART_LEGEND_SIZE) {
        return Utils.getToolTipText(mapLegend.get(point).getTimeSeries(), TOOLTIPS_MAXLINE);
      }
    }
    return null;
  }
}
