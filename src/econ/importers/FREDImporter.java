package econ.importers;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import econ.TimeSeries;
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
    return null;
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
