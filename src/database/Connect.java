package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {

    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "floraartistry";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private static Connection con;
    private static Connect connect;

    public static Connect getInstance() {
        if (connect == null) {
            connect = new Connect();
        }
        return connect;
    }

    private Connect () {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("Error executing query: " + query);
            e.printStackTrace();
            return null;
        }
    }

    public void updateQuery(String query) {
        try {
            Statement stmt = con.createStatement(); 
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}