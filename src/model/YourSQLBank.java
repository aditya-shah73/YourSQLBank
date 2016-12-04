// package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;;

//RUN IN CMD WITH: cd C:\Users\dutta\Dropbox\Apps\Github\YourSQLBank\src\model && cls && javac YourSQLBank.java && java -cp mysql-connector-java-5.1.40-bin.jar; YourSQLBank

public class YourSQLBank {
    Connection connection;

    // ==== Testing Code ====
    public static void main(String[] argv) {
        YourSQLBank bankDB = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "root");
        bankDB.test();
        //try {bankDB.test();} catch(Exception e) {handleError(e, "Failed testing... Check output below:");}
    }

    public void test() {
        // executeUpdateStatement("INSERT INTO t_customer VALUES (4, 1, 'duttaoindril@gmail.com', 'Oindril', 'Dutta', 'duttaoindril', 'odutta', 1, '2016-11-30 18:55:50', null)");

        //Testing Admin Level Table Calls
        print("SELECT * FROM User_TB join Account_TB ON User_TB.USER_ID = Account_TB.USER_ID;");
        print("SELECT * FROM User_TB JOIN Account_TB ON User_TB.USER_ID = Account_TB.USER_ID JOIN History_TB ON User_TB.USER_ID = History_TB.USER_ID;");
        print(getAdminPanelUserInfoTable());
        print(getAdminPanelTransactionHistoryTable());
        //Testing User Login Row Calls
        print(login("groot", "groot"));
        print(login("soham", "pass"));
        print(login("FAIL", "FAIL"));
        //Testing User Get Information Row Calls
        print(getInfo("groot"));
        print(getInfo("soham"));
        print(getInfo("FAIL"));
        //Testing User Transaction History Table Calls
        print(getUserTransactionHistoryTable("groot"));
        print(getUserTransactionHistoryTable("soham"));
        print(getUserTransactionHistoryTable("FAIL"));
    }

    // ==== MySQL JDBC General Handler Code ====
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

    public ResultSet executeQueryStatement(String command) throws SQLException {
        return (connection.createStatement()).executeQuery(command);
    }

    public ResultSet executeQueryStatement(String command, String error) {
        try {
            return executeQueryStatement(command);
        } catch (SQLException e) {
            handleError(e, error);
            return null;
        }
    }

    public void executeUpdateStatement(String command) throws SQLException {
        (connection.createStatement()).executeUpdate(command);
    }

    public void executeUpdateStatement(String command, String error) {
        try {
            executeUpdateStatement(command);
        } catch (SQLException e) {
            handleError(e, error);
        }
    }

    public List<String[]> getTable(String purpose, String log, String command, String error) {
        List<String[]> rs = getTable(purpose, log, command, error, false);
        if(rs.isEmpty()) {
            System.out.println(error+"... Try again.\n");
            return null;
        }
        return rs;
    }

    public List<String[]> getTable(String purpose, String log, String command, String error, boolean row) {
        String label = "table";
        if(row)
            label = "row";
        System.out.println("Attempting to return "+label+" for "+purpose+" with Columns: "+log+"...\n");
        return convertRStoArray(executeQueryStatement(command, error));
    }

    public String[] getRow(String purpose, String log, String command, String error) {
        List<String[]> rs = getTable(purpose, log, command, error, true);
        if(rs.isEmpty()) {
            System.out.println(error+"... Try again.\n");
            return null;
        }
        return rs.get(0);
    }

    public List<String[]> convertRStoArray(ResultSet rs) {
        int nCol = rs.getMetaData().getColumnCount();
        List<String[]> table = new ArrayList<>();
        while(rs.next()) {
            String[] row = new String[nCol];
            for(int iCol = 1; iCol <= nCol; iCol++){
                Object obj = rs.getObject( iCol );
                row[iCol-1] = (obj == null)? null : obj.toString();
            }
            table.add(row);
        }
        return table;
    }

    public void print(String command) {
        print(executeQueryStatement(command));
    }

    public void print(ResultSet rs) {
        if(rs != null)
            print(convertRStoArray(rs));
    }

    public void print(String[] lst) {
        if(lst == null || lst.length < 1)
            return;
        for(String s:lst)
            System.out.print(" " + s);
        System.out.println("\n");
    }

    public void print(List<String[]> rs) {
        if(rs == null || rs.isEmpty())
            return;
        for(String[] row:rs) {
            for(String s:row)
                System.out.print(" " + s);
            System.out.println();
        }
        System.out.println();
    }

    public static void handleError(Exception e, String msg) {
        System.out.println(msg + "! Check output below:\n");
        System.out.println(e.getMessage() + "\n");
        e.printStackTrace();
        System.out.println();
    }

    // ==== YourSQLBank Specific JDBC Query Methods ====
    public List<String[]> getAdminPanelUserInfoTable() {
        return getTable("Admin Panel User Info Table", "USERNAME,CHECKING_BALANCE,SAVING_BALANCE", "SELECT USERNAME, CHECKINGACC.BALANCE AS CHECKING_BALANCE, SAVINGACC.BALANCE AS SAVING_BALANCE FROM User_TB JOIN Account_TB AS CHECKINGACC ON User_TB.USER_ID = CHECKINGACC.USER_ID AND CHECKINGACC.ACC_TYPE = 'CHKN' JOIN Account_TB AS SAVINGACC ON User_TB.USER_ID = SAVINGACC.USER_ID AND SAVINGACC.ACC_TYPE = 'SVNG'", "Failed to Get Admin Panel User Info Table Data");
    }

    public List<String[]> getAdminPanelTransactionHistoryTable() {
        return getTable("Admin Panel Transaction History Table", "TRSN_ID,ACC_ID,ACC_TYPE,TRSN_AMT,TRSN_TYPE", "SELECT TRSN_ID, account_tb.ACC_ID, ACC_TYPE, TRSN_AMT, TRSN_TYPE FROM account_tb JOIN history_tb ON account_tb.USER_ID = history_tb.USER_ID AND account_tb.ACC_ID = history_tb.ACC_ID;", "Failed to Get Admin Panel User Info Table Data");
    }

    public List<String[]> getUserTransactionHistoryTable(String username) {
        return getTable("User "+username+"'s Transaction History", "TRSN_ID,ACC_ID,ACC_TYPE,TRSN_AMT,TRSN_TYPE", "SELECT TRSN_ID, account_tb.ACC_ID, ACC_TYPE, TRSN_AMT, TRSN_TYPE FROM account_tb JOIN history_tb ON account_tb.USER_ID = history_tb.USER_ID AND account_tb.ACC_ID = history_tb.ACC_ID JOIN user_tb ON user_tb.USER_ID = account_tb.USER_ID WHERE user_tb.USERNAME = '"+username+"';", "Failed to Get User Transaction History Table Data");
    }

    public String[] login(String username, String pass) {
        return getRow("logging in as "+username+" with pass "+pass, "USER_ID,USERNAME,CHECKING_ACC_ID,SAVING_ACC_ID,ADMIN,ACTIVE,F_NAME,L_NAME,PASS,CREATED_ON", "SELECT * FROM User_TB where User_TB.USERNAME = '"+username+"' and User_TB.PASS = '"+pass+"';", "Unable to verify Login details");
    }

    public String[] getInfo(String username) {
        return getRow("getting user "+username+" info", "USERNAME,F_NAME,L_NAME,ADMIN,ACTIVE,CHECKING_BALANCE,SAVING_BALANCE,PASS,CREATED_ON", "SELECT USERNAME, F_NAME, L_NAME, ADMIN, ACTIVE, CHECKINGACC.BALANCE AS CHECKING_BALANCE, SAVINGACC.BALANCE AS SAVING_BALANCE, PASS, CREATED_ON FROM User_TB JOIN Account_TB AS CHECKINGACC ON User_TB.USER_ID = CHECKINGACC.USER_ID AND CHECKINGACC.ACC_TYPE = 'CHKN' JOIN Account_TB AS SAVINGACC ON User_TB.USER_ID = SAVINGACC.USER_ID AND SAVINGACC.ACC_TYPE = 'SVNG' WHERE USERNAME = '"+username+"'", "Unable to get User Information");
    }

    //Try to remember what this was for?
    public String[] getAccountInfo() {
        return null;
    }

    // ==== YourSQLBank Specific JDBC Query Methods ====
    public void AddTransaction() {
        //executeUpdateStatement("INSERT INTO history_tb (TRSN_TYPE, TRSN_AMT, USER_ID, ACC_ID) VALUES ('WDRW', 10, 1, 1)");
    }

    public void closeAccount() {

    }

    public void signUp() {

    }

