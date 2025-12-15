package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

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

    private void openMedicoScreen() {
        mainApp.showLogin("MEDICO");
    }

    private void openSegretarioScreen() {
        mainApp.showLogin("SEGRETARIO");

    }

}
