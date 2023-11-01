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
    list.add("Lists user functions and user/system aliases");
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
          aliasSet.add(key + "() -> " + decl.toString());
        }
      }
    }
    System.out.println("User-Defined Functions");
    System.out.println("----------------------");
    for (String fn: valueSet) {
      System.out.println(fn);
    }
    System.out.println();
    System.out.println("(Note: to see the list of system functions, just type ':Help();')");
    System.out.println();
    System.out.println("Aliases to User-Defined Functions");
    System.out.println("---------------------------------");
    for (String fn: aliasSet) {
      System.out.println(fn);
    }
    System.out.println();
    System.out.println("Aliases to System Functions");
    System.out.println("---------------------------");
    alias();
    return null;
  }

  public void alias() {
    for (String key: FunctionCaller.getInstance().getAliasMap().keySet()) {
      System.out.println(key + "() -> " + FunctionCaller.getInstance().getAliasMap().get(key) + "()");
    }
  }
}