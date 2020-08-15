package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        KindergartenDAO dao = KindergartenDAO.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home_screen.fxml"), dao.getBundle());
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
