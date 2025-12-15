package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.util.AlertUtils;
import it.unicas.project.template.address.model.Medici;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.MediciDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static it.unicas.project.template.address.util.AlertUtils.showConfirmationAlert;
import static it.unicas.project.template.address.util.AlertUtils.showErrorAlert;

public class RegistrazioneMedicoController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField specializzazioneField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        //caricamento logo
        nomeField.sceneProperty().addListener((obs, oldScene, newScene) -> {
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

    @FXML
    private void onRegister() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String specializzazione = specializzazioneField.getText();
        String telefono = telefonoField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || specializzazione.isEmpty() ||
                telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorAlert("Tutti i campi devono essere compilati");;
            return;
        }

        for (char c : nome.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                showErrorAlert("Il nome deve contenere solo lettere.");
                return;
            }
        }

        for (char c : cognome.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                showErrorAlert("Il cognome deve contenere solo lettere.");
                return;
            }
        }

        for (char c : specializzazione.toCharArray()) {
            if (!Character.isLetter(c)) {
                showErrorAlert("La specializzazione deve contenere solo lettere.");
                return;
            }
        }


        if(!telefonoValido(telefono)){
            showErrorAlert("Il numero di telefono deve contenere esattamente 10 cifre.");
            return;
        }

        if(!emailValida(email)){
            showErrorAlert("L'email inserita non è valida.");
            return;
        }

        System.out.println("Registrazione medico: " + nome + " " + cognome +
                ", " + specializzazione + ", " + telefono + ", " + email);

        Medici medico = new Medici(nome, cognome, specializzazione, telefono, email, password);

        try {
            DAO dao = MediciDAOMySQLImpl.getInstance();
            dao.insert(medico);
            showConfirmationAlert("Account creato correttamente");
            // Chiude la finestra di registrazione
            nomeField.getScene().getWindow().hide();

            // Reindirizza alla pagina di login
            mainApp.showLogin("MEDICO");

        } catch (DAOException e) {

            String msg = e.getMessage();

            if (msg.contains("Duplicate entry")) {

                if (msg.contains("'" + medico.getEmail() + "'")) {
                    showErrorAlert("L'email inserita è già utilizzata da un altro utente.");
                }
                else if (msg.contains("'" + medico.getTelefono() + "'")) {
                    showErrorAlert("Il numero di telefono è già utilizzato da un altro utente.");
                }

            } else {
                showErrorAlert("Errore durante la registrazione: " + msg);
            }
        }

    }

    @FXML
    private void onBack() {
        // Chiude la finestra di registrazione
        nomeField.getScene().getWindow().hide();
    }


    // Metodo di utilità per verificare se un numero di telefono è valido
    private boolean telefonoValido(String telefono) {
        // Controllo lunghezza
        if (telefono.length() != 10) {
            return false;
        }

        // Controllo che tutti i caratteri siano cifre
        for (char c : telefono.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }
    // Metodo di utilità per verificare se un'email è valida
    private boolean emailValida(String email) {
        int atIndex = email.indexOf("@");
        int dotIndex = email.lastIndexOf(".");

        // Controlli principali
        if (atIndex <= 0) {
            return false; // niente prima della @ oppure @ all'inizio
        }
        if (dotIndex < atIndex + 2) {
            return false; // il . deve venire dopo la @ e almeno un carattere tra @ e .
        }
        if (dotIndex == email.length() - 1) {
            return false; // non deve terminare con .
        }

        return true;
    }



}
