package es.gui;

import java.awt.Color;
import java.io.File;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.core.TimeSeries;
import es.core.Utils;
import es.parser.Symbol;
import es.parser.SymbolTable;

public class XMLParser {
  public static final int MAX_LEVEL = 8;
  private File file;
  private int level;
  private String basename;
  private SymbolTable symbolTable;
  
  public XMLParser(File file, int level, SymbolTable symbolTable) throws Exception {
    if (level > MAX_LEVEL) {
      throw new Exception("exceeds maximum include level");
    }

    this.file = file;
    this.level = level;
    this.basename = Paths.get(file.getAbsolutePath()).getParent().toString();
    this.symbolTable = symbolTable;
  }
  
  public Context parse() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(file);
    doc.getDocumentElement().normalize();
    Element root = doc.getDocumentElement();
    if (!root.getNodeName().equals("context")) {
      throw new Exception("unexpected root node: " + root.getNodeName());
    }
    
    Context ctx = new Context(symbolTable);
    NamedNodeMap map = doc.getDocumentElement().getAttributes();
    if (map.getLength() > 0) {
      throw new Exception("unexpected context attribute(s)");
    }

    NodeList nodeList = doc.getDocumentElement().getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        if (node.getNodeName().equals("panel")) {
          ctx.getPanels().add(parsePanel(node));
        } else if (node.getNodeName().equals("include")) {
          Context ctx2 = parseInclude(basename, node, level);
          for (Panel panel: ctx2.getPanels()) {
            ctx.getPanels().add(panel);
          }
        } else {
          throw new Exception("unexpected context element:" + node.getNodeName());
        }
      }
    }
    
    return ctx;
  }
  
  private Context parseInclude(String basename, Node node, int level) throws Exception {
    String filename = null;
    NamedNodeMap map = node.getAttributes();
    for (int i = 0; i < map.getLength(); i++) {
      Node attribute = map.item(i);
      if (attribute.getNodeName().equals("name")) {
        filename = attribute.getNodeValue();
      } else {
        throw new Exception("unexpected include attribute: " + attribute.getNodeName());
      }
    }
    
    if (filename == null) {
      throw new Exception("missing include name attribute");
    }
    
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node2 = nodeList.item(i);
      if (node2.getNodeType() == Node.ELEMENT_NODE) {
        throw new Exception("unexpected include element: " + node2.getNodeName());
      }
    }
    
    XMLParser parser;
    if (Paths.get(filename).isAbsolute()) {
      parser = new XMLParser(new File(filename), level + 1, symbolTable);
    } else {
      parser = new XMLParser(new File(basename + File.separator + filename), level + 1, symbolTable);
    }
    return parser.parse();
  }
  
  private Panel parsePanel(Node node) throws Exception {
    Panel panel = new Panel(symbolTable);
    NamedNodeMap map = node.getAttributes();
    for (int i = 0; i < map.getLength(); i++) {
      Node attribute = map.item(i);
      if (attribute.getNodeName().equals("label")) {
        panel.setLabel(attribute.getNodeValue());
      } else if (attribute.getNodeName().equals("backgroundcolor")) {
        panel.setBackgroundColor(parseColorAttribute(attribute));
      } else if (attribute.getNodeName().equals("dxincr")) {
        panel.setDxIncr(parseIntAttribute(attribute));
      } else if (attribute.getNodeName().equals("gridlinetextwidth")) {
        panel.setGridLineTextWidth(parseIntAttribute(attribute));
      } else if (attribute.getNodeName().equals("fontname")) {
        panel.setFontName(attribute.getNodeValue());
      } else if (attribute.getNodeName().equals("fontcolor")) {
        panel.setFontColor(parseColorAttribute(attribute));
      } else if (attribute.getNodeName().equals("fontsize")) {
        panel.setFontSize(parseIntAttribute(attribute));
      } else if (attribute.getNodeName().equals("frequency")) {
        panel.setFrequency(parseFrequencyAttribute(attribute));
      } else {
        throw new Exception("unexpected panel attribute: " + attribute.getNodeName());
      }
    }
    
    if (panel.getLabel() == null) {
      throw new Exception("missing panel label attribute");
    }
    
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node2 = nodeList.item(i);
      if (node2.getNodeType() == Node.ELEMENT_NODE) {
        if (node2.getNodeName().equals("chart")) {
          panel.getCharts().add(parseChart(node2));
        } else {
          throw new Exception("unexpected panel element: " + node2.getNodeName());
        }
      }
    }
    
    int spanTotal = 0;
    for (Chart chart: panel.getCharts()) {
      spanTotal += chart.getSpan();
    }
    if (spanTotal != 100) {
      throw new Exception("chart span attributes do not sum to 100: " + panel.getLabel());
    }
    
    return panel;
  }
  
  private Chart parseChart(Node node) throws Exception {
    Chart chart = new Chart(symbolTable);
    boolean gridlinesFound = false;
    NamedNodeMap map = node.getAttributes();
    for (int i = 0; i < map.getLength(); i++) {
      Node attribute = map.item(i);
      if (attribute.getNodeName().equals("label")) {
        chart.setLabel(attribute.getNodeValue());
      } else if (attribute.getNodeName().equals("backgroundcolor")) {
        chart.setBackgroundColor(parseColorAttribute(attribute));
      } else if (attribute.getNodeName().equals("linecolor")) {
        chart.setLineColor(parseColorAttribute(attribute));
      } else if (attribute.getNodeName().equals("rectcolor")) {
        chart.setRectColor(parseColorAttribute(attribute));
      } else if (attribute.getNodeName().equals("ngridlines")) {
        chart.setNGridLines(parseIntAttribute(attribute));
      } else if (attribute.getNodeName().equals("gridlines")) {
        parseGridlineAttribute(chart, attribute);
        gridlinesFound = true;
      } else if (attribute.getNodeName().equals("span")) {
        int span = parseIntAttribute(attribute);
        if (span < 1 || span > 100) {
          throw new Exception("chart span attribute out of bounds: " + span);
        }
        chart.setSpan(span);
      } else if (attribute.getNodeName().equals("scale")) {
        chart.setScaler(parseScaleAttribute(attribute));
      } else {
        throw new Exception("unexpected chart attribute: " + attribute.getNodeName());
      }
    }

    if (chart.getLabel() == null) {
      throw new Exception("missing chart label attribute");
    }
    
    if (!gridlinesFound) {
      chart.getGridlines().add(0f);
    }
    
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node2 = nodeList.item(i);
      if (node2.getNodeType() == Node.ELEMENT_NODE) {
        if (node2.getNodeName().equals("series")) {
          chart.getSeries().add(parseSeries(node2));
        } else {
          throw new Exception("unexpected chart element: " + node2.getNodeName());
        }
      }
    }
    
    return chart;
  }
  
  private Series parseSeries(Node node) throws Exception {
    Series series = new Series(symbolTable);
    NamedNodeMap map = node.getAttributes();
    for (int i = 0; i < map.getLength(); i++) {
      Node attribute = map.item(i);
      if (attribute.getNodeName().equals("ref")) {
        String name = attribute.getNodeValue();
        if (name.length() == 0) {
          throw new Exception("invalid series ref attribute (empty)");
        }
        Symbol symbol = symbolTable.get(name);
        if (symbol == null) {
          throw new Exception("series ref attribute not found: " + name);
        }
        if (!(symbol.getValue() instanceof TimeSeries)) {
          throw new Exception("series ref attribute not a series: " + name);
        }
        series.setTimeSeries((TimeSeries) symbol.getValue());
      } else if (attribute.getNodeName().equals("color")) {
        series.setColor(parseColorAttribute(attribute));
      }
    }
    
    if (series.getTimeSeries() == null) {
      throw new Exception("missing series ref attribute");
    }
    
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node2 = nodeList.item(i);
      if (node2.getNodeType() == Node.ELEMENT_NODE) {
        throw new Exception("unexpected series element: " + node2.getNodeName());
      }
    }
    return series;
  }
  
  private int parseIntAttribute(Node attribute) throws Exception {
    try {
      return Integer.parseInt(attribute.getNodeValue());
    } catch(NumberFormatException e) {
      throw generateAttributeException(attribute);
    }
  }
  
  private void parseGridlineAttribute(Chart chart, Node attribute) throws Exception {
    String value = attribute.getNodeValue();
    String[] gridlines = value.split(",");
    for (String gridline: gridlines) {
      String s = gridline.trim();
      if (!s.equals("")) {
        try {
          chart.getGridlines().add(Float.parseFloat(s));
        } catch(NumberFormatException e) {
          throw generateAttributeException(attribute);
        }
      }
    }
  }
  
  private Color parseColorAttribute(Node attribute) throws Exception {
    String color = attribute.getNodeValue();
    if (color.length() == 0) {
      throw generateAttributeException(attribute);
    }
    char ch = color.charAt(0);
    if (ch == '#') {
      if (color.length() == 1 || color.length() > 7) {
        throw generateAttributeException(attribute);
      }
      return new Color(Utils.parseHex(color.substring(1)));
    } else {
      Symbol symbol = symbolTable.get(color);
      if (symbol == null) {
        throw generateAttributeException(attribute);
      }
      if (!(symbol.getValue() instanceof Integer)) {
        throw generateAttributeException(attribute);
      }
      return new Color((Integer) symbol.getValue());
    }
  }
  
  private Scaler parseScaleAttribute(Node attribute) throws Exception {
    switch(attribute.getNodeValue()) {
    case "linear":
      return new LinearScaler();
    case "log":
      return new LogScaler();
    default:
      throw generateAttributeException(attribute);
      }
  }
  
  private int parseFrequencyAttribute(Node attribute) throws Exception {
    switch(attribute.getNodeValue()) {
    case "none":
      return Panel.FREQUENCY_NONE;
    case "days":
      return Panel.FREQUENCY_DAYS;
    case "months":
      return Panel.FREQUENCY_MONTHS;
    case "years":
      return Panel.FREQUENCY_YEARS;
    default:
      throw generateAttributeException(attribute);
    }
  }
  
  private Exception generateAttributeException(Node attribute) {
    return new Exception("invalid attribute: " + attribute.getNodeName() + "=" + attribute.getNodeValue());
  }
}
