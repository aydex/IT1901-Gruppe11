package ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import db.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import org.joda.time.DateTime;

/**
 * This class contains static variables and methods used for event handling
 * and initializing the program from the database.
 *
 * @author Sondre Hjetland
 * @author Sondre Slåttedal Havellen
 * @author Nikolai Hegelstad
 * @author Adrian Hundseth
 */
public class Controller {

    static ArrayList<Cabin> cabinList = GetData.getCabins();
    static ArrayList<Reservation> reservations = GetData.getReservations();
    static ArrayList<Report> reports = GetData.getReports();
    static ArrayList<LostItem> lostItems  = GetData.getLostItems();
    private SendMail sm = new SendMail();

    /**
     * Fills the ListView <i>#hytteListe</i> with the content of the supplied
     * static ArrayList<Cabin> <code>cabinList</code>. MapMarkers are then
     * added to the map, and all of the cabins in #hytteListe are assigned
     * onclick events.
     */
    public void deployCabins() {

        final ListView t = (ListView) Main.getRoot().lookup("#hytteListe");
        final int[] selected_reservation_id = new int[1];
        final int[] selected_report_id = new int[1];
        final int[] selected_lostitem_id = new int[1];
        ObservableList<Cabin> items = FXCollections.observableArrayList();

        for (Cabin v : cabinList) {
            items.add(v);
            WebMap.addMarker(v.getCoords(), v.getName());
        }
        t.setItems(items);

        //Legger til funksjon til elementene i listen, som kjøres når de klikkes på.
        t.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            /*
             * Handles the onclick event of a cabin.
             * Removes everything inside the AnchorPane #info, and replace
             * the previous content with new JavaFX Elements(table, ListView, buttons, input-fields).
             * The new elements are then filled with the content of the supplied static
             * ArrayList<Reservation> reservations, ArrayList<Report> reports.
             */
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    final AnchorPane v = (AnchorPane) Main.getRoot().lookup("#info");
                    Cabin cabin = (Cabin) t.getSelectionModel().getSelectedItem();

                    //Finner alle reservasjoner på hytta. Legger de til i ArrayListen reservations
                    ObservableList<Reservation> obsReservations = FXCollections.observableArrayList();
                    for (Reservation r : GetData.getStatsByCabin(cabin.getId())) {
                        obsReservations.add(r);
                    }
                    //Finner alle mangler på hytta. Legger de til i ArrayListen deficiency
                    ObservableList<Report> obsReports = FXCollections.observableArrayList();
                    for (Report rep : reports) {
                        if (rep.getKoie_id() == cabin.getId()) {
                            obsReports.add(rep);
                        }
                    }

                    //Finner alle mangler på hytta. Legger de til i ArrayListen deficiency
                    ObservableList<LostItem> obsLostItems = FXCollections.observableArrayList();
                    for (LostItem item : lostItems) {
                        if (item.getKoie_id() == cabin.getId()) {
                            obsLostItems.add(item);
                        }
                    }

                    //Lager en AnchorPane hvor alle elementene legges. AnchorPanenen blir dermed lagt til i rot-Panen.
                    AnchorPane content = new AnchorPane();
                    content.setPrefHeight(900);
                    content.setPrefWidth(900);

                    //Elementer som skal legges i content
                    //Headers
                    Label res_header = new Label("Reservasjoner");
                    Label def_header = new Label("Rapporter");
                    Label lost_header = new Label("Gjenglemte Ting");
                    Label main_header = new Label(cabin.getName());
                    Label exceptionOutPut = new Label("");

                    //Reports
                    ListView reports_lw = new ListView();
                    reports_lw.setItems(obsReports);
                    TextArea addReport = new TextArea();
                    addReport.setPromptText("Add a new report..");
                    Button addReport_button = new Button("Add Report");
                    Button delReport_button = new Button("Delete Report");

                    //Lost Items
                    ListView lostItems_lw = new ListView();
                    lostItems_lw.setItems(obsLostItems);
                    TextArea addLostItem = new TextArea();
                    addLostItem.setPromptText("New Lost Item..");
                    Button addLostItem_button = new Button("Add Lost Item");
                    Button delLostItem_button = new Button("Del Lost Item");

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

                    table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                        @Override
                        /*
                         * Changes the value of the final int[1] selected_reservation_id
                         * to the reservation_id of the selected db.Reservation
                         * in the TableView<Reservation> table.
                         */
                        public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
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
                    TextField mailExtra = (TextField) Main.getRoot().lookup("#email_extra");
                    final TextField addEmail = new TextField(mailExtra.getText());
                    addEmail.setMaxWidth(200);
                    addEmail.setPromptText("Email");
                    final Button addButton = new Button("Add");
                    final Button delReservation = new Button("Delete");


                    final Button backButton = new Button("<--");

                    backButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        /*
                         * @throws java.io.IOException
                         */
                        public void handle(ActionEvent event) {
                            AnchorPane p = (AnchorPane) Main.getRoot().lookup("#info");
                            try {
                                p.getChildren().setAll((AnchorPane) FXMLLoader.load(getClass().getResource("map.fxml")));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    delReservation.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        /*
                         * Deletes a db.Reservation from the database utilizing the method
                         * db.DelData#delReservation(int)with the provided
                         * final int[1] selected_reservation_id
                         */
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
                        /*
                         * Adds a db.Reservation to the database by
                         * utilizing db.MakeData#makeReservation(int, org.joda.time.DateTime, org.joda.time.DateTime, String, int)
                         * with arguments provided by final TextFields addNumPersons, addDateTo, addDateFrom, addEmail.
                         * An Email is sent to the provided Email address using db.SendMail#createAndSendMail(String).
                         */
                        public void handle(ActionEvent e) {

                            DateTime parsedt = null;
                            DateTime parsedf = null;
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String datet = addDateTo.getText();
                                System.out.println(datet);
                                String datef = addDateFrom.getText();
                                parsedt = new DateTime(format.parse(datet));
                                parsedf = new DateTime(format.parse(datef));
                            } catch (Exception n) {
                                n.printStackTrace();
                            }
                            try {
                                MakeData.makeReservation(Integer.parseInt(addNumPersons.getText()), parsedt, parsedf, addEmail.getText(), cabin.getId());
                                reservations = GetData.getReservations();
                                obsReservations.add(new Reservation(
                                        Integer.parseInt(addNumPersons.getText()),
                                        DateTime.parse(addDateTo.getText()),
                                        DateTime.parse(addDateFrom.getText()),
                                        addEmail.getText(),
                                        0,
                                        cabin.getId()
                                ));

                                Thread t1 = new Thread(new Runnable() {
                                    public void run() {
                                        sm.createAndSendMail(addEmail.getText());
                                    }
                                });
                                t1.start();
                            } catch (KoieException e1) {
                                exceptionOutPut.setText(e1.getMessage());
                            }
                            addNumPersons.clear();
                            addDateTo.clear();
                            addDateFrom.clear();
                            addEmail.clear();
                        }
                    });

