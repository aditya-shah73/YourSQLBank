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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.YourSQLBank;
import java.sql.ResultSet;
import java.util.List;

public class UserWindowController {
    @FXML private VBox mainWindow = new VBox();
    @FXML private GridPane userTableHeader = new GridPane();
    @FXML private Button depositCheckingButton = new Button();
    @FXML private Button depositSavingButton = new Button();
    @FXML private Button withdrawCheckingButton = new Button();
    @FXML private Button withdrawSavingButton = new Button();
    @FXML private TextField transactionAmount = new TextField();

    @FXML private StringProperty userName;
    @FXML private StringProperty checkingAccountBalance;
    @FXML private StringProperty savingAccountBalance;
    @FXML private StringProperty transactionHistory;
    String loggedInUsername = LoginLogoutController.username_;
    YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "root");

    public UserWindowController() {
        userName = new SimpleStringProperty();
        checkingAccountBalance = new SimpleStringProperty();
        savingAccountBalance = new SimpleStringProperty();
        transactionHistory = new SimpleStringProperty();
        updateGui();
    }

    public void updateGui() {
        System.out.println("Updating Gui...");
        String[] data = db.getInfo(loggedInUsername);
        setUserName(data[1]+" "+data[2]);
        setcheckingAccountBalance(data[5]);
        setSavingAccountBalance(data[6]);
        setTransactionHistory("\n\tTRSN_ID\tACC_ID\tACC_TYPE\tTRSN_TYPE\tTRSN_AMT\n"+YourSQLBank.printS(db.getUserTransactionHistoryTable(loggedInUsername)));
        //if(data[4].contains("1"))
           // doSomething("To disable all the buttons!");
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String user_name) {
        userName.set(user_name);
    }

    public String getTransactionHistory() {
        return transactionHistory.get();
    }

    public StringProperty transactionHistoryProperty() {
        return transactionHistory;
    }

    public void setTransactionHistory(String transaction_history) {
        transactionHistory.set(transaction_history);
    }

    public String getCheckingAccountBalance() {
        return checkingAccountBalance.get();
    }

    public StringProperty checkingAccountBalanceProperty() {
        return checkingAccountBalance;
    }

    public void setcheckingAccountBalance(String checkingAccount_Balance) {
        checkingAccountBalance.set(checkingAccount_Balance);
    }

    public String getSavingAccountBalance() {
        return savingAccountBalance.get();
    }

    public StringProperty savingAccountBalanceProperty() {
        return savingAccountBalance;
    }

    public void setSavingAccountBalance(String savingAccount_Balance) {
        savingAccountBalance.set(savingAccount_Balance);
    }

    public double formattedAmount(TextField amount) {
        String amountS = amount.getText();
        double amountD = Double.parseDouble(amountS);
        return amountD;
    }

    public void depositMoneyInChecking(ActionEvent actionEvent) throws Exception {
        createTransaction("DPST", formattedAmount(this.transactionAmount), "CHKG");
    }

    public void depositMoneyInSaving() throws Exception {
        createTransaction("DPST", formattedAmount(this.transactionAmount), "SVNG");
    }

    public void withdrawFromChecking() throws Exception {
        createTransaction("WTDW", formattedAmount(this.transactionAmount), "CHKG");
    }

    public void withdrawFromSaving() throws Exception {
        createTransaction("WTDW", formattedAmount(this.transactionAmount), "SVNG");
    }

    public void createTransaction(String transaction_type, double amount, String account_type) {
        String[] data = db.getInfo(loggedInUsername);
        if(data[4].contains("1") && safeTransaction(loggedInUsername, transaction_type, amount, account_type, data))
            db.AddTransaction(loggedInUsername, transaction_type, amount, account_type);
        transactionAmount.clear();
        updateGui();
    }

    public boolean safeTransaction(String loggedInUsername, String transaction_type, double amount, String account_type, String[] data) {
        //Implement Checking System...
        return true;
    }

    public void closeAccount(ActionEvent actionEvent) {
        db.closeAccount(loggedInUsername);
        updateGui();
    }

    public void showLogOutUserWindow(ActionEvent actionEvent) throws Exception {
        Parent logoutPageParent = FXMLLoader.load(getClass().getResource("/fxml/goodbye.fxml"));
        Scene logoutPageScene = new Scene(logoutPageParent, 1000, 500);
        Stage logOutStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        logOutStage.setScene(logoutPageScene);
        logOutStage.show();
        Thread.sleep(2000);
        System.exit(0);
    }
}

