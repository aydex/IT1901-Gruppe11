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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
                ObservableList<Reservation> obsReservations = FXCollections.observableArrayList();
                for (Reservation r : DBConnect.getReservations()) {
                    if (r.getKoie_id() == cabin.getId()) {
                        obsReservations.add(r);
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
                Label def_label = new Label(def_string);

                //Table
                TableView<Reservation> table = new TableView<Reservation>();
                table.setEditable(true);
                TableColumn idCol = new TableColumn("ID");
                TableColumn numPersCol = new TableColumn("Persons");
                TableColumn fromCol = new TableColumn("From");
                TableColumn toCol = new TableColumn("To");
                TableColumn emailCol = new TableColumn("Email");
                //Legger til data i hver av kolonne
                idCol.setMinWidth(30);
                idCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("reservation_id"));

                numPersCol.setMinWidth(50);
                numPersCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("num_persons"));

                fromCol.setMinWidth(75);
                fromCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("date_from"));

                toCol.setMinWidth(75);
                toCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("test"));

                emailCol.setMinWidth(200);
                emailCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("email"));

                table.setItems(obsReservations);
                table.getColumns().addAll(idCol, numPersCol, fromCol, toCol, emailCol);

                //Styles skal etterhvert legges i en egen .css-fil
                content.setStyle("-fx-background-color: #FFF");
                main_header.setStyle("-fx-font-size: 40px; -fx-text-alignment: center");
                res_header.setStyle("-fx-font-size: 30px");
                def_header.setStyle("-fx-font-size: 30px");

                //Positions
                def_label.setLayoutY(120);
                def_label.setLayoutX(300);
                res_header.setLayoutY(70);
                def_header.setLayoutX(300);
                def_header.setLayoutY(70);
                table.setLayoutY(140);

                //Size
                int tableWidth = 500;

                main_header.setPrefWidth(500);
                table.setPrefWidth(tableWidth);


                //Legger til alle elementene i content.
                content.getChildren().addAll(main_header, res_header, def_header, def_label, table);

                if (v.getChildren().size() > 0)
                    v.getChildren().remove(0);
                v.getChildren().add(content);

            }
        });
    }
}

