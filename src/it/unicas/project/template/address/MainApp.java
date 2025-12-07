package it.unicas.project.template.address;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

import it.unicas.project.template.address.model.Amici;
import it.unicas.project.template.address.model.Pazienti;
import it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings;
import it.unicas.project.template.address.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static it.unicas.project.template.address.model.AlertUtils.alertExit;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Constructor
     */
    public MainApp() {
    }

    /**
     * The data as an observable list of Colleghis.
     */
    private ObservableList<Amici> colleghiData = FXCollections.observableArrayList();

    /**
     * Returns the data as an observable list of Colleghis.
     * @return
     */
    public ObservableList<Amici> getColleghiData() {
        return colleghiData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Clinic App");

// Set application icon
        primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));

// Mostra subito la schermata di identificazione
        showIdentificazione();

        primaryStage.show();

    }

    public void showIdentificazione() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Identificazione.fxml"));
            VBox identificazione = loader.load();

            // Crea una scena direttamente con VBox
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
            dialogStage.initModality(Modality.WINDOW_MODAL); // blocca la finestra principale
            dialogStage.initOwner(primaryStage); // rende la finestra figlia della principale
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // 3. Passa eventuale riferimento al MainApp nel controller
            RegistrazioneSegretarioController controller = loader.getController();
            controller.setMainApp(this); // se vuoi avere accesso al main app dal controller

            // 4. Mostra la finestra
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
            dialogStage.initModality(Modality.WINDOW_MODAL); // blocca la finestra principale
            dialogStage.initOwner(primaryStage); // rende la finestra figlia della principale
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // 3. Passa eventuale riferimento al MainApp nel controller
            RegistrazioneMedicoController controller = loader.getController();
            controller.setMainApp(this); // per avere accesso al main app dal controller

            // 4. Mostra la finestra
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

    public void showMedicoDashboard() {
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRicercaPazienteDialog(String loginMode) {
        System.out.println("Apri pannello Ricerca Paziente");
        try {
            // 1. Carica l'FXML della ricerca pazienti
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RicercaPaziente.fxml"));
            VBox page = loader.load();

            // 2. Crea un nuovo stage per la finestra
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ricerca Pazienti");
            dialogStage.initModality(Modality.WINDOW_MODAL); // blocca la finestra principale
            dialogStage.initOwner(primaryStage); // finestra figlia della principale

            Scene scene = new Scene(page, 500, 600);
            dialogStage.setScene(scene);

            // 3. Passa eventuale riferimento al MainApp nel controller
            RicercaPazienteController controller = loader.getController();

            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            // PASSA IL RUOLO
            controller.setLoginRole(loginMode);

            // 4. Mostra la finestra e aspetta la chiusura
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCalendarioDialog() {
        System.out.println("Apri pannello Calendario");
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CalendarioMedico.fxml"));
            VBox page = loader.load();


            Stage dialogStage = new Stage();
            dialogStage.setTitle("Calendario Medico");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page, 800, 600);
            dialogStage.setScene(scene);

            CalendarioMedicoController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInserimentoFasceDialog() {
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
            stage.initOwner(primaryStage);  // finestra principale
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(page);
            stage.setScene(scene);

            // Controller
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
     * Initializes the root layout and tries to load the last opened
     * Amici file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(windowEvent ->
            {
                windowEvent.consume();
                handleExit();
            });


            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the application.
     */
   public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sei sicuro di voler uscire?");
        alert.setHeaderText("Esci dall'applicazione");

        ButtonType buttonTypeOne = new ButtonType("Si");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            System.exit(0);
        }

    }



    /**
     * Shows the Amici overview inside the root layout.
     */
    public void showColleghiOverview() {
        try {
            // Load Amici overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ColleghiOverview.fxml"));

            // Set Amici overview into the center of root layout.
            rootLayout.setCenter(loader.load());

            // Give the controller access to the main app.
            ColleghiOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showSettingsEditDialog(DAOMySQLSettings daoMySQLSettings){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SettingsEditDialog.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("DAO settings");
            dialogStage.initModality((Modality.WINDOW_MODAL));
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);


            // Set the colleghi into the controller.
            SettingsEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSettings(daoMySQLSettings);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();


            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Opens a dialog to edit details for the specified colleghi. If the user
     * clicks OK, the changes are saved into the provided colleghi object and true
     * is returned.
     *
     * @param colleghi the colleghi object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showColleghiEditDialog(Amici colleghi, boolean verifyLen) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ColleghiEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Amici");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the colleghi into the controller.
            ColleghiEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage, verifyLen);
            controller.setColleghi(colleghi);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            dialogStage.setScene(new Scene(loader.load()));

            // Set the Colleghis into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setColleghiData(colleghiData);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Amici file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getColleghiFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setColleghiFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
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
        //System.out.println("Finito");
    }
}


class MyEventHandler implements EventHandler<WindowEvent> {
    @Override
    public void handle(WindowEvent windowEvent) {
        windowEvent.consume();
        // handleExit();
    }
}