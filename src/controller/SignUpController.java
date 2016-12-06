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

public class SignUpController {
    @FXML private TextField newAccountFirstName;
    @FXML private TextField newAccountLastName;
    @FXML private TextField newAccountUsername;
    @FXML private TextField newAccountPassword;

    YourSQLBank db = LoginLogoutController.db;

    public void signUpAccount(ActionEvent actionEvent) throws Exception {
        LoginLogoutController.username_ = newAccountUsername.getText();
        db.signUp(newAccountUsername.getText(), false, newAccountFirstName.getText(), newAccountLastName.getText(), newAccountPassword.getText(), 0.0);
        Parent pageParent = FXMLLoader.load(getClass().getResource("/fxml/userwindow.fxml"));
        Scene pageScene = new Scene(pageParent, 1000, 800);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(pageScene);
        stage.show();
    }
}
