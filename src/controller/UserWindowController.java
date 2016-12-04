package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.YourSQLBank;

import java.sql.ResultSet;

/**
 * Created by sohamshah on 11/27/16.
 */
public class UserWindowController {

    @FXML private StringProperty userName; // stores the name of the admin for the greeting in gui.

    @FXML private TextField transactionAmount;

    YourSQLBank yourSqlBankObject = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "mysqlrootpassword");

    @FXML private Button depositCheckingButton;
    @FXML private Button depositSavingButton;
    @FXML private Button withdrawCheckingButton;
    @FXML private Button withdrawSavingButton;



    /**
     * Constructor for AdminWindowController.
     */
    public UserWindowController(){
        userName = new SimpleStringProperty();
        //depositCheckingButton.setDisable(false);
        try {
            ResultSet rs = yourSqlBankObject.executeQueryStatement("SELECT * FROM User_TB WHERE USERNAME = '"+LoginLogoutController.username_+"';");
            while(rs.next()) {
                System.out.println(rs.getString("USERNAME"));
            }

        } catch(Exception e) {
            yourSqlBankObject.handleError(e, "Failed to Execute Query Statement! Check output below:");

        }

        setUserName(LoginLogoutController.username_);// Need a method that returns the name of admin as a string from db. which is found by the username entered while logging in.
        //place the above line in th method that queries the database for the name of the admin by using username and password.

    }



    /**
     * Gets administrator's name.
     * @return Administrator's name.
     */
    public String getUserName() {
        return userName.get();
    }

    /**
     * To greet administrator with his/her name, passes the administrator name to fxml file.
     * @return Administrator's name that can be passed to fxml file.
     */
    public StringProperty userNameProperty() {
        return userName;
    }

    /**
     * Set's the name of the administrator for throwing it up on gui.
     * @param user_name name of the administrator.
     */
    public void setUserName(String user_name){
        userName.set(user_name);
    }


    /**
     * Takes in transaction amount from gui, and converts it to a double value
     * @param amount transaction amount from gui.
     * @return double amount.
     */
    public double formattedAmount(TextField amount) {

//        String amountS = amount.getText();
//        double amountD = Double.parseDouble(amountS);
//        return amountD;
        return 0;
    }

    /**
     * Deposites money in checking account
     */
    public void depositMoneyInChecking(ActionEvent actionEvent) throws Exception{

     //     double tAmount = formattedAmount(this.transactionAmount);
        //query that makes transaction type to depositchecking.

        //add a query that deposits tAmount in checking account, and updates balance.

    }

    public void depositMoneyInSaving() throws Exception{

        double tAmount = formattedAmount(this.transactionAmount);
        //query that makes transaction type to depositsaving

        //add a query that deposits tAmount in saving account, and updates balance.

    }

    public void withdrawFromChecking() throws Exception {

        double tAmount = formattedAmount(this.transactionAmount);
        //query that makes transaction type to withdrawchecking

        //add a query that withdraws tAmount from checking account, and updates balance.
    }

    public void withdrawFromSaving() throws Exception {

        double tAmount = formattedAmount(this.transactionAmount);
        //query that makes transaction type to withdrawsaving.

        //add a query that withdraws tAmount from saving account, and updates balance. and adds tDescription with it.

    }

    /**
     * Once the user decides to logout and clicks on the logoutbutton, logout window gets displayed.
     * @param actionEvent
     * @throws Exception
     */
    public void showLogOutUserWindow(ActionEvent actionEvent) throws Exception {
        Parent logoutPageParent = FXMLLoader.load(getClass().getResource("/fxml/goodbye.fxml"));
        Scene logoutPageScene = new Scene(logoutPageParent, 1000, 500);
        Stage logOutStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        logOutStage.setScene(logoutPageScene);
        logOutStage.show();
    }
}
