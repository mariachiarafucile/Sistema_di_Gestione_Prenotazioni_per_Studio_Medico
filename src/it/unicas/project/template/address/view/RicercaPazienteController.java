package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import it.unicas.project.template.address.model.Pazienti;
import java.util.List;
import it.unicas.project.template.address.model.dao.mysql.PazientiDAOMySQLImpl;

import static it.unicas.project.template.address.util.AlertUtils.showErrorAlert;

public class RicercaPazienteController {

    @FXML
    private TextField cfField;
    @FXML
    private Button cercaButton;
    @FXML
    private VBox risultatiBox;
    @FXML
    private Button modificaBtn;
    @FXML
    private Button visiteBtn;
    @FXML
    private Button prenotaBtn;
    @FXML
    private Button modificaNoteBtn;
    @FXML
    private Button aggiungiPrescrizioneBtn;

    private MainApp mainApp;

    private Stage dialogStage;

    private Pazienti pazienteTrovato;

    private String loginMode; // "MEDICO" o "SEGRETARIO"

    public void setLoginRole(String role) {
        this.loginMode = role;
    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        //caricamento logo
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
                modificaBtn.setManaged(true);
                visiteBtn.setVisible(true);
                visiteBtn.setManaged(true);
                prenotaBtn.setVisible(true);
                prenotaBtn.setManaged(true);
            }

            if ("MEDICO".equals(loginMode)) {
                modificaNoteBtn.setVisible(true);
                modificaNoteBtn.setManaged(true);
                aggiungiPrescrizioneBtn.setVisible(true);
                aggiungiPrescrizioneBtn.setManaged(true);
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
    private void onModificaNoteCliniche() {

        // Apri una finestra di dialogo per modificare le note cliniche
        TextField input = new TextField(pazienteTrovato.getNoteCliniche());
        Stage stage = new Stage();
        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            stage.getIcons().add(logo);
        } catch (Exception ex) {
            System.out.println("Logo non trovato");
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modifica Note Cliniche");

        VBox box = new VBox(10, new Label("Modifica Note Cliniche:"), input);
        box.setPadding(new Insets(15));
        Button salva = new Button("Salva");
        salva.setOnAction(e -> {
            //Aggiorno l'oggetto in memoria
            pazienteTrovato.setNoteCliniche(input.getText());
            // Aggiorno il DB
            try {
                PazientiDAOMySQLImpl.getInstance().update(pazienteTrovato);
            } catch (Exception ex) {
                showErrorAlert("Errore aggiornamento DB: " + ex.getMessage());
            }
            stage.close();

            // Aggiorno solo la GUI
            onCerca();
        });

        HBox buttons = new HBox(salva);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(5, 0, 0, 0));

        box.getChildren().add(buttons);

        stage.setScene(new Scene(box, 350, 150));
        stage.showAndWait();
    }

    @FXML
    private void onAggiungiPrescrizione() {
        if (pazienteTrovato == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListaVisite.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Modifica Prescrizioni");

            stage.setScene(new Scene(loader.load()));

            ListaVisiteController controller = loader.getController();
            controller.setDialogStage(stage);
            controller.setCodiceFiscale(pazienteTrovato.getCodiceFiscale());
            controller.setRuoloUtente(loginMode); // passa MEDICO o SEGRETARIO
            controller.setModalitaPrescrizione(true); // solo prescrizioni modificabili

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Errore aprendo la finestra prescrizioni: " + e.getMessage());
        }
    }


    @FXML
    private void onPrenotazioni() {
        mainApp.showListaPrenotazioniDialog(pazienteTrovato.getCodiceFiscale());
    }

    @FXML
    private void apriListaVisite() {
        if (pazienteTrovato == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListaVisite.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Lista Visite");

            stage.setScene(new Scene(loader.load()));

            ListaVisiteController controller = loader.getController();
            controller.setDialogStage(stage);
            controller.setCodiceFiscale(pazienteTrovato.getCodiceFiscale());
            controller.setRuoloUtente(loginMode);
            controller.setModalitaPrescrizione(false); // importo/stato modificabili

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Errore aprendo la finestra visite: " + e.getMessage());
        }
    }

}
