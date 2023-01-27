package econ.command;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import econ.core.TimeSeries;
import econ.core.Utils;
import econ.gui.Chart;
import econ.gui.Context;
import econ.gui.Frame;
import econ.gui.Lock;
import econ.gui.Panel;
import econ.gui.Series;
import econ.gui.XMLParser;
import econ.parser.Symbol;

public class PlotCommand implements Command {
  @Override
  public String getSummary() {
    return "int plot(String filename);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Plots series as defined in the context file 'filename'");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "0";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (params.size() > 1) {
      throw new Exception("too many arguments");
    }
    
    if (params.size() == 0) {
      throw new Exception("missing argument");
    }

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
