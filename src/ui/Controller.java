package ui;

import db.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    static ArrayList<Cabin> cabinList = GetData.getCabins();
    static ArrayList<Reservation> reservations = GetData.getReservations();
    static ArrayList<Report> reports = GetData.getReports();
    private SendMail sm = new SendMail();

    public void deployCabins() {

        final ListView t = (ListView) Main.getRoot().lookup("#hytteListe");
        final int[] selected_reservation_id = new int[1];
        ObservableList<Cabin> items = FXCollections.observableArrayList();

        //Legger alle "Cabins" inn i ListView
        for (Cabin v : cabinList) {
            items.add(v);
            WebMap.addMarker(v.getCoords(), v.getName());
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
                for (Reservation r : GetData.getStatsByCabin(cabin.getId())) {
                    obsReservations.add(r);
                }
                //Finner alle mangler på hytta. Legger de til i ArrayListen deficiency
                ArrayList<Report> cabin_reports = new ArrayList<Report>();
                String def_string = "";
                for (Report rep : reports) {
                    if (rep.getKoie_id() == cabin.getId()) {
                        cabin_reports.add(rep);
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
                //Headers
                Label res_header = new Label("Reservasjoner");
                Label def_header = new Label("Rapporter");
                Label main_header = new Label(cabin.getName());
                Label exceptionOutPut = new Label("");

                //Reports
                Text reports_text = new Text(def_string);
                ScrollPane reports_pane = new ScrollPane();
                reports_pane.setContent(reports_text);
                TextArea addReport = new TextArea();
                addReport.setPromptText("Add a new report..");
                Button addReport_button = new Button("Add Report");


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
                        new PropertyValueFactory<Reservation, String>("date_from_formatted"));

                toCol.setMinWidth(75);
                toCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("date_to_formatted"));

                emailCol.setMinWidth(200);
                emailCol.setCellValueFactory(
                        new PropertyValueFactory<Reservation, String>("email"));

                table.setItems(obsReservations);
                table.getColumns().addAll(idCol, numPersCol, fromCol, toCol, emailCol);

                //ChangeListener som finner reservation_id på reservasjonen du klikker på i tabellen.
                table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                        //Check whether item is selected and set value of selected item to Label
                        if (table.getSelectionModel().getSelectedItem() != null) {
                            selected_reservation_id[0] = table.getSelectionModel().getSelectedItem().getReservation_id();
                            System.out.println(selected_reservation_id[0]);
                        }
                    }
                });

                //Lager input-felt
                final TextField addNumPersons = new TextField();
                addNumPersons.setPromptText("#Persons");
                addNumPersons.setMaxWidth(50);
                final TextField addDateFrom = new TextField();
                addDateFrom.setMaxWidth(90);
                addDateFrom.setPromptText("From Date");
                final TextField addDateTo = new TextField();
                addDateTo.setMaxWidth(90);
                addDateTo.setPromptText("To Date");
                final TextField addEmail = new TextField();
                addEmail.setMaxWidth(200);
                addEmail.setPromptText("Email");
                final Button addButton = new Button("Add");
                final Button delReservation = new Button("Delete");


                final Button backButton = new Button("<--");
                backButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        AnchorPane p = (AnchorPane) Main.getRoot().lookup("#info");
                        try {
                            p.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("map.fxml")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //Sletter reservasjonen som er selected
                delReservation.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (selected_reservation_id[0] != 0) {
                            try {
                                int temp_reservation_id = -1;
                                for (Reservation r : obsReservations) {
                                    if (r.getReservation_id() == selected_reservation_id[0]) {
                                        temp_reservation_id = selected_reservation_id[0];
                                        obsReservations.remove(r);
                                        break;
                                    }
                                }
                                DelData.delReservation(temp_reservation_id);
                            } catch (Exception v) {
                                v.printStackTrace();
                            }
                        }
                    }
                });

                //Legger til ny reservasjon
                addButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        java.sql.Date sqlt = new java.sql.Date(0, 0, 0);
                        java.sql.Date sqlf = new java.sql.Date(0, 0, 0);
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String datet = addDateTo.getText();
                            System.out.println(datet);
                            String datef = addDateFrom.getText();
                            Date parsedt = format.parse(datet);
                            Date parsedf = format.parse(datef);
                            sqlt = new java.sql.Date(parsedt.getTime());
                            sqlf = new java.sql.Date(parsedf.getTime());
                        } catch (Exception ø) {
                            ø.printStackTrace();
                        }
                        try {
                            MakeData.makeReservation(Integer.parseInt(addNumPersons.getText()), sqlt,
                                    sqlf, addEmail.getText(), cabin.getId());
                            reservations = GetData.getReservations();
                            obsReservations.add(new Reservation(
                                    Integer.parseInt(addNumPersons.getText()),
                                    DateTime.parse(addDateTo.getText()),
                                    DateTime.parse(addDateFrom.getText()),
                                    addEmail.getText(),
                                    0,
                                    cabin.getId()
                            ));
                        } catch (KoieException e1) {
                            exceptionOutPut.setText(e1.getMessage());
                        }
                        Thread t1 = new Thread(new Runnable() {
                            public void run() {
                                sm.createAndSendMail(addEmail.getText());
                            }
                        });
                        t1.start();

                        addNumPersons.clear();
                        addDateTo.clear();
                        addDateFrom.clear();
                        addEmail.clear();
                    }
                });

                //Legger til en ny rapport
                addReport_button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        String report_string =
                                "ID: " + " N/A" + " \n\n" + "Rapport: " + "\n" + addReport.getText() +
                                        "\n" + "------------------------" + "\n";
                        reports_text.setText(reports_text.getText() + report_string);
                        try {
                            MakeData.makeReport(addReport.getText(), cabin.getId());
                            reports = GetData.getReports();
                        } catch (KoieException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                //Styles skal etterhvert legges i en egen .css-fil
                content.setStyle("-fx-background-color: #FFF");
                main_header.setStyle("-fx-font-size: 40px; -fx-text-alignment: center");
                res_header.setStyle("-fx-font-size: 30px");
                def_header.setStyle("-fx-font-size: 30px");
                exceptionOutPut.setStyle("-fx-background-color: red");

                //Positions
                reports_text.setLayoutY(120);
                reports_text.setLayoutX(500);
                res_header.setLayoutY(70);
                def_header.setLayoutX(510);
                def_header.setLayoutY(70);
                exceptionOutPut.setLayoutY(520);
                exceptionOutPut.setLayoutX(225);
                table.setLayoutY(150);
                main_header.setLayoutX(75);
                //Reservasjon
                int inputY = 520;
                int inputX = 30;
                addNumPersons.setLayoutX(inputX - 25);
                addNumPersons.setLayoutY(inputY);
                addDateFrom.setLayoutX(inputX + 32);
                addDateFrom.setLayoutY(inputY);
                addDateTo.setLayoutX(inputX + 127);
                addDateTo.setLayoutY(inputY);
                addEmail.setLayoutX(inputX + 225);
                addEmail.setLayoutY(inputY);
                addButton.setLayoutX(inputX + 380);
                addButton.setLayoutY(inputY);
                delReservation.setLayoutX(5);
                delReservation.setLayoutY(115);
                backButton.setLayoutX(10);
                backButton.setLayoutY(10);
                //Reports
                reports_pane.setLayoutY(150);
                reports_pane.setLayoutX(500);
                addReport.setLayoutY(430);
                addReport.setLayoutX(500);
                addReport_button.setLayoutX(500);
                addReport_button.setLayoutY(inputY);

                //Size
                main_header.setPrefWidth(500);
                backButton.setPrefWidth(40);
                backButton.setPrefHeight(40);

                //Table
                int tableWidth = 450;
                table.setPrefWidth(tableWidth);
                table.setPrefHeight(360);
                //reports_pane
                reports_text.setWrappingWidth(225);
                reports_pane.setPrefWidth(250);
                reports_pane.setPrefHeight(275);
                addReport.setPrefWidth(250);
                addReport.setPrefHeight(80);
                addReport_button.setPrefWidth(250);


                //Legger til alle elementene i content.
                content.getChildren().addAll(main_header, res_header, def_header, reports_text, table, backButton, addButton,
                        addDateFrom, addDateTo, addEmail, addNumPersons, reports_pane, addReport, addReport_button, exceptionOutPut, delReservation);
                if (v.getChildren().size() > 0)
                    v.getChildren().remove(0);
                v.getChildren().add(content);
            }
        });
    }
}

