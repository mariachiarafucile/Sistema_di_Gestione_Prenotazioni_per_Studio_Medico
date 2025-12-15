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

public class ModificaPazienteController {

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

    // OGGETTO DA MODIFICARE
    private Pazienti pazienteDaModificare;

    private boolean okClicked = false;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        // Caricamento del logo
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

    // Metodo per riempire automaticamente i campi
    public void setPaziente(Pazienti p) {
        this.pazienteDaModificare = p;

        nomeField.setText(p.getNome());
        cognomeField.setText(p.getCognome());
        dataNascitaField.setText(p.getDataNascita());
        codiceFiscaleField.setText(p.getCodiceFiscale());
        indirizzoField.setText(p.getIndirizzo());
        telefonoField.setText(p.getTelefono());
        emailField.setText(p.getEmail());
        noteClinicheField.setText(p.getNoteCliniche());
    }

    @FXML
    private void onRegistra() {
        // VALIDAZIONI INPUT
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
            showErrorAlert("Tutti i campi (eccetto note cliniche) devono essere compilati.");
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

        try {
            LocalDate dataNascitaLD = LocalDate.parse(dataNascita);
            if (!dataNascitaLD.isBefore(LocalDate.now())) {
                showErrorAlert("La data di nascita non può essere oggi o futura.");
                return;
            }
        } catch (Exception e) {
            showErrorAlert("Formato data non valido. Usa YYYY-MM-DD.");
            return;
        }

        if (cf.length() != 16) {
            showErrorAlert("Il codice fiscale deve avere 16 caratteri.");
            return;
        }

        for (int i = 0; i < cf.length(); i++) {
            char c = cf.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
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

        if (!telefonoValido(telefono)) {
            showErrorAlert("Il numero di telefono deve contenere esattamente 10 cifre.");
            return;
        }

        if (!emailValida(email)) {
            showErrorAlert("L'email non è valida.");
            return;
        }

        // AGGIORNAMENTO DATI DEL PAZIENTE
        pazienteDaModificare.setNome(nome);
        pazienteDaModificare.setCognome(cognome);
        pazienteDaModificare.setDataNascita(dataNascita);
        pazienteDaModificare.setCodiceFiscale(cf);
        pazienteDaModificare.setIndirizzo(indirizzo);
        pazienteDaModificare.setTelefono(telefono);
        pazienteDaModificare.setEmail(email);
        pazienteDaModificare.setNoteCliniche(note);

      try {
            DAO dao = PazientiDAOMySQLImpl.getInstance();
            dao.update(pazienteDaModificare);  // UPDATE INVECE DI INSERT
            showConfirmationAlert("Dati del paziente aggiornati correttamente.");
        } catch (DAOException e) {
            e.printStackTrace();
            showErrorAlert("Errore durante l'aggiornamento.");
            return;
        }
        okClicked = true;

        if (dialogStage != null) dialogStage.close();
    }

    @FXML
    private void onAnnulla() {
        if (dialogStage != null) dialogStage.close();
    }

    private boolean telefonoValido(String telefono) {
        if (telefono.length() != 10) return false;
        for (char c : telefono.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private boolean emailValida(String email) {
        int atIndex = email.indexOf("@");
        int dotIndex = email.lastIndexOf(".");
        if (atIndex <= 0) return false;
        if (dotIndex < atIndex + 2) return false;
        if (dotIndex == email.length() - 1) return false;
        return true;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

}
