package econ.core;

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
  private static TimeSeriesDAO instance = new TimeSeriesDAO();
  
  private TimeSeriesDAO() {
  }
  
  public static TimeSeriesDAO getInstance() throws Exception {
    return instance;
  }
  
  public void connect(String host, String database, String username, String password) throws Exception {
    if (conn != null) {
      throw new Exception("already connected to datastore");
    }
    Class.forName("org.postgresql.Driver");
    String url = "jdbc:postgresql://" + host + "/" + database + "?user=" + username;
    if (password != null) {
      url = url + "&password=" + password;
    }
    conn = DriverManager.getConnection(url);  
  }

  public List<TimeSeries> listSeries() throws Exception {
    if (conn == null) {
      throw new Exception("not connected to datatore");
    }
    List<TimeSeries> list = new ArrayList<>();
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source, source_id, notes from time_series order by id");
    ResultSet resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeries series = new TimeSeries(TimeSeries.NULL);
      series.setId(resultSet.getInt(1));
      series.setName(resultSet.getString(2));
      series.setTitle(resultSet.getString(3));
      series.setSource(resultSet.getString(4));
      series.setSourceId(resultSet.getString(5));
      series.setNotes(resultSet.getString(6));
      list.add(series);
    }
    return list;
  }
  
  public int size(TimeSeries timeSeries) throws Exception {
    PreparedStatement ps = conn.prepareStatement("select count(*) from time_series_data where id = ?");
    ps.setInt(1, timeSeries.getId());
    ResultSet resultSet = ps.executeQuery();
    resultSet.next();
    return resultSet.getInt(1);
  }

  public void drop(int id) throws Exception {
    if (conn == null) {
      throw new Exception("not connected to datatore");
    }
    try {
      conn.setAutoCommit(false);
      PreparedStatement ps = conn.prepareStatement("delete from time_series_data where id = ?");
      ps.setInt(1, id);
      ps.executeUpdate();
      
      ps = conn.prepareStatement("delete from time_series where id = ?");
      ps.setInt(1, id);
      ps.executeUpdate();
      conn.commit();
    } catch(Exception ex) {
      conn.rollback();
      throw ex;
    }
  }
  
  public void merge(MergeData mergeData) throws Exception {
    if (conn == null) {
      throw new Exception("not connected to datatore");
    }
    try {
      conn.setAutoCommit(false);
      TimeSeries timeSeries = mergeData.getTimeSeriesInsert();
      for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
        PreparedStatement ps = conn.prepareStatement("insert into time_series_data(id, datestamp, value) values (?, ?, ?)");
        ps.setInt(1, timeSeries.getId());
        ps.setDate(2, new java.sql.Date(timeSeriesData.getDate().getTime()));
        ps.setFloat(3, (Float) timeSeriesData.getValue());
        ps.executeUpdate();
      }
      
      timeSeries = mergeData.getTimeSeriesUpdate();
      for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
        PreparedStatement ps = conn.prepareStatement("update time_series_data set value = ? where id = ? and datestamp = ?");
        ps.setFloat(1, (Float) timeSeriesData.getValue());
        ps.setInt(2, timeSeries.getId());
        ps.setDate(3, new java.sql.Date(timeSeriesData.getDate().getTime()));
        ps.executeUpdate();
      }

      timeSeries = mergeData.getTimeSeriesDelete();
      for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
        PreparedStatement ps = conn.prepareStatement("delete from time_series_data where id = ? and datestamp = ?");
        ps.setInt(1, timeSeries.getId());
        ps.setDate(2, new java.sql.Date(timeSeriesData.getDate().getTime()));
        ps.executeUpdate();
      }
      
      if (mergeData.getTimeSeriesMerge() != null) {
        PreparedStatement ps = conn.prepareStatement("update time_series set name = ?, title = ?, source = ?, source_id = ?, notes = ? where id = ?");
        ps.setString(1, mergeData.getTimeSeriesMerge().getName());
        ps.setString(2, mergeData.getTimeSeriesMerge().getTitle());
        ps.setString(3, mergeData.getTimeSeriesMerge().getSource());
        ps.setString(4, mergeData.getTimeSeriesMerge().getSourceId());
        ps.setString(5, mergeData.getTimeSeriesMerge().getNotes());
        ps.setInt(6, mergeData.getTimeSeriesMerge().getId());
        ps.executeUpdate();
      }
      
      conn.commit();
    } catch(Exception ex) {
      conn.rollback();
      throw ex;
    }
  }
  
  public void saveSeries(TimeSeries timeSeries) throws Exception {
    Utils.ASSERT(timeSeries.getType() == TimeSeries.FLOAT, "series must be of type float");
    if (conn == null) {
      throw new Exception("not connected to datatore");
    }
    try {
      conn.setAutoCommit(false);
      if (timeSeries.getId() == null) {
        throw new Exception("id is null");
      }
      PreparedStatement ps = conn.prepareStatement("insert into time_series(id, name, title, source, source_id, notes) values (?, ?, ?, ?, ?, ?)");
      ps.setInt(1, timeSeries.getId());
      ps.setString(2, timeSeries.getName());
      ps.setString(3, timeSeries.getTitle());
      ps.setString(4, timeSeries.getSource());
      ps.setString(5, timeSeries.getSourceId());
      ps.setString(6, timeSeries.getNotes());
      ps.executeUpdate();
      
      for (TimeSeriesData timeSeriesData: timeSeries.getTimeSeriesDataList()) {
        ps = conn.prepareStatement("insert into time_series_data(id, datestamp, value) values (?, ?, ?)");
        ps.setInt(1, timeSeriesData.getId());
        ps.setDate(2, new java.sql.Date(timeSeriesData.getDate().getTime()));
        ps.setFloat(3, (Float) timeSeriesData.getValue());
        ps.executeUpdate();
      }
      conn.commit();
    } catch(Exception ex) {
      conn.rollback();
      throw ex;
    }
  }
  
  public TimeSeries loadSeriesById(int id) throws Exception {
    if (conn == null) {
      throw new Exception("not connected to datatore");
    }
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source, source_id, notes from time_series where id = ?");
    ps.setInt(1, id);
    ResultSet resultSet = ps.executeQuery();
    if (!resultSet.next()) {
      // not found
      return null;
    }
    TimeSeries series = new TimeSeries(TimeSeries.FLOAT);
    series.setId(resultSet.getInt(1));
    series.setName(resultSet.getString(2));
    series.setTitle(resultSet.getString(3));
    series.setSource(resultSet.getString(4));
    series.setSourceId(resultSet.getString(5));
    series.setNotes(resultSet.getString(6));
    Utils.ASSERT(!resultSet.next(), "resultSet not empty");
    
    ps = conn.prepareStatement("select id, datestamp, value from time_series_data where id = ? order by datestamp");
    ps.setInt(1, series.getId());
    resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setId(resultSet.getInt(1));
      timeSeriesData.setDate(resultSet.getDate(2));
      timeSeriesData.setValue(resultSet.getFloat(3));
      series.add(timeSeriesData);
    }
    series.setBase(true);
    return series;
  }
  
  public TimeSeries loadSeriesByName(String name) throws Exception {
    if (conn == null) {
      throw new Exception("not connected to datatore");
    }
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source, source_id, notes from time_series where name = ?");
    ps.setString(1, name);
    ResultSet resultSet = ps.executeQuery();
    if (!resultSet.next()) {
      // not found
      return null;
    }
    TimeSeries series = new TimeSeries(TimeSeries.FLOAT);
    series.setId(resultSet.getInt(1));
    series.setName(resultSet.getString(2));
    series.setTitle(resultSet.getString(3));
    series.setSource(resultSet.getString(4));
    series.setSourceId(resultSet.getString(5));
    series.setNotes(resultSet.getString(6));
    Utils.ASSERT(!resultSet.next(), "resultSet not empty");
    
    ps = conn.prepareStatement("select id, datestamp, value from time_series_data where id = ? order by datestamp");
    ps.setInt(1, series.getId());
    resultSet = ps.executeQuery();
    while (resultSet.next()) {
      TimeSeriesData timeSeriesData = new TimeSeriesData();
      timeSeriesData.setId(resultSet.getInt(1));
      timeSeriesData.setDate(resultSet.getDate(2));
      timeSeriesData.setValue(resultSet.getFloat(3));
      series.add(timeSeriesData);
    }
    series.setBase(true);
    return series;
  }
  
  public void close() {
    if (conn == null) {
      return;
    }
    try {
      conn.close();
    } catch(Exception e) {
      log.warn("could not close dao", e);
    }
  }
}
