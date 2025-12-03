package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class RegistrazionePazienteController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private DatePicker dataNascitaField;
    @FXML private TextField codiceFiscaleField;
    @FXML private TextField indirizzoField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextArea noteClinicheField;

    private MainApp mainApp;

    private Stage dialogStage;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void onRegistra() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String dataNascita = (dataNascitaField.getValue() != null) ? dataNascitaField.getValue().toString() : "";
        String cf = codiceFiscaleField.getText();
        String indirizzo = indirizzoField.getText();
        String telefono = telefonoField.getText();
        String email = emailField.getText();
        String note = noteClinicheField.getText();

        System.out.println("Registrazione paziente:");
        System.out.println(nome + " " + cognome + " - " + dataNascita + " - " + cf);

        if (dialogStage != null) dialogStage.close();
    }

    @FXML
    private void onAnnulla() {
        if (dialogStage != null) dialogStage.close();
    }
}
