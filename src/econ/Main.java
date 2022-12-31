package econ;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
  private static final Log log = LogFactory.getFactory().getInstance(Main.class);
  
  public static void main(String[] args) {
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
    if (args.length > 1) {
      log.error("usage: econ.Main [<script.ec>]");
      System.exit(1);
    }
    
    try {
      // Initialize the instance before we get too far
      TimeSeriesDAO.getInstance();
      Tokenizer tokenizer;
      if (args.length == 1) {
        tokenizer = new Tokenizer(new File(args[0]), 0);
      } else {
        tokenizer = new Tokenizer();
      }
      TokenIterator itr = tokenizer.tokenize();
      if (itr.hasNext()) {
        Parser parser = new Parser();
        Token tk = itr.next();
        parser.parse(tk, itr);
      }
      
      TimeSeriesDAO.getInstance().close();
      System.exit(0);
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
