package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RicercaPazienteController {

    @FXML
    private TextField cfField;

    @FXML
    private Button cercaButton;

    private MainApp mainApp;

    private Stage dialogStage;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        // Quando la scena è pronta, allora carichiamo il logo
        cfField.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        try {
                            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
                            ((Stage) newWindow).getIcons().add(logo);
                        } catch (Exception e) {
                            System.out.println("Logo non trovato");
                        }
                    }
                });
            }
        });
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    @FXML
    private void onCerca() {
        String cf = cfField.getText();
        if (cf == null || cf.trim().isEmpty()) {
            System.out.println("Inserisci un Codice Fiscale valido");
            return;
        }

        // TODO: inserire la logica di ricerca paziente nel database
        System.out.println("Ricerca paziente con CF: " + cf);
    }
}
