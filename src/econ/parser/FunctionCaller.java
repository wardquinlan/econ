package econ.parser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import econ.command.Assert;
import econ.command.Average;
import econ.command.Cat;
import econ.command.Collapse;
import econ.command.Command;
import econ.command.Connect;
import econ.command.Create;
import econ.command.Data;
import econ.command.Delete;
import econ.command.Exit;
import econ.command.FRED;
import econ.command.Help;
import econ.command.Insert;
import econ.command.Ds;
import econ.command.Fonts;
import econ.command.Get;
import econ.command.Load;
import econ.command.Merge;
import econ.command.Meta;
import econ.command.Normalize;
import econ.command.Offset;
import econ.command.Plot;
import econ.command.Print;
import econ.command.QDB;
import econ.command.QTP;
import econ.command.Save;
import econ.command.SetId;
import econ.command.SetName;
import econ.command.SetNotes;
import econ.command.SetTitle;
import econ.command.Size;
import econ.command.Timestamp;
import econ.command.Today;
import econ.command.Update;

public class FunctionCaller {
  private Map<String, Command> commandMap = new TreeMap<>();

  static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 30};
  static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};

  public FunctionCaller() {
    commandMap.put("exit", new Exit());
    commandMap.put("print", new Print());
    commandMap.put("fonts", new Fonts());
    commandMap.put("help", new Help());
    commandMap.put("load", new Load());
    commandMap.put("plot", new Plot());
    commandMap.put("ds", new Ds());
    commandMap.put("data", new Data());
    commandMap.put("meta", new Meta());
    commandMap.put("cat", new Cat());
    commandMap.put("create", new Create());
    commandMap.put("insert", new Insert());
    commandMap.put("size", new Size());
    commandMap.put("connect", new Connect());
    commandMap.put("save", new Save());
    commandMap.put("setId", new SetId());
    commandMap.put("setTitle", new SetTitle());
    commandMap.put("setNotes", new SetNotes());
    commandMap.put("setName", new SetName());
    commandMap.put("today", new Today());
    commandMap.put("fred", new FRED());
    commandMap.put("qdb", new QDB());
    commandMap.put("qtp", new QTP());
    commandMap.put("timestamp", new Timestamp());
    commandMap.put("log", new econ.command.Log());
    commandMap.put("date", new econ.command.Date());
    commandMap.put("average", new Average());
    commandMap.put("merge", new Merge());
    commandMap.put("delete", new Delete());
    commandMap.put("update", new Update());
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
