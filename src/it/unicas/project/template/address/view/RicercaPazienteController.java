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

/**
 * Controller della vista di ricerca del paziente.
 * Gestisce la ricerca di un paziente tramite codice fiscale,
 * la visualizzazione dei dati anagrafici e l’accesso alle operazioni
 * consentite in base al ruolo dell’utente (MEDICO o SEGRETARIO).
 */
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

    /**
     * Imposta il ruolo dell'utente loggato.
     *
     * @param role
     */
    public void setLoginRole(String role) {
        this.loginMode = role;
    }

    /**
     * Imposta il riferimento all'applicazione principale.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Imposta lo stage della finestra di dialogo.
     *
     * @param stage
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }


    /**
     * Inizializza il controller caricando il logo nell'applicazione.
     */
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

    /**
     * Gestisce l'azione di ricerca del paziente.
     * Esegue la ricerca tramite codice fiscale, visualizza i dati
     * del paziente trovato e abilita i pulsanti in base al ruolo utente.
     */
    @FXML
    private void onCerca() {
        String cf = cfField.getText();
        if (cf == null || cf.trim().isEmpty()) {
            showErrorAlert("Inserire un codice fiscale valido.");
            return;
        }

        try {

            risultatiBox.getChildren().clear();

            Pazienti filtro = new Pazienti();
            filtro.setCodiceFiscale(cf.trim());

            List<Pazienti> lista = PazientiDAOMySQLImpl.getInstance().select(filtro);

            if (lista.isEmpty()) {
                risultatiBox.getChildren().add(new Label("❌ Nessun paziente trovato"));
                return;
            }

            Pazienti p = lista.get(0);
            this.pazienteTrovato = p;

            Label nomeLbl = new Label("Nome: " + p.getNome());
            Label cognomeLbl = new Label("Cognome: " + p.getCognome());
            Label cfLbl = new Label("Codice Fiscale: " + p.getCodiceFiscale());
            Label dataLbl = new Label("Data di nascita: " + p.getDataNascita());
            Label indirizzoLbl = new Label("Indirizzo: " + p.getIndirizzo());
            Label telefonoLbl = new Label("Telefono: " + p.getTelefono());
            Label emailLbl = new Label("Email: " + p.getEmail());
            Label noteLbl = new Label("Note cliniche: " + (p.getNoteCliniche() != null ? p.getNoteCliniche() : "-"));

            if ("SEGRETARIO".equals(loginMode)) {
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

    /**
     * Gestisce la modifica dei dati anagrafici del paziente.
     * Apre la finestra di modifica e aggiorna la visualizzazione
     * in caso di conferma.
     */
    @FXML
    private void onModifica() {

        boolean ok = mainApp.showModificaPazienteDialog(pazienteTrovato);

        if (ok) {

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

    /**
     * Gestisce la modifica delle note cliniche del paziente.
     * Permette al medico di aggiornare le note cliniche
     * e salva le modifiche nel database.
     */
    @FXML
    private void onModificaNoteCliniche() {

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

            pazienteTrovato.setNoteCliniche(input.getText());

            try {
                PazientiDAOMySQLImpl.getInstance().update(pazienteTrovato);
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Errore aggiornamento DB: " + ex.getMessage());
            }
            stage.close();

            onCerca();
        });

        HBox buttons = new HBox(salva);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(5, 0, 0, 0));

        box.getChildren().add(buttons);

        stage.setScene(new Scene(box, 350, 150));
        stage.showAndWait();
    }

    /**
     * Apre la finestra per l'aggiunta o modifica delle prescrizioni,
     * da parte del MEDICO.
     */
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

    /**
     * Apre la lista delle prenotazioni associate al paziente.
     */
    @FXML
    private void onPrenotazioni() {
        mainApp.showListaPrenotazioniDialog(pazienteTrovato.getCodiceFiscale());
    }

    /**
     * Apre la lista delle visite del paziente.
     * Consente la visualizzazione o modifica delle visite
     * in base al ruolo dell'utente (SEGRETARIO o MEDICO).
     */
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
