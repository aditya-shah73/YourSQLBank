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

import java.io.IOException;

public class AdminWindowController {
    @FXML private StringProperty adminName; // stores the name of the admin for the greeting in gui.

    @FXML private TextField newAccountFirstName; //Stores input from GUI
    @FXML private TextField newAccountLastName; //Stores input from GUI
    @FXML private TextField newAccountUsername; //Stores input from GUI
    @FXML private TextField newAccountPassword; //Stores input from GUI
    @FXML private TextField newAccountInitialBalance; //Stores input from GUI

    private String newFirstName; //holds first name for new account
    private String newLastName; //holds last name for new account
    private String newUsername; //holds username for new account
    private String newPassword; //holds password for new account
    private double newBalance; //holds  balance for new account

    /**
     * Constructor for AdminWindowController.
     */
    public AdminWindowController() {
        adminName = new SimpleStringProperty();
        setAdminName(LoginLogoutController.username_);// Need a method that returns the name of admin as a string from db. which is found by the username entered while logging in.
        //place the above line in th method that queries the database for the name of the admin by using username and password.
    }

    /**
     * Gets administrator's name.
     * @return Administrator's name.
     */
    public String getAdminName() {
        return adminName.get();
    }

    /**
     * To greet administrator with his/her name, passes the administrator name to fxml file.
     * @return Administrator's name that can be passed to fxml file.
     */
    public StringProperty adminNameProperty() {
        return adminName;
    }

    /**
     * Set's the name of the administrator for throwing it up on gui.
     * @param administratorName name of the administrator.
     */
    public void setAdminName(String administratorName){
        adminName.set(administratorName);
    }


    /**
     * When Create Account Button is clicked, all the user information for the new account is taken from the textfields,
     * and after correcting the formats, stored in the global variables, which are ready to be placed in the db.
     * @throws Exception
     */
    public void createAccount(ActionEvent actionEvent)throws Exception {

        this.newFirstName = newAccountFirstName.getText();
        this.newLastName = newAccountLastName.getText();
        this.newUsername = newAccountUsername.getText();
        this.newPassword = newAccountPassword.getText();
        this.newBalance = Double.parseDouble(newAccountInitialBalance.getText());

        createAccountInDB(  this.newFirstName,
                            this.newLastName,
                            this.newUsername,
                            this.newPassword,
                            this.newBalance);
    }

    /**
     * creates an account in Database with mysql query.
     * @param fName first name
     * @param lName last name
     * @param uName username
     * @param pWord password
     * @param balanceAmount initial balance.
     */
    public void createAccountInDB(String fName, String lName, String uName, String pWord, double balanceAmount) {


    }

    /**
     * Shows Log out window, when log out button is pressed.
     * @param actionEvent
     * @throws Exception
     */
    public void showLogOutAdminWindow(ActionEvent actionEvent) throws Exception {
        Parent logoutPageParent = FXMLLoader.load(getClass().getResource("/fxml/goodbye.fxml"));
        Scene logoutPageScene = new Scene(logoutPageParent, 1000, 500);
        Stage logOutStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        logOutStage.setScene(logoutPageScene);
        logOutStage.show();
    }

}
