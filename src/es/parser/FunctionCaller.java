package es.parser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.command.SystemAlias;
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
import es.command.DlgConfirm;
import es.command.DlgInput;
import es.command.DlgMessage;
import es.command.Drop;
import es.command.Ds;
import es.command.SubString;
import es.command.Equals;
import es.command.Exit;
import es.command.FRED;
import es.command.Fonts;
import es.command.Functions;
import es.command.GGet;
import es.command.GPut;
import es.command.Get;
import es.command.GetDate;
import es.command.GetEnv;
import es.command.GetFrequency;
import es.command.GetFrequencyShort;
import es.command.GetId;
import es.command.GetName;
import es.command.GetNotes;
import es.command.GetSource;
import es.command.GetSourceId;
import es.command.GetTitle;
import es.command.GetType;
import es.command.GetUnits;
import es.command.GetUnitsShort;
import es.command.Help;
import es.command.Insert;
import es.command.IsAdmin;
import es.command.IsConnected;
import es.command.Iterate;
import es.command.Ln;
import es.command.Load;
import es.command.Merge;
import es.command.Meta;
import es.command.Normalize;
import es.command.NotEquals;
import es.command.GetOffset;
import es.command.GetSeriesType;
import es.command.PChange;
import es.command.ParseFloat;
import es.command.ParseInt;
import es.command.Plot;
import es.command.Print;
import es.command.Printf;
import es.command.QDB;
import es.command.QTP;
import es.command.Random;
import es.command.Save;
import es.command.SetFrequency;
import es.command.SetFrequencyShort;
import es.command.SetId;
import es.command.SetLogLevel;
import es.command.SetName;
import es.command.SetNotes;
import es.command.SetSource;
import es.command.SetSourceId;
import es.command.SetTitle;
import es.command.SetUnits;
import es.command.SetUnitsShort;
import es.command.GetLength;
import es.command.GetSize;
import es.command.Status;
import es.command.StdDev;
import es.command.Sum;
import es.command.Timestamp;
import es.command.Update;
import es.command.UserAlias;
import es.command.Version;
import es.core.ESIterator;
import es.core.Settings;
import es.core.Utils;

public class FunctionCaller {
  private Map<String, Command> commandMap = new TreeMap<>();
  private Map<String, String> aliasMap = new TreeMap<>();

  private static FunctionCaller instance = new FunctionCaller();
  
  public static FunctionCaller getInstance() {
    return instance;
  }
  
