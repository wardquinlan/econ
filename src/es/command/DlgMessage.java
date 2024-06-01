package es.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import es.core.Utils;
import es.parser.SymbolTable;

public class DlgMessage implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "DlgMessage(String message[, int messageType]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Shows a dialog box displaying the message 'message', where 'messageType' is one of:");
    list.add("  - INFO (1) (default)");
    list.add("  - WARN (2)");
    list.add("  - ERROR (3)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 2);
    if (!(params.get(0) instanceof String)) {
      throw new Exception("invalid message");
    }
    String message = (String) params.get(0);
    int messageType = JOptionPane.INFORMATION_MESSAGE;
    if (params.size() == 2) {
      if (!(params.get(1) instanceof Integer)) {
        throw new Exception("invalid message type");
      }
      int tmp = (int) params.get(1);
      if (tmp == 0 || tmp == 1 || tmp == 2) {
        messageType = JOptionPane.INFORMATION_MESSAGE;
      } else if (tmp == 3) {
        messageType = JOptionPane.WARNING_MESSAGE;
      } else if (tmp == 4) {
        messageType = JOptionPane.ERROR_MESSAGE;
      } else {
        throw new Exception("invalid message type");
      }
    }
    JOptionPane.showMessageDialog(null, message, "Message", messageType);
    return null;
  }
}
