package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.util.AlertUtils;
import it.unicas.project.template.address.model.Pazienti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.PazientiDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.time.LocalDate;

import static it.unicas.project.template.address.util.AlertUtils.showConfirmationAlert;
import static it.unicas.project.template.address.util.AlertUtils.showErrorAlert;

/**
 * Controller per la registrazione di un paziente.
 *
 * Gestisce la visualizzazione del form di registrazione paziente,
 * la validazione dei dati inseriti (nome, cognome, data di nascita,
 * codice fiscale, indirizzo, telefono, email, note cliniche) e
 * l'inserimento del nuovo paziente nel database.
 *
 * Mostra messaggi di conferma o di errore in base all'esito.
 *
 */
public class RegistrazionePazienteController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField dataNascitaField;
    @FXML private TextField codiceFiscaleField;
    @FXML private TextField indirizzoField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextArea noteClinicheField;

    private MainApp mainApp;
    private Stage dialogStage;

    /**
     * Imposta il riferimento all'applicazione principale.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Imposta il riferimento alla finestra di dialogo.
     *
     * @param stage
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * Inizializza il controller, caricando il logo nell'applicazione.
     */
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

    /**
     * Gestisce il click sul pulsante "Conferma".
     * Valida i campi, crea un oggetto Pazienti e lo inserisce nel database.
     * Mostra alert di conferma o di errore e chiude la finestra se la registrazione ha successo.
     */
    @FXML
    private void onRegistra() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String dataNascita = dataNascitaField.getText();
        String cf = codiceFiscaleField.getText();
        String indirizzo = indirizzoField.getText();
        String telefono = telefonoField.getText();
        String email = emailField.getText();
        String note = noteClinicheField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty() ||
                cf.isEmpty() || indirizzo.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            showErrorAlert("Tutti i campi (eccetto le note cliniche) devono essere compilati");
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

        // converte temporaneamente la data di nascita in LocalDate per la validazione
        try {
            LocalDate dataNascitaLD = LocalDate.parse(dataNascita);

            if (!dataNascitaLD.isBefore(LocalDate.now())) {
                showErrorAlert("La data di nascita non può essere oggi o futura.");
                return;
            }
        } catch (Exception e) {
            showErrorAlert("Formato della data non valido. Usa YYYY-MM-DD");
            return;
        }

        if (cf.length() != 16) {
            showErrorAlert("Il codice fiscale deve essere lungo esattamente 16 caratteri.");
            return;
        } else {
            // Controlla che tutti i caratteri siano alfanumerici
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
            return;
        }

        if (dialogStage != null) dialogStage.close();
    }

    /**
     * Gestisce il click sul pulsante "Annulla", chiudendo la finestra di registrazione.
     */
    @FXML
    private void onAnnulla() {
        if (dialogStage != null) dialogStage.close();
    }

    /**
     * Verifica se il numero di telefono inserito è valido (10 cifre).
     *
     * @param telefono
     */
    private boolean telefonoValido(String telefono) {
        if (telefono.length() != 10) {
            return false;
        }

        for (char c : telefono.toCharArray()){
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se l'email inserita è valida.
     *
     * @param email
     */
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
