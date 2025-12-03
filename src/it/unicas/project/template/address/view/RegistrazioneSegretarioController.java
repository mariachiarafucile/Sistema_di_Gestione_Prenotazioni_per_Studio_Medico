package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrazioneSegretarioController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cognomeField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void onRegister() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Compila tutti i campi.");
            return;
        }

        System.out.println("Registrazione segretario: " + nome + " " + cognome + " (" + email + ")");
        // TODO: aggiungere salvataggio su DB
    }

    @FXML
    private void onBack() {
        // TODO: chiudi finestra o torna al login
        nomeField.getScene().getWindow().hide();
    }
}


