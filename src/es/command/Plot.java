package es.command;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import es.core.Settings;
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
    return "void    ES:Plot(Object object...);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Plots a series or a context, where 'object' is:");
    list.add("  - a Series");
    list.add("  - a context file name");
    list.add("");
    list.add("Multiple Series may be plotted at once (currently up to 4)");
    return list;
  }
  
  @Override
  public String getReturns() {
    return null;
  }
  
  @Override
  public Object run(SymbolTable symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 4);
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
    } else {
      Chart chart = new Chart(symbolTable);
      chart.setLabel((String) symbolTable.get("defaults.chart.label").getValue());
      for (int i = 0; i < params.size(); i++) {
        if (!(params.get(i) instanceof TimeSeries)) {
          log.info("argument is not a Series, ignoring: " + params.get(i));
          continue;
        }
        TimeSeries timeSeries = (TimeSeries) params.get(i);
        Series series = new Series(symbolTable);
        switch (timeSeries.getType()) {
          case TimeSeries.FLOAT:
            series.setType(Series.LINE);
            break;
          case TimeSeries.BOOLEAN:
            series.setType(Series.BACKGROUND);
            break;
          default:
            throw new Exception("cannot plot this type of series: " + timeSeries.getType());
        }
        series.setColor(new Color((int) symbolTable.get("defaults.series.linecolor" + i).getValue()));
        series.setTimeSeries(timeSeries);
        chart.getSeries().add(series);
      }
      Panel panel = new Panel(symbolTable);
      panel.setLabel((String) symbolTable.get("defaults.panel.label").getValue());
      panel.getCharts().add(chart);
      ctx = new Context(symbolTable);
      ctx.getPanels().add(panel);
    }
    JFrame frame = new Frame(ctx);
    if (Settings.getInstance().testFunctions()) {
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
    }
    return null;
  }
}
