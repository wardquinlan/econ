package econ.command;

import java.io.File;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public abstract class Command {
  protected String name;

  public abstract String getSummary();
  public abstract List<String> getDetails();
  public abstract String getReturns();
  
  public abstract Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception;
  
  public Command(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
