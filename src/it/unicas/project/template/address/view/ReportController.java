package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import it.unicas.project.template.address.model.dao.mysql.PagamentiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.DAOException;


public class ReportController {

    @FXML
    private ComboBox<String> mesePicker;

    @FXML
    private Label visitePagateLabel;

    @FXML
    private Label visiteDaSaldareLabel;

    @FXML
    private Label totaleIncassatoLabel;

    @FXML
    private Label totaleDaIncassareLabel;

    @FXML
    private Button scaricaButton;

    private Stage dialogStage;
    private MainApp mainApp;

    // Campi per salvare i valori correnti del report
    private int currentVisitePagate = 0;
    private int currentVisiteDaSaldare = 0;
    private double currentTotaleIncassato = 0.0;
    private double currentTotaleDaIncassare = 0.0;
    private String currentMese = "";


    @FXML
    private void initialize() {

        // --- Aggiunta del logo nella finestra ---
        scaricaButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        try {
                            Image logo = new Image(
                                    ReportController.class.getResourceAsStream("/images/logo.png")
                            );
                            ((Stage) newWindow).getIcons().add(logo);
                        } catch (Exception e) {
                            System.out.println("Logo non trovato");
                        }
                    }
                });
            }
        });

        // Carica icona
        if (scaricaButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaScarica.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(15);
                iv.setFitHeight(15);
                iv.setPreserveRatio(true);
                iv.setSmooth(true);

                scaricaButton.setGraphic(iv);
                scaricaButton.setText("");
                scaricaButton.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-alignment: center;");
            } catch (Exception e) {
                System.out.println("Errore caricamento iconaScarica: " + e.getMessage());
            }

            scaricaButton.setOnAction(e -> scaricaReport());
        }

        mesePicker.getItems().addAll(
                "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
                "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"
        );
        mesePicker.setPromptText("Seleziona mese");

        mesePicker.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (newIndex != null && newIndex.intValue() >= 0) {
                aggiornaReport(newIndex.intValue() + 1); // Convertiamo l'indice (0-11) in mese (1-12)
            }
        });

    }

    private void aggiornaReport(int mese) {
        try {
            PagamentiDAOMySQLImpl.ReportResult r = ((PagamentiDAOMySQLImpl) PagamentiDAOMySQLImpl.getInstance()).getReportByMonth(mese);

            visitePagateLabel.setText("Numero visite pagate: " + r.visitePagate);
            visiteDaSaldareLabel.setText("Numero visite da saldare: " + r.visiteDaSaldare);
            totaleIncassatoLabel.setText("Totale incassato: " + r.totaleIncassato + " €");
            totaleDaIncassareLabel.setText("Totale da incassare: " + r.totaleDaIncassare + " €");

            // --- Aggiorna campi per CSV ---
            currentVisitePagate = r.visitePagate;
            currentVisiteDaSaldare = r.visiteDaSaldare;
            currentTotaleIncassato = r.totaleIncassato;
            currentTotaleDaIncassare = r.totaleDaIncassare;
            currentMese = mesePicker.getValue();

            System.out.println("Report aggiornato per il mese: " + mese);
        } catch (DAOException e) {
            e.printStackTrace();
            System.out.println("Errore nel generare il report: " + e.getMessage());
        }

    }

    // --- Metodo per salvare il report in CSV ---
    private void scaricaReport() {
        if (currentMese.isEmpty()) {
            System.out.println("Seleziona un mese prima di scaricare il report.");
            return;
        }

        String fileName = "Report_" + currentMese + ".csv";

        try (java.io.PrintWriter writer = new java.io.PrintWriter(fileName)) {
            writer.println("Mese,Visite Pagate,Totale Incassato,Visite Da Saldare,Totale Da Incassare");
            writer.println(currentMese + "," +
                    currentVisitePagate + "," +
                    currentTotaleIncassato + "," +
                    currentVisiteDaSaldare + "," +
                    currentTotaleDaIncassare);

            System.out.println("Report salvato in: " + fileName);
        } catch (Exception e) {
            System.out.println("Errore nel salvare il report: " + e.getMessage());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
