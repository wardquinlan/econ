package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.FunctionDeclaration;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class Functions implements Command {
  
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Functions();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Displays a list of user-defined functions");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    Set<String> valueSet = new TreeSet<>();
    Set<String> aliasSet = new TreeSet<>();
    
    for(String key: symbolTable.keySet()) {
      Symbol symbol = symbolTable.get(key);
      if (symbol.getValue() instanceof FunctionDeclaration) {
        FunctionDeclaration decl = (FunctionDeclaration) symbol.getValue();
        if (key.equals(decl.getName())) {
          valueSet.add(decl.toString() + ";");
        } else {
          aliasSet.add(key + " -> " + decl.toString());
        }
      }
    }
    for (String fn: valueSet) {
      System.out.println(fn);
    }
    System.out.println();
    for (String fn: aliasSet) {
      System.out.println(fn);
    }
    System.out.println();
    FunctionCaller.getInstance().alias();
    return null;
  }
}
