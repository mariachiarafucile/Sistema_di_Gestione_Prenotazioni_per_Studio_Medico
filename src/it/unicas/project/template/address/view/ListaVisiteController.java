package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.*;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.PagamentiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.VisiteDAOMySQLImpl;
import it.unicas.project.template.address.util.AlertUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import static it.unicas.project.template.address.util.AlertUtils.showErrorAlert;

import java.util.List;

public class ListaVisiteController {

    @FXML
    private TableView<Visite> visiteTable;

    @FXML
    private TableColumn<Visite, String> dataCol;

    @FXML
    private TableColumn<Visite, String> prescrizioneCol;

    @FXML
    private TableColumn<Visite, Double> importoCol;

    @FXML
    private TableColumn<Visite, String> statoCol;

    private boolean soloPrescrizione = false;
    private String ruoloUtente = "SEGRETARIO"; // default

    private Stage dialogStage;
    private String codiceFiscalePaziente;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setCodiceFiscale(String cf) {
        this.codiceFiscalePaziente = cf;
        caricaVisite();
    }

    public void setRuoloUtente(String ruolo) {
        this.ruoloUtente = ruolo;
        aggiornaEditable();
    }

    public void setModalitaPrescrizione(boolean value) {
        this.soloPrescrizione = value;
        aggiornaEditable();
    }

    // Nuovo metodo per settare quali colonne sono editabili
    private void aggiornaEditable() {
            visiteTable.setEditable(true);

            if ("MEDICO".equals(ruoloUtente)) {
                prescrizioneCol.setEditable(true);
                importoCol.setEditable(false);
                statoCol.setEditable(false);
            } else {
                prescrizioneCol.setEditable(false);
                importoCol.setEditable(true);
                statoCol.setEditable(true);
            }
    }


    @FXML
    private void initialize() {

        visiteTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
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

        dataCol.setCellValueFactory(cell -> cell.getValue().dataOraProperty());
        prescrizioneCol.setCellValueFactory(cell -> cell.getValue().prescrizioneProperty());

        // Rende editabile la colonna prescrizione e salva su DB
        prescrizioneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        prescrizioneCol.setOnEditCommit(event -> {
                Visite visita = event.getRowValue();
                String nuovaPrescrizione = event.getNewValue();
                visita.setPrescrizione(nuovaPrescrizione); // aggiorna l'oggetto in memoria

                try {
                    VisiteDAOMySQLImpl.getInstance().update(visita); // salva su DB
                    AlertUtils.showConfirmationAlert("Prescrizione aggiornata correttamente");
                } catch (DAOException e) {
                    e.printStackTrace();
                    AlertUtils.showErrorAlert("Errore aggiornando la prescrizione: " + e.getMessage());
                }

           visiteTable.refresh();
        });

        importoCol.setCellValueFactory(cell -> {
            try {
                Pagamenti pagamento = PagamentiDAOMySQLImpl.getInstance()
                        .selectByVisitaId(cell.getValue().getIdVisita());
                if (pagamento != null && pagamento.getImporto() != null) {
                    return pagamento.importoProperty().asObject();
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
            return new SimpleDoubleProperty(0.0).asObject(); // fallback
        });

        // Creazione di una cella editabile con controllo che valore inserito sia compatibile con tipo Double
        importoCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                return value != null ? value.toString() : "";
            }

            @Override
            public Double fromString(String input) {
                try {
                    return Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    AlertUtils.showErrorAlert("Valore non valido! Inserire un numero.");
                    return null;
                }
            }
        }));

        importoCol.setOnEditCommit(event -> {
            Visite visita = event.getRowValue();
            Double nuovoImporto = event.getNewValue();

            // Se il valore inserito non è valido, torna al valore precedente
            if (nuovoImporto == null) {
                visiteTable.refresh();
                return;
            }

            try {
                PagamentiDAOMySQLImpl dao = PagamentiDAOMySQLImpl.getInstance();
                Pagamenti pagamento = dao.selectByVisitaId(visita.getIdVisita());

                if (pagamento != null) {
                    pagamento.setImporto(nuovoImporto);
                    dao.update(pagamento);

                    AlertUtils.showConfirmationAlert(
                            "Importo aggiornato correttamente a " + nuovoImporto + "€"
                    );
                }

                visiteTable.refresh();

            } catch (DAOException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Errore aggiornando l'importo: " + e.getMessage());
            }
        });

        statoCol.setCellValueFactory(cell -> {
            try {
                Pagamenti pagamento =
                        ((PagamentiDAOMySQLImpl) PagamentiDAOMySQLImpl.getInstance())
                                .selectByVisitaId(cell.getValue().getIdVisita());

                if (pagamento != null && pagamento.getStato() != null) {
                    return new SimpleStringProperty(pagamento.getStato());
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty("");
        });

        statoCol.setCellFactory(
                ComboBoxTableCell.forTableColumn("pagata", "da saldare")
        );

        statoCol.setOnEditCommit(event -> {
            Visite visita = event.getRowValue();
            String nuovoStato = event.getNewValue();

            try {
                PagamentiDAOMySQLImpl dao =
                        (PagamentiDAOMySQLImpl) PagamentiDAOMySQLImpl.getInstance();

                Pagamenti pagamento = dao.selectByVisitaId(visita.getIdVisita());

                if (pagamento != null) {
                    pagamento.setStato(nuovoStato);
                    dao.update(pagamento);

                    AlertUtils.showConfirmationAlert("Stato aggiornato correttamente a '" + nuovoStato + "'");
                }

                visiteTable.refresh();

            } catch (DAOException e) {
                e.printStackTrace();
            }
        });

    }

    private void caricaVisite() {

        try {
            // Filtra le visite per CF
            Visite filtro = new Visite();
            filtro.setPazienteCodiceFiscale(codiceFiscalePaziente);

            List<Visite> lista = VisiteDAOMySQLImpl.getInstance().selectByCF(codiceFiscalePaziente);

            ObservableList<Visite> obs = FXCollections.observableArrayList(lista);
            visiteTable.setItems(obs);

        } catch (Exception e) {
            showErrorAlert("Errore nel caricamento visite: " + e.getMessage());
        }
    }

    @FXML
    private void onChiudi() {
        dialogStage.close();
    }
}
