package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Calendario su griglia che mostra i giorni correttamente e, al click su un giorno,
 * mostra le fasce orarie associate.
 */
public class CalendarioMedicoController {

    @FXML private GridPane calendarioGrid;
    @FXML private Label meseLabel;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private ListView<String> fasceOrarieList;

    private YearMonth meseCorrente;
    private MainApp mainApp;
    private Stage dialogStage;

    /**
     * Mappa per la disponibilità per data.
     */
    private Map<LocalDate, List<String>> disponibilitaPerData = new HashMap<>();


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

        aggiornaCalendario();

        prevButton.setOnAction(e -> {
            meseCorrente = meseCorrente.minusMonths(1);
            aggiornaCalendario();
        });

        nextButton.setOnAction(e -> {
            meseCorrente = meseCorrente.plusMonths(1);
            aggiornaCalendario();
        });
    }


    private void aggiornaCalendario() {
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
        int firstColumn = firstDay.getDayOfWeek().getValue() - 1; // Monday=1 -> col 0
        int giorniNelMese = meseCorrente.lengthOfMonth();

        int col = firstColumn;
        int row = 1;

        for (int day = 1; day <= giorniNelMese; day++) {
            LocalDate date = meseCorrente.atDay(day);

            Label lbl = new Label(String.valueOf(day));
            lbl.setMinSize(36, 36);
            lbl.setStyle("-fx-padding: 8; -fx-border-color: #cccccc; -fx-alignment: center;");

             if (hasDisponibilita(date)) {
                // giorni cliccabili con disponibilità
                lbl.setStyle(lbl.getStyle() + "-fx-background-color: #e8fff0;");
                lbl.setOnMouseClicked(evt -> {
                    if (evt.getButton() == MouseButton.PRIMARY) {
                        mostraFascePerData(date);
                    }
                });
            } else {
                // giorni non disponibili -> grigi e non cliccabili
                lbl.setStyle(lbl.getStyle() + "-fx-background-color: #f0f0f0; -fx-opacity: 0.6; -fx-text-fill: #888888;");
            }

            calendarioGrid.add(lbl, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        fasceOrarieList.getItems().clear();
    }


    /**
     * Controlla se una data ha fasce disponibili.
     */
    private boolean hasDisponibilita(LocalDate date) {
        return disponibilitaPerData.containsKey(date);
    }


    private void mostraFascePerData(LocalDate date) {
        fasceOrarieList.getItems().clear();

        List<String> list = disponibilitaPerData.get(date);

        if (list == null || list.isEmpty()) {
            fasceOrarieList.getItems().add("Nessuna fascia disponibile");
            return;
        }

        fasceOrarieList.getItems().addAll(list);
    }


    /**
     * Metodo pubblico per impostare disponibilità specifiche per data.
     */
    public void setDisponibilitaPerData(Map<LocalDate, List<String>> map) {
        if (map == null) return;
        this.disponibilitaPerData.clear();
        this.disponibilitaPerData.putAll(map);

        aggiornaCalendario();
    }


    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public void setMainApp(MainApp mainApp) { this.mainApp = mainApp; }
}
