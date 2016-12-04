package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Statement;
import java.sql.ResultSet;
import model.YourSQLBank;

public class LoginLogoutController {

    @FXML private TextField loginUsername; // this stores username of the user that he/she inputs in the gui login window.
    @FXML private TextField loginPassword; // this stores password of the user that he/she inputs in the gui login window.

    private String loginUsernameString;
    private String loginPasswordString;

    public static String username_;
    public String firstName_; //--
    public Double checkingBalance_; //--
    public Double savingBalance_; //--

    YourSQLBank yourSqlBankObject = new YourSQLBank("jdbc:mysql://localhost:3306/YourSQLBank_DB", "root", "mysqlrootpassword");


    /**
     * Takes in Username and password from gui, and shows appropriate account window.
     * @param actionEvent
     */
    public void showLoggedInWindow(ActionEvent actionEvent) throws Exception {
        this.loginUsernameString = loginUsername.getText();
        this.loginPasswordString = loginPassword.getText();

        String userType = checkUserInfoInDb(loginUsernameString, loginPasswordString);
        if(userType == null) {

            System.out.println("something is null");

        } else if(userType.equals("admin")){

            Parent adminPageParent = FXMLLoader.load(getClass().getResource("/fxml/adminwindow.fxml"));
            Scene adminPageScene = new Scene(adminPageParent, 1000, 800);
            Stage adminStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            adminStage.setScene(adminPageScene);
            adminStage.show();

        }else if(userType.equals("normalUser")){

            Parent normalUserPageParent = FXMLLoader.load(getClass().getResource("/fxml/userwindow.fxml"));
            Scene normalUserPageScene = new Scene(normalUserPageParent, 1000, 800);
            Stage normalUserStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            normalUserStage.setScene(normalUserPageScene);
            normalUserStage.show();

        }

    }//End of dummyLoginMethod()

    /**
     * Takes UserInput from login window namely, username and password, and checks for existence of the user in the db.
     * if the user exists, the method returns "admin" or "normalUser".
     * @return "admin" or "normalUser"
     */
    public String checkUserInfoInDb(String uName, String pWord) {

        try {
            ResultSet rs = yourSqlBankObject.executeQueryStatement("SELECT * FROM User_TB;");
            while(rs.next()) {
                if(uName.equals(rs.getString("USERNAME")) && pWord.equals(rs.getString("PASS"))) {
                    if(rs.getBoolean("ADMIN")) {

                        gatherInfo(rs.getString("USERNAME"));
                        return "admin" ;
                    }else {
                        gatherInfo(rs.getString("USERNAME"));
                        return "normalUser";
                    }
                }

            }


            return null;
        } catch(Exception e) {
            yourSqlBankObject.handleError(e, "Failed to Execute Query Statement! Check output below:");
            return null;

        }


    }//End of checkUserInDb method.

    public static void gatherInfo(String uName){

        username_ = uName;

    }


}//End of LoginLogoutController class.
