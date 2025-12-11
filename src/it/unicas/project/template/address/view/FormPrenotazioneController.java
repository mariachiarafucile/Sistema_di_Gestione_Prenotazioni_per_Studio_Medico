package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.FasceOrarie;
import it.unicas.project.template.address.model.Prenotazioni;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.mysql.FasceOrarieDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.PrenotazioniDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class FormPrenotazioneController {

    // ==== FXML ====
    @FXML private GridPane calendarioGrid;
    @FXML private Label giornoSelezionatoLabel;
    @FXML private ComboBox<String> oraInizioCombo;
    @FXML private ComboBox<String> oraFineCombo;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label meseLabel;
    @FXML private TextField medicoField;

    // ==== Stato ====
    private String codiceFiscalePaziente;
    private Stage dialogStage;
    private LocalDate giornoSelezionato;
    private YearMonth meseCorrente;

    private final DAO<Prenotazioni> daoPren = PrenotazioniDAOMySQLImpl.getInstance();
    private final DAO<FasceOrarie> daoFasce = FasceOrarieDAOMySQLImpl.getInstance();

    // Cache fasce del medico per il mese corrente
    private Map<LocalDate, List<FasceOrarie>> fascePerData = new HashMap<>();

    // ==== INIT ====
    @FXML
    private void initialize() {
        meseCorrente = YearMonth.now();
        popolaCalendarioBase();

        prevButton.setOnAction(e -> {
            meseCorrente = meseCorrente.minusMonths(1);
            popolaCalendarioBase();
            coloraGiorni();
        });

        nextButton.setOnAction(e -> {
            meseCorrente = meseCorrente.plusMonths(1);
            popolaCalendarioBase();
            coloraGiorni();
        });

        medicoField.textProperty().addListener((obs, oldV, newV) -> {
            caricaFasceMedico();
            coloraGiorni();
        });
    }

    // ===== Calendar Layout =====
    private void popolaCalendarioBase() {
        calendarioGrid.getChildren().clear();

        meseLabel.setText(
                meseCorrente.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())
                        + " " + meseCorrente.getYear()
        );

        DayOfWeek[] order = {
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
        };

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
            lbl.setPrefSize(36, 36);
            lbl.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            lbl.setStyle(baseStyle("#ffffff"));

            lbl.setOnMouseClicked(evt -> {
                if (evt.getButton() == MouseButton.PRIMARY) {
                    giornoSelezionato = date;
                    giornoSelezionatoLabel.setText("Giorno selezionato: " + date);
                    aggiornaOrariDisponibili(date);
                }
            });

            calendarioGrid.add(lbl, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        caricaFasceMedico();
        coloraGiorni();
    }

    // ===== Load fasce per mese =====
    private void caricaFasceMedico() {
        fascePerData.clear();

        String medico = medicoField.getText();
        if (medico == null || medico.isEmpty()) return;

        try {
            // workaround: carico tutto e filtro manualmente
            List<FasceOrarie> tutte = daoFasce.select(new FasceOrarie(1,"","",""));

            for (FasceOrarie f : tutte) {
                LocalDate d = LocalDate.parse(f.getData());

                if (YearMonth.from(d).equals(meseCorrente)) {
                    fascePerData
                            .computeIfAbsent(d, k -> new ArrayList<>())
                            .add(f);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Colora giorni =====
    private void coloraGiorni() {
        for (var n : calendarioGrid.getChildren()) {
            if (!(n instanceof Label)) continue;
            Label lbl = (Label) n;

            if (!lbl.getText().matches("\\d+")) continue;

            int giorno = Integer.parseInt(lbl.getText());
            LocalDate data = meseCorrente.atDay(giorno);

            boolean disponibile = fascePerData.containsKey(data);

            if (disponibile) {
                lbl.setStyle(baseStyle("#c8f7c5")); // verde
            } else {
                lbl.setStyle(baseStyle("#f7c5c5")); // rosso
            }
        }
    }

    private String baseStyle(String bg) {
        return "-fx-padding: 8;" +
                "-fx-border-color: #cccccc;" +
                "-fx-alignment: center;" +
                "-fx-background-color: " + bg + ";";
    }

    // ===== Popola combo orari dal DB =====
    private void aggiornaOrariDisponibili(LocalDate data) {
        oraInizioCombo.getItems().clear();
        oraFineCombo.getItems().clear();

        List<FasceOrarie> fasce = fascePerData.getOrDefault(data, Collections.emptyList());

        for (FasceOrarie f : fasce) {
            oraInizioCombo.getItems().add(f.getOraInizio());
            oraFineCombo.getItems().add(f.getOraFine());
        }
    }

    // ===== Setters =====
    public void setPazienteCF(String cf) {
        this.codiceFiscalePaziente = cf;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    // ===== Save =====
    @FXML
    private void onSalva() {
        try {
            if (giornoSelezionato == null ||
                    oraInizioCombo.getValue() == null ||
                    oraFineCombo.getValue() == null ||
                    medicoField.getText().isEmpty()) {

                showErr("Compila tutti i campi");
                return;
            }

            String medico = medicoField.getText();
            String data = giornoSelezionato.toString();
            String oraInizio = oraInizioCombo.getValue();
            String oraFine = oraFineCombo.getValue();

            FasceOrarie fascia = new FasceOrarie(null, data, oraInizio, oraFine);
            daoFasce.insert(fascia);

            int fasciaId = daoFasce.select(new FasceOrarie(1,"","",""))
                    .stream()
                    .filter(f -> f.getData().equals(data) &&
                            f.getOraInizio().equals(oraInizio) &&
                            f.getOraFine().equals(oraFine))
                    .map(FasceOrarie::getIdFasciaOraria)
                    .findFirst()
                    .orElseThrow();

            Prenotazioni p = new Prenotazioni(null, codiceFiscalePaziente, medico, fasciaId);
            daoPren.insert(p);

            dialogStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showErr("Errore nel salvataggio");
        }
    }

    @FXML
    private void onAnnulla() {
        dialogStage.close();
    }

    private void showErr(String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Errore");
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}