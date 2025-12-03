package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;

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

        // Inserisce orari (inizio 09:00 - 19:00, fine 09:30 - 20:00)
        for (int h = 9; h <= 19; h++) {
            oraInizioCombo.getItems().add(String.format("%02d:00", h));
            oraInizioCombo.getItems().add(String.format("%02d:30", h));
        }

        for (int h = 9; h <= 20; h++) {
            oraFineCombo.getItems().add(String.format("%02d:30", h));
        }

        salvaButton.setOnAction(e -> salvaFascia());
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

    private void salvaFascia() {
        if (giornoSelezionato == null || oraInizioCombo.getValue() == null || oraFineCombo.getValue() == null) {
            System.out.println("Seleziona giorno e orari!");
            return;
        }

        System.out.println("Fascia inserita: " + giornoSelezionato + " " + oraInizioCombo.getValue() + " - " + oraFineCombo.getValue());
        // TODO: inviare al DB

        dialogStage.close();
    }

    public void setMainApp(MainApp mainApp) { this.mainApp = mainApp; }
    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
}
