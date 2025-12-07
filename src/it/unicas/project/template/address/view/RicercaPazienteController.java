package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import it.unicas.project.template.address.model.Pazienti;

import java.io.IOException;
import java.util.List;
import it.unicas.project.template.address.model.dao.mysql.PazientiDAOMySQLImpl;

import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;

public class RicercaPazienteController {

    @FXML
    private TextField cfField;

    @FXML
    private Button cercaButton;

    private MainApp mainApp;

    private Stage dialogStage;

    @FXML
    private VBox risultatiBox;
    @FXML
    private Button modificaBtn;
    @FXML
    private Button visiteBtn;
    @FXML
    private Button prenotaBtn;

    private Pazienti pazienteTrovato;

    private String loginMode; // "MEDICO" o "SEGRETARIO"

    public void setLoginRole(String role) {
        this.loginMode = role;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        // Quando la scena è pronta, allora carichiamo il logo
        cfField.sceneProperty().addListener((obs, oldScene, newScene) -> {
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
    private void onCerca() {
        String cf = cfField.getText();
        if (cf == null || cf.trim().isEmpty()) {
            showErrorAlert("Inserire un codice fiscale valido.");
            return;
        }

        // --- LOGICA DI RICERCA DEL PAZIENTE ---
        try {
            // Pulisco eventuali risultati precedenti
            risultatiBox.getChildren().clear();

            // Creo un oggetto filtro con il CF
            Pazienti filtro = new Pazienti();
            filtro.setCodiceFiscale(cf.trim());

            // Uso il DAO esistente per cercare il paziente
            List<Pazienti> lista = PazientiDAOMySQLImpl.getInstance().select(filtro);

            if (lista.isEmpty()) {
                risultatiBox.getChildren().add(new Label("❌ Nessun paziente trovato"));
                return;
            }

            // CF è univoco, prendo il primo elemento
            Pazienti p = lista.get(0);
            this.pazienteTrovato = p;

            // Creo le label per visualizzare i dati
            Label nomeLbl = new Label("Nome: " + p.getNome());
            Label cognomeLbl = new Label("Cognome: " + p.getCognome());
            Label cfLbl = new Label("Codice Fiscale: " + p.getCodiceFiscale());
            Label dataLbl = new Label("Data di nascita: " + p.getDataNascita());
            Label indirizzoLbl = new Label("Indirizzo: " + p.getIndirizzo());
            Label telefonoLbl = new Label("Telefono: " + p.getTelefono());
            Label emailLbl = new Label("Email: " + p.getEmail());
            Label noteLbl = new Label("Note cliniche: " + (p.getNoteCliniche() != null ? p.getNoteCliniche() : "-"));

            if ("SEGRETARIO".equals(loginMode)) {
                // Rende visibili i bottoni centrati in fondo
                modificaBtn.setVisible(true);
                visiteBtn.setVisible(true);
                prenotaBtn.setVisible(true);
            }

            risultatiBox.getChildren().addAll(
                    nomeLbl, cognomeLbl, cfLbl, dataLbl, indirizzoLbl, telefonoLbl, emailLbl, noteLbl
            );

        } catch (Exception e) {
            risultatiBox.getChildren().add(new Label("❌ Errore nella ricerca: " + e.getMessage()));
            e.printStackTrace();
        }

        System.out.println("Ricerca paziente con CF: " + cf);
    }

    @FXML
    private void onModifica() {

        boolean ok = mainApp.showModificaPazienteDialog(pazienteTrovato);

        if (ok) {

            // Qui mettiamo tutto il codice che aggiorna la GUI

            risultatiBox.getChildren().clear();

            Label nomeLbl = new Label("Nome: " + pazienteTrovato.getNome());
            Label cognomeLbl = new Label("Cognome: " + pazienteTrovato.getCognome());
            Label cfLbl = new Label("Codice Fiscale: " + pazienteTrovato.getCodiceFiscale());
            Label dataLbl = new Label("Data di nascita: " + pazienteTrovato.getDataNascita());
            Label indirizzoLbl = new Label("Indirizzo: " + pazienteTrovato.getIndirizzo());
            Label telefonoLbl = new Label("Telefono: " + pazienteTrovato.getTelefono());
            Label emailLbl = new Label("Email: " + pazienteTrovato.getEmail());
            Label noteLbl = new Label("Note cliniche: " + pazienteTrovato.getNoteCliniche());

            risultatiBox.getChildren().addAll(
                    nomeLbl, cognomeLbl, cfLbl, dataLbl, indirizzoLbl,
                    telefonoLbl, emailLbl, noteLbl
            );
        }
    }

    @FXML
    private void onPrenotazioni() {
        mainApp.showListaPrenotazioniDialog(pazienteTrovato.getCodiceFiscale());
    }

}
