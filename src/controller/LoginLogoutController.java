package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.YourSQLBank;

public class LoginLogoutController {
    @FXML private TextField loginUsername; // this stores username of the user that he/she inputs in the gui login window.
    @FXML private TextField loginPassword; // this stores password of the user that he/she inputs in the gui login window.

    public static String username_;
    YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "mysqlrootpassword");

    /**
     * Takes in Username and password from gui, and shows appropriate account window.
     * @param actionEvent
     */
    public void showLoggedInWindow(ActionEvent actionEvent) throws Exception {
        String userType = checkUserInfoInDb(loginUsername.getText(), loginPassword.getText());
        if(userType == null)
            return;
        Parent pageParent = FXMLLoader.load(getClass().getResource("/fxml/"+userType+"window.fxml")); // admin
        Scene pageScene = new Scene(pageParent, 1000, 800);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(pageScene);
        stage.show();
    }

    /**
     * Takes UserInput from login window namely, username and password, and checks for existence of the user in the db.
     * if the user exists, the method returns "admin" or "user".
     * @return "admin" or "user"
     */
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
}
