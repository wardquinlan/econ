package econ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
  private static final Log log = LogFactory.getFactory().getInstance(Main.class);
  public static void main(String[] args) {
    log.info("application starting");
    
    if (System.getenv("ECON_HOST") == null) {
      log.error("ECON_HOST not set");
      System.exit(1);
    }
    if (System.getenv("ECON_DATABASE") == null) {
      log.error("ECON_DATABASE not set");
      System.exit(1);
    }
    if (System.getenv("ECON_USERNAME") == null) {
      log.error("ECON_USERNAME not set");
      System.exit(1);
    }
    if (System.getenv("ECON_PASSWORD") == null) {
      log.error("ECON_PASSWORD not set");
      System.exit(1);
    }
    if (args.length != 1) {
      log.error("usage: econ.Main <profile>.xml");
      System.exit(1);
    }
    
    try {
      // Initialize the instance before we get too far
      TimeSeriesDAO.getInstance();
      XMLParser xmlParser = new XMLParser();
      EconContext context = xmlParser.parse(args[0], 0);
      TimeSeriesDAO.getInstance().close();
    } catch(Exception e) {
      log.error(e);
      try {
        if (TimeSeriesDAO.getInstance() != null) {
          TimeSeriesDAO.getInstance().close();
        }
      } catch(Exception e2) {
        log.warn("unable to close DAO", e2);
      }
      System.exit(1);
    }
    
    /*
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
        */
  }
}
