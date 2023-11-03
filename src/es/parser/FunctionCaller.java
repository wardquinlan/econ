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
import es.command.ToString;
import es.command.Today;
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
    commandMap.put(":Exit", new Exit());
    commandMap.put(":Print", new Print());
    commandMap.put(":Help", new Help());
    commandMap.put(":Load", new Load());
    commandMap.put(":Plot", new Plot());
    commandMap.put(":Ds", new Ds());
    commandMap.put(":Meta", new Meta());
    commandMap.put(":Cat", new Cat());
    commandMap.put(":Create", new Create());
    commandMap.put(":Insert", new Insert());
    commandMap.put(":GetSize", new GetSize());
    commandMap.put(":Connect", new Connect());
    commandMap.put(":Save", new Save());
    commandMap.put(":SetId", new SetId());
    commandMap.put(":SetTitle", new SetTitle());
    commandMap.put(":SetNotes", new SetNotes());
    commandMap.put(":SetName", new SetName());
    commandMap.put(":Today", new Today());
    commandMap.put(":Fred", new FRED());
    commandMap.put(":Timestamp", new Timestamp());
    commandMap.put(":Ln", new Ln());
    commandMap.put(":Log", new es.command.Log());
    commandMap.put(":Date", new es.command.Date());
    commandMap.put(":Average", new Average());
    commandMap.put(":Merge", new Merge());
    commandMap.put(":Delete", new Delete());
    commandMap.put(":Update", new Update());
    commandMap.put(":Drop", new Drop());
    commandMap.put(":SetSourceId", new SetSourceId());
    commandMap.put(":SetSource", new SetSource());
    commandMap.put(":Status", new Status());
    commandMap.put(":Version", new Version());
    commandMap.put(":Data", new Data());
    commandMap.put(":IsConnected", new IsConnected());
    commandMap.put(":IsAdmin", new IsAdmin());
    commandMap.put(":GetId", new GetId());
    commandMap.put(":GetName", new GetName());
    commandMap.put(":GetTitle", new GetTitle());
    commandMap.put(":GetNotes", new GetNotes());
    commandMap.put(":GetSource", new GetSource());
    commandMap.put(":GetSourceId", new GetSourceId());
    commandMap.put(":Sum", new Sum());
    commandMap.put(":Change", new Change());
    commandMap.put(":PChange", new PChange());
    commandMap.put(":Max", new es.command.Max());
    commandMap.put(":Min", new es.command.Min());
    commandMap.put(":Get", new Get());
    commandMap.put(":Defined", new Defined());
    commandMap.put(":GetType", new GetType());
    commandMap.put(":GetUnits", new GetUnits());
    commandMap.put(":GetFrequency", new GetFrequency());
    commandMap.put(":GetUnitsShort", new GetUnitsShort());
    commandMap.put(":GetFrequencyShort", new GetFrequencyShort());
    commandMap.put(":SetUnits", new SetUnits());
    commandMap.put(":SetFrequency", new SetFrequency());
    commandMap.put(":SetUnitsShort", new SetUnitsShort());
    commandMap.put(":SetFrequencyShort", new SetFrequencyShort());
    commandMap.put(":Stdev", new StdDev());
    commandMap.put(":GGet", new GGet());
    commandMap.put(":DlgInput", new DlgInput());
    commandMap.put(":DlgMessage", new DlgMessage());
    commandMap.put(":ParseFloat", new ParseFloat());
    commandMap.put(":ParseInt", new ParseInt());
    commandMap.put(":DlgConfirm", new DlgConfirm());
    commandMap.put(":Equals", new Equals());
    commandMap.put(":NotEquals", new NotEquals());
    commandMap.put(":Printf", new Printf());
    commandMap.put(":SetLogLevel", new SetLogLevel());
    commandMap.put(":GetSeriesType", new GetSeriesType());
    commandMap.put(":GetEnv", new GetEnv());
    commandMap.put(":SubString", new SubString());
    commandMap.put(":GetLength", new GetLength());
    commandMap.put(":Iterate", new Iterate());
    commandMap.put(":SystemAlias", new SystemAlias());
    commandMap.put(":UserAlias", new UserAlias());
    commandMap.put(":Functions", new Functions());
    commandMap.put(":GPut", new GPut());
    commandMap.put(":Random", new Random());
    commandMap.put(":ToString", new ToString());
    if (Settings.getInstance().isTestMode()) {
      commandMap.put(":Collapse", new Collapse());
      commandMap.put(":Normalize", new Normalize());
      commandMap.put(":GetOffset", new GetOffset());
      commandMap.put(":Fonts", new Fonts());
      commandMap.put(":Qdb", new QDB());
      commandMap.put(":Qtp", new QTP());
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
