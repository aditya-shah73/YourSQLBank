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
    @FXML private StringProperty userName;
    @FXML private StringProperty checkingAccountBalance;
    @FXML private StringProperty savingAccountBalance;
    @FXML private VBox mainWindow = new VBox();
    @FXML private GridPane userTableHeader = new GridPane();
    @FXML private Button depositCheckingButton = new Button();
    @FXML private Button depositSavingButton = new Button();
    @FXML private Button withdrawCheckingButton = new Button();
    @FXML private Button withdrawSavingButton = new Button();
    @FXML private TextField transactionAmount = new TextField();

    @FXML private StringProperty transactionHistory;
    // Add in Text Area for Transaction History

    String loggedInUsername = LoginLogoutController.username_;

    YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "mysqlrootpassword");

    /**
     * Constructor for AdminWindowController.
     */
    public UserWindowController() {
        userName = new SimpleStringProperty();
        checkingAccountBalance = new SimpleStringProperty();
        savingAccountBalance = new SimpleStringProperty();
        transactionHistory = new SimpleStringProperty();
        //depositCheckingButton.setDisable(false);
        String[] data = db.getInfo(loggedInUsername);
        setUserName(data[1]+" "+data[2]);
        setcheckingAccountBalance(data[5]);
        setSavingAccountBalance(data[6]);

        setTransactionHistory(YourSQLBank.printS(db.getUserTransactionHistoryTable(loggedInUsername)));


        // showUserTableData(yourSqlBankObject.getUserTransactionHistoryTable(loggedInUsername));
        System.out.println(YourSQLBank.printS(db.getUserTransactionHistoryTable(loggedInUsername)));


        //if(data[4] == "0")
           // doSomething("To disable all the buttons!");
    }

    /**
     * Gets user's username.
     * @return user's username.
     */
    public String getUserName() {
        return userName.get();
    }

    /**
     * Set's the username of the user for throwing it up on gui.
     * @param user_name name of the administrator.
     */
    public void setUserName(String user_name) {
        userName.set(user_name);
    }

    public String getTransactionHistory(){
        return transactionHistory.get();
    }

    public void setTransactionHistory(String transaction_history) {
        transactionHistory.set(transaction_history);
    }

    /**
     * Gets user's checking account balance.
     * @return checking account balance.
     */
    public String getCheckingAccountBalance() {
        return checkingAccountBalance.get();
    }

    /**
     * Set's the checking account balance of the user for throwing it up on gui.
     * @param checkingAccount_Balance of the user.
     */
    public void setcheckingAccountBalance(String checkingAccount_Balance) {
        checkingAccountBalance.set(checkingAccount_Balance);
    }

    /**
     * Gets user's saving account balance.
     * @return saving account balance.
     */
    public String getSavingAccountBalance() {
        return savingAccountBalance.get();
    }

    /**
     * Set's the saving account balance of the user for throwing it up on gui.
     * @param savingAccount_Balance of the user.
     */
    public void setSavingAccountBalance(String savingAccount_Balance) {
        savingAccountBalance.set(savingAccount_Balance);
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
     * Deposites money in checking account
     */
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
        if(data[4].contains("1"))
            db.AddTransaction(loggedInUsername, "WTDW", formattedAmount(this.transactionAmount), "SVNG");
        transactionAmount.clear();
        System.out.println(data[4].contains("1"));
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

    public void closeAccount(ActionEvent actionEvent) {
        db.closeAccount(loggedInUsername);
    }
}

