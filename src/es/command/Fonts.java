package es.command;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.core.Utils;
import es.parser.SymbolTable;

public class Fonts implements Command {
  @Override
  public String getSummary() {
    return "void    " + Utils.ROOT_NAMESPACE + "Fonts();";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Lists system font names");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 0, 0);
    Font fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    for (Font font: fonts) {
      System.out.println(font.getFontName());
    }  
    return null;
  }
}
