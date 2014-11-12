package ui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class WebMap implements Initializable {

    @FXML
    private AnchorPane maps;

    private static final WebView webView = new WebView();
    private static final WebEngine webEngine = webView.getEngine();

    private static JSObject jsObject;

    private static Map<String, LatLong> markers = new HashMap<String, LatLong>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // create web engine and view

        webEngine.load(getClass().getResource("googlemap.html").toString());

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            jsObject = (JSObject) webEngine.executeScript("window");
                            //jsObject.setMember("app", new JavaApp());
                            for (Map.Entry<String, LatLong> entry : markers.entrySet())
                            {
                                String currentName = entry.getKey();
                                LatLong currentLatLong = entry.getValue();
                                String run = "document.addMarkerToMap(" + currentLatLong.lat + ", " + currentLatLong.lon + ", \"" + currentName + "\")";
                                webEngine.executeScript(run);
                            }
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
        markers.put(name, latLong);
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