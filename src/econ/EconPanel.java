package econ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class EconPanel extends JPanel {
  private EconContext context;
  private JButton button1 = new JButton("<<");
  private JButton button2 = new JButton(">>");
  
  public EconPanel(EconContext context) {
    super();
    this.context = context;
    JPanel gridPanel = new JPanel();
    gridPanel.setPreferredSize(new Dimension(200, 200));
    gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
    setLayout(new BorderLayout());
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
    setBackground(new Color((int) context.get("settings.panel.background.color")));
    int widthPanel = getWidth();
    FontMetrics m = g.getFontMetrics(g.getFont());
    int heightFont = m.getHeight();
    int widthText = m.stringWidth("Chart Title");
    g.drawString("Chart Title", (widthPanel - widthText) / 2, heightFont);
    
    button1.setBounds(10, 10, 50, 20);
    button2.setBounds(60, 10, 50, 20);
    
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
