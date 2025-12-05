package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

            ricercaPazienteButton.setOnAction(e -> apriRicercaPaziente());
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

            calendarioButton.setOnAction(e -> apriCalendario());
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

            fasceOrarieButton.setOnAction(e -> apriInserimentoFasce());
        }
    }

    private void apriRicercaPaziente() {
        System.out.println("Apri pannello Ricerca Paziente");
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

    private void apriCalendario() {
        System.out.println("Apri pannello Calendario");
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CalendarioMedico.fxml"));
            VBox page = loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Calendario Medico");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());

            Scene scene = new Scene(page, 800, 600);
            dialogStage.setScene(scene);

            CalendarioMedicoController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void apriInserimentoFasce() {
        System.out.println("Apri pannello Inserimento Fasce Orarie");
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/InserimentoFasce.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Inserimento Fasce Orarie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());

            Scene scene = new Scene(page, 500, 600);
            dialogStage.setScene(scene);

            InserimentoFasceController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
