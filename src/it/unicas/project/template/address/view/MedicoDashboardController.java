package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MedicoDashboardController {

    @FXML
    private Button ricercaPazienteButton;

    @FXML
    private Button calendarioButton;

    @FXML
    private Button fasceOrarieButton;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        // Carica immagini
        if (ricercaPazienteButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaRicerca.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                ricercaPazienteButton.setGraphic(iv);
                ricercaPazienteButton.setText("");
                ricercaPazienteButton.setStyle("-fx-content-display: center;");

            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaRicerca: " + e.getMessage());
            }

            ricercaPazienteButton.setOnAction(e -> mainApp.showRicercaPazienteDialog("MEDICO"));
        }

        if (calendarioButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/calendar.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                calendarioButton.setGraphic(iv);
                calendarioButton.setText("");
                calendarioButton.setStyle("-fx-content-display: center;");

            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaCalendario: " + e.getMessage());
            }

            calendarioButton.setOnAction(e -> mainApp.showCalendarioDialog());
        }

        if (fasceOrarieButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaInserisciFascia.png")); // metti l'icona
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                fasceOrarieButton.setGraphic(iv);
                fasceOrarieButton.setText("");
                fasceOrarieButton.setStyle("-fx-content-display: center;");

            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaFasce: " + e.getMessage());
            }

            fasceOrarieButton.setOnAction(e -> mainApp.showInserimentoFasceDialog());
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
