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

import java.util.Optional;

import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;

public class LoginController {

    @FXML
    private Label titleLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private MainApp mainApp;
    private String loginMode; // "MEDICO" o "SEGRETARIO"

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Imposta se siamo nel login Medico o Segretario.
     */
    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;

        // if (titleLabel != null) {
        titleLabel.setText("Login ");
        //  }
    }

    @FXML
    private void onLogin() throws DAOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        System.out.println("Tentativo login (" + loginMode + "): "
                + user + " / " + pass);

        if (user.isEmpty() || pass.isEmpty()) {
            showErrorAlert("Credenziali non valide. Riprova.");
            return;
        }

        if (loginMode.equals("MEDICO")) {
            DAO<Medici> dao = MediciDAOMySQLImpl.getInstance();
            boolean valid = ((MediciDAOMySQLImpl) dao).existsMedico(user, pass);

            if (!valid) {
                showErrorAlert("Credenziali non valide. Riprova.");
                return;
            }
            mainApp.showMedicoDashboard(user);
        }
        if (loginMode.equals("SEGRETARIO")) {
            DAO<Segretari> dao = SegretariDAOMySQLImpl.getInstance();
            boolean valid = ((SegretariDAOMySQLImpl) dao).existsSegretario(user, pass);

            if (!valid) {
                showErrorAlert("Credenziali non valide. Riprova.");
                return;
               }
            mainApp.showSegretarioDashboard();
            }
        }


    @FXML
    private void onRegister() {

        if (loginMode.equals("MEDICO")) {
            mainApp.showRegistrazioneMedico();
        } else {
            mainApp.showRegistrazioneSegretario();
        }
    }

    @FXML
    private void onBack() {
        mainApp.showIdentificazione();
    }


}