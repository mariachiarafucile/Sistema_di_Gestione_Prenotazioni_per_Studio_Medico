package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.model.AlertUtils;
import it.unicas.project.template.address.model.Pazienti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.PazientiDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

import static it.unicas.project.template.address.model.AlertUtils.showConfirmationAlert;
import static it.unicas.project.template.address.model.AlertUtils.showErrorAlert;

public class RegistrazionePazienteController {

    @FXML private TextField nomeField;
    @FXML private TextField cognomeField;
    @FXML private TextField codiceFiscaleField;
    @FXML private TextField indirizzoField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private TextArea noteClinicheField;
    @FXML private TextField dataNascitaFieldVisibile;
    @FXML private Button calendarToggleButton;
    @FXML private VBox calendarContainer;
    @FXML private GridPane calendarioGrid;
    @FXML private Label meseLabel;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;

    private MainApp mainApp;
    private Stage dialogStage;
    private YearMonth meseCorrente;

    public void setMainApp(MainApp mainApp) { this.mainApp = mainApp; }
    public void setDialogStage(Stage stage) { this.dialogStage = stage; }

    @FXML
    private void initialize() {
        // Carica logo
        nomeField.sceneProperty().addListener((obs, oldScene, newScene) -> {
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

        // Inizializza calendario
        meseCorrente = YearMonth.now();
        aggiornaCalendario();

        // Mostra/nascondi calendario completo
        calendarToggleButton.setOnAction(e -> {
            boolean isVisible = calendarContainer.isVisible();
            calendarContainer.setVisible(!isVisible);
            calendarContainer.setManaged(!isVisible);
        });

        prevMonthButton.setOnAction(e -> {
            meseCorrente = meseCorrente.minusMonths(1);
            aggiornaCalendario();
        });

        nextMonthButton.setOnAction(e -> {
            meseCorrente = meseCorrente.plusMonths(1);
            aggiornaCalendario();
        });
    }

    private void aggiornaCalendario() {
        calendarioGrid.getChildren().clear();

        meseLabel.setText(meseCorrente.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault())
                + " " + meseCorrente.getYear());

        DayOfWeek[] order = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};

        // Intestazione giorni
        for (int c = 0; c < 7; c++) {
            Label h = new Label(order[c].getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            h.setMinSize(30, 30);
            h.setAlignment(Pos.CENTER);
            h.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-padding: 2;");
            calendarioGrid.add(h, c, 0);
        }

        LocalDate firstDay = meseCorrente.atDay(1);
        int firstColumn = firstDay.getDayOfWeek().getValue() - 1;
        int giorniNelMese = meseCorrente.lengthOfMonth();

        int col = firstColumn;
        int row = 1;

        for (int day = 1; day <= giorniNelMese; day++) {
            LocalDate date = meseCorrente.atDay(day);

            Label lbl = new Label(String.valueOf(day));
            lbl.setMinSize(30, 30);
            lbl.setAlignment(Pos.CENTER);
            lbl.setStyle("-fx-font-size: 10px; -fx-padding: 4; -fx-border-color: #cccccc; -fx-alignment: center; -fx-cursor: hand;");

            lbl.setOnMouseClicked(evt -> {
                dataNascitaFieldVisibile.setText(date.toString());
                calendarContainer.setVisible(false);
                calendarContainer.setManaged(false);
            });

            calendarioGrid.add(lbl, col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void onRegistra() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String dataNascita = dataNascitaFieldVisibile.getText();
        String cf = codiceFiscaleField.getText();
        String indirizzo = indirizzoField.getText();
        String telefono = telefonoField.getText();
        String email = emailField.getText();
        String note = noteClinicheField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty() ||
                cf.isEmpty() || indirizzo.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            showErrorAlert("Tutti i campi (eccetto le note cliniche) devono essere compilati");
            return;
        }

        for (char c : nome.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                showErrorAlert("Il nome deve contenere solo lettere.");
                return;
            }
        }

        for (char c : cognome.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                showErrorAlert("Il cognome deve contenere solo lettere.");
                return;
            }
        }

        //Converte temporalmente la data di nascita in LocalDate per la validazione
        LocalDate dataNascitaLD = LocalDate.parse(dataNascita);
        if (!dataNascitaLD.isBefore(LocalDate.now())) {
            showErrorAlert("La data di nascita non può essere oggi o futura.");
            return;
        }

        if (cf.length() != 16) {
            showErrorAlert("Il codice fiscale deve essere lungo esattamente 16 caratteri.");
            return;
        } else {
            // Controllo che siano solo caratteri alfanumerici
            boolean valido = true;
            for (int i = 0; i < cf.length(); i++) {
                char c = cf.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    valido = false;
                    break;
                }
            }

            if (!valido) {
                showErrorAlert("Il codice fiscale deve contenere solo caratteri alfanumerici.");
                return;
            }
        }

        for (char c : indirizzo.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != ' ') {
                showErrorAlert("L'indirizzo non può contenere caratteri speciali.");
                return;
            }
        }

        if (!telefonoValido(telefono)) {
            showErrorAlert("Il numero di telefono deve contenere esattamente 10 cifre.");
            return;
        }

        if (!emailValida(email)) {
            showErrorAlert("L'email inserita non è valida.");
            return;
        }

        System.out.println("Registrazione paziente:");
        System.out.println(nome + " " + cognome + " - " + dataNascita + " - " + cf);

        Pazienti paziente = new Pazienti(nome, cognome, dataNascita, cf, indirizzo, telefono, email, note);

        try {
            DAO dao = PazientiDAOMySQLImpl.getInstance();
            dao.insert(paziente);
            showConfirmationAlert("Account creato correttamente");
        } catch (DAOException e) {
            e.printStackTrace();
            showErrorAlert("Il codice fiscale inserito è già utilizzato da un altro utente.");
            return;
        }

        if (dialogStage != null) dialogStage.close();
    }

    @FXML
    private void onAnnulla() {
        if (dialogStage != null) dialogStage.close();
    }

    private boolean telefonoValido(String telefono) {
        if (telefono.length() != 10) {
            return false;
        }


        for (char c : telefono.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean emailValida(String email) {
        int atIndex = email.indexOf("@");
        int dotIndex = email.lastIndexOf(".");

        // Controlli principali
        if (atIndex <= 0) {
            return false; // niente prima della @ oppure @ all'inizio
        }
        if (dotIndex < atIndex + 2) {
            return false; // il . deve venire dopo la @ e almeno un carattere tra @ e .
        }
        if (dotIndex == email.length() - 1) {
            return false; // non deve terminare con .
        }

        return true;
    }

}
