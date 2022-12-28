package econ;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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
  private JButton button1 = new JButton("<<");
  private JButton button2 = new JButton(">>");
  private Calendar cal = new GregorianCalendar();
  
  public ChartsPanel(Context ctx, Panel panel) {
    super();
    this.ctx = ctx;
    this.panel = panel;
    JPanel gridPanel = new JPanel();
    gridPanel.setPreferredSize(new Dimension(200, 200));
    gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    setLayout(null);
    //add(gridPanel, BorderLayout.WEST);
    //JPanel panel = new JPanel();
    //panel.setBorder(BorderFactory.createLineBorder(Color.RED));
    //add(panel, BorderLayout.CENTER);
    //button.setBounds(10, 10, getWidth() / 2, getHeight() / 2);
    //add(button);
    add(button1);
    add(button2);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    final Color PANEL_BACKGROUND = new Color((int) ctx.get("settings.panel.background.color"));
    final Color CHART_BACKGROUND = new Color((int) ctx.get("settings.chart.background.color"));
    final Color CHART_RECT = new Color((int) ctx.get("settings.chart.rect.color"));
    final Color CHART_LINE = new Color((int) ctx.get("settings.chart.line.color"));
    final Color CHART_FONT = new Color((int) ctx.get("settings.chart.font.color"));
    final int CHART_SEPARATOR = (int) ctx.get("settings.chart.separator");
    final int CHART_HPADDING = (int) ctx.get("settings.chart.hpadding");
    final int CHART_VPADDING = (int) ctx.get("settings.chart.vpadding");
    final int DXINCR = (int) ctx.get("settings.panel.dxincr");
    
    List<TimeSeries> list = Utils.consolidate(panel);
    TimeSeries timeSeries = Utils.collapse(list);
    
    setBackground(PANEL_BACKGROUND);
    FontMetrics m = g.getFontMetrics(g.getFont());
    
    // button1.setBounds(getWidth() - 100 - (int) ctx.get("settings.panel.padding.right"), getHeight() - 20 - (int) ctx.get("settings.panel.padding.bottom"), 50, 20);
    // button2.setBounds(getWidth() - 50 - (int) ctx.get("settings.panel.padding.right"), getHeight() - 20 - (int) ctx.get("settings.panel.padding.bottom"), 50, 20);
    
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
      ((Graphics2D) g).setStroke(strokeOrig);
      g.setColor(CHART_RECT);
      g.drawRect(CHART_HPADDING, y, chartWidth, chartHeight - CHART_SEPARATOR - 1);

      // Draw the label
      g.setColor(CHART_FONT);
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
            g.setColor(CHART_FONT);
            g.drawString(Utils.getMonthString(cal), x, getHeight() - m.getHeight());
          }
        }
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
