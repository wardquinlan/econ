package econ;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class EconPanel extends JPanel {
  private EconContext context;
  
  public EconPanel(EconContext context) {
    super();
    this.context = context;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(new Color((int) context.get("settings.panel.background.color")));
    int widthPanel = getWidth();
    FontMetrics m = g.getFontMetrics(g.getFont());
    int heightFont = m.getHeight();
    int widthText = m.stringWidth("Chart Title");
    g.drawString("Chart Title", (widthPanel - widthText) / 2, heightFont);
    
    int x = (int) context.get("settings.panel.padding.left");
    int y = m.getHeight();
    int width = getWidth() - x - (int) context.get("settings.panel.padding.right");
    int height = getHeight() - y - (int) context.get("settings.panel.padding.bottom");
    g.drawRect(x, y, width, height);
    /*
    g.drawLine(0, 0, 100, 100);
    g.drawRect(10, 10, 500, 200);
    g.setColor(Color.BLUE);
    g.fillRect(5, 5, 1000, 1000);
    g.setColor(Color.GREEN);
    g.drawLine(0, 0, 100, 100);
    */
  }
  
}
