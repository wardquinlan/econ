package econ.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.parser.Parser;
import econ.parser.Symbol;
import econ.parser.Token;
import econ.parser.TokenIterator;
import econ.parser.Tokenizer;

public class Main {
  private static final Log log = LogFactory.getFactory().getInstance(Main.class);
  
  public static void main(String[] args) {
    if (System.getenv("ECON_HOME") == null) {
      log.error("ECON_HOME not set");
      System.exit(1);
    }
    if (args.length > 1) {
      log.error("usage: econ.Main [<script.ec>]");
      System.exit(1);
    }
    
    try {
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
        log.debug("found .econ.ec...");
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
            System.out.print("econ> ");
            String line = rdr.readLine();
            if (line == null) {
              break;
            }
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
