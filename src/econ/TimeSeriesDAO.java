package econ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

  public List<TimeSeries> listSeries() throws Exception {
    List<TimeSeries> list = new ArrayList<>();
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source_org, source_name, notes from time_series order by id");
    ResultSet resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeries series = new TimeSeries();
      series.setId(resultSet.getInt(1));
      series.setName(resultSet.getString(2));
      series.setTitle(resultSet.getString(3));
      series.setSourceOrg(resultSet.getString(4));
      series.setSourceName(resultSet.getString(5));
      series.setNotes(resultSet.getString(6));
      list.add(series);
    }
    return list;
  }

  public void createSeries(TimeSeries timeSeries) throws Exception {
    PreparedStatement ps = conn.prepareStatement("insert into time_series(id, name, title, source_org, source_name) values(?,?,?,?,?)");
    ps.setInt(1, timeSeries.getId());
    ps.setString(2, timeSeries.getName());
    ps.setString(3, timeSeries.getTitle());
    ps.setString(4, timeSeries.getSourceOrg());
    ps.setString(5, timeSeries.getSourceName());
    ps.executeUpdate();    
  }
  
  public TimeSeries loadSeriesById(int id) throws Exception {
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source_org, source_name, notes from time_series where id = ?");
    ps.setInt(1, id);
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
    Utils.ASSERT(!resultSet.next(), "resultSet not empty");
    
    ps = conn.prepareStatement("select datestamp, value from time_series_data where id = ? order by datestamp");
    ps.setInt(1, series.getId());
    resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(resultSet.getDate(1));
      timeSeriesData.setValue(resultSet.getDouble(2));
      series.add(timeSeriesData);
    }
    return series;
  }
  
  public TimeSeries loadSeriesByName(String name) throws Exception {
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
    Utils.ASSERT(!resultSet.next(), "resultSet not empty");
    
    ps = conn.prepareStatement("select datestamp, value from time_series_data where id = ? order by datestamp");
    ps.setInt(1, series.getId());
    resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setDate(resultSet.getDate(1));
      timeSeriesData.setValue(resultSet.getDouble(2));
      series.add(timeSeriesData);
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
