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

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Constructor
     */
    public MainApp() {
    }


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

        // Gestisce prenotazioni scadute
        gestisciPrenotazioniScadute();

        // Mostra subito la schermata di identificazione
        showIdentificazione();

        primaryStage.show();

    }

    private void gestisciPrenotazioniScadute() {
        try {
            //Ritorna solo le prenotazioni scadute
            List<Prenotazioni> prenotazioni = PrenotazioniDAOMySQLImpl.getInstance().selectPrenotazioniScadute();

            for (Prenotazioni p : prenotazioni) {
                // Recupera la fascia oraria
                FasceOrarie fascia = (FasceOrarie) FasceOrarieDAOMySQLImpl.getInstance()
                        .select(new FasceOrarie(p.getFasciaOrariaId(), null, null, null)).get(0);

                // Crea nuova visita
                Visite visita = new Visite();
                visita.setDataOra(fascia.getData() + " " + fascia.getOraInizio() + ":00");
                visita.setPazienteCodiceFiscale(p.getPazienteCodiceFiscale());
                visita.setPrescrizione("");
                visita.setSegretarioEmail("segretario@sistema.it");

                //Inserisce visita e recupera ID generato
                VisiteDAOMySQLImpl.getInstance().insert(visita);

                Pagamenti pagamento = new Pagamenti(
                        null,
                        "da saldare",
                        0.0,
                        visita.getSegretarioEmail(),
                        visita.getIdVisita()
                );

                PagamentiDAOMySQLImpl.getInstance().insert(pagamento);

                // Elimina prenotazione scaduta
                PrenotazioniDAOMySQLImpl.getInstance().delete(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

            // Collega controller ↔ main app
            IdentificazioneController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();


        }

    }

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

    public void showRegistrazioneSegretario() {
        try {
            // 1. Carica il FXML della registrazione del segretario
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegistrazioneSegretario.fxml"));
            VBox page = loader.load();

            // 2. Crea un nuovo stage per la finestra di registrazione
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrazione Segretario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // 3. Passa riferimento al MainApp nel controller
            RegistrazioneSegretarioController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRegistrazioneMedico() {
        try {
            // 1. Carica il FXML della registrazione del medico
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RegistrazioneMedico.fxml"));
            VBox page = loader.load();

            // 2. Crea un nuovo stage per la finestra di registrazione
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Registrazione Medico");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // 3. Passa riferimento al MainApp nel controller
            RegistrazioneMedicoController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSegretarioDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SegretarioDashboard.fxml"));
            BorderPane dashboard = loader.load();

            // Crea la scena con la dashboard
            Scene scene = new Scene(dashboard);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Passa il riferimento al MainApp nel controller
            SegretarioDashboardController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMedicoDashboard(String emailMedicoCorrente) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MedicoDashboard.fxml"));
            BorderPane dashboard = loader.load();

            // Crea la scena con la dashboard
            Scene scene = new Scene(dashboard);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Passa il riferimento al MainApp nel controller
            MedicoDashboardController controller = loader.getController();
            controller.setMainApp(this);

            // Passa l'email del medico corrente
              controller.setEmailMedicoCorrente(emailMedicoCorrente);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRicercaPazienteDialog(String loginMode) {
        System.out.println("Apri pannello di Ricerca Paziente");
        try {
            // 1. Carica l'FXML della ricerca pazienti
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RicercaPaziente.fxml"));
            VBox page = loader.load();

            // 2. Crea un nuovo stage per la finestra
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ricerca Pazienti");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page, 500, 600);
            dialogStage.setScene(scene);

            // 3. Passa riferimento al MainApp nel controller
            RicercaPazienteController controller = loader.getController();

            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            // Passa il ruolo di login
            controller.setLoginRole(loginMode);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            // Passa l'email del medico corrente
            controller.setEmailMedicoCorrente(emailMedicoCorrente);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

            // Passaggio dati al controller
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

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        MainApp.launch(args);
    }
}
