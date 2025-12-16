package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.Medici;
import it.unicas.project.template.address.model.Segretari;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.MediciDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.SegretariDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import static it.unicas.project.template.address.util.AlertUtils.showErrorAlert;

/**
 * Controller per la schermata di login.
 *
 * Permette l'accesso di medici e segretari
 * e gestisce la registrazione e il ritorno alla schermata di identificazione.
 *
 */
public class LoginController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private MainApp mainApp;
    private String loginMode; // "MEDICO" o "SEGRETARIO"

    /**
     * Imposta il riferimento all'applicazione principale.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Imposta la modalità di login (medico o segretario).
     *
     * @param loginMode
     */
    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
        titleLabel.setText("Login ");
    }

    /**
     * Esegue il login verificando le credenziali.
     *
     */
    @FXML
    private void onLogin() throws DAOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        System.out.println("Tentativo login (" + loginMode + "): "
                + user + " / " + pass);

        if (user.isEmpty() || pass.isEmpty()) {
            System.out.println("Errore: credenziali non valide.");
            showErrorAlert("Credenziali non valide. Riprova.");
            return;
        }

        if (loginMode.equals("MEDICO")) {
            DAO<Medici> dao = MediciDAOMySQLImpl.getInstance();
            boolean valid = ((MediciDAOMySQLImpl) dao).existsMedico(user, pass);

            if (!valid) {
                System.out.println("Errore: credenziali non valide.");
                showErrorAlert("Credenziali non valide. Riprova.");
                return;
            }
            mainApp.showMedicoDashboard(user);
        }
        if (loginMode.equals("SEGRETARIO")) {
            DAO<Segretari> dao = SegretariDAOMySQLImpl.getInstance();
            boolean valid = ((SegretariDAOMySQLImpl) dao).existsSegretario(user, pass);

            if (!valid) {
                System.out.println("Errore: credenziali non valide.");
                showErrorAlert("Credenziali non valide. Riprova.");
                return;
               }
            mainApp.showSegretarioDashboard();
            }
        }

    /**
     * Apre la schermata di registrazione corrispondente al ruolo.
     */
    @FXML
    private void onRegister() {

        if (loginMode.equals("MEDICO")) {
            mainApp.showRegistrazioneMedico();
        } else {
            mainApp.showRegistrazioneSegretario();
        }
    }

    /**
     * Torna alla schermata di identificazione.
     */
    @FXML
    private void onBack() {
        mainApp.showIdentificazione();
    }


}