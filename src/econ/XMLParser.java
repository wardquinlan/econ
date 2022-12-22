package econ;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
  private static Log log = LogFactory.getFactory().getInstance(XMLParser.class);
  private Map<String, Symbol> symbolTable;
  
	public EconContext parse(String filename) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(filename));
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		if (root.getNodeName() != "econ-context") {
			throw new Exception("Unexpected root node: " + root.getNodeName());
		}
		
		EconContext context = new EconContext();
		NamedNodeMap map = doc.getDocumentElement().getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node attribute = map.item(i);
			if (attribute.getNodeName().equals("script")) {
				context.setScript(attribute.getNodeValue());
			} else {
				throw new Exception("Unexpected econ attribute: " + attribute.getNodeName());
			}
		}
		if (context.getScript() == null) {
			throw new Exception("Missing econ script attribute");
		}

		// invoke the parser so we have access to symbols for the remainder of the file
    Tokenizer tokenizer = new Tokenizer(context.getScript());
    TokenIterator itr = tokenizer.tokenize();
    if (!itr.hasNext()) {
      log.error("empty script file");
      System.exit(1);
    }
    Parser parser = new Parser();
    Token tk = itr.next();
    symbolTable = parser.parse(tk, itr);
		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
		  Node node = nodeList.item(i);
		  if (node.getNodeType() == Node.ELEMENT_NODE) {
		    if (node.getNodeName().equals("chart")) {
		      context.getCharts().add(parseChart(node));
		    }
		  }
		}
		return context;
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
	      throw new Exception("Unexpected chart attribute: " + attribute.getNodeName());
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
          parseSeries(node2);
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
          throw new Exception("invalid symbol (empty)");
        }
        Symbol symbol = symbolTable.get(name);
        if (symbol == null) {
          throw new Exception("symbol not found: " + name);
        }
        if (!(symbol.getValue() instanceof TimeSeries)) {
          throw new Exception("symbol not a series: " + name);
        }
        series.setTimeSeries((TimeSeries) symbol.getValue());
      } else if (attribute.getNodeName().equals("color")) {
        String color = attribute.getNodeValue();
        if (color.length() == 0) {
          throw new Exception("invalid color (empty)");
        }
        char ch = color.charAt(0);
        if (ch == '#') {
          if (color.length() == 1) {
            throw new Exception("invalid color: " + color);
          }
          series.setColor(new Color(Utils.parseHex(color.substring(1))));
        } else {
          Symbol symbol = symbolTable.get(color);
          if (symbol == null) {
            throw new Exception("symbol not found: " + color);
          }
          if (!(symbol.getValue() instanceof Integer)) {
            throw new Exception("invalid color: " + color);
          }
          series.setColor(new Color((Integer) symbol.getValue()));
        }
      }
    }
    return series;
	}
}
