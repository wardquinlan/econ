package econ.importers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
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

import econ.TimeSeries;
import econ.TimeSeriesData;
import econ.Utils;
import econ.parser.Symbol;

public class FREDImporter implements Importer {
  private final String BASEURL;
  private final String APIKEY;
  
  public FREDImporter(Map<String, Symbol> symbolTable) throws Exception {
    if (symbolTable.get("importers.fred.baseurl") == null) {
      throw new Exception("importers.fred.baseurl not defined");
    }
    if (symbolTable.get("importers.fred.apikey") == null) {
      throw new Exception("importers.fred.apikey not defined");
    }
    
    BASEURL = (String) symbolTable.get("importers.fred.baseurl").getValue();
    APIKEY = (String) symbolTable.get("importers.fred.apikey").getValue();
  }
  
  @Override
  public TimeSeries run(File file, List<Object> params) throws Exception {
    if (params.size() < 1) {
      throw new Exception("missing argument(s)");
    }
    
    if (params.size() > 2) {
      throw new Exception("too many arguments");
    }
    
    if (!(params.get(0) instanceof String) || (params.size() == 2 && !(params.get(1) instanceof String))) {
      throw new Exception("argument(s) not a string");
    }
    
    String sourceId = (String) params.get(0);
    String units = null;
    if (params.size() == 2) {
      units = (String) params.get(1);
    }
    
    TimeSeries timeSeries = new TimeSeries();
    timeSeries.setSource("FRED");
    meta(timeSeries, sourceId);
    data(timeSeries, sourceId, units);
    return timeSeries;
  }

  private void data(TimeSeries timeSeries, String serId, String units) throws Exception {
    InputStream stream = getInputStream("/series/observations", "series_id", serId, units);
    if (stream == null) {
      throw new Exception("cannot open input stream, series might not exist");
    }
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
        } catch(NumberFormatException | ParseException e) {
          // don't include missing data
          log.warn("cannot parse data", e);
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
    if (stream == null) {
      throw new Exception("cannot open input stream, series might not exist");
    }
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
  
  private InputStream getInputStream(String relPath, String requestParamKey, String requestParamValue, String units) {
    InputStream stream = null;
    String url = BASEURL + relPath + "?" + requestParamKey + "=" + requestParamValue + "&api_key=" + APIKEY;
    if (units != null ) {
      url += "&units=" + units;
    }
    log.info("constructed url=" + url);
    try {
      URL myurl = new URL(url);
      HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
      con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
      stream = con.getInputStream();
    } catch(Exception e) {
      log.info("caught exception", e);
    }
    return stream;
  }
}
