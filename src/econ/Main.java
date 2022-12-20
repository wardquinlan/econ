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
    log.info("Application starting...");
    if (args.length != 1) {
      log.error("Usage: Main <profile>.xml");
      System.exit(1);
    }
    
    try {
      XMLParser xmlParser = new XMLParser();
      Econ econ = xmlParser.parse(args[0]);
      Tokenizer tokenizer = new Tokenizer(econ.getScript());
      TokenIterator itr = tokenizer.tokenize();
      if (!itr.hasNext()) {
        throw new Exception("empty script file");
      }
      Parser parser = new Parser();
      Token tk = itr.next();
      parser.parse(tk, itr);
    } catch(Exception e) {
      log.error("Unable to parse profile", e);
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
