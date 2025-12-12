package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.Pagamenti;
import it.unicas.project.template.address.model.Visite;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.PagamentiDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.VisiteDAOMySQLImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;

import java.util.List;

public class ListaVisiteController {

    @FXML
    private TableView<Visite> visiteTable;

    @FXML
    private TableColumn<Visite, String> dataCol;

    @FXML
    private TableColumn<Visite, String> prescrizioneCol;

    @FXML
    private TableColumn<Visite, String> segretarioCol;

    @FXML
    private TableColumn<Visite, String> importoCol;

    @FXML
    private TableColumn<Visite, String> statoCol;

    private Stage dialogStage;
    private String codiceFiscalePaziente;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setCodiceFiscale(String cf) {
        this.codiceFiscalePaziente = cf;
        caricaVisite();
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
        segretarioCol.setCellValueFactory(cell -> cell.getValue().segretarioEmailProperty());
        importoCol.setCellValueFactory(cell -> {

            int idVisita = cell.getValue().getIdVisita();
            System.out.println("Visita id: " + idVisita); // DEBUG

            // cerca pagamento relativo alla visita
            try {
                Pagamenti pagamento = ((PagamentiDAOMySQLImpl) PagamentiDAOMySQLImpl.getInstance()).selectByVisitaId(cell.getValue().getIdVisita());
                System.out.println("Pagamento trovato: " + pagamento); // DEBUG
                if (pagamento != null && pagamento.getImporto() != null) {
                    return new SimpleStringProperty(String.format("%.2f", pagamento.getImporto()));
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty("");
        });

        statoCol.setCellValueFactory(cell -> {
            try {
                Pagamenti pagamento = ((PagamentiDAOMySQLImpl) PagamentiDAOMySQLImpl.getInstance()).selectByVisitaId(cell.getValue().getIdVisita());
                if (pagamento != null) {
                    return new SimpleStringProperty(pagamento.getStato());
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty("");
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
