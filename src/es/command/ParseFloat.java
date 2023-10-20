package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import es.core.Utils;
import es.parser.SymbolTable;

public class ParseFloat implements Command {
  @Override
  public String getSummary() {
    return "void    parseFloat(String value);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Parses 'value', a string representation of a float");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "floating-point value of 'value'";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    if (!(params.get(0) instanceof String)) {
      throw new Exception("invalid value");
    }
    return Float.parseFloat((String) params.get(0));
  }
}
