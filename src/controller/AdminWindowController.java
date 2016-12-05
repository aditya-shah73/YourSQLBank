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
import model.YourSQLBank;

public class AdminWindowController {
    @FXML private StringProperty adminName; // stores the name of the admin for the greeting in gui.
    @FXML private TextField newAccountFirstName; //Stores input from GUI
    @FXML private TextField newAccountLastName; //Stores input from GUI
    @FXML private TextField newAccountUsername; //Stores input from GUI
    @FXML private TextField newAccountPassword; //Stores input from GUI
    @FXML private TextField newAccountInitialBalance; //Stores input from GUI
    @FXML private TextField usernameCloseAccount; //Store the which account username to close.


    @FXML private StringProperty userInfo;
    @FXML private StringProperty transactionInfo;

    String loggedInUsername = LoginLogoutController.username_;

    YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "mysqlrootpassword");

    /**
     * Constructor for AdminWindowController.
     */
    public AdminWindowController() {
        adminName = new SimpleStringProperty();
        userInfo = new SimpleStringProperty();
        transactionInfo = new SimpleStringProperty();
        String[] data = db.getInfo(loggedInUsername);
        setAdminName(data[1]+" "+data[2]);

        setUserInfo(YourSQLBank.printS(db.getAdminPanelUserInfoTable()));
        setTransactionInfo(YourSQLBank.printS(db.getAdminPanelTransactionHistoryTable()));

        System.out.println(YourSQLBank.printS(db.getAdminPanelUserInfoTable()));
        System.out.println(YourSQLBank.printS(db.getAdminPanelTransactionHistoryTable()));
    }

    /**
     * Gets administrator's name.
     * @return Administrator's name.
     */
    public String getAdminName() {
        return adminName.get();
    }

    /**
     * Set's the name of the administrator for throwing it up on gui.
     * @param administratorName name of the administrator.
     */
    public void setAdminName(String administratorName){
        adminName.set(administratorName);
    }

    public String getUserInfo() {
        return userInfo.get();
    }

    public void setUserInfo(String user_info){
        userInfo.set(user_info);
    }

    public String getTransactionInfo(){
        return transactionInfo.get();
    }

    public void setTransactionInfo(String transaction_info) {
        transactionInfo.set(transaction_info);
    }


    /**
     * When Create Account Button is clicked, all the user information for the new account is taken from the textfields,
     * and after correcting the formats, stored in the global variables, which are ready to be placed in the db.
     * @throws Exception
     */
    public void createAccount(ActionEvent actionEvent) throws Exception {
        db.signUp(newAccountUsername.getText(), false, newAccountFirstName.getText(), newAccountLastName.getText(), newAccountPassword.getText(), Double.parseDouble(newAccountInitialBalance.getText()));
        newAccountUsername.clear();
        newAccountFirstName.clear();
        newAccountLastName.clear();
        newAccountPassword.clear();
        newAccountInitialBalance.clear();
    }

    /**
     * When Close Account button is closed, this method is being called.
     * @param actionEvent
     */
    public void closeAccount(ActionEvent actionEvent) {

        db.closeAccount(this.usernameCloseAccount.getText());
        usernameCloseAccount.clear();
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
