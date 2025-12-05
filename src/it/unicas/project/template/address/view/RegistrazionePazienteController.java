package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.AlertUtils;
import it.unicas.project.template.address.model.Medici;
import it.unicas.project.template.address.model.Pazienti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.MediciDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.PazientiDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.LocalDate;

import static it.unicas.project.template.address.model.AlertUtils.showConfirmationAlert;
import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;


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

    @FXML
    private void initialize() {
        // Quando la scena è pronta, allora carichiamo il logo
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

        if (nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty() ||
                cf.isEmpty() || indirizzo.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            showErrorAlert("Tutti i campi (eccetto le note cliniche) devono essere compilati");;
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

        // converto temporaneamente in LocalDate per la validazione
        LocalDate dataNascitaLD = LocalDate.parse(dataNascita);

        if (!dataNascitaLD.isBefore(LocalDate.now())) {
            showErrorAlert("La data di nascita non può essere oggi o futura.");
            return;
        }

        if (cf.length() != 16) {

            showErrorAlert("Il codice fiscale deve essere lungo esattamente 16 caratteri.");
            return;

        } else {
            // Controllo che siano solo caratteri alfanumerici
            boolean valido = true;
            for (int i = 0; i < cf.length(); i++) {
                char c = cf.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    valido = false;
                    break;
                }
            }

            if (!valido) {
                showErrorAlert("Il codice fiscale deve contenere solo caratteri alfanumerici.");
                return;
            }
        }


        for (char c : indirizzo.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ') {
                showErrorAlert("L'indirizzo non può contenere caratteri speciali.");
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


        System.out.println("Registrazione paziente:");
        System.out.println(nome + " " + cognome + " - " + dataNascita + " - " + cf);

        Pazienti paziente = new Pazienti(nome, cognome, dataNascita, cf, indirizzo, telefono, email, note);

        try {
            DAO dao = PazientiDAOMySQLImpl.getInstance();
            dao.insert(paziente);
            showConfirmationAlert("Account creato correttamente");
        } catch (DAOException e) {
            e.printStackTrace();
            showErrorAlert("Il codice fiscale inserito è già utilizzato da un altro utente.");
        }


        if (dialogStage != null) dialogStage.close();
    }

    @FXML
    private void onAnnulla() {
        if (dialogStage != null) dialogStage.close();
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

