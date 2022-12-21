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
  private static Log log = LogFactory.getFactory().getInstance(Main.class);
  public static void main(String[] args) {
    log.info("Econ: application starting");
    
    if (System.getenv("ECON_HOST") == null) {
      log.error("Econ: ECON_HOST not set");
      System.exit(1);
    }
    if (System.getenv("ECON_DATABASE") == null) {
      log.error("Econ: ECON_DATABASE not set");
      System.exit(1);
    }
    if (System.getenv("ECON_USERNAME") == null) {
      log.error("Econ: ECON_USERNAME not set");
      System.exit(1);
    }
    if (System.getenv("ECON_PASSWORD") == null) {
      log.error("Econ: ECON_PASSWORD not set");
      System.exit(1);
    }
    if (args.length != 1) {
      log.error("Econ: usage: Econ <profile>.xml");
      System.exit(1);
    }
    
    try {
      SeriesDAO.getInstance();
      XMLParser xmlParser = new XMLParser();
      Econ econ = xmlParser.parse(args[0]);
      Tokenizer tokenizer = new Tokenizer(econ.getScript());
      TokenIterator itr = tokenizer.tokenize();
      if (!itr.hasNext()) {
        log.error("Econ: empty script file");
        System.exit(1);
      }
      Parser parser = new Parser();
      Token tk = itr.next();
      parser.parse(tk, itr);
      SeriesDAO.getInstance().close();
    } catch(Exception e) {
      log.error("Econ: fatal error", e);
      try {
        if (SeriesDAO.getInstance() != null) {
          SeriesDAO.getInstance().close();
        }
      } catch(Exception e2) {
        log.warn("Econ: unable to close DAO", e2);
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
