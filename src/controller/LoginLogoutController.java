package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.YourSQLBank;

public class LoginLogoutController {
    // ==== LoginLogoutController Global Static Code ====
    public static String username_;
    public static YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "root");

    public static void displayAlert(ActionEvent e, String summary, String detailed, String description) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(((Node) e.getTarget()).getScene().getWindow());
        alert.setTitle(summary);
        alert.setHeaderText(detailed);
        alert.setContentText(description);
        alert.showAndWait();
    }

    // ==== LoginLogoutController Specific Code ====
    @FXML private TextField loginUsername;
    @FXML private TextField loginPassword;

    public void showLoggedInWindow(ActionEvent actionEvent) throws Exception {
        String userType = checkUserInfoInDb(loginUsername.getText(), loginPassword.getText());
        if(userType == null) {
            LoginLogoutController.displayAlert(actionEvent, "Incorrect Credentials", "You put in a wrong username and password.", "Please input your correct login credentials next time...");
            return;
        }
        Parent pageParent = FXMLLoader.load(getClass().getResource("/fxml/"+userType+"window.fxml")); // admin
        Scene pageScene = new Scene(pageParent, 1000, 800);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(pageScene);
        stage.show();
    }

    public String checkUserInfoInDb(String uName, String pWord) {
        String[] data = db.login(uName, pWord);
        if(data == null)
            return null;
        YourSQLBank.print(data);
        username_ = data[0];
        return Boolean.parseBoolean(data[3]) ? "admin" : "user";
    }

    public void showSignUpWindow(ActionEvent actionEvent) throws Exception {
        Parent signUpPageParent = FXMLLoader.load(getClass().getResource("/fxml/signup.fxml"));
        Scene signUpPageScene = new Scene(signUpPageParent, 1000, 500);
        Stage signUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        signUpStage.setScene(signUpPageScene);
        signUpStage.show();
    }

    public void showQuitLoginLogoutWindow(ActionEvent actionEvent) throws Exception {
        Parent logoutPageParent = FXMLLoader.load(getClass().getResource("/fxml/goodbye.fxml"));
        Scene logoutPageScene = new Scene(logoutPageParent, 1000, 500);
        Stage logOutStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        logOutStage.setScene(logoutPageScene);
        logOutStage.show();
        Thread.sleep(2000);
        System.exit(0);
    }
}
