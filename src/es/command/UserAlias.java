package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import es.core.Utils;
import es.parser.FunctionDeclaration;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class UserAlias implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "UserAlias();";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Lists all user aliases.");
    return list;
  }

  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    Set<String> aliasSet = new TreeSet<>();
    
    for(String key: symbolTable.keySet()) {
      Symbol symbol = symbolTable.get(key);
      if (symbol.getValue() instanceof FunctionDeclaration) {
        FunctionDeclaration decl = (FunctionDeclaration) symbol.getValue();
        if (!key.equals(decl.getName())) {
          aliasSet.add((symbol.isConstant() ? "const " : "") + key + "() -> " + decl.toString());
        }
      }
    }
    for (String fn: aliasSet) {
      System.out.println(fn);
    }
    System.out.println();
    return null;
  }
}
