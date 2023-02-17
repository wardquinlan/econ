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

public class Plot implements Command {
  @Override
  public String getSummary() {
    return "int    plot(Object object);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Plots a series or a context, where 'object' is:");
    list.add("  - a Series");
    list.add("  - a context file name");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    Utils.validate(params, 1, 1);
    Context ctx;
    if (params.get(0) instanceof String) {
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
      chart.setSpan(100);
      chart.setLabel(Utils.stringWithNULL(timeSeries.getName()));
      chart.getSeries().add(series);
      Panel panel = new Panel(symbolTable);
      panel.setLabel(Utils.stringWithNULL(timeSeries.getName()));
      panel.getCharts().add(chart);
      ctx = new Context(symbolTable);
      ctx.getPanels().add(panel);
    } else {
      throw new Exception("invalid argument: " + params.get(0));
    }
    
    JFrame frame = new Frame(ctx);
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
    
    return 0;
  }
}