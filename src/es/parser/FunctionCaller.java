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
import es.command.Defined;
import es.command.Delete;
import es.command.Drop;
import es.command.Ds;
import es.command.Exists;
import es.command.Exit;
import es.command.ExitIf;
import es.command.FRED;
import es.command.Fonts;
import es.command.GPut;
import es.command.Get;
import es.command.GetId;
import es.command.GetName;
import es.command.GetNotes;
import es.command.GetSource;
import es.command.GetSourceId;
import es.command.GetTitle;
import es.command.GetType;
import es.command.Help;
import es.command.Insert;
import es.command.IsAdmin;
import es.command.IsConnected;
import es.command.Ln;
import es.command.Load;
import es.command.Merge;
import es.command.Meta;
import es.command.Normalize;
import es.command.GetOffset;
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
import es.command.GetSize;
import es.command.Status;
import es.command.Sum;
import es.command.Timestamp;
import es.command.Today;
import es.command.Update;
import es.command.Version;
import es.core.ESIterator;
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
    commandMap.put("getSize", new GetSize());
    commandMap.put("connect", new Connect());
    commandMap.put("save", new Save());
    commandMap.put("setId", new SetId());
    commandMap.put("setTitle", new SetTitle());
    commandMap.put("setNotes", new SetNotes());
    commandMap.put("setName", new SetName());
    commandMap.put("today", new Today());
    commandMap.put("fred", new FRED());
    commandMap.put("timestamp", new Timestamp());
    commandMap.put("ln", new Ln());
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
    commandMap.put("getId", new GetId());
    commandMap.put("getName", new GetName());
    commandMap.put("getTitle", new GetTitle());
    commandMap.put("getNotes", new GetNotes());
    commandMap.put("getSource", new GetSource());
    commandMap.put("getSourceId", new GetSourceId());
    commandMap.put("sum", new Sum());
    commandMap.put("change", new Change());
    commandMap.put("pchange", new PChange());
    commandMap.put("max", new es.command.Max());
    commandMap.put("min", new es.command.Min());
    commandMap.put("get", new Get());
    commandMap.put("assert", new Assert());
    commandMap.put("defined", new Defined());
    commandMap.put("exists", new Exists());
    commandMap.put("getType", new GetType());
    // I can't think of a way to use this to conditionally include files.  If we create an 'if' block, then all the constants (or whatever) are in the
    // scope of the if block.  So this doesn't work.
    
    // this might be relevant for other reasons, though: it is a way to modify a global scope from within a local scope
    commandMap.put("gPut", new GPut());
    if (Settings.getInstance().testFunctions()) {
      commandMap.put("collapse", new Collapse());
      commandMap.put("normalize", new Normalize());
      commandMap.put("getOffset", new GetOffset());
      commandMap.put("fonts", new Fonts());
      commandMap.put("qdb", new QDB());
      commandMap.put("qtp", new QTP());
    }
  }
  
  public boolean isFunction(String funcName) {
    return funcName.equals("help") || commandMap.keySet().contains(funcName);
  }
  
  public Object invokeFunction(String funcName, SymbolTable symbolTable, File file, List<Object> params) throws Exception {
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
    if (commandMap.get(funcName) != null) {
      // call built-in function
      return commandMap.get(funcName).run(symbolTable, file, params);
    }
    if (symbolTable.get(funcName) == null || !(symbolTable.get(funcName).getValue() instanceof FunctionDeclaration)) {
      throw new Exception("symbol not found or symbol is not a function: " + funcName);
    }
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) symbolTable.get(funcName).getValue();
    if (functionDeclaration.getParams().size() != params.size()) {
      throw new Exception("param list size mismatch during function call: " + funcName);
    }
    SymbolTable childSymbolTable = new SymbolTable(symbolTable);
    for (int i = 0; i < params.size(); i++) {
      childSymbolTable.localPut(functionDeclaration.getParams().get(i), new Symbol(functionDeclaration.getParams().get(i), params.get(i)));
    }
    Evaluator e = new Evaluator(childSymbolTable);
    ESIterator<Statement> itr = new ESIterator<>(functionDeclaration.getStatements());
    e.evaluate(itr);
    return null;
  }
}
