import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml")); // this is the actual fxml that should be called when main method is called.
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/userwindow.fxml"));// this line is to debug and check other fxml files.
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/adminwindow.fxml"));// this line is to debut and check other fxml files.
        primaryStage.setTitle("Your SQL Bank");
        primaryStage.setScene(new Scene(root, 3000, 1500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
