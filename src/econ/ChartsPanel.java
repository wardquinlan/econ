package econ;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChartsPanel extends JPanel {
  private static final long serialVersionUID = 8263376302676172047L;
  private Context ctx;
  private Panel panel;
  private JButton button1 = new JButton("<<");
  private JButton button2 = new JButton(">>");
  
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
    
    setBackground(PANEL_BACKGROUND);
    FontMetrics m = g.getFontMetrics(g.getFont());
    
    // button1.setBounds(getWidth() - 100 - (int) ctx.get("settings.panel.padding.right"), getHeight() - 20 - (int) ctx.get("settings.panel.padding.bottom"), 50, 20);
    // button2.setBounds(getWidth() - 50 - (int) ctx.get("settings.panel.padding.right"), getHeight() - 20 - (int) ctx.get("settings.panel.padding.bottom"), 50, 20);
    
    //int chartHeight = (getHeight() - CHART_SEPARATOR) / panel.getCharts().size();
    int chartWidth = getWidth() - 1 - 2 * CHART_HPADDING;
    int y = CHART_SEPARATOR;
    for (int i = 0; i < panel.getCharts().size(); i++) {
      Chart chart = panel.getCharts().get(i);
      int chartHeight = (getHeight() - CHART_SEPARATOR) * chart.getSpan() / 100;
      
      // Fill the rectangle with the background color
      g.setColor(CHART_BACKGROUND);
      g.fillRect(CHART_HPADDING, y, chartWidth, chartHeight - CHART_SEPARATOR - 1);

      // Draw the grid lines
      g.setColor(CHART_LINE);
      Stroke strokeOrig = ((Graphics2D) g).getStroke();
      for (int x = CHART_HPADDING; x < chartWidth; x += DXINCR) {
        Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1}, 0);
        ((Graphics2D) g).setStroke(stroke);
        g.drawLine(x, y, x, y + chartHeight - CHART_SEPARATOR - 1);
      }
      
      // Draw the rectangle
      ((Graphics2D) g).setStroke(strokeOrig);
      g.setColor(CHART_RECT);
      g.drawRect(CHART_HPADDING, y, chartWidth, chartHeight - CHART_SEPARATOR - 1);

      // Draw the label
      g.setColor(CHART_FONT);
      g.drawString(chart.getLabel(), CHART_HPADDING, y - CHART_VPADDING);

      y += chartHeight;
    }
    
    
    
    /*
    g.drawLine(0, 0, 100, 100);
    g.drawRect(10, 10, 500, 200);
    g.setColor(Color.BLUE);
    g.fillRect(5, 5, 1000, 1000);
    g.setColor(Color.GREEN);
    g.drawLine(0, 0, 100, 100);
    */
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
