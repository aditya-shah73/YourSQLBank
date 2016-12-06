package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//RUN IN CMD WITH: cd C:\Users\dutta\Dropbox\Apps\Github\YourSQLBank\src\model && cls && javac YourSQLBank.java && java -cp mysql-connector-java-5.1.40-bin.jar; YourSQLBank

public class YourSQLBank {
    Connection connection;

    // ==== Testing Code ====
    public static void main(String[] argv) {
        YourSQLBank bankDB = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "root");
        bankDB.test();
    }

    private void test() {
        // print("SELECT * FROM User_TB join Account_TB ON User_TB.USERNAME = Account_TB.USERNAME;");
        // print("SELECT * FROM User_TB JOIN Account_TB ON User_TB.USERNAME = Account_TB.USERNAME JOIN History_TB ON User_TB.USERNAME = History_TB.USERNAME;");
        //Testing Admin Level Table Calls
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

        // print("SELECT * FROM User_TB JOIN Account_TB ON User_TB.USERNAME = Account_TB.USERNAME JOIN History_TB ON User_TB.USERNAME = History_TB.USERNAME;");
        // print("SELECT * FROM User_TB JOIN Account_TB_Archive ON User_TB.USERNAME = Account_TB_Archive.USERNAME JOIN History_TB_Archive ON User_TB.USERNAME = History_TB_Archive.USERNAME;");
        // closeAccount("groot");
        // print("SELECT * FROM User_TB JOIN Account_TB ON User_TB.USERNAME = Account_TB.USERNAME JOIN History_TB ON User_TB.USERNAME = History_TB.USERNAME;");
        // print("SELECT * FROM User_TB JOIN Account_TB_Archive ON User_TB.USERNAME = Account_TB_Archive.USERNAME JOIN History_TB_Archive ON User_TB.USERNAME = History_TB_Archive.USERNAME;");

        //Testing User Transaction History Insertion
        // print(getAdminPanelTransactionHistoryTable());
        // AddTransaction("groot", "WTDW", 50, "CHKG");
        // AddTransaction("groot", "DPST", 10, "SVNG");
        // AddTransaction("soham", "WTDW", 5, "CHKG");
        // AddTransaction("soham", "DPST", 10, "SVNG");
        // AddTransaction("FAIL", "WTDW", 0, "CHKG");
        // AddTransaction("FAIL", "DPST", 0, "SVNG");
        // print(getAdminPanelTransactionHistoryTable());

        //Testing User Insertion
        // print("SELECT * FROM User_TB join Account_TB ON User_TB.USERNAME = Account_TB.USERNAME;");
        // signUp("oindril", true, "Oindril", "Dutta", "odutta", 50, 100);
        // signUp("aditya", false, "Aditya", "Shah", "apass", 150, 0);
        // print("SELECT * FROM User_TB join Account_TB ON User_TB.USERNAME = Account_TB.USERNAME;");
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

    // ==== YourSQLBank Specific JDBC Query Methods ====
    public List<String[]> getAdminPanelUserInfoTable() {
        return getTable("Admin Panel User Info Table", "USERNAME,ACTIVE,CHECKING_BALANCE,SAVING_BALANCE",
        "SELECT User_TB.USERNAME, ACTIVE, CHECKINGACC.BALANCE AS CHECKING_BALANCE, SAVINGACC.BALANCE AS SAVING_BALANCE FROM User_TB JOIN Account_TB AS CHECKINGACC ON User_TB.USERNAME = CHECKINGACC.USERNAME AND CHECKINGACC.ACC_TYPE = 'CHKG' JOIN Account_TB AS SAVINGACC ON User_TB.USERNAME = SAVINGACC.USERNAME AND SAVINGACC.ACC_TYPE = 'SVNG' UNION SELECT User_TB.USERNAME, ACTIVE, Archive_CHECKINGACC.BALANCE AS CHECKING_BALANCE, Archive_SAVINGACC.BALANCE AS SAVING_BALANCE FROM User_TB JOIN Account_TB_Archive AS Archive_CHECKINGACC ON User_TB.USERNAME = Archive_CHECKINGACC.USERNAME AND Archive_CHECKINGACC.ACC_TYPE = 'CHKG' JOIN Account_TB_Archive AS Archive_SAVINGACC ON User_TB.USERNAME = Archive_SAVINGACC.USERNAME AND Archive_SAVINGACC.ACC_TYPE = 'SVNG';", "Failed to Get Admin Panel User Info Table Data");
    }

    public List<String[]> getAdminPanelTransactionHistoryTable() {
        return getTable("Admin Panel Transaction History Table", "TRSN_ID,ACC_ID,ACC_TYPE,TRSN_TYPE,TRSN_AMT", "SELECT TRSN_ID, Account_TB.ACC_ID, ACC_TYPE, TRSN_TYPE, TRSN_AMT FROM Account_TB JOIN History_TB ON Account_TB.USERNAME = History_TB.USERNAME AND Account_TB.ACC_ID = History_TB.ACC_ID UNION SELECT TRSN_ID, Account_TB_Archive.ACC_ID, ACC_TYPE, TRSN_TYPE, TRSN_AMT FROM Account_TB_Archive JOIN History_TB_Archive ON Account_TB_Archive.USERNAME = History_TB_Archive.USERNAME AND Account_TB_Archive.ACC_ID = History_TB_Archive.ACC_ID;", "Failed to Get Admin Panel User Info Table Data");
    }

    public List<String[]> getUserTransactionHistoryTable(String username) {
        return getTable("User "+username+"'s Transaction History", "TRSN_ID,ACC_ID,ACC_TYPE,TRSN_TYPE,TRSN_AMT", "SELECT TRSN_ID, Account_TB.ACC_ID, ACC_TYPE, TRSN_TYPE, TRSN_AMT FROM Account_TB JOIN History_TB ON Account_TB.USERNAME = History_TB.USERNAME AND Account_TB.ACC_ID = History_TB.ACC_ID JOIN User_TB ON User_TB.USERNAME = Account_TB.USERNAME WHERE User_TB.USERNAME = '"+username+"' UNION SELECT TRSN_ID, Account_TB_Archive.ACC_ID, ACC_TYPE, TRSN_TYPE, TRSN_AMT FROM Account_TB_Archive JOIN History_TB_Archive ON Account_TB_Archive.USERNAME = History_TB_Archive.USERNAME AND Account_TB_Archive.ACC_ID = History_TB_Archive.ACC_ID JOIN User_TB ON User_TB.USERNAME = Account_TB_Archive.USERNAME WHERE User_TB.USERNAME = '"+username+"';", "Failed to Get User Transaction History Table Data");
    }

    public String[] login(String username, String pass) {
        return getRow("logging in as "+username+" with pass "+pass, "USERNAME,CHECKING_ACC_ID,SAVING_ACC_ID,ADMIN,ACTIVE,F_NAME,L_NAME,PASS,CREATED_ON", "SELECT * FROM User_TB where User_TB.USERNAME = '"+username+"' and User_TB.PASS = '"+pass+"';", "Unable to verify Login details");
    }

    public String[] getInfo(String username) {
        return getRow("getting user "+username+" info", "USERNAME,F_NAME,L_NAME,ADMIN,ACTIVE,CHECKING_BALANCE,SAVING_BALANCE,PASS,CREATED_ON", "SELECT User_TB.USERNAME, F_NAME, L_NAME, ADMIN, ACTIVE, CHECKINGACC.BALANCE AS CHECKING_BALANCE, SAVINGACC.BALANCE AS SAVING_BALANCE, PASS, CREATED_ON  FROM User_TB JOIN Account_TB AS CHECKINGACC ON User_TB.USERNAME = CHECKINGACC.USERNAME AND CHECKINGACC.ACC_TYPE = 'CHKG' JOIN Account_TB AS SAVINGACC ON User_TB.USERNAME = SAVINGACC.USERNAME AND SAVINGACC.ACC_TYPE = 'SVNG' WHERE User_TB.USERNAME = '"+username+"' UNION SELECT User_TB.USERNAME, F_NAME, L_NAME, ADMIN, ACTIVE, CHECKINGACC_Archive.BALANCE AS CHECKING_BALANCE, SAVINGACC_Archive.BALANCE AS SAVING_BALANCE, PASS, CREATED_ON FROM User_TB JOIN Account_TB_Archive AS CHECKINGACC_Archive ON User_TB.USERNAME = CHECKINGACC_Archive.USERNAME AND CHECKINGACC_Archive.ACC_TYPE = 'CHKG' JOIN Account_TB_Archive AS SAVINGACC_Archive ON User_TB.USERNAME = SAVINGACC_Archive.USERNAME AND SAVINGACC_Archive.ACC_TYPE = 'SVNG' WHERE User_TB.USERNAME = '"+username+"';", "Unable to get User Information");
    }

    // ==== YourSQLBank Specific JDBC Update Methods ====
    public void AddTransaction(String username, String trsn_type, double amt, int acc_id) {
        //trsn_type can be WTDW for Withdraw and DPST for Deposit
        executeUpdateStatement("INSERT INTO History_TB (TRSN_TYPE, TRSN_AMT, USERNAME, ACC_ID) VALUES ('"+trsn_type+"', "+amt+", '"+username+"', "+acc_id+");", "Could not Insert Transaction");
    }

    public void AddTransaction(String username, String trsn_type, double amt, String acc_type) {
        //acc_type can be CHKG for Checking Account and SVNG for Saving Account
        String[] acc_id = getRow("getting acc_id from acc_type", "ACC_ID", "SELECT ACC_ID FROM account_tb WHERE ACC_TYPE = '"+acc_type+"' AND USERNAME = '"+username+"';", "Could not get ACC_ID from ACC_TYPE");
        if(acc_id != null)
            AddTransaction(username, trsn_type, amt, Integer.parseInt(acc_id[0]));
            //System.out.println(Integer.parseInt(acc_id[0]) + "\n");
    }

    public void signUp(String username, boolean admin, String fName, String lName, String pass, double checkingBalance, double savingBalance) {
        String error = "Failed in adding user "+username;
        executeUpdateStatement("INSERT INTO Account_TB (USERNAME, ACC_TYPE, BALANCE) values ('"+username+"', 'CHKG', "+checkingBalance+");", error);
        executeUpdateStatement("INSERT INTO Account_TB (USERNAME, ACC_TYPE, BALANCE) values ('"+username+"', 'SVNG', "+savingBalance+");", error);
        String[] acc_ids = getRow("getting acc_ids for user "+username+" insertion", "ACC_ID_A,ACC_ID_B", "SELECT acca.ACC_ID as ACC_ID_A, accb.ACC_ID as ACC_ID_B FROM Account_TB AS acca JOIN Account_TB AS accb WHERE acca.USERNAME = '"+username+"' AND accb.USERNAME = '"+username+"' AND acca.ACC_TYPE = 'CHKG' AND accb.ACC_TYPE = 'SVNG';", "This should not be happening. Could not get ACC_IDs from Account_TB for user insertion");
        executeUpdateStatement("INSERT INTO User_TB (USERNAME, CHECKING_ACC_ID, SAVING_ACC_ID, ADMIN, ACTIVE, F_NAME, L_NAME, PASS) values ('"+username+"', "+acc_ids[0]+", "+acc_ids[1]+", "+Boolean.toString(admin).toUpperCase()+", TRUE, '"+fName+"', '"+lName+"', '"+pass+"');", error);
    }

    public void signUp(String username, boolean admin, String fName, String lName, String pass, double balance) {
        signUp(username, admin, fName, lName, pass, balance, balance);
    }

    public void closeAccount(String username) {
        executeUpdateStatement("UPDATE User_TB SET ACTIVE = FALSE WHERE USERNAME = '"+username+"';", "Failed to close "+username+"'s' account'");
    }

    // ==== General Static Handler Code ====
    public static List<String[]> convertRStoArray(ResultSet rs) {
        try {
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
        } catch(Exception e) {
            handleError(e, "Failed to Convert RS to Array! Check output below:");
            return null;
        }
    }

    public void print(String command) {
        print(executeQueryStatement(command, "Cannot print command"));
    }

    public String printS(String command) {
        return printS(executeQueryStatement(command, "Cannot print command"));
    }

    public static void print(ResultSet rs) {
        if(rs != null)
            print(convertRStoArray(rs));
    }

    public static String printS(ResultSet rs) {
        if(rs == null)
            return null;
        return printS(convertRStoArray(rs));
    }

    public static void print(String[] lst) {
        if(lst == null || lst.length < 1)
            return;
        for(String s:lst)
            System.out.print(" " + s);
        System.out.println("\n");
    }

    public static String printS(String[] lst) {
        String temp = "";
        if(lst == null || lst.length < 1)
            return null;
        for(String s:lst)
            temp += "\t\t" + s;
        temp += "\n\n";
        return temp;
    }

    public static void print(List<String[]> rs) {
        if(rs == null || rs.isEmpty())
            return;
        for(String[] row:rs) {
            for(String s:row)
                System.out.print(" " + s);
            System.out.println();
        }
        System.out.println();
    }

    public static String printS(List<String[]> rs) {
        String temp = "";
        if(rs == null || rs.isEmpty())
            return null;
        for(String[] row:rs) {
            for(String s:row)
                temp += "\t\t" + s;
            temp += "\n";
        }
        temp += "\n";
        return temp;
    }

    public static void handleError(Exception e, String msg) {
        System.out.println(msg + "! Check output below:\n");
        System.out.println(e.getMessage() + "\n");
        e.printStackTrace();
        System.out.println();
    }
}