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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.AnchorPane;

/**
 * This class contains the controller for the WebView that displays the Google Maps page.
 *
 * @author Sondre Slåttedal Havellen
 */

public class WebMap implements Initializable {

    @FXML
    private AnchorPane maps;

    private static final WebView webView = new WebView();
    private static final WebEngine webEngine = webView.getEngine();
    private static Map<String, LatLong> markers = new HashMap<String, LatLong>();


    /**
     * Initializes the WebMap and loads JavaScript into an webengine in order for JavaFX
     * to communicate with the webview.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // create web engine and view

        webEngine.load(getClass().getResource("googlemap.html").toString());

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
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

        webView.setPrefWidth(790);
        webView.setPrefHeight(786);
        maps.getChildren().add(webView);

    }

    /**
     * This function lets other instances add markers to the map before the html-page
     * is loaded. This is done by filling a HashMap, <i>#markers</i> with locations, <code>latLong</code>,
     * and names, <code>name</code> which the eventlistener "changed" uses when the document is finished
     * loading.
     * @param latLong The location stored in a <code>LatLong</code>-object.
     * @param name The name of the cabin.
     */
    public static void addMarker(LatLong latLong, String name) {
        markers.put(name, latLong);
    }

    /**
     * This class stores the latitude and longitude of a marker as a single object.
     */
    public static class LatLong{


        /**
         * Stores the latitude.
         */
        public float lat;

        /**
         * Stores the longitude.
         */
        public float lon;

        /**
         * Initializes the LatLong-object.
         * @param lat Input latitude.
         * @param lon Input longitude.
         */
        public LatLong(float lat, float lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

}