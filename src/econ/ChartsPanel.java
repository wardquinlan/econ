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
    final String PANEL_FONT_NAME = (String) ctx.get("settings.panel.font.name");
    final Integer PANEL_FONT_SIZE = (Integer) ctx.get("settings.panel.font.size");
    final int CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    final int CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    final int CHART_VPADDING = (int) ctx.get("settings.chart.vpadding");
    final int DXINCR = (int) ctx.get("settings.panel.dxincr");
    final Font PANEL_FONT;
    
    if (PANEL_FONT_NAME != null && PANEL_FONT_SIZE != null) {
      PANEL_FONT = new Font(PANEL_FONT_NAME, Font.PLAIN, PANEL_FONT_SIZE);
    } else if (PANEL_FONT_NAME != null) {
      PANEL_FONT = new Font(PANEL_FONT_NAME, Font.PLAIN, g.getFont().getSize());
    } else if (PANEL_FONT_SIZE != null) {
      PANEL_FONT = new Font(g.getFont().getName(), Font.PLAIN, PANEL_FONT_SIZE);
    } else {
      PANEL_FONT = null;
    }

    List<TimeSeries> list = Utils.consolidate(panel);
    TimeSeries timeSeries = Utils.collapse(list);
    
    setBackground(PANEL_BACKGROUND);
    if (PANEL_FONT != null) {
      g.setFont(PANEL_FONT);
    }

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
      for (int idx = 1, x = CHART_HPADDING + DXINCR; idx < timeSeries.getTimeSeriesData().size() && x < chartWidth; idx++, x += DXINCR) {
        cal.setTime(timeSeries.getTimeSeriesData().get(idx - 1).getDate());
        int monthPrev = cal.get(Calendar.MONTH);

        cal.setTime(timeSeries.getTimeSeriesData().get(idx).getDate());
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
      
      // Draw the series themselves
      ((Graphics2D) g).setStroke(strokeOrig);
      int yy = 200;
      for (Series series: chart.getSeries()) {
        TimeSeries timeSeriesCurr = series.getTimeSeries();
        g.setColor(series.getColor());
        int idxCurr = 0;
        for (int idx = 0, x = CHART_HPADDING; idx < timeSeries.getTimeSeriesData().size() && x < chartWidth; idx++, x += DXINCR) {
          TimeSeriesData data = timeSeries.getTimeSeriesData().get(idx);
          TimeSeriesData dataCurr = timeSeriesCurr.getTimeSeriesData().get(idxCurr);
          if (dataCurr.getDate().compareTo(data.getDate()) == 0) {
            g.drawString("x", x, yy);
            if (idxCurr < timeSeriesCurr.getTimeSeriesData().size() - 1) {
              idxCurr++;
            }
          }
        }
        yy += 40;
      }
      
      y += chartHeight;
    }
  }
  
  /* Transformation Function:
   *
   * s1 = series low point
   * s2 = series high point
   * y1 = grid low point
   * y2 = grid high point
   * 
   * f(y) = y1 + y * (y2 - y1) / (s2 - s1)
   */
}
