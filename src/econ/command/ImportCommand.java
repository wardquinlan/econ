package econ.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import econ.importers.FREDImporter;
import econ.importers.Importer;
import econ.importers.QDBImporter;
import econ.importers.QTemplateImporter;
import econ.parser.Symbol;

public class ImportCommand implements Command {
  @Override
  public String getSummary() {
    return "Series import(\"QTEMPLATE\", String templateFilePath, String sourceId);\n" +
           "Series import(\"QDB\", String dbFilePath);\n" +
           "Series import(\"FRED\", String sourceId[, String units]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Imports data from one of the following external sources:");
    list.add("");
    list.add("  QTEMPLATE: a Quote template file");
    list.add("  QBD      : a Quote data file");
    list.add("  FRED     : a FRED series");
    list.add("");
    list.add("With:");
    list.add("");
    list.add("  'templateFilePath' is the path to a Quote template file");
    list.add("  'sourceId' is the sourceId (series name) from the external source");
    list.add("  'dbFilePath' is the path to a Quote data file");
    list.add("  'units' is the units");
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
    } else if (source.equals("FRED")) {
      importer = new FREDImporter(symbolTable);
    } else {
      throw new Exception("unsupported import source: " + source);
    }
    return importer.run(file, params);
  }
}
