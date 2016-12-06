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
    @FXML private TextField loginUsername;
    @FXML private TextField loginPassword;

    public static String username_;
    public static YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "root");

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
