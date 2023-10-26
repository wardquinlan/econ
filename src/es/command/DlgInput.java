package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import es.core.Utils;
import es.parser.SymbolTable;

public class DlgInput implements Command {
  @Override
  public String getSummary() {
    return "String  ES:DlgInput([String prompt]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Shows a dialog box requesting user input, prompting the user with 'prompt'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "User input string if OK is selected; otherwise null";
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 1);
    String prompt = "Enter input:";
    if (params.size() > 0) {
      if (!(params.get(0) instanceof String)) {
        throw new Exception("invalid message");
      }
      prompt = (String) params.get(0);
    }
    return JOptionPane.showInputDialog(prompt);
  }
}
