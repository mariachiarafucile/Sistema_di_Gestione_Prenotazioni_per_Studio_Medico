package it.unicas.project.template.address.model;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AlertUtils {

    // Alert di errore
    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);

        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);
        } catch (Exception e) {
            // ignora se logo non trovato
        }

        alert.showAndWait();
    }

    // Alert di conferma
    public static void showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);

        try {
            Image logo = new Image(AlertUtils.class.getResourceAsStream("/images/logo.png"));
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(logo);
        } catch (Exception e) {
            // ignora se logo non trovato
        }

        alert.showAndWait();
    }
}
