package econ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
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
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        requestFocus();
      }
    });
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          keyLeft();
          return;
        case KeyEvent.VK_RIGHT:
          keyRight();
          return;
        case KeyEvent.VK_HOME:
          keyHome();
          return;
        case KeyEvent.VK_END:
          keyEnd();
          return;
        }
      }
    });
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    System.out.println("REPAINTING...");
    final Color PANEL_BACKGROUND = new Color((int) ctx.get("settings.panel.background.color"));
    final int CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    final int CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    final int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING;
    
    // consolidate all time series into a single list
    List<TimeSeries> list = Utils.consolidate(panel);
    
    // collapse all time series into a single, consolidated time series
    TimeSeries timeSeriesCollapsed = Utils.collapse(list);
    log.debug("collapsed series=" + timeSeriesCollapsed.toStringVerbose());
    
    // set up the background panel and save the stroke
    setBackground(PANEL_BACKGROUND);
    Stroke strokeOrig = ((Graphics2D) g).getStroke();

    // save the calculated pairs and gridlines for later
    Map<Integer, MinMaxPair> mapPair = new HashMap<>();
    Map<Integer, float[]> mapGridLines = new HashMap<>();
    
    // iterate through the charts
    int gridLineStringWidth = 0;
    int yBase = CHART_SEPARATOR;
    for (int i = 0; i < panel.getCharts().size(); i++) {
      // create the UITools instance
      Chart chart = panel.getCharts().get(i);
      UITools ut = new UITools(chart, this, timeSeriesCollapsed, g, ctx, yBase, 0);
      
      // calculate the minimum and maximum
      MinMaxPair pair = new MinMaxPair();
      for (Series series: chart.getSeries()) {
        TimeSeries timeSeries = Utils.normalize(timeSeriesCollapsed, series.getTimeSeries());
        pair = Utils.calculateMinMax(pair, ctx, timeSeries, timeSeriesCollapsed, chartWidth);
      }
      mapPair.put(i, pair);
      log.debug("pair=" + pair);
      
      // compute the gridlines
      float gridLines[];
      try {
        gridLines = ut.calculateGridlines(pair);
        mapGridLines.put(i, gridLines);
        gridLineStringWidth = ut.getMaximumStringWidth(gridLines, gridLineStringWidth);
      } catch(Exception ex) {
        log.error("dyGridLines overflowed, not displaying chart: " + chart.getLabel());
        return;
      }
    }
    
    for (int i = 0; i < panel.getCharts().size(); i++) {
      // create the UITools instance
      Chart chart = panel.getCharts().get(i);
      int chartHeight = (getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;
      UITools ut = new UITools(chart, this, timeSeriesCollapsed, g, ctx, yBase, gridLineStringWidth);
      
      // draw the chart background
      ut.drawChartBackground(chart, i == 0);
      
      // draw the horizontal gridlines
      ut.drawHorizontalGridlines(mapGridLines.get(i), mapPair.get(i));
      
      // draw the series themselves
      ((Graphics2D) g).setStroke(strokeOrig);
      ut.drawSeries(chart, mapPair.get(i));
      
      // advance to the next chart
      yBase += chartHeight;
    }
  }

  private void keyLeft() {
    System.out.println("LEFT");
    System.out.println(panel.getLabel());
    repaint();
  }
  
  private void keyRight() {
    System.out.println("RIGHT");
  }
   
  private void keyHome() {
    System.out.println("HOME");
  }
  
  private void keyEnd() {
    System.out.println("END");
  }

  @Override
  public String getToolTipText(MouseEvent event) {
    System.out.println(event.getX());
    System.out.println(event.getY());
    StringBuffer sb = new StringBuffer();
    sb.append("<html>");
    sb.append("<h2><strong>" + panel.getLabel() + "</strong></h2>");
    sb.append("<p>" + "The rain in spain falls mainly on the plain" + "</p>");
    sb.append("</html>");
    return sb.toString();
  }
}
