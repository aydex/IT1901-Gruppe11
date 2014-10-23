package ui;

import db.Cabin;
import db.DBConnect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    static Pane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
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
