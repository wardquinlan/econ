package econ;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

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
      File file = new File(args[0]);
      Path path = Paths.get(file.getAbsolutePath());
      String basename = path.getParent().toString();
      String filename = file.getName();
      Context ctx = xmlParser.parse(basename, filename, 0);
      JFrame frame = new Frame(ctx);
      frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            log.info("closing");
            synchronized (Lock.instance()) {
              Lock.instance().notify();
            }
            //System.exit(0);
        }
      });
      synchronized (Lock.instance()) {
        Lock.instance().wait();
      }
      
      System.out.println("***");
      // TimeSeriesDAO.getInstance().close();
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
  }
}