//         INSERT INTO Account_TB (USER_ID, ACC_TYPE, BALANCE) values (0, "CHKN", 100);
// INSERT INTO Account_TB (USER_ID, ACC_TYPE, BALANCE) values (0, "SVNG", 200);
// INSERT INTO User_TB
//     (USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, EMAIL, F_NAME, L_NAME, PASS, PIN, CREATED_ON, LAST_LOGIN_DATE) values
//     ("aditya", 1, 2, FALSE, TRUE, "aditya7395@gmail.com", "Aditya", "Shah", "1234", "4321", "9999-12-31 23:59:59", "9999-12-31 23:59:59");
//     }

// INSERT INTO Account_TB (USER_ID, ACC_TYPE, BALANCE) values (1, "CHKN", 100);
// INSERT INTO Account_TB (USER_ID, ACC_TYPE, BALANCE) values (1, "SVNG", 100);
// INSERT INTO User_TB
//     (USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, F_NAME, L_NAME, PASS) values
//     ("groot", 1, 2, TRUE, TRUE, "I AM", "GROOT", "groot");

// INSERT INTO Account_TB (USER_ID, ACC_TYPE, BALANCE) values (2, "CHKN", 100);
// INSERT INTO Account_TB (USER_ID, ACC_TYPE, BALANCE) values (2, "SVNG", 100);
// INSERT INTO User_TB
//     (USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, F_NAME, L_NAME, PASS) values
//     ("soham", 3, 4, FALSE, TRUE, "Soham", "Shah", "pass");

// SELECT * FROM User_TB join Account_TB ON User_TB.USER_ID = Account_TB.USER_ID;


}
