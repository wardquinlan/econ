package econ;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SeriesDAO {
  private static Log log = LogFactory.getFactory().getInstance(SeriesDAO.class);
  private Connection conn;
  private Calendar cal = new GregorianCalendar();
  private static SeriesDAO instance;
  
  public static SeriesDAO getInstance() throws Exception {
    if (instance == null) {
      instance = new SeriesDAO();
    }
    return instance;
  }
  
  private SeriesDAO() throws Exception {
    Class.forName("org.postgresql.Driver");
    log.info("attempting to connect to database with host=" + System.getenv("ECON_HOST") + 
             ", name=" + System.getenv("ECON_DATABASE") +
             ", username=" + System.getenv("ECON_USERNAME"));
    String url = "jdbc:postgresql://" + System.getenv("ECON_HOST") +  
                 "/" + System.getenv("ECON_DATABASE") + 
                 "?user=" + System.getenv("ECON_USERNAME") +
                 "&password=" + System.getenv("ECON_PASSWORD");
    conn = DriverManager.getConnection(url);  
    PreparedStatement ps = conn.prepareStatement("select id, datestamp, value from time_series_data order by datestamp");
    ResultSet resultSet = ps.executeQuery();
    while (resultSet.next()) {
      //System.out.println(resultSet.getInt(1));
      cal.setTime(resultSet.getDate(2));
      //System.out.println(cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH));
      //System.out.println(resultSet.getDouble(3));
    }
  }

  public Object loadSeriesByName(String name) throws Exception {
    PreparedStatement ps = conn.prepareStatement("select id, name, title, source_org, source_name, notes from time_series where name = ?");
    ps.setString(1, name);
    ResultSet resultSet = ps.executeQuery();
    resultSet.next();
    Series series = new Series();
    series.setId(resultSet.getInt(1));
    series.setName(resultSet.getString(2));
    series.setTitle(resultSet.getString(3));
    series.setSourceOrg(resultSet.getString(4));
    series.setSourceName(resultSet.getString(5));
    series.setNotes(resultSet.getString(6));
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
