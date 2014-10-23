package ui;

import db.Cabin;
import db.DBConnect;
import db.Report;
import db.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class Controller {


    public void onCabinClicked() {

    }

    public void deployCabins() {
        ArrayList<Cabin> cabinList = DBConnect.getCabins();
        final ListView t = (ListView) Main.getRoot().lookup("#hytteListe");
        ObservableList<Cabin> items = FXCollections.observableArrayList();
        //Legger alle "Cabins" inn i ListView
        for (Cabin v : cabinList) {
            items.add(v);
        }
        t.setItems(items);
        //Legger til funksjon til elementene i listen, som kjøres når de klikkes på.
        t.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final AnchorPane v = (AnchorPane) Main.getRoot().lookup("#info");
                Cabin cabin = (Cabin) t.getSelectionModel().getSelectedItem();

                //Finner alle reservasjoner på hytta. Legger de til i ArrayListen reservations
                ArrayList<Reservation> reservations = new ArrayList<Reservation>();
                String res_string = "";
                for (Reservation r : DBConnect.getReservations()) {
                    if (r.getKoie_id() == cabin.getId()) {
                        reservations.add(r);
                        res_string +=
                                "ID: " + r.getReservation_id() + " \n" + "Email: " + r.getEmail() +
                                        "\n" + "Antall personer: " + r.getNum_persons() + "\n" + "Fra dato: " +
                                        r.getDate_from() + "\n" + "Til dato: " + r.getDate_to() + "\n" +
                                        "------------------------" + "\n";
                    }
                }
                //Finner alle mangler på hytta. Legger de til i ArrayListen deficiency
                ArrayList<Report> reports = new ArrayList<Report>();
                String def_string = "";
                for (Report rep : DBConnect.getReports()) {
                    if (rep.getKoie_id() == cabin.getId()) {
                        reports.add(rep);
                        def_string +=
                                "ID: " + rep.getReport_id() + " \n\n" + "Rapport: " + "\n" + rep.getDeficiency() +
                                        "\n" + "------------------------" + "\n";
                    }
                }

                //Lager en AnchorPane hvor alle elementene legges. AnchorPanenen blir dermed lagt til i rot-Panen.
                AnchorPane content = new AnchorPane();
                content.setPrefHeight(600);
                content.setPrefWidth(900);

                //Elementer som skal legges i content
                Label res_header = new Label("Reservasjoner");
                Label def_header = new Label("Rapporter");
                Label main_header = new Label(cabin.getName());
                Label res_label = new Label(res_string);
                Label def_label = new Label(def_string);

                //Styles skal etterhvert legges i en egen .css-fil
                content.setStyle("-fx-background-color: #FFF");
                main_header.setStyle("-fx-font-size: 40px; -fx-text-alignment: center");
                res_header.setStyle("-fx-font-size: 30px");
                def_header.setStyle("-fx-font-size: 30px");

                //Positions
                res_label.setLayoutY(120);
                res_label.setLayoutX(10);
                def_label.setLayoutY(120);
                def_label.setLayoutX(300);
                res_header.setLayoutY(70);
                def_header.setLayoutX(300);
                def_header.setLayoutY(70);

                //Size
                main_header.setPrefWidth(500);

                //Legger til alle elementene i content.
                content.getChildren().addAll(main_header, res_header, def_header, res_label, def_label);

                if (v.getChildren().size() > 0)
                    v.getChildren().remove(0);
                v.getChildren().add(content);

            }
        });
    }
}

