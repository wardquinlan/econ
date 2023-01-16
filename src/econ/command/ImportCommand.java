package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.Symbol;

public class ImportCommand implements Command {
  @Override
  public String getSummary() {
    return "Series import(\"QTEMPLATE\", String templateFilePath, String name);\n" +
           "Series import(\"QDB\", String dbFilePath);\n" +
           "Series import(\"FRED\", String id);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Series";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() == 0) {
      throw new Exception("missing argument(s)");
    }
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception("argument not a string");
    }
    
    String source = (String) params.remove(0);
    Importer importer;
    if (source.equals("QTEMPLATE")) {
      importer = new QTemplateImporter();
    } else if (source.equals("QDB")) {
      importer = new QDBImporter();
    } else {
      throw new Exception("unsupported import source: " + source);
    }
    return importer.run(symbolTable, file, params);
  }
}
