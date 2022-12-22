package econ;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	public Econ parse(String filename) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(filename));
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		if (root.getNodeName() != "econ") {
			throw new Exception("Unexpected root node: " + root.getNodeName());
		}
		
		Econ econ = new Econ();
		NamedNodeMap map = doc.getDocumentElement().getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node attribute = map.item(i);
			if (attribute.getNodeName().equals("script")) {
				econ.setScript(attribute.getNodeValue());
			} else {
				throw new Exception("Unexpected econ attribute: " + attribute.getNodeName());
			}
		}
		if (econ.getScript() == null) {
			throw new Exception("Missing econ script attribute");
		}
		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
		  Node node = nodeList.item(i);
		  if (node.getNodeType() == Node.ELEMENT_NODE) {
		    if (node.getNodeName().equals("chart")) {
		      econ.getCharts().add(parseChart(node));
		    }
		  }
		}
		return econ;
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
	  return chart;
	}
}
