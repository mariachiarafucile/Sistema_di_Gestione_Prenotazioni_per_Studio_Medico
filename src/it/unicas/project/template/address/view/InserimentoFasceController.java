package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.FasceOrarie;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.FasceOrarieDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

import static it.unicas.project.template.address.model.AlertUtils.showConfirmationAlert;
import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;

public class InserimentoFasceController {

    @FXML private GridPane calendarioGrid;
    @FXML private Label giornoSelezionatoLabel;
    @FXML private ComboBox<String> oraInizioCombo;
    @FXML private ComboBox<String> oraFineCombo;
    @FXML private Button salvaButton;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label meseLabel;

    private Stage dialogStage;
    private MainApp mainApp;
    private LocalDate giornoSelezionato;

    private YearMonth meseCorrente;

    @FXML
    private void initialize() {

        // --- Aggiunta del logo nella finestra ---
        calendarioGrid.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        try {
                            Image logo = new Image(
                                    CalendarioMedicoController.class.getResourceAsStream("/images/logo.png")
                            );
                            ((Stage) newWindow).getIcons().add(logo);
                        } catch (Exception e) {
                            System.out.println("Logo non trovato");
                        }
                    }
                });
            }
        });

        meseCorrente = YearMonth.now();
        popolaCalendario();

        // Navigazione mesi
        prevButton.setOnAction(e -> {
            meseCorrente = meseCorrente.minusMonths(1);
            popolaCalendario();
        });
        nextButton.setOnAction(e -> {
            meseCorrente = meseCorrente.plusMonths(1);
            popolaCalendario();
        });

        // ORARI DI INIZIO: 09:00 → 19:30
        for (int h = 9; h <= 19; h++) {
            oraInizioCombo.getItems().add(String.format("%02d:00", h));
            oraInizioCombo.getItems().add(String.format("%02d:30", h));
        }

        // ORARI DI FINE: 09:30 → 20:00
        // Primo orario fisso: 09:30
        oraFineCombo.getItems().add("09:30");

        // Da 10:00 fino a 20:00 ogni 30 minuti
        for (int h = 10; h <= 20; h++) {
            oraFineCombo.getItems().add(String.format("%02d:00", h));
            if (h < 20) {  // aggiungo :30 solo fino alle 19:30
                oraFineCombo.getItems().add(String.format("%02d:30", h));
            }
        }

    }

    private void popolaCalendario() {
        calendarioGrid.getChildren().clear();

        meseLabel.setText(meseCorrente.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())
                + " " + meseCorrente.getYear());

        DayOfWeek[] order = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY };


        for (int c = 0; c < 7; c++) {
            Label h = new Label(order[c].getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            h.setStyle("-fx-font-weight: bold; -fx-padding: 6; -fx-alignment: center;");
            calendarioGrid.add(h, c, 0);
        }

        LocalDate firstDay = meseCorrente.atDay(1);
        int firstColumn = firstDay.getDayOfWeek().getValue() - 1;
        int giorniNelMese = meseCorrente.lengthOfMonth();

        int col = firstColumn;
        int row = 1;

        for (int day = 1; day <= giorniNelMese; day++) {
            LocalDate date = meseCorrente.atDay(day);
            Label lbl = new Label(String.valueOf(day));
            lbl.setMinSize(36, 36);
            lbl.setStyle("-fx-padding: 8; -fx-border-color: #cccccc; -fx-alignment: center; -fx-background-color: #ffffff;");

            lbl.setOnMouseClicked(evt -> {
                if (evt.getButton() == MouseButton.PRIMARY) {
                    giornoSelezionato = date;
                    giornoSelezionatoLabel.setText("Giorno selezionato: " + giornoSelezionato);
                }
            });

            calendarioGrid.add(lbl, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
    @FXML
    private void salvaFascia() {
        if (giornoSelezionato == null || oraInizioCombo.getValue() == null || oraFineCombo.getValue() == null) {
            System.out.println("Seleziona giorno e orari!");
            return;
        }

        // Controllo data non passata
        if (giornoSelezionato.isBefore(LocalDate.now())) {
            showErrorAlert("Non puoi selezionare una data passata!");
            return;
        }

        String data = giornoSelezionato.toString();
        String oraInizio = oraInizioCombo.getValue();
        String oraFine = oraFineCombo.getValue();

        // Controllo orario
        LocalTime inizio = LocalTime.parse(oraInizio);
        LocalTime fine = LocalTime.parse(oraFine);
        if (!inizio.isBefore(fine)) {
            showErrorAlert("L'orario di inizio deve essere precedente all'orario di fine!");
            return;
        }
        System.out.println("Fascia inserita: " + giornoSelezionato + " " + oraInizioCombo.getValue() + " - " + oraFineCombo.getValue());

        try {

            // ID null -> MySQL lo genera
            FasceOrarie fascia = new FasceOrarie(null, data, oraInizio, oraFine);

            DAO dao = FasceOrarieDAOMySQLImpl.getInstance();
            dao.insert(fascia);

            showConfirmationAlert("Fascia oraria inserita correttamente.");

            dialogStage.close();

        } catch (DAOException e) {
            e.printStackTrace();
            showErrorAlert("Errore nel salvataggio: " + e.getMessage());
        }

        dialogStage.close();
    }

    public void setMainApp(MainApp mainApp) { this.mainApp = mainApp; }
    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
}
