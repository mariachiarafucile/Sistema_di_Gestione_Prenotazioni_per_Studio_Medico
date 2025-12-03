package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;


public class SegretarioDashboardController {

    @FXML
    private ImageView iconaRicerca;
    @FXML
    private ImageView iconaAggiungi;
    @FXML
    private ImageView iconaPortafoglio;

    @FXML
    private Button ricercaButton;
    @FXML
    private Button aggiungiButton;
    @FXML
    private Button portafoglioButton;

    @FXML
    private VBox pannelloPrincipale;

    private Stage stage;
    private MainApp mainApp;


    @FXML
    private void initialize() {
        if (ricercaButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaRicerca.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);   // dimensione icona
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                ricercaButton.setGraphic(iv);
                ricercaButton.setText(""); // nessun testo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                ricercaButton.setStyle("-fx-content-display: center;"); // centra la grafica
            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaRicerca: " + e.getMessage());
            }

            ricercaButton.setOnAction(e -> apriRicercaPaziente());
        }

        if (aggiungiButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaAggiungi.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);   // dimensione icona
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                aggiungiButton.setGraphic(iv);
                aggiungiButton.setText(""); // Rivedi testo
                aggiungiButton.setStyle("-fx-content-display: center;"); // centra la grafica
            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaAggiungi: " + e.getMessage());
            }

            aggiungiButton.setOnAction(e -> apriAggiungiPaziente());
        }

        if (portafoglioButton != null) {
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/iconaPortafoglio.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(75);   // dimensione icona
                iv.setFitHeight(75);
                iv.setPreserveRatio(true);

                portafoglioButton.setGraphic(iv);
                portafoglioButton.setText(""); // Rivedi testo
                portafoglioButton.setStyle("-fx-content-display: center;"); // centra la grafica
            } catch (Exception e) {
                System.out.println("Errore caricamento immagine iconaPortafoglio: " + e.getMessage());
            }

            portafoglioButton.setOnAction(e -> apriReport());
        }
    }

    @FXML
    private void apriRicercaPaziente() {
        System.out.println("Apri pannello Ricerca Pazienti");
            try {
                // 1. Carica l'FXML della ricerca pazienti
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/RicercaPaziente.fxml"));
                VBox page = loader.load();

                // 2. Crea un nuovo stage per la finestra
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Ricerca Pazienti");
                dialogStage.initModality(Modality.WINDOW_MODAL); // blocca la finestra principale
                dialogStage.initOwner(mainApp.getPrimaryStage()); // finestra figlia della principale

                Scene scene = new Scene(page, 500, 600);
                dialogStage.setScene(scene);

                // 3. Passa eventuale riferimento al MainApp nel controller
                RicercaPazienteController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setDialogStage(dialogStage);

                // 4. Mostra la finestra e aspetta la chiusura
                dialogStage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    @FXML
    private void apriAggiungiPaziente() {
        System.out.println("Apri pannello Aggiungi Paziente");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegistrazionePaziente.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrazione Paziente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RegistrazionePazienteController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);

            dialogStage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    @FXML
    private void apriReport() {
        System.out.println("Apri pannello Report");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Report.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Report Mensile");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ReportController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
