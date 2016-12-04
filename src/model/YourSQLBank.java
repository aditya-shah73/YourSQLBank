package model;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
/**
 * Created by sohamshah on 12/3/16.
 */
public class YourSQLBank {

    Connection connection;

    // ==== YourSQLBank MySQL JDBC Handler Code ====
    public YourSQLBank(String url, String user, String pass) {
        connection = connectDB(url, user, pass);
    }

    public Connection connectDB(String url, String usrnme, String pass) {
        System.out.println("\n-------- MySQL JDBC Connection ------------");
        System.out.println("\nMySQL JDBC Driver Registered!\n");
        try {
            Connection connection = null;
            connection = DriverManager.getConnection(url, usrnme, pass);
            if (connection == null)
                throw new SQLException();
            System.out.println("\nYou made it, take control your database now!\n");
            return connection;
        } catch (SQLException e) {
            handleError(e, "Failed to make connection! Check output below:");
            System.exit(0);
        }
        return null;
    }

    public ResultSet executeQueryStatement(String command) {
        try {
            return (connection.createStatement()).executeQuery(command);
        } catch (SQLException e) {
            handleError(e, "Failed to Execute Query Statement! Check output below:");
            return null;
        }
    }

    public void executeUpdateStatement(String command) {
        try {
            (connection.createStatement()).executeUpdate(command);
        } catch (SQLException e) {
            handleError(e, "Failed to Execute Update Statement! Check output below:");
        }
    }

    public static void handleError(Exception e, String msg) {
        System.out.println(msg + "\n");
        System.out.println(e.getMessage() + "\n");
        e.printStackTrace();
        System.out.println();
    }

}
