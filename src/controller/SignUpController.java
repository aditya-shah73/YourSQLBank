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
    @FXML private TextField newAccountFirstName; //Stores input from GUI
    @FXML private TextField newAccountLastName; //Stores input from GUI
    @FXML private TextField newAccountUsername; //Stores input from GUI
    @FXML private TextField newAccountPassword; //Stores input from GUI

    YourSQLBank db = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "mysqlrootpassword");

    /**
     * When Create Account Button is clicked, all the user information for the new account is taken from the textfields,
     * and after correcting the formats, stored in the global variables, which are ready to be placed in the db.
     * @throws Exception
     */
    public void signUpAccount(ActionEvent actionEvent) throws Exception {
        LoginLogoutController.username_ = newAccountUsername.getText();
        db.signUp(newAccountUsername.getText(), false, newAccountFirstName.getText(), newAccountLastName.getText(), newAccountPassword.getText(), 0.0);
        Parent pageParent = FXMLLoader.load(getClass().getResource("/fxml/userwindow.fxml")); // admin
        Scene pageScene = new Scene(pageParent, 1000, 800);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(pageScene);
        stage.show();

    }
}
