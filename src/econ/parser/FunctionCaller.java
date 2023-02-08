package econ.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.command.Assert;
import econ.command.CatCommand;
import econ.command.Collapse;
import econ.command.Command;
import econ.command.Connect;
import econ.command.CreateCommand;
import econ.command.Data;
import econ.command.Exit;
import econ.command.FRED;
import econ.command.HelpCommand;
import econ.command.InsertCommand;
import econ.command.LsCommand;
import econ.command.Fonts;
import econ.command.Get;
import econ.command.LoadCommand;
import econ.command.Meta;
import econ.command.Normalize;
import econ.command.Offset;
import econ.command.Plot;
import econ.command.PrintCommand;
import econ.command.QDB;
import econ.command.QTP;
import econ.command.Save;
import econ.command.SetId;
import econ.command.SetNotes;
import econ.command.SetTitle;
import econ.command.Size;
import econ.command.Timestamp;
import econ.command.Today;
import econ.core.TimeSeries;
import econ.core.TimeSeriesDAO;
import econ.core.Utils;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);
  private Map<String, Command> commandMap = new TreeMap<>();

  static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 30};
  static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};

  public FunctionCaller() {
    commandMap.put("exit", new Exit());
    commandMap.put("print", new PrintCommand());
    commandMap.put("fonts", new Fonts());
    commandMap.put("help", new HelpCommand());
    commandMap.put("load", new LoadCommand());
    commandMap.put("plot", new Plot());
    commandMap.put("ls", new LsCommand());
    commandMap.put("data", new Data());
    commandMap.put("meta", new Meta());
    commandMap.put("cat", new CatCommand());
    commandMap.put("create", new CreateCommand());
    commandMap.put("insert", new InsertCommand());
    commandMap.put("size", new Size());
    commandMap.put("connect", new Connect());
    commandMap.put("save", new Save());
    commandMap.put("setId", new SetId());
    commandMap.put("setTitle", new SetTitle());
    commandMap.put("setNotes", new SetNotes());
    commandMap.put("today", new Today());
    commandMap.put("fred", new FRED());
    commandMap.put("qdb", new QDB());
    commandMap.put("qtp", new QTP());
    commandMap.put("timestamp", new Timestamp());
    commandMap.put("log", new econ.command.Log());
    if ("true".equals(System.getProperty("econ.test"))) {
      commandMap.put("collapse", new Collapse());
      commandMap.put("normalize", new Normalize());
      commandMap.put("assert", new Assert());
      commandMap.put("offset", new Offset());
      commandMap.put("get", new Get());
    }
  }
  
  public boolean isFunction(String funcName) {
    return funcName.equals("help") || commandMap.keySet().contains(funcName);
  }
  
  public Object invokeFunction(String funcName, Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (funcName.equals("help")) {
      if (params.size() == 1) {
        Command command = commandMap.get(params.get(0));
        if (command != null) {
          System.out.println(command.getSummary());
          System.out.println();
          for (String detail: command.getDetails()) {
            System.out.println(detail);
          }
          System.out.println();
          System.out.println("Returns: " + command.getReturns());
          return 0;
        }
      }
      for (String name: commandMap.keySet()) {
        Command command = commandMap.get(name);
        System.out.println(command.getSummary());
      }
      return 0;
    }
    return commandMap.get(funcName).run(symbolTable, file, params);
  }
}
