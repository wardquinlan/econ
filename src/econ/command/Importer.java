package econ.command;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.Symbol;
import econ.TimeSeries;

public interface Importer {
  public static Log log = LogFactory.getFactory().getInstance(Importer.class);
  
  public TimeSeries run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception;
}