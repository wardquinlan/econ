package es.command;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import es.core.TimeSeries;
import es.core.Utils;
import es.gui.Chart;
import es.gui.Context;
import es.gui.Frame;
import es.gui.Lock;
import es.gui.Panel;
import es.gui.Series;
import es.gui.XMLParser;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class Plot implements Command {
  @Override
  public String getSummary() {
    return "void    plot(Object object[, int dxincr[, int frequency]]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Plots a series or a context, where 'object' is:");
    list.add("  - a Series");
    list.add("  - a context file name");
    list.add("");
    list.add("Note that dxincr must be > 0 and that frequency must be 0 <= frequency <= 3, namely:");
    list.add("  - NONE (0)");
    list.add("  - DAYS (1)");
    list.add("  - MONTHS (2)");
    list.add("  - YEARS (3)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 3);
    Symbol symbol = symbolTable.get("settings.loaded");
    if (symbol == null || !((Boolean) symbol.getValue())) {
      throw new Exception("settings not loaded");
    }
    symbol = symbolTable.get("defaults.loaded");
    if (symbol == null || !((Boolean) symbol.getValue())) {
      throw new Exception("defaults not loaded");
    }
    
    Context ctx;
    if (params.get(0) instanceof String) {
      Utils.validate(params, 1, 1);
      String filename = (String) params.get(0);
      XMLParser xmlParser;
      if (Paths.get(filename).isAbsolute()) {
        xmlParser = new XMLParser(new File(filename), 0, symbolTable);
      } else {
        String basename = Paths.get(file.getAbsolutePath()).getParent().toString();
        xmlParser = new XMLParser(new File(basename + File.separator + filename), 0, symbolTable);
      }
      ctx = xmlParser.parse();
    } else if (params.get(0) instanceof TimeSeries) {
      TimeSeries timeSeries = (TimeSeries) params.get(0);
      Series series = new Series(symbolTable);
      series.setType(Series.LINE); // TODO: in theory could support bar series - can look at series data type
      series.setTimeSeries(timeSeries);
      Chart chart = new Chart(symbolTable);
      chart.setLabel(Utils.stringWithNULL(timeSeries.getName()));
      chart.getSeries().add(series);
      Panel panel = new Panel(symbolTable);
      panel.setLabel(Utils.stringWithNULL(timeSeries.getName()));
      panel.getCharts().add(chart);
      if (params.size() >= 2) {
        if (params.get(1) instanceof Integer) {
          int dxIncr = (Integer) params.get(1);
          if (dxIncr <= 0) {
            throw new Exception("invalid dxincr: " + dxIncr);
          }
          panel.setDxIncr(dxIncr);
        } else {
          throw new Exception("argument must be an int: " + params.get(1));
        }
      }
      if (params.size() == 3) {
        if (params.get(2) instanceof Integer) {
          int frequency = (Integer) params.get(2);
          if (frequency != Panel.FREQUENCY_NONE && frequency != Panel.FREQUENCY_DAYS && frequency != Panel.FREQUENCY_MONTHS && frequency != Panel.FREQUENCY_YEARS) {
            throw new Exception("invalid frequency: " + frequency);
          }
          panel.setFrequency(frequency);
        } else {
          throw new Exception("argument must be an int: " + params.get(2));
        }
      }
      ctx = new Context(symbolTable);
      ctx.getPanels().add(panel);
    } else {
      throw new Exception("argument must be a String or a Series: " + params.get(0));
    }
    new Frame(ctx);
/*    
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
          log.debug("closing window");
          synchronized (Lock.instance()) {
            Lock.instance().notify();
          }
      }
    });

    synchronized (Lock.instance()) {
      Lock.instance().wait();
    }
*/    
    return null;
  }
}
