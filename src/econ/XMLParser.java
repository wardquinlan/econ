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
  
	public EconContext parse(String filename, int level) throws Exception {
	  if (level == MAX_LEVEL) {
	    throw new Exception("maximum include level exceeded");
	  }
	  
	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(filename));
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
		    if (node.getNodeName().equals("chart")) {
		      econContext.getCharts().add(parseChart(node));
		    } else if (node.getNodeName().equals("include")) {
		      EconContext econContext2 = parseInclude(node, level);
          for (Chart chart: econContext2.getCharts()) {
            econContext.getCharts().add(chart);
          }
		    } else {
		      throw new Exception("unexpected econ-context element:" + node.getNodeName());
		    }
		  }
		}
		
		econContext.getSymbolTable().putAll(symbolTable);
		return econContext;
	}
	
	private EconContext parseInclude(Node node, int level) throws Exception {
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
    
    return parse(name, level + 1);
	}
	
	private Chart parseChart(Node node) throws Exception {
	  Chart chart = new Chart();
	  NamedNodeMap map = node.getAttributes();
	  for (int i = 0; i < map.getLength(); i++) {
	    Node attribute = map.item(i);
	    if (attribute.getNodeName().equals("id")) {
	      chart.setId(attribute.getNodeValue());
	    } else if (attribute.getNodeName().equals("title")) {
	      chart.setTitle(attribute.getNodeValue());
	    } else {
	      throw new Exception("unexpected chart attribute: " + attribute.getNodeName());
	    }
	  }
	  
	  if (chart.getId() == null) {
	    throw new Exception("missing chart id attribute");
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
