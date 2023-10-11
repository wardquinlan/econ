package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class GPut implements Command {
  @Override
  public String getSummary() {
    return "void    gPut(String symbolName, Object value);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Creates symbol with name 'symbolName' and value 'value' in the global scope");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 2, 2);
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a String");
    }
    String symbolName = (String) params.get(0);
    validateSymbolName(symbolName);
    Object value = params.get(1);
    symbolTable.globalPut(symbolName, new Symbol(symbolName, value));
    return null;
  }
  
  private void validateSymbolName(String symbolName) throws Exception {
    if (symbolName.length() == 0) {
      throw new Exception("invalid symbol name: empty string");
    }
    if (!Character.isLetter(symbolName.charAt(0))) {
      throw new Exception("invalid symbol name: must start with a letter");
    }
    for (int i = 1; i < symbolName.length(); i++) {
      char ch = symbolName.charAt(i);
      if (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '_' && ch != '.') {
        throw new Exception("invalid symbol name: invalid character: " + ch);
      }
    }
  }
}
