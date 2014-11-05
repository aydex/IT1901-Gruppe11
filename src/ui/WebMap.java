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
import netscape.javascript.JSObject;

import javax.swing.event.DocumentEvent;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

public class WebMap implements Initializable {

    @FXML
    private AnchorPane maps;

    private static boolean webMap_loaded = false;

    private static final WebView webView = new WebView();
    private static final WebEngine webEngine = webView.getEngine();

    private static Queue<LatLong> locations = new LinkedList<LatLong>();
    private static Queue<String> names = new LinkedList<String>();

    public static JSObject jsObject;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // create web engine and view

        webEngine.load(getClass().getResource("googlemap.html").toString());

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        webMap_loaded = true;
                        if (newState == Worker.State.SUCCEEDED) {
                            jsObject = (JSObject) webEngine.executeScript("window");
                            jsObject.setMember("app", new Main());
                            while(!locations.isEmpty() || !names.isEmpty()){
                                String currentName = names.poll();
                                LatLong currentLatLong = locations.poll();
                                String run = "document.addMarkerToMap(" + currentLatLong.lat + ", " + currentLatLong.lon + ", \"" + currentName + "\")";
                                webEngine.executeScript(run);
                            }
//                        if (newState == Worker.State.)
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

    public static void onClick2(){
        System.out.println("HELLO!");
    }

    public static void addMarker(LatLong latLong, String name) {
        names.add(name);
        locations.add(latLong);
    }

    public static class LatLong{

        public float lat;
        public float lon;

        public LatLong(float lat, float lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

}