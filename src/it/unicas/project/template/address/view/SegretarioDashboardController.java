package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller della dashboard del segretario.
 * Gestisce le funzionalità principali disponibili per il ruolo SEGRETARIO,
 * come la ricerca dei pazienti, l'aggiunta di nuovi pazienti
 * e l'accesso alla sezione report/portafoglio.
 */
public class SegretarioDashboardController {

    @FXML
    private Button ricercaButton;
    @FXML
    private Button aggiungiButton;
    @FXML
    private Button portafoglioButton;

    private Stage stage;
    private MainApp mainApp;

    /**
     * Imposta lo stage principale della dashboard.
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Imposta il riferimento all'applicazione principale.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Inizializza il controller caricando il logo nell'applicazione.
     */
    @FXML
    private void initialize() {
        if (ricercaButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaRicerca.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                ricercaButton.setGraphic(iv);
                ricercaButton.setStyle("-fx-content-display: center;");
            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaRicerca: " + e.getMessage());
            }

            ricercaButton.setOnAction(e -> mainApp.showRicercaPazienteDialog("SEGRETARIO"));
        }

        if (aggiungiButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaAggiungi.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                aggiungiButton.setGraphic(iv);
                aggiungiButton.setStyle("-fx-content-display: center;");
            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaAggiungi: " + e.getMessage());
            }

            aggiungiButton.setOnAction(e -> mainApp.showAggiungiPazienteDialog());
        }

        if (portafoglioButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaPortafoglio.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                portafoglioButton.setGraphic(iv);
                portafoglioButton.setStyle("-fx-content-display: center;");
            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaPortafoglio: " + e.getMessage());
            }

            portafoglioButton.setOnAction(e -> mainApp.ShowReportDialog());
        }
    }

}
