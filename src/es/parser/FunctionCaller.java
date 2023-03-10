package es.parser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.command.Assert;
import es.command.Average;
import es.command.Cat;
import es.command.Change;
import es.command.Collapse;
import es.command.Command;
import es.command.Connect;
import es.command.Create;
import es.command.Data;
import es.command.Delete;
import es.command.Drop;
import es.command.Ds;
import es.command.Exit;
import es.command.ExitIf;
import es.command.FRED;
import es.command.Fonts;
import es.command.Get;
import es.command.GetNotes;
import es.command.GetSource;
import es.command.GetSourceId;
import es.command.GetTitle;
import es.command.Help;
import es.command.Insert;
import es.command.IsAdmin;
import es.command.IsConnected;
import es.command.Load;
import es.command.Merge;
import es.command.Meta;
import es.command.Normalize;
import es.command.Offset;
import es.command.PChange;
import es.command.Plot;
import es.command.Print;
import es.command.QDB;
import es.command.QTP;
import es.command.Save;
import es.command.SetId;
import es.command.SetName;
import es.command.SetNotes;
import es.command.SetSource;
import es.command.SetSourceId;
import es.command.SetTitle;
import es.command.Size;
import es.command.Status;
import es.command.Sum;
import es.command.Timestamp;
import es.command.Today;
import es.command.Update;
import es.command.Version;
import es.core.Settings;

public class FunctionCaller {
  private Map<String, Command> commandMap = new TreeMap<>();

  static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 30};
  static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};

  public FunctionCaller() {
    commandMap.put("exit", new Exit());
    commandMap.put("print", new Print());
    commandMap.put("help", new Help());
    commandMap.put("load", new Load());
    commandMap.put("plot", new Plot());
    commandMap.put("ds", new Ds());
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
    commandMap.put("log", new es.command.Log());
    commandMap.put("date", new es.command.Date());
    commandMap.put("average", new Average());
    commandMap.put("merge", new Merge());
    commandMap.put("delete", new Delete());
    commandMap.put("update", new Update());
    commandMap.put("drop", new Drop());
    commandMap.put("setSourceId", new SetSourceId());
    commandMap.put("setSource", new SetSource());
    commandMap.put("status", new Status());
    commandMap.put("version", new Version());
    commandMap.put("data", new Data());
    commandMap.put("isConnected", new IsConnected());
    commandMap.put("exitIf", new ExitIf());
    commandMap.put("isAdmin", new IsAdmin());
    commandMap.put("getTitle", new GetTitle());
    commandMap.put("getNotes", new GetNotes());
    commandMap.put("getSource", new GetSource());
    commandMap.put("getSourceId", new GetSourceId());
    commandMap.put("sum", new Sum());
    commandMap.put("change", new Change());
    commandMap.put("pchange", new PChange());
    commandMap.put("max", new es.command.Max());
    commandMap.put("min", new es.command.Min());
    if (Settings.getInstance().testFunctions()) {
      commandMap.put("collapse", new Collapse());
      commandMap.put("normalize", new Normalize());
      commandMap.put("assert", new Assert());
      commandMap.put("offset", new Offset());
      commandMap.put("get", new Get());
      commandMap.put("fonts", new Fonts());
    }
  }
  
  public boolean isFunction(String funcName) {
    return funcName.equals("help") || commandMap.keySet().contains(funcName);
  }
  
  public Object invokeFunction(String funcName, Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (funcName.equals("help")) {
      if (params.size() == 1 && params.get(0) instanceof String) {
        Command command = commandMap.get(params.get(0));
        if (command != null) {
          System.out.println(command.getSummary());
          System.out.println();
          for (String detail: command.getDetails()) {
            System.out.println(detail);
          }
          if (command.getReturns() != null) {
            System.out.println();
            System.out.println("Returns: " + command.getReturns());
          }
          return null;
        }
      }
      for (String name: commandMap.keySet()) {
        Command command = commandMap.get(name);
        System.out.println(command.getSummary());
      }
      return null;
    }
    return commandMap.get(funcName).run(symbolTable, file, params);
  }
}
