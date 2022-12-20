package econ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SeriesDAO {
  private static Log log = LogFactory.getFactory().getInstance(SeriesDAO.class);
  Connection conn;
  
  public SeriesDAO() throws Exception {
    Class.forName("org.postgresql.Driver");
    log.info("attempting to connect to database with host=" + System.getenv("ECON_HOST") + 
             ", name=" + System.getenv("ECON_DATABASE") +
             ", username=" + System.getenv("ECON_USERNAME"));
    String url = "jdbc:postgresql://" + System.getenv("ECON_HOST") +  
                 "/" + System.getenv("ECON_DATABASE") + 
                 "?user=" + System.getenv("ECON_USERNAME") +
                 "&password=" + System.getenv("ECON_PASSWORD");
    conn = DriverManager.getConnection(url);  
    PreparedStatement ps = conn.prepareStatement("select count(*) from series");
    ResultSet resultSet = ps.executeQuery();
    while (resultSet.next()) {
      System.out.println("RS: " + resultSet);
    }
  }

  public void close() {
    try {
      conn.close();
    } catch(Exception e) {
      log.warn("could not close dao", e);
    }
  }
}
