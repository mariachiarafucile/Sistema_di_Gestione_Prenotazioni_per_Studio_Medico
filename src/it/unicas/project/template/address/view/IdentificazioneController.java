package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller della schermata di identificazione.
 *
 * Permette la scelta del ruolo tra medico e segretario
 * e reindirizza alla schermata di login corrispondente.
 *
 */
public class IdentificazioneController {

    @FXML
    private Button medicoButton;

    @FXML
    private Button segretarioButton;

    @FXML
    private ImageView medicoImageView;

    @FXML
    private ImageView segretarioImageView;

    private MainApp mainApp;


    /**
     * Imposta il riferimento all'applicazione principale.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Inizializza il controller.
     */
    @FXML
    private void initialize() {

        if (medicoImageView != null) {
            try {
                medicoImageView.setImage(new Image(getClass().getResourceAsStream("/images/medico.png")));
            } catch (Exception ignored) {}
        }

        if (segretarioImageView != null) {
            try {
                segretarioImageView.setImage(new Image(getClass().getResourceAsStream("/images/segretario.png")));
            } catch (Exception ignored) {}
        }

        if (medicoButton != null) {
            medicoButton.setOnAction(e -> openMedicoScreen());
        }

        if (segretarioButton != null) {
            segretarioButton.setOnAction(e -> openSegretarioScreen());
        }
    }

    /**
     * Apre la schermata di login per il medico.
     */
    private void openMedicoScreen() {
        mainApp.showLogin("MEDICO");
    }

    /**
     * Apre la schermata di login per il segretario.
     */
    private void openSegretarioScreen() {
        mainApp.showLogin("SEGRETARIO");

    }

}
