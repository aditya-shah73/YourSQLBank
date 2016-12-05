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
    @FXML private TextField newAccountFirstName;
    @FXML private TextField newAccountLastName;
    @FXML private TextField newAccountUsername;
    @FXML private TextField newAccountPassword;
    @FXML private TextField newAccountInitialBalance;
    @FXML private TextField usernameCloseAccount;

    @FXML private StringProperty adminName;
    @FXML private StringProperty userInfo;
    @FXML private StringProperty transactionInfo;
    String loggedInUsername = LoginLogoutController.username_;
    YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "root");

    public AdminWindowController() {
        adminName = new SimpleStringProperty();
        userInfo = new SimpleStringProperty();
        transactionInfo = new SimpleStringProperty();
        updateGui();
    }

    public void updateGui() {
        String[] data = db.getInfo(loggedInUsername);
        setAdminName(data[1]+" "+data[2]);
        setUserInfo("\n\tUSERNAME\tACTIVE\tCHECKING_BALANCE\tSAVING_BALANCE\n"+YourSQLBank.printS(db.getAdminPanelUserInfoTable()));
        setTransactionInfo("\n\tTRSN_ID\tACC_ID\tACC_TYPE\tTRSN_TYPE\tTRSN_AMT\n"+YourSQLBank.printS(db.getAdminPanelTransactionHistoryTable()));
    }

    public String getAdminName() {
        return adminName.get();
    }

    public StringProperty adminNameProperty() {
        return adminName;
    }

    public void setAdminName(String administratorName){
        adminName.set(administratorName);
    }

    public String getUserInfo() {
        return userInfo.get();
    }

    public StringProperty userInfoProperty() {
        return userInfo;
    }

    public void setUserInfo(String user_info){
        userInfo.set(user_info);
    }

    public String getTransactionInfo(){
        return transactionInfo.get();
    }

    public StringProperty transactionInfoProperty() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transaction_info) {
        transactionInfo.set(transaction_info);
    }

    public void createAccount(ActionEvent actionEvent) throws Exception {
        db.signUp(newAccountUsername.getText(), false, newAccountFirstName.getText(), newAccountLastName.getText(), newAccountPassword.getText(), Double.parseDouble(newAccountInitialBalance.getText()));
        newAccountUsername.clear();
        newAccountFirstName.clear();
        newAccountLastName.clear();
        newAccountPassword.clear();
        newAccountInitialBalance.clear();
        updateGui();
    }

    public void closeAccount(ActionEvent actionEvent) {
        db.closeAccount(this.usernameCloseAccount.getText());
        usernameCloseAccount.clear();
        updateGui();
    }

    public void showLogOutAdminWindow(ActionEvent actionEvent) throws Exception {
        Parent logoutPageParent = FXMLLoader.load(getClass().getResource("/fxml/goodbye.fxml"));
        Scene logoutPageScene = new Scene(logoutPageParent, 1000, 500);
        Stage logOutStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        logOutStage.setScene(logoutPageScene);
        logOutStage.show();
        Thread.sleep(2000);
        System.exit(0);
    }
}