                    //Legger til en ny Lost Item
                    addLostItem_button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        /*
                         * Adds a db.Report to the database by
                         * utilizing db.MakeData#makeReport(String, int)
                         * with arguments provided by final TextField addReport.
                         */
                        public void handle(ActionEvent e) {
                            obsLostItems.add(new LostItem(
                                    addLostItem.getText(),
                                    cabin.getId(),
                                    0));
                            try {
                                MakeData.makeLostItem(addLostItem.getText(), cabin.getId());
                                lostItems = GetData.getLostItems();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    });
                    //Fjerner rapporten som er markert
                    delLostItem_button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        /*
                         * Deletes a db.Report from the database utilizing the method
                         * db.DelData#delReport(int) with the provided
                         * final int[1] selected_report_id
                         */
                        public void handle(ActionEvent event) {
                            if (selected_lostitem_id[0] != 0) {
                                try {
                                    int temp_lostitem_id = -1;
                                    for (LostItem r : lostItems) {
                                        if (r.getLost_id() == selected_lostitem_id[0]) {
                                            temp_lostitem_id = selected_lostitem_id[0];
                                            obsLostItems.remove(r);
                                            break;
                                        }
                                    }
                                    DelData.delLostItem(temp_lostitem_id);
                                    lostItems = GetData.getLostItems();
                                } catch (Exception v) {
                                    v.printStackTrace();
                                }
                            }
                        }
                    });
                    //ChangeListener som holder styr på hvilken rapport som er markert
                    lostItems_lw.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                        @Override
                        /*
                         * Changes the value of the final int[1] selected_report_id
                         * to the report_id of the selected db.Reservation
                         * in the ListView<Report> report_lw.
                         */
                        public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                            if (lostItems_lw.getItems().size() > 0) {
                                selected_lostitem_id[0] = ((LostItem) lostItems_lw.getSelectionModel().getSelectedItem()).getLost_id();
                            } else {
                                selected_lostitem_id[0] = 0;
                            }
                        }
                    });

                    //Legger til en ny rapport
                    addReport_button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        /*
                         * Adds a db.Report to the database by
                         * utilizing db.MakeData#makeReport(String, int)
                         * with arguments provided by final TextField addReport.
                         */
                        public void handle(ActionEvent e) {
                            obsReports.add(new Report(
                                    addReport.getText(),
                                    cabin.getId(),
                                    0));
                            try {
                                MakeData.makeReport(addReport.getText(), cabin.getId());
                                reports = GetData.getReports();
                            } catch (KoieException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    //Fjerner rapporten som er markert
                    delReport_button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        /*
                         * Deletes a db.Report from the database utilizing the method
                         * db.DelData#delReport(int) with the provided
                         * final int[1] selected_report_id
                         */
                        public void handle(ActionEvent event) {
                            if (selected_report_id[0] != 0) {
                                try {
                                    int temp_report_id = -1;
                                    for (Report r : reports) {
                                        if (r.getReport_id() == selected_report_id[0]) {
                                            temp_report_id = selected_report_id[0];
                                            obsReports.remove(r);
                                            break;
                                        }
                                    }
                                    DelData.delReport(temp_report_id);
                                    reports = GetData.getReports();
                                } catch (Exception v) {
                                    v.printStackTrace();
                                }
                            }
                        }
                    });
                    //ChangeListener som holder styr på hvilken rapport som er markert
                    reports_lw.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                        @Override
                        /*
                         * Changes the value of the final int[1] selected_report_id
                         * to the report_id of the selected db.Reservation
                         * in the ListView<Report> report_lw.
                         */
                        public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                            selected_report_id[0] = ((Report) reports_lw.getSelectionModel().getSelectedItem()).getReport_id();
                        }
                    });



                    //Styles skal etterhvert legges i en egen .css-fil
                    content.setStyle("-fx-background-color: #FFF; -fx-font-size: 12px");
                    main_header.setStyle("-fx-font-size: 40px; -fx-text-alignment: center");
                    res_header.setStyle("-fx-font-size: 30px");
                    def_header.setStyle("-fx-font-size: 30px");
                    lost_header.setStyle("-fx-font-size: 30px");
                    exceptionOutPut.setStyle("-fx-text-fill: red");

                    //Positions
                    res_header.setLayoutY(70);
                    def_header.setLayoutX(510);
                    def_header.setLayoutY(70);
                    exceptionOutPut.setLayoutY(565);
                    exceptionOutPut.setLayoutX(125);
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

                    reports_lw.setLayoutY(150);
                    reports_lw.setLayoutX(500);
                    addReport.setLayoutY(430);
                    addReport.setLayoutX(500);
                    addReport_button.setLayoutX(500);
                    addReport_button.setLayoutY(inputY);
                    delReport_button.setLayoutX(500);
                    delReport_button.setLayoutY(inputY + 30);

                    //reports_pane
                    reports_lw.setPrefHeight(275);
                    reports_lw.setPrefWidth(250);
                    addReport.setPrefWidth(250);
                    addReport.setPrefHeight(80);
                    addReport_button.setPrefWidth(250);
                    delReport_button.setPrefWidth(250);

                    //Lost Items
                    lost_header.setLayoutY(550);
                    lost_header.setLayoutX(0);
                    lostItems_lw.setLayoutY(600);
                    lostItems_lw.setLayoutX(0);
                    addLostItem.setLayoutY(755);
                    addLostItem.setLayoutX(0);
                    addLostItem_button.setLayoutX(410);
                    addLostItem_button.setLayoutY(760);
                    delLostItem_button.setLayoutX(410);
                    delLostItem_button.setLayoutY(730);

                    //Lost Items Pane
                    lostItems_lw.setPrefHeight(150);
                    lostItems_lw.setPrefWidth(400);
                    addLostItem.setPrefWidth(400);
                    addReport_button.setPrefWidth(250);
                    addLostItem.setPrefHeight(10);
                    delReport_button.setPrefWidth(250);

                    //Size
                    main_header.setPrefWidth(500);
                    backButton.setPrefWidth(40);
                    backButton.setPrefHeight(40);

                    //Table
                    int tableWidth = 450;
                    table.setPrefWidth(tableWidth);
                    table.setPrefHeight(360);

                    //Legger til alle elementene i content.
                    content.getChildren().addAll(main_header, res_header, def_header, table, backButton, addButton,
                            addDateFrom, addDateTo, addEmail, addNumPersons, addReport, addReport_button, delReport_button,
                            exceptionOutPut, delReservation, reports_lw, addLostItem, addLostItem_button, delLostItem_button,
                            lostItems_lw, lost_header);
                    if (v.getChildren().size() > 0)
                        v.getChildren().remove(0);
                    v.getChildren().add(content);
                } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                    t.getSelectionModel().select(t.getSelectionModel().getSelectedItems());
                }
            }
        });
    }
}

