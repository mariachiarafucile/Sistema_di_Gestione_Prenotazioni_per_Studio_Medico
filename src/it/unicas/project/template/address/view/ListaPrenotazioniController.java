package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.FasceOrarie;
import it.unicas.project.template.address.model.Prenotazioni;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.FasceOrarieDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.PrenotazioniDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.List;

import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;

public class ListaPrenotazioniController {

    @FXML
    private VBox prenotazioniBox;

    private Stage dialogStage;
    private String codiceFiscalePaziente;
    @FXML
    private Button nuovaPrenotazioneBtn;
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    
    public void setPazienteCF(String cf) {
        this.codiceFiscalePaziente = cf;
        caricaPrenotazioni();
    }

    @FXML
    private void initialize() {
        // Carica logo finestra
        prenotazioniBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        try {
                            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
                            ((Stage) newWindow).getIcons().add(logo);
                        } catch (Exception e) {
                            System.out.println("Logo non trovato");
                        }
                    }
                });
            }
        });
    }

    private void caricaPrenotazioni() {
        prenotazioniBox.getChildren().clear();

        try {
            DAO<Prenotazioni> daoPren = PrenotazioniDAOMySQLImpl.getInstance();
            DAO<FasceOrarie> daoFasce = FasceOrarieDAOMySQLImpl.getInstance();

            Prenotazioni filtro = new Prenotazioni();
            filtro.setPazienteCodiceFiscale(codiceFiscalePaziente);

            List<Prenotazioni> lista = daoPren.select(filtro);

            if (lista.isEmpty()) {
                prenotazioniBox.getChildren().add(new Label("❌ Nessuna prenotazione trovata"));
                return;
            }

            for (Prenotazioni p : lista) {
                FasceOrarie fo = cercaFascia(p.getFasciaOrariaId(), daoFasce);

                HBox riga = new HBox(20); // spacing tra campi
                riga.getChildren().addAll(
                        new Label("Medico: " + p.getMedicoEmail()),
                        new Label("Data: " + (fo != null ? fo.getData() : "-")),
                        new Label("Ora Inizio: " + (fo != null ? fo.getOraInizio() : "-")),
                        new Label("Ora Fine: " + (fo != null ? fo.getOraFine() : "-"))
                );

                prenotazioniBox.getChildren().add(riga);
            }

        } catch (DAOException e) {
            e.printStackTrace();
            showErrorAlert("Errore durante il caricamento delle prenotazioni.");
        }
    }

    private FasceOrarie cercaFascia(Integer id, DAO<FasceOrarie> daoFasce) {
        try {
            if (id == null) return null;

            FasceOrarie filtro = new FasceOrarie();
            filtro.setIdFasciaOraria(id);

            List<FasceOrarie> lista = daoFasce.select(filtro);
            if (lista.isEmpty()) return null;
            return lista.get(0);
        } catch (DAOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void onNuovaPrenotazione() {
       // if (mainApp != null) {
            //mainApp.showNuovaPrenotazioneDialog(codiceFiscalePaziente);
     //   }
    }

}
