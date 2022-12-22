package econ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeSeriesDAO {
  private static Log log = LogFactory.getFactory().getInstance(TimeSeriesDAO.class);
  private Connection conn;
  private static TimeSeriesDAO instance;
  
  public static TimeSeriesDAO getInstance() throws Exception {
    if (instance == null) {
      instance = new TimeSeriesDAO();
    }
    return instance;
  }
  
  private TimeSeriesDAO() throws Exception {
    Class.forName("org.postgresql.Driver");
    log.info("attempting to connect to database with host=" + System.getenv("ECON_HOST") + 
             ", name=" + System.getenv("ECON_DATABASE") +
             ", username=" + System.getenv("ECON_USERNAME"));
    String url = "jdbc:postgresql://" + System.getenv("ECON_HOST") +  
                 "/" + System.getenv("ECON_DATABASE") + 
                 "?user=" + System.getenv("ECON_USERNAME") +
                 "&password=" + System.getenv("ECON_PASSWORD");
    conn = DriverManager.getConnection(url);  
  }

  public Object loadSeriesByName(String name) throws Exception {
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source_org, source_name, notes from time_series where name = ?");
    ps.setString(1, name);
    ResultSet resultSet = ps.executeQuery();
    if (!resultSet.next()) {
      // not found
      return null;
    }
    TimeSeries series = new TimeSeries();
    series.setId(resultSet.getInt(1));
    series.setName(resultSet.getString(2));
    series.setTitle(resultSet.getString(3));
    series.setSourceOrg(resultSet.getString(4));
    series.setSourceName(resultSet.getString(5));
    series.setNotes(resultSet.getString(6));
    
    ps = conn.prepareStatement("select datestamp, value from time_series_data where id = ? order by datestamp");
    ps.setInt(1, series.getId());
    resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(resultSet.getDate(1));
      timeSeriesData.setValue(resultSet.getDouble(2));
      series.getTimeSeriesData().add(timeSeriesData);
    }
    return series;
  }
  
  public void close() {
    try {
      conn.close();
    } catch(Exception e) {
      log.warn("could not close dao", e);
    }
  }
}
