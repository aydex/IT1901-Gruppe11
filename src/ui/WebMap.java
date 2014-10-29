package ui;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.event.DocumentEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class WebMap implements Initializable {

    @FXML
    private AnchorPane maps;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // create web engine and view
/*
        AnchorPane.setLeftAnchor(Main.root.lookup("#subPane"), 0.0);
        AnchorPane.setRightAnchor(Main.root.lookup("#subPane"), 0.0);
        AnchorPane.setTopAnchor(Main.root.lookup("#subPane"), 0.0);
        AnchorPane.setBottomAnchor(Main.root.lookup("#subPane"), 0.0);
*/
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            float lat = 63.4305f;
                            float lon = 10.4219f;
                            String name = "Yolo";
                            String run = "document.addMarkerToMap(" + lat + ", " + lon + ", \"" + name + "\")";
                            webEngine.executeScript(run);
                        }
                    }
                });

        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) { webEngine.executeScript("document.zoomIn()"); }
        });
        Button zoomOut = new Button("Zoom Out");
        zoomOut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) { webEngine.executeScript("document.zoomOut()"); }
        });

        // create markers

        maps.getChildren().add(webView);

    }
}