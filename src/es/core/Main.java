package es.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.parser.Evaluator;
import es.parser.Parser;
import es.parser.Statement;
import es.parser.SymbolTable;
import es.parser.Token;
import es.parser.Tokenizer;

public class Main {
  private static final Log log = LogFactory.getFactory().getInstance(Main.class);
  
  private static void evaluateWithExit(Tokenizer tokenizer, SymbolTable symbolTable) throws Exception {
    ESIterator<Token> itr = tokenizer.tokenize();
    if (itr.hasNext()) {
      Parser p = new Parser();
      Token tk = itr.next();
      ESIterator<Statement> itr2 = p.parse(tk, itr);
      if (itr2.hasNext()) {
        Evaluator e = new Evaluator(symbolTable);
        Statement statement = itr2.next();
        Object result = e.evaluate(statement, itr2);
        if (result != null) {
          if (result instanceof Integer) {
            log.info("exiting with status code " + (Integer) result);
            System.exit((Integer) result);
          }
          log.info("evaluator returned non-integral result (" + result + "); ignoring");
        }
      }
    }
  }

  public static void main(String[] args) {
    if (System.getenv("ES_HOME") == null) {
      log.error("ES_HOME not set");
      System.exit(1);
    }

    try {
      InputStream input = new FileInputStream(System.getenv("ES_HOME") + File.separator + "es.d" + File.separator + "es.properties");
      Properties props = new Properties();
      props.load(input);
      if (props.get("es.version") == null) {
        log.error("cannot load version");
        System.exit(1);
      }
      Settings.getInstance().setVersion((String) props.get("es.version"));
    } catch (IOException e) {
      log.error("cannot load version", e);
      System.exit(1);
    }

    Options options = new Options();
    Option opt = new Option("s", "suppress-autoload", false, "suppress auto-loading of .es");
    options.addOption(opt);
    opt = new Option("t", "test", false, "include test commands");
    options.addOption(opt);
    opt = new Option("a", "admin", false, "run in administrative mode");
    options.addOption(opt);
    opt = new Option("p", "suppress-prompt", false, "suppresses prompt in interactive mode");
    options.addOption(opt);
    opt = new Option("h", "help", false, "display this screen");
    options.addOption(opt);
    opt = new Option("c", "command", true, "run specified command(s) then exit");
    options.addOption(opt);
    CommandLine cmd = null;
    try {
      CommandLineParser parser = new DefaultParser();
      cmd = parser.parse(options, args);
      if (cmd.hasOption("help")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("es", options);
        System.exit(0);
      }
      Settings.getInstance().setOptions(cmd);
    } catch(ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("es", options);
      System.exit(1);
    }
    
    args = cmd.getArgs();
    if (args.length > 1) {
      log.error("usage: es.core.Main [<script-name.es>]");
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
      
      SymbolTable symbolTable = new SymbolTable();
      File file = new File(System.getenv("ES_HOME") + File.separator + ".es");
      if (file.exists() && !Settings.getInstance().suppressAutoload()) {
        log.info("found autoload file '.es'; loading...");
        Tokenizer tokenizer = new Tokenizer(file, 0);
        evaluateWithExit(tokenizer, symbolTable);
      } else {
        log.warn("skipping the loading of .es...");
      }
      
      if (cmd.hasOption("command") && args.length == 1) {
        throw new Exception("cannot specify both --command option and an input file");
      }
      if (args.length == 1) {
        Tokenizer tokenizer = new Tokenizer(new File(args[0]), 0);
        evaluateWithExit(tokenizer, symbolTable);
      } else if (cmd.hasOption("command")) {
        String value = cmd.getOptionValue("command");
        Tokenizer tokenizer = new Tokenizer(new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)));
        evaluateWithExit(tokenizer, symbolTable);
      } else {
        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
          try {
            if (!Settings.getInstance().isPromptSuppressed()) {
              if (Settings.getInstance().isAdmin()) {
                System.out.print("ES # ");
              } else {
                System.out.print("ES $ ");
              }
            }
            String line = rdr.readLine();
            if (line == null) {
              break;
            }
            Tokenizer tokenizer = new Tokenizer(new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8)));
            evaluateWithExit(tokenizer, symbolTable);
          } catch(Exception e) {
            log.error(e);
          }
        }
      }
      System.exit(0);
    } catch(Exception e) {
      log.error(e);
      log.info("exiting with status code 1...");
      System.exit(1);
    }
  }
}
