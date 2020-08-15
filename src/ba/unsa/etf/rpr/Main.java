package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        KindergartenDAO dao = KindergartenDAO.getInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("Translate", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_screen.fxml"), bundle);
        HomeScreenController ctrl = new HomeScreenController(dao);
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Kindergarten App");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
