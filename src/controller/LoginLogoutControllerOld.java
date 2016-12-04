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
            Scene adminPageScene = new Scene(adminPageParent, 2000, 1000);
            Stage adminStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            adminStage.setScene(adminPageScene);
            adminStage.show();
        } else if(userType.equals("normalUser")){
            Parent normalUserPageParent = FXMLLoader.load(getClass().getResource("/fxml/userwindow.fxml"));
            Scene normalUserPageScene = new Scene(normalUserPageParent, 2000, 1000);
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
        // String[] data = yourSqlBankObject.login(uName, pWord);

        //dummy code to see how it all works
//        if(uName.equals("admin")) {
//            return "admin";
//        }else if(uName.equals("normalUser")) {
//            return "normalUser";
//        }else {
//            return null; // remove this line later.
//        }
        try {
            ResultSet rs = yourSqlBankObject.executeQueryStatement("SELECT * FROM User_TB;");
            while(rs.next()) {
                if(uName.equals(rs.getString("USERNAME")) && pWord.equals(rs.getString("PASS"))) {
                    if(rs.getBoolean("ADMIN")) {

                       // controller.UserWindowController aUser = new controller.UserWindowController( ""+rs.getInt("USER_ID"), rs.getString("USERNAME"), ""+ rs.getInt("CHECKING_ACC_ID"), ""+rs.getInt("SAVING_ACC_ID"));
                        return "admin" ;
                    }else {

                    //    controller.UserWindowController nUser = new controller.UserWindowController( ""+rs.getInt("USER_ID"), rs.getString("USERNAME"), ""+ rs.getInt("CHECKING_ACC_ID"), ""+rs.getInt("SAVING_ACC_ID"));
                    //    System.out.println(nUser.toString());
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


}//End of LoginLogoutController class.
