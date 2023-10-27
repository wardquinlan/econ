package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Printf implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Printf(String format[, object...]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("C-style printf: prints output according to 'format'");
    return list;
  }

  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() == 0) {
      throw new Exception("missing argument(s)");
    }
    if (!(params.get(0) instanceof String)) {
      throw new Exception("'format' must be a string");
    }
    String format = (String) params.remove(0);
    System.out.printf(format, params.toArray());
    return null;
  }
}
