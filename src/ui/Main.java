package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    static Pane root;
    public static Main main;
    @Override
    public void start(Stage primaryStage) throws Exception {
        main = this;
        root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setTitle("Koie");
        primaryStage.setScene(new Scene(root, 1000, 815));
        primaryStage.setResizable(false);
        primaryStage.show();
        Controller con = new Controller();
        con.deployCabins();
    }

    public static Pane getRoot(){
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
