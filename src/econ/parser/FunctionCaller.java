package econ.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.command.AssertCommand;
import econ.command.CatCommand;
import econ.command.CollapseCommand;
import econ.command.Command;
import econ.command.ConnectCommand;
import econ.command.CreateCommand;
import econ.command.DataCommand;
import econ.command.ExitCommand;
import econ.command.FREDCommand;
import econ.command.HelpCommand;
import econ.command.ImportCommand;
import econ.command.InsertCommand;
import econ.command.LsCommand;
import econ.command.FontsCommand;
import econ.command.LoadCommand;
import econ.command.MetaCommand;
import econ.command.NormalizeCommand;
import econ.command.PlotCommand;
import econ.command.PrintCommand;
import econ.command.QDBCommand;
import econ.command.QTPCommand;
import econ.command.SaveCommand;
import econ.command.SetIdCommand;
import econ.command.SetTitleCommand;
import econ.command.SizeCommand;
import econ.command.TodayCommand;
import econ.core.TimeSeries;
import econ.core.TimeSeriesDAO;
import econ.core.Utils;

public class FunctionCaller {
  private static Log log = LogFactory.getFactory().getInstance(FunctionCaller.class);
  private Map<String, Command> commandMap = new TreeMap<>();

  static final int TIME_SERIES_COL_WIDTHS[] = {5, 20, 30, 12, 30};
  static final int TIME_SERIES_DATA_COL_WIDTHS[] = {5, 10, 10};

  public FunctionCaller() {
    commandMap.put("exit", new ExitCommand());
    commandMap.put("print", new PrintCommand());
    commandMap.put("fonts", new FontsCommand());
    commandMap.put("help", new HelpCommand());
    commandMap.put("load", new LoadCommand());
    commandMap.put("plot", new PlotCommand());
    commandMap.put("ls", new LsCommand());
    commandMap.put("data", new DataCommand());
    commandMap.put("meta", new MetaCommand());
    commandMap.put("cat", new CatCommand());
    commandMap.put("create", new CreateCommand());
    commandMap.put("insert", new InsertCommand());
    commandMap.put("collapse", new CollapseCommand());
    commandMap.put("normalize", new NormalizeCommand());
    commandMap.put("size", new SizeCommand());
    commandMap.put("assert", new AssertCommand());
    commandMap.put("connect", new ConnectCommand());
    commandMap.put("save", new SaveCommand());
    commandMap.put("setId", new SetIdCommand());
    commandMap.put("setTitle", new SetTitleCommand());
    commandMap.put("today", new TodayCommand());
    commandMap.put("fred", new FREDCommand());
    commandMap.put("qdb", new QDBCommand());
    commandMap.put("qtp", new QTPCommand());
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
  
  private Object insert(List<Object> params) throws Exception {
    if (params.size() > 3) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() < 3) {
      throw new Exception("missing argument(s)");
    }

    List<Object> params2 = new ArrayList<>();
    params2.add(params.get(0));
    TimeSeries timeSeries = (TimeSeries) Utils.load(params2);
    if (timeSeries == null) {
      throw new Exception("time series not found: " + params.get(0));
    }
    
    Date date = Utils.DATE_FORMAT.parse((String) params.get(1));
    float value;
    if (params.get(2) instanceof Integer) {
      value = ((Integer) params.get(2)).floatValue();
    } else if (params.get(2) instanceof Float) {
      value = (Float) params.get(2);
    } else {
      throw new Exception("invalid value: " + params.get(2));
    }
    TimeSeriesDAO.getInstance().insertSeriesData(timeSeries.getId(), date, value);
    return 0;
  }
  
  private Object delete(Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    Symbol symbol = symbolTable.get("settings.confirm");
    if (symbol == null || !symbol.getValue().equals(1)) {
      throw new Exception("settings.confirm != 1");
    }
    
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument(s)");
    }

    List<Object> params2 = new ArrayList<>();
    params2.add(params.get(0));
    TimeSeries timeSeries = (TimeSeries) Utils.load(params2);
    if (timeSeries == null) {
      log.warn("time series not found: " + params.get(0));
      return 0;
    }
    
    Date date = null;
    if (params.size() == 2) {
      date = Utils.DATE_FORMAT.parse((String) params.get(1));
      TimeSeriesDAO.getInstance().deleteSeriesData(timeSeries.getId(), date);
    } else {
      TimeSeriesDAO.getInstance().deleteSeries(timeSeries.getId());
    }
    
    return 0;
  }
  
  private Object create(List<Object> params) throws Exception {
    if (params.size() < 4) {
      throw new Exception("missing argument(s)");
    }

    if (params.size() > 5) {
      throw new Exception("too many arguments");
    }
    
    TimeSeries timeSeries = new TimeSeries();
    timeSeries.setId((Integer) params.get(0));
    timeSeries.setName((String) params.get(1));
    timeSeries.setTitle((String) params.get(2));
    timeSeries.setSource((String) params.get(3));
    if (params.size() == 5) {
      timeSeries.setSourceId((String) params.get(4));
    }
    TimeSeriesDAO.getInstance().createSeries(timeSeries);
    return 0;
  }
  
  private Object printData(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("not a series: " + params.get(0));
    }
    
    TimeSeries timeSeries  = (TimeSeries) params.get(0);
    System.out.println(timeSeries.toStringVerbose());
    return timeSeries;
  }

  private Object importData(Map<String, Symbol> symbolTable, List<Object> params) throws Exception {
    System.out.println("TODO: IMPORT DATA");
    Symbol symbol = symbolTable.get("settings.confirm");
    if (symbol == null || !symbol.getValue().equals(1)) {
      throw new Exception("settings.confirm != 1");
    }
    return 0;
  }
  
  private Object getNotes(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    if (!(params.get(0) instanceof TimeSeries)) {
      throw new Exception("not a series: " + params.get(0));
    }
    
    TimeSeries timeSeries = (TimeSeries) params.get(0);
    return timeSeries.getNotes();
  }
  
  private Object notes(List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }
    
    TimeSeries timeSeries = (TimeSeries) Utils.load(params);
    if (timeSeries == null) {
      throw new Exception("time series not found: " + params.get(0));
    }

    System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", "Id", "Name", "Title", "Source Org", "Source Name");
    System.out.printf(Utils.generateUnderlineString(TIME_SERIES_COL_WIDTHS) + "\n");
    System.out.printf(Utils.generateFormatString(TIME_SERIES_COL_WIDTHS) + "\n", 
        timeSeries.getId().toString(), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 1, timeSeries.getName()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 2, timeSeries.getTitle()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 3, timeSeries.getSource()), 
        Utils.generateTruncatedData(TIME_SERIES_COL_WIDTHS, 4, Utils.stringWithNULL(timeSeries.getSourceId())));
    System.out.println();
    System.out.println(timeSeries.getNotes() == null ? "NULL" : timeSeries.getNotes());
    return 0;
  }
  
}
