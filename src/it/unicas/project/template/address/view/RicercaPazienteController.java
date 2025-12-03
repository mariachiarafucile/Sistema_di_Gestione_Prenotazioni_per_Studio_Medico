package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
