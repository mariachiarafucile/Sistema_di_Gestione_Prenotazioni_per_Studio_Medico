package it.unicas.project.template.address;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

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

        primaryStage.getIcons().add(new Image("file:resources/images/logo.png"));

        showIdentificazione();

        primaryStage.show();

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
                handleExit();
            });

            // Collega controller con main app
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

            // 3. Passa riferimento al MainApp nel controller
            RegistrazioneSegretarioController controller = loader.getController();
            controller.setMainApp(this); // accesso al main app dal controller

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

            // 3. Passa riferimento al MainApp nel controller
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