package econ;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class EconPanel extends JPanel {
  private EconContext econContext;
  
  public EconPanel(EconContext econContext) {
    super();
    this.econContext = econContext;
    System.out.println("**" + econContext.getSymbolTable().get("settings.background.color"));
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(new Color((int) econContext.getSymbolTable().get("settings.background.color").getValue()));
    g.setColor(Color.RED);
    int widthPanel = getWidth();
    FontMetrics m = g.getFontMetrics(g.getFont());
    int heightFont = m.getHeight();
    int widthText = m.stringWidth("Chart Title");
    g.drawString("Chart Title", (widthPanel - widthText) / 2, heightFont);
    g.drawLine(0, 0, 100, 100);
    g.drawRect(10, 10, 500, 200);
  }
  
}
