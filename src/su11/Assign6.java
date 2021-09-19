package su11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ya Su
 */
public class Assign6 extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("CookiesFXML.fxml"));

        Scene scene = new Scene(root);

   
        scene.getStylesheets().add(getClass().getResource("Cookiesfxml.css")
                .toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Cookie Inventory");
        stage.show();

    }

}
