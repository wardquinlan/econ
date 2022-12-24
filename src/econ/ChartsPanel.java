package econ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

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
    final Color CHART_RECT = new Color((int) ctx.get("settings.chart.line.color"));
    final Color CHART_FONT = new Color((int) ctx.get("settings.chart.font.color"));
    
    setBackground(PANEL_BACKGROUND);
    FontMetrics m = g.getFontMetrics(g.getFont());
    
    // button1.setBounds(getWidth() - 100 - (int) ctx.get("settings.panel.padding.right"), getHeight() - 20 - (int) ctx.get("settings.panel.padding.bottom"), 50, 20);
    // button2.setBounds(getWidth() - 50 - (int) ctx.get("settings.panel.padding.right"), getHeight() - 20 - (int) ctx.get("settings.panel.padding.bottom"), 50, 20);
    
    int chartHeight = (getHeight() - m.getHeight()) / panel.getCharts().size();
    for (int i = 0; i < panel.getCharts().size(); i++) {
      Chart chart = panel.getCharts().get(i);
      int y = m.getHeight() + i * chartHeight;
      
      g.setColor(CHART_BACKGROUND);
      g.fillRect(0, y, getWidth() - 1, chartHeight - m.getHeight() - 1);
      
      g.setColor(CHART_RECT);
      g.drawRect(0, y, getWidth() - 1, chartHeight - m.getHeight() - 1);

      g.setColor(CHART_FONT);
      g.drawString(chart.getLabel(), 0, y);
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
