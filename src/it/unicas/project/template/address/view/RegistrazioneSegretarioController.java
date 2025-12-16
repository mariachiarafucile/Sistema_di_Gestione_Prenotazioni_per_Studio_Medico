package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.util.AlertUtils;
import it.unicas.project.template.address.model.Segretari;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.SegretariDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import static it.unicas.project.template.address.util.AlertUtils.*;

/**
 * Controller per la registrazione di un segretario.
 *
 * Gestisce la visualizzazione del form di registrazione segretario,
 * la validazione dei dati inseriti (nome, cognome, email, password)
 * e l'inserimento del nuovo segretario nel database.
 *
 * Mostra messaggi di conferma o di errore in base all'esito della registrazione.
 *
 */
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

    /**
     * Imposta il riferimento all'applicazione principale.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
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
     * Gestisce il click sul pulsante "Registrati".
     * Valida i campi, crea un oggetto Segretari e lo inserisce nel database.
     * Mostra alert di conferma o di errore e chiude la finestra se la registrazione ha successo.
     */
    @FXML
    private void onRegister() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorAlert("Tutti i campi devono essere compilati");
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

        if (!emailValida(email)) {
            showErrorAlert("L'email inserita non è valida.");
            return;
        }

        System.out.println("Registrazione segretario: " + nome + " " + cognome + " (" + email + ")");


    Segretari segretario = new Segretari(nome, cognome, email, password);

    try {
        DAO dao = SegretariDAOMySQLImpl.getInstance();
        dao.insert(segretario);
        showConfirmationAlert("Account creato correttamente");

        // Chiude la finestra di registrazione
        nomeField.getScene().getWindow().hide();

        // Reindirizza alla pagina di login
        mainApp.showLogin("SEGRETARIO");  // Metodo nel MainApp per mostrare il login

    } catch (DAOException e) {
        e.printStackTrace();
        showErrorAlert("L'email inserita è già utilizzata da un altro utente.");
    }
    }

    /**
     * Gestisce il click sul pulsante "Indietro", chiudendo la finestra di registrazione.
     */
    @FXML
    private void onBack() {
        nomeField.getScene().getWindow().hide();
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
            return false;
        }

        return true;
    }
    }



