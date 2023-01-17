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
  @Override
  public TimeSeries run(Map<String, Symbol> symbolTable, File file, List<Object> params) throws Exception {
    String baseURL = (String) symbolTable.get("importers.fred.baseurl").getValue();
    String apikey = (String) symbolTable.get("importers.fred.apikey").getValue();
    return null;
  }

  private InputStream getInputStream(String relPath, String requestParamKey, String requestParamValue, String units) {
    InputStream stream = null;
    String baseURL = System.getProperty("datamgr.baseurl");
    if (baseURL == null) {
      log.error("datamgr.baseurl not defined");
      return null;
    }
    String url = baseURL + relPath + "?" + requestParamKey + "=" + requestParamValue + "&api_key=" + System.getProperty("datamgr.apikey");
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
