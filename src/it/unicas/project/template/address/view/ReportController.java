package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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

    @FXML
    private void initialize() {
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
    }

    private void scaricaReport() {
        System.out.println("Scarica report mese: " + (mesePicker.getValue() != null ? mesePicker.getValue() : "non selezionato"));
        // Logica per generare/esportare il report
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
