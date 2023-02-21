package es.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.parser.Symbol;

public class FRED implements Command {
  private String BASEURL;
  private String APIKEY;
  
  @Override
  public String getSummary() {
    return "Series  fred(String sourceId[, String units]);";
  }
  
  @Override
  public List<String> getDetails() {
    List<String> list = new ArrayList<>();
    list.add("Imports a series from the FRED database, with:");
    list.add("  'sourceId' as the FRED source id");
    list.add("  'units' as the FRED unit string");
    list.add("");
    list.add("@see: https://fred.stlouisfed.org/docs/api/fred/series_observations.html#units for additional information on units");
    list.add("");
    list.add("Also note that the following settings must be defined:");
    list.add("");
    list.add("sources.fred.baseurl - must point to the base URL of FRED");
    list.add("sources.fred.apikey - must point to the FRED-issued developer API key");
    return list;
  }
  
  @Override
  public String getReturns() {
    return "Imported series from FRED";
  }
  
  @Override
  public Object run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    if (symbolTable.get("sources.fred.baseurl") == null) {
      throw new Exception("sources.fred.baseurl not defined");
    }
    if (symbolTable.get("sources.fred.apikey") == null) {
      throw new Exception("sources.fred.apikey not defined");
    }
    
    BASEURL = (String) symbolTable.get("sources.fred.baseurl").getValue();
    APIKEY = (String) symbolTable.get("sources.fred.apikey").getValue();

    Utils.validate(params, 1, 2);
    
    if (!(params.get(0) instanceof String)) {
      throw new Exception(params.get(0) + " is not a string");
    }
    if (params.size() == 2 && !(params.get(1) instanceof String)) {
      throw new Exception(params.get(1) + " is not a string");
    }
    
    String sourceId = (String) params.get(0);
    String units = null;
    if (params.size() == 2) {
      units = (String) params.get(1);
    }
    
    TimeSeries timeSeries = new TimeSeries(TimeSeries.FLOAT);
    timeSeries.setSource("FRED");
    meta(timeSeries, sourceId);
    data(timeSeries, sourceId, units);
    timeSeries.setBase(true);
    return timeSeries;
  }

  private void data(TimeSeries timeSeries, String serId, String units) throws Exception {
    InputStream stream = getInputStream("/series/observations", "series_id", serId, units);
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(stream);
      Element root = doc.getDocumentElement();
      if (!root.getNodeName().equals("observations")) {
        throw new Exception("root element is not 'observations': " + root.getNodeName());
      }
      NodeList nodeList = root.getChildNodes();
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }
        if (!node.getNodeName().equals("observation")) {
          throw new Exception("child node is not 'observation': " + node.getNodeName());
        }
        NamedNodeMap map = node.getAttributes();
        Node date = map.getNamedItem("date");
        if (date == null || date.getNodeType() != Node.ATTRIBUTE_NODE) {
          throw new Exception("date attribute not found");
        }
        Node value = map.getNamedItem("value");
        if (value == null || value.getNodeType() != Node.ATTRIBUTE_NODE) {
          throw new Exception("title attribute not found");
        }
        try {
          TimeSeriesData timeSeriesData = new TimeSeriesData();
          Date d = Utils.DATE_FORMAT.parse(date.getNodeValue());
          Float v = Float.parseFloat(value.getNodeValue());
          timeSeriesData.setDate(d);
          timeSeriesData.setValue(v);
          timeSeries.add(timeSeriesData);
        } catch(NumberFormatException e) {
          // don't include missing data
        } catch(ParseException e) {
          log.warn("cannot parse date", e);
        }
      }
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch(IOException e) {
          log.warn("cannot close input stream", e);
        }
      }
    }
  }

  private void meta(TimeSeries timeSeries, String serId) throws Exception {
    InputStream stream = getInputStream("/series", "series_id", serId, null);
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(stream);
      Element root = doc.getDocumentElement();
      if (!root.getNodeName().equals("seriess")) {
        throw new Exception("root element is not 'seriess': " + root.getNodeName());
      }
      NodeList nodeList = root.getChildNodes();
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        if (node.getNodeType() != Node.ELEMENT_NODE) {
          continue;
        }
        if (!node.getNodeName().equals("series")) {
          throw new Exception("child node is not 'series': " + node.getNodeName());
        }
        NamedNodeMap map = node.getAttributes();
        Node id = map.getNamedItem("id");
        if (id == null || id.getNodeType() != Node.ATTRIBUTE_NODE) {
          throw new Exception("id attribute not found");
        }
        Node title = map.getNamedItem("title");
        if (title == null || title.getNodeType() != Node.ATTRIBUTE_NODE) {
          throw new Exception("title attribute not found");
        }
        Node notes = map.getNamedItem("notes");
        // default the name to the node value (but user can change later if needed)
        timeSeries.setName(id.getNodeValue());
        timeSeries.setSourceId(id.getNodeValue());
        timeSeries.setTitle(title.getNodeValue());
        timeSeries.setNotes(notes.getNodeValue());
      }
    } finally {
      if (stream != null) {
        try {
          stream.close();
        } catch(IOException e) {
          log.warn("cannot close input stream", e);
        }
      }
    }
  }
  
  private InputStream getInputStream(String relPath, String requestParamKey, String requestParamValue, String units) throws Exception {
    InputStream stream = null;
    String url = BASEURL + relPath + "?" + requestParamKey + "=" + requestParamValue + "&api_key=" + APIKEY;
    if (units != null ) {
      url += "&units=" + units;
    }
    log.debug("constructed url=" + url);
    URL myurl = new URL(url);
    HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
    stream = con.getInputStream();
    return stream;
  }
}
