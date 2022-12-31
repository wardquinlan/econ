package econ;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
  private static final Log log = LogFactory.getFactory().getInstance(Main.class);
  
  public static void main(String[] args) {
    if (System.getenv("ECON_HOME") == null) {
      log.error("ECON_HOME not set");
      System.exit(1);
    }
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
      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          log.info("shutting down...");
          try {
            TimeSeriesDAO.getInstance().close();
            log.info("DAO connection closed");
          } catch(Exception e) {
            log.error("could not close DAO connection", e);
          }
        }
      });
      
      Map<String, Symbol> symbolTable = new HashMap<String, Symbol>();
      File file = new File(System.getenv("ECON_HOME") + File.separator + ".econ.ec");
      if (file.exists()) {
        log.info("found .econ.ec...");
        Tokenizer tokenizer = new Tokenizer(file, 0);
        TokenIterator itr = tokenizer.tokenize();
        if (itr.hasNext()) {
          Parser parser = new Parser(symbolTable);
          Token tk = itr.next();
          parser.parse(tk, itr);
        }
      } else {
        log.warn(".econ.ec does not exist, skipping");
      }
      
      if (args.length == 1) {
        Tokenizer tokenizer = new Tokenizer(new File(args[0]), 0);
        TokenIterator itr = tokenizer.tokenize();
        if (itr.hasNext()) {
          Parser parser = new Parser(symbolTable);
          Token tk = itr.next();
          parser.parse(tk, itr);
        }
      } else {
        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
          try {
            System.out.print("> ");
            String line = rdr.readLine();
            Tokenizer tokenizer = new Tokenizer(new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8)));
            TokenIterator itr = tokenizer.tokenize();
            if (itr.hasNext()) {
              Parser parser = new Parser(symbolTable);
              Token tk = itr.next();
              parser.parse(tk, itr);
            }
          } catch(Exception e) {
            log.error(e);
          }
        }
      }
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
