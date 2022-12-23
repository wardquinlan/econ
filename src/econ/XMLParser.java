package econ;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
  public static final int MAX_LEVEL = 8;
  private Map<String, Symbol> symbolTable;
  
	public EconContext parse(String basename, String filename, int level) throws Exception {
	  if (level == MAX_LEVEL) {
	    throw new Exception("maximum include level exceeded");
	  }
	  
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		File file = new File(basename + File.separator + filename);
		Document doc = builder.parse(file);
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		if (!root.getNodeName().equals("econ-context")) {
			throw new Exception("unexpected root node: " + root.getNodeName());
		}
		
		EconContext econContext = new EconContext();
		NamedNodeMap map = doc.getDocumentElement().getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node attribute = map.item(i);
			if (attribute.getNodeName().equals("script")) {
				econContext.setScript(attribute.getNodeValue());
			} else {
				throw new Exception("unexpected econ-context attribute: " + attribute.getNodeName());
			}
		}

		if (level == 0) {
		  if (econContext.getScript() == null) {
		    throw new Exception("missing econ-context script attribute");
		  }
	    
		  // invoke the parser so we have access to symbols for the remainder of the file
	    Tokenizer tokenizer = new Tokenizer(econContext.getScript());
	    TokenIterator itr = tokenizer.tokenize();
	    if (itr.hasNext()) {
  	    Parser parser = new Parser();
  	    Token tk = itr.next();
  	    symbolTable = parser.parse(tk, itr);
	    } else {
	      symbolTable = new HashMap<String, Symbol>();
	    }
		} else {
		  if (econContext.getScript() != null) {
		    throw new Exception("unexpected econ-context script attribute: " + econContext.getScript());
		  }
		}

		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
		  Node node = nodeList.item(i);
		  if (node.getNodeType() == Node.ELEMENT_NODE) {
		    if (node.getNodeName().equals("panel")) {
		      econContext.getPanels().add(parsePanel(node));
		    } else if (node.getNodeName().equals("include")) {
		      EconContext econContext2 = parseInclude(basename, node, level);
          for (Panel panel: econContext2.getPanels()) {
            econContext.getPanels().add(panel);
          }
		    } else {
		      throw new Exception("unexpected econ-context element:" + node.getNodeName());
		    }
		  }
		}
		
		Symbol symbol = symbolTable.get("settings.loaded");
		if (symbol == null || !symbol.getValue().equals(1)) {
		  throw new Exception("settings not loaded");
		}
		econContext.getSymbolTable().putAll(symbolTable);
		return econContext;
	}
	
	private EconContext parseInclude(String basename, Node node, int level) throws Exception {
	  String name = null;
	  NamedNodeMap map = node.getAttributes();
    for (int i = 0; i < map.getLength(); i++) {
      Node attribute = map.item(i);
      if (attribute.getNodeName().equals("name")) {
        name = attribute.getNodeValue();
      } else {
        throw new Exception("unexpected include attribute: " + attribute.getNodeName());
      }
    }
    
    if (name == null) {
      throw new Exception("missing include name attribute");
    }
    
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node2 = nodeList.item(i);
      if (node2.getNodeType() == Node.ELEMENT_NODE) {
        throw new Exception("unexpected include element: " + node2.getNodeName());
      }
    }
    
    return parse(basename, name, level + 1);
	}
	
	private Panel parsePanel(Node node) throws Exception {
	  Panel panel = new Panel();
    NamedNodeMap map = node.getAttributes();
    for (int i = 0; i < map.getLength(); i++) {
      Node attribute = map.item(i);
      if (attribute.getNodeName().equals("label")) {
        panel.setLabel(attribute.getNodeValue());
      } else {
        throw new Exception("unexpected panel attribute: " + attribute.getNodeName());
      }
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
    
    return panel;
	}
	
	private Chart parseChart(Node node) throws Exception {
	  Chart chart = new Chart();
	  NamedNodeMap map = node.getAttributes();
	  for (int i = 0; i < map.getLength(); i++) {
	    Node attribute = map.item(i);
	    if (attribute.getNodeName().equals("label")) {
	      chart.setLabel(attribute.getNodeValue());
	    } else {
	      throw new Exception("unexpected chart attribute: " + attribute.getNodeName());
	    }
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
	  Series series = new Series();
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
        String color = attribute.getNodeValue();
        if (color.length() == 0) {
          throw new Exception("invalid series color attribute (empty)");
        }
        char ch = color.charAt(0);
        if (ch == '#') {
          if (color.length() == 1) {
            throw new Exception("invalid series color attribute: " + color);
          }
          series.setColor(new Color(Utils.parseHex(color.substring(1))));
        } else {
          Symbol symbol = symbolTable.get(color);
          if (symbol == null) {
            throw new Exception("series color attribute not found: " + color);
          }
          if (!(symbol.getValue() instanceof Integer)) {
            throw new Exception("invalid series color attribute: " + color);
          }
          series.setColor(new Color((Integer) symbol.getValue()));
        }
      }
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
}