  private FunctionCaller() {
    commandMap.put(Utils.ROOT_NAMESPACE + "Exit", new Exit());
    commandMap.put(Utils.ROOT_NAMESPACE + "Print", new Print());
    commandMap.put(Utils.ROOT_NAMESPACE + "Help", new Help());
    commandMap.put(Utils.ROOT_NAMESPACE + "Load", new Load());
    commandMap.put(Utils.ROOT_NAMESPACE + "Plot", new Plot());
    commandMap.put(Utils.ROOT_NAMESPACE + "Ds", new Ds());
    commandMap.put(Utils.ROOT_NAMESPACE + "Meta", new Meta());
    commandMap.put(Utils.ROOT_NAMESPACE + "Cat", new Cat());
    commandMap.put(Utils.ROOT_NAMESPACE + "Create", new Create());
    commandMap.put(Utils.ROOT_NAMESPACE + "Insert", new Insert());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetSize", new GetSize());
    commandMap.put(Utils.ROOT_NAMESPACE + "Connect", new Connect());
    commandMap.put(Utils.ROOT_NAMESPACE + "Save", new Save());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetId", new SetId());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetTitle", new SetTitle());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetNotes", new SetNotes());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetName", new SetName());
    commandMap.put(Utils.ROOT_NAMESPACE + "Fred", new FRED());
    commandMap.put(Utils.ROOT_NAMESPACE + "Timestamp", new Timestamp());
    commandMap.put(Utils.ROOT_NAMESPACE + "Ln", new Ln());
    commandMap.put(Utils.ROOT_NAMESPACE + "Log", new es.command.Log());
    commandMap.put(Utils.ROOT_NAMESPACE + "Date", new es.command.Date());
    commandMap.put(Utils.ROOT_NAMESPACE + "Average", new Average());
    commandMap.put(Utils.ROOT_NAMESPACE + "Merge", new Merge());
    commandMap.put(Utils.ROOT_NAMESPACE + "Delete", new Delete());
    commandMap.put(Utils.ROOT_NAMESPACE + "Update", new Update());
    commandMap.put(Utils.ROOT_NAMESPACE + "Drop", new Drop());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetSourceId", new SetSourceId());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetSource", new SetSource());
    commandMap.put(Utils.ROOT_NAMESPACE + "Status", new Status());
    commandMap.put(Utils.ROOT_NAMESPACE + "Version", new Version());
    commandMap.put(Utils.ROOT_NAMESPACE + "Data", new Data());
    commandMap.put(Utils.ROOT_NAMESPACE + "IsConnected", new IsConnected());
    commandMap.put(Utils.ROOT_NAMESPACE + "IsAdmin", new IsAdmin());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetId", new GetId());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetName", new GetName());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetTitle", new GetTitle());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetNotes", new GetNotes());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetSource", new GetSource());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetSourceId", new GetSourceId());
    commandMap.put(Utils.ROOT_NAMESPACE + "Sum", new Sum());
    commandMap.put(Utils.ROOT_NAMESPACE + "Change", new Change());
    commandMap.put(Utils.ROOT_NAMESPACE + "PChange", new PChange());
    commandMap.put(Utils.ROOT_NAMESPACE + "Max", new es.command.Max());
    commandMap.put(Utils.ROOT_NAMESPACE + "Min", new es.command.Min());
    commandMap.put(Utils.ROOT_NAMESPACE + "Get", new Get());
    commandMap.put(Utils.ROOT_NAMESPACE + "Defined", new Defined());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetType", new GetType());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetUnits", new GetUnits());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetFrequency", new GetFrequency());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetUnitsShort", new GetUnitsShort());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetFrequencyShort", new GetFrequencyShort());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetUnits", new SetUnits());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetFrequency", new SetFrequency());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetUnitsShort", new SetUnitsShort());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetFrequencyShort", new SetFrequencyShort());
    commandMap.put(Utils.ROOT_NAMESPACE + "Stdev", new StdDev());
    commandMap.put(Utils.ROOT_NAMESPACE + "GGet", new GGet());
    commandMap.put(Utils.ROOT_NAMESPACE + "DlgInput", new DlgInput());
    commandMap.put(Utils.ROOT_NAMESPACE + "DlgMessage", new DlgMessage());
    commandMap.put(Utils.ROOT_NAMESPACE + "ParseFloat", new ParseFloat());
    commandMap.put(Utils.ROOT_NAMESPACE + "ParseInt", new ParseInt());
    commandMap.put(Utils.ROOT_NAMESPACE + "DlgConfirm", new DlgConfirm());
    commandMap.put(Utils.ROOT_NAMESPACE + "Equals", new Equals());
    commandMap.put(Utils.ROOT_NAMESPACE + "NotEquals", new NotEquals());
    commandMap.put(Utils.ROOT_NAMESPACE + "Printf", new Printf());
    commandMap.put(Utils.ROOT_NAMESPACE + "SetLogLevel", new SetLogLevel());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetSeriesType", new GetSeriesType());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetEnv", new GetEnv());
    commandMap.put(Utils.ROOT_NAMESPACE + "SubString", new SubString());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetLength", new GetLength());
    commandMap.put(Utils.ROOT_NAMESPACE + "Iterate", new Iterate());
    commandMap.put(Utils.ROOT_NAMESPACE + "SystemAlias", new SystemAlias());
    commandMap.put(Utils.ROOT_NAMESPACE + "UserAlias", new UserAlias());
    commandMap.put(Utils.ROOT_NAMESPACE + "Functions", new Functions());
    commandMap.put(Utils.ROOT_NAMESPACE + "GPut", new GPut());
    commandMap.put(Utils.ROOT_NAMESPACE + "Random", new Random());
    commandMap.put(Utils.ROOT_NAMESPACE + "GetDate", new GetDate());
    if (Settings.getInstance().isTestMode()) {
      commandMap.put(Utils.ROOT_NAMESPACE + "Collapse", new Collapse());
      commandMap.put(Utils.ROOT_NAMESPACE + "Normalize", new Normalize());
      commandMap.put(Utils.ROOT_NAMESPACE + "GetOffset", new GetOffset());
      commandMap.put(Utils.ROOT_NAMESPACE + "Fonts", new Fonts());
      commandMap.put(Utils.ROOT_NAMESPACE + "Qdb", new QDB());
      commandMap.put(Utils.ROOT_NAMESPACE + "Qtp", new QTP());
    }
  }

  public Map<String, Command> getCommandMap() {
    return commandMap;
  }

  public Map<String, String> getAliasMap() {
    return aliasMap;
  }

  public boolean isSystemFunction(String funcName) {
    // Note: we do not check for aliases
    return commandMap.keySet().contains(funcName);
  }
  
  public Object invokeFunction(String funcName, SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (commandMap.get(funcName) != null) {
      return invokeSystemFunction(funcName, symbolTable, file, params);
    } else {
      return invokeUserFunction(funcName, symbolTable, file, params);
    }
  }

  private Object invokeUserFunction(String funcName, SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (symbolTable.get(funcName) == null || !(symbolTable.get(funcName).getValue() instanceof FunctionDeclaration)) {
      throw new Exception("symbol not found or symbol is not a function: " + funcName);
    }
    FunctionDeclaration functionDeclaration = (FunctionDeclaration) symbolTable.get(funcName).getValue();
    if (params.size() > functionDeclaration.getParams().size()) {
      throw new Exception("calling function with too many parameters: " + funcName);
    }
    while (params.size() < functionDeclaration.getParams().size()) {
      params.add(null);
    }
    Utils.ASSERT(functionDeclaration.getParams().size() == params.size(), "function parmeters size mismatch");
    SymbolTable childSymbolTable = new SymbolTable(symbolTable);
    for (int i = 0; i < params.size(); i++) {
      childSymbolTable.put(functionDeclaration.getParams().get(i), new Symbol(functionDeclaration.getParams().get(i), params.get(i)));
    }
    ESIterator<Statement> itr = new ESIterator<>(functionDeclaration.getStatements());
    if (itr.hasNext()) {
      Evaluator e = new Evaluator(childSymbolTable);
      Statement statement = itr.next();
      return e.evaluate(statement, itr);
    }
    return null;
  }
  
  private Object invokeSystemFunction(String funcName, SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    return commandMap.get(funcName).run(symbolTable, file, params);
  }
}
