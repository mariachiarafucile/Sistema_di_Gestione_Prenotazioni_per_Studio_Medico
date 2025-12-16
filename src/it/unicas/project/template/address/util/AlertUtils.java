package it.unicas.project.template.address.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * Classe di utilità per la gestione degli alert in JavaFX.
 * Fornisce metodi statici per mostrare alert di errore, conferma, domanda e
 * uscita dall'applicazione.
 */

public class AlertUtils {

    /**
     * Mostra un alert di errore con il messaggio specificato.
     *
     * @param message
     */

    public static void showErrorAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);

        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);

        } catch (Exception e) {
           System.out.println("Logo non trovato");
        }

        alert.showAndWait();
    }

    /**
     * Mostra un alert di conferma con il messaggio specificato.
     *
     * @param message
     */

    public static void showConfirmationAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);

        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);

        } catch (Exception e) {
            System.out.println("Logo non trovato");
        }

        alert.showAndWait();
    }

    /**
     * Mostra un alert di domanda con opzioni "Sì" e "No".

     * @param message
     * @param onYes
     */

    public static void showQuestionAlert(String message, Runnable onYes) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);

        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);

        } catch (Exception e) {
            System.out.println("Logo non trovato");
        }

        ButtonType yesButton = new ButtonType("Sì");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            if (onYes != null) {
                onYes.run();
            }
        }
    }

    /**
     * Mostra un alert di conferma per l'uscita dall'applicazione.
     * Se l'utente conferma, l'applicazione viene chiusa.
     */

    public static void alertExit() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sei sicuro di voler uscire?");
        alert.setHeaderText("Esci dall'applicazione");

        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);
        } catch (Exception e) {
            System.out.println("Logo non trovato");
        }

        ButtonType buttonTypeOne = new ButtonType("Si");
        ButtonType buttonTypeCancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            System.exit(0);
        }

    }

}
