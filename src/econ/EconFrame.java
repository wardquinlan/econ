package econ;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class EconFrame extends JFrame {
  public EconFrame(EconContext econContext) {
    super();
    JFrame frame = new JFrame("Econ");
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JPanel() {
          @Override
          public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(0xc0c0c0));
              int widthPanel = getWidth();
              FontMetrics m = g.getFontMetrics(g.getFont());
              int heightFont = m.getHeight();
              int widthText = m.stringWidth("Chart Title");
              g.drawString("Chart Title", (widthPanel - widthText) / 2, heightFont);
              g.drawLine(0, 0, 100, 100);
          }
        });
        frame.setVisible(true);
  }
}
