package it.unicas.project.template.address;

import java.io.IOException;
import java.util.List;
import it.unicas.project.template.address.model.*;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.*;
import it.unicas.project.template.address.view.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static it.unicas.project.template.address.util.AlertUtils.alertExit;

/**
 * Classe principale dell'applicazione.
 * Gestisce l'avvio dell'applicazione, il caricamento delle schermate e la gestione delle prenotazioni scadute.
 */

public class MainApp extends Application {

    private Stage primaryStage;

    /**
     * Costruttore
     */

    public MainApp() {
    }

    /**
     * Avvia l'applicazione, inizializza il stage principale e mostra la schermata di identificazione.
     *
     * @param primaryStage
     */

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Clinic App");

        // Mostra il logo
        primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));

        try {
            SegretariDAOMySQLImpl segDAO = SegretariDAOMySQLImpl.getInstance();

            if (!segDAO.existsByEmail("segretario@sistema.it")) {
                Segretari sistema = new Segretari(
                        "segretario@sistema.it",
                        "Segretario",
                        "di Sistema",
                        "SYSTEM"
                );
                segDAO.insert(sistema);
            }

        } catch (DAOException e) {
            e.printStackTrace();
        }

        gestisciPrenotazioniScadute();

        showIdentificazione();

        primaryStage.show();

    }

    /**
     * Gestisce tutte le prenotazioni scadute trasformandole in visite e
     * generando i pagamenti corrispondenti.
     */

    private void gestisciPrenotazioniScadute() {
        try {

            List<Prenotazioni> prenotazioni = PrenotazioniDAOMySQLImpl.getInstance().selectPrenotazioniScadute();

            for (Prenotazioni p : prenotazioni) {

                FasceOrarie fascia = (FasceOrarie) FasceOrarieDAOMySQLImpl.getInstance()
                        .select(new FasceOrarie(p.getFasciaOrariaId(), null, null, null)).get(0);

                Visite visita = new Visite();
                visita.setDataOra(fascia.getData() + " " + fascia.getOraInizio() + ":00");
                visita.setPazienteCodiceFiscale(p.getPazienteCodiceFiscale());
                visita.setPrescrizione("");
                visita.setSegretarioEmail("segretario@sistema.it");

                VisiteDAOMySQLImpl.getInstance().insert(visita);

                Pagamenti pagamento = new Pagamenti(
                        null,
                        "da saldare",
                        0.0,
                        visita.getSegretarioEmail(),
                        visita.getIdVisita()
                );

                PagamentiDAOMySQLImpl.getInstance().insert(pagamento);

                PrenotazioniDAOMySQLImpl.getInstance().delete(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la schermata di identificazione iniziale.
     */

    public void showIdentificazione() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Identificazione.fxml"));
            VBox identificazione = loader.load();

            Scene scene = new Scene(identificazione);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(windowEvent ->
            {
                windowEvent.consume();
                alertExit();
            });

            IdentificazioneController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();


        }

    }

    /**
     * Mostra la schermata di login per un ruolo specifico.
     *
     * @param role
     */

    public void showLogin(String role) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Login.fxml"));
            VBox page = loader.load();

            Scene scene = new Scene(page);
            primaryStage.setScene(scene);

            LoginController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoginMode(role);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra il dialog per la registrazione di un segretario.
     */

    public void showRegistrazioneSegretario() {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegistrazioneSegretario.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrazione Segretario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RegistrazioneSegretarioController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra il dialog per la registrazione di un medico.
     */

    public void showRegistrazioneMedico() {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegistrazioneMedico.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrazione Medico");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RegistrazioneMedicoController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la dashboard per il segretario.
     */

    public void showSegretarioDashboard() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SegretarioDashboard.fxml"));
            BorderPane dashboard = loader.load();

            Scene scene = new Scene(dashboard);
            primaryStage.setScene(scene);
            primaryStage.show();

            SegretarioDashboardController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la dashboard per il medico corrente.
     *
     * @param emailMedicoCorrente
     */

    public void showMedicoDashboard(String emailMedicoCorrente) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MedicoDashboard.fxml"));
            BorderPane dashboard = loader.load();

            Scene scene = new Scene(dashboard);
            primaryStage.setScene(scene);
            primaryStage.show();

            MedicoDashboardController controller = loader.getController();
            controller.setMainApp(this);

            controller.setEmailMedicoCorrente(emailMedicoCorrente);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la finestra di ricerca paziente.
     *
     * @param loginMode
     */

    public void showRicercaPazienteDialog(String loginMode) {

        System.out.println("Apri pannello di Ricerca Paziente");

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RicercaPaziente.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ricerca Pazienti");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page, 500, 600);
            dialogStage.setScene(scene);

            RicercaPazienteController controller = loader.getController();

            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            controller.setLoginRole(loginMode);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la finestra per l'inserimento delle fasce orarie di un medico.
     *
     * @param emailMedicoCorrente
     */

    public void showInserimentoFasceDialog(String emailMedicoCorrente) {

        System.out.println("Apri pannello Inserimento Fasce Orarie");

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/InserimentoFasce.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Inserimento Fasce Orarie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page, 500, 600);
            dialogStage.setScene(scene);

            InserimentoFasceController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            controller.setEmailMedicoCorrente(emailMedicoCorrente);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la finestra per la registrazione di un nuovo paziente.
     */

    public void showAggiungiPazienteDialog() {

        System.out.println("Apri pannello Aggiungi Paziente");

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegistrazionePaziente.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrazione Paziente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RegistrazionePazienteController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la finestra del report mensile.
     */

    public void ShowReportDialog() {

        System.out.println("Apri pannello Report");

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Report.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Report Mensile");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ReportController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra la finestra per modificare i dati di un paziente.
     *
     * @param p
     */

    public boolean showModificaPazienteDialog(Pazienti p) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ModificaPaziente.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifica Paziente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ModificaPazienteController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(this);
            controller.setPaziente(p);

            dialogStage.showAndWait();
            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mostra la lista delle prenotazioni di un paziente.
     *
     * @param codiceFiscale
     */

    public void showListaPrenotazioniDialog(String codiceFiscale) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/unicas/project/template/address/view/ListaPrenotazioni.fxml"));

            VBox page = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Prenotazioni paziente");
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(page);
            stage.setScene(scene);

            ListaPrenotazioniController controller = loader.getController();
            controller.setDialogStage(stage);
            controller.setMainApp(this);
            controller.setPazienteCF(codiceFiscale);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra il form per creare o modificare una prenotazione.
     *
     * @param codiceFiscalePaziente
     * @param prenotazioneDaModificare
     */

    public boolean showFormPrenotazioneDialog(String codiceFiscalePaziente, Prenotazioni prenotazioneDaModificare) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/it/unicas/project/template/address/view/FormPrenotazione.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Form Prenotazione");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            FormPrenotazioneController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPazienteCF(codiceFiscalePaziente);
            controller.setPrenotazioneDaModificare(prenotazioneDaModificare);

            dialogStage.showAndWait();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
