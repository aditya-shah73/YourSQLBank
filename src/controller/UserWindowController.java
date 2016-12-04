package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by sohamshah on 11/27/16.
 */
public class UserWindowController {

    @FXML private StringProperty userName; // stores the name of the admin for the greeting in gui.
    @FXML private TextField transactionAmount;
    @FXML private TextField transactionDescription;


    /**
     * Constructor for AdminWindowController.
     */
    public UserWindowController(){
        userName = new SimpleStringProperty();
        setUserName("Soham");// Need a method that returns the name of admin as a string from db. which is found by the username entered while logging in.
        //place the above line in th method that queries the database for the name of the admin by using username and password.
    }

    /**
     * Gets user's name.
     * @return User's name.
     */
    public String getUserName() {
        return userName.get();
    }

    /**
     * To greet user with his/her name, passes the user's name to fxml file.
     * @return User's name that can be passed to fxml file.
     */
    public StringProperty userNameProperty() {
        return userName;
    }

    /**
     * Set's the name of the administrator for throwing it up on gui.
     * @param user_Name name of the administrator.
     */
    public void setUserName(String user_Name){
        userName.set(user_Name);
    }

    /**
     * when checking account button is pressed, checking account detail is shown.
     */
    public void showCheckingAccount() {


    }

    /**
     * when saving account button is pressed, saving account detail is shown.
     */
    public void showSavingAccount() {


    }

    /**
     * Takes in transaction amount from gui, and converts it to a double value
     * @param amount transaction amount from gui.
     * @return double amount.
     */
    public double formattedAmount(TextField amount) {

        String amountS = amount.getText();
        double amountD = Double.parseDouble(amountS);
        return amountD;
    }

    /**
     * Takes in transaction double from gui, and converts it to a string value.
     * @param description
     * @return
     */
    public String formattedDescription(TextField description) {

        String descriptionS = description.getText();
        return descriptionS;
    }

    /**
     * Deposites money in checking account
     */
    public void depositMoneyInChecking() throws Exception{

        double tAmount = formattedAmount(this.transactionAmount);
        String tDescription = formattedDescription(this.transactionDescription);

        //add a query that deposits tAmount in checking account, and updates balance. and adds tDescription with it.

    }

    public void depositMoneyInSaving() throws Exception{

        double tAmount = formattedAmount(this.transactionAmount);
        String tDescription = formattedDescription(this.transactionDescription);

        //add a query that deposits tAmount in saving account, and updates balance. and adds tDescription with it.

    }

    public void withdrawFromChecking() throws Exception {

        double tAmount = formattedAmount(this.transactionAmount);
        String tDescription = formattedDescription(this.transactionDescription);

        //add a query that withdraws tAmount from checking account, and updates balance. and adds tDescription with it.
    }

    public void withdrawFromSaving() throws Exception {

        double tAmount = formattedAmount(this.transactionAmount);
        String tDescription = formattedDescription(this.transactionDescription);

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
