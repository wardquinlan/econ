package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.FunctionCaller;
import es.parser.SymbolTable;

public class SystemAlias implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "SystemAlias(String alias, String systemFunction);";
  }

  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates the system alias 'alias' to ES System Function 'systemFunction'");
    return list;
  }

  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() == 0) {
      alias();
      return null;
    }
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    if (!(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a String");
    }
    alias((String) params.get(0), (String) params.get(1));
    return null;
  }

  public void alias() {
    for (String key: FunctionCaller.getInstance().getAliasMap().keySet()) {
      System.out.println(key + "() -> " + FunctionCaller.getInstance().getAliasMap().get(key) + "()");
    }
  }
  
  public void alias(String alias, String name) throws Exception {
    Command cmd = FunctionCaller.getInstance().getCommandMap().get(name);
    if (cmd == null) {
      throw new Exception("system function not found: " + name);
    }
    Utils.checkNameSpace(alias);
    Utils.validateRootNameSpaceWrite(alias);
    FunctionCaller.getInstance().getCommandMap().put(alias, cmd);
    FunctionCaller.getInstance().getAliasMap().put(alias, name);
  }
}
