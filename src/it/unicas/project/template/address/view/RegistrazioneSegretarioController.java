package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.AlertUtils;
import it.unicas.project.template.address.model.Segretari;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.SegretariDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import static it.unicas.project.template.address.model.AlertUtils.*;

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

    @FXML
    private void onBack() {

        nomeField.getScene().getWindow().hide();
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
            return false;
        }

        return true;
    }
    }



