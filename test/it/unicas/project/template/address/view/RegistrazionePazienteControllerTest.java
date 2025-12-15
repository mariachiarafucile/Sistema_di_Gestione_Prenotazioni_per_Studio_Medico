package it.unicas.project.template.address.view;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RegistrazionePazienteControllerTest {

    @BeforeClass
    public static void initJFX() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    public void testValidazioneCompleta() throws Exception {
        // CONTROLLO 1: Nome Errato
        System.out.println("[STEP 1] Test Nome Errato (Mario123)...");
        eseguiAzione("Mario123", "Rossi", "2000-01-01", "RSSMRA00A01F205X",
                "Via Roma 1", "1234567890", "mario@test.it", "Note cliniche");

        // CONTROLLO 2: Cognome Errato
        System.out.println("[STEP 2] Test Cognome Errato (Rossi123)...");
        eseguiAzione("Mario", "Rossi123", "2000-01-01", "RSSMRA00A01F205X",
                "Via Roma 1", "1234567890", "mario@test.it", "Note cliniche");

        // CONTROLLO 3: Data di nascita futura
        System.out.println("[STEP 3] Test Data di nascita futura...");
        eseguiAzione("Mario", "Rossi", "3000-01-01", "RSSMRA00A01F205X",
                "Via Roma 1", "1234567890", "mario@test.it", "Note cliniche");

        // CONTROLLO 4: Codice fiscale errato (lunghezza)
        System.out.println("[STEP 4] Test Codice fiscale troppo corto...");
        eseguiAzione("Mario", "Rossi", "2000-01-01", "RSSMRA00A01F20",
                "Via Roma 1", "1234567890", "mario@test.it", "Note cliniche");

        // CONTROLLO 5: Codice fiscale errato (caratteri non alfanumerici)
        System.out.println("[STEP 5] Test Codice fiscale con caratteri speciali...");
        eseguiAzione("Mario", "Rossi", "2000-01-01", "RSSMRA00A01F205#",
                "Via Roma 1", "1234567890", "mario@test.it", "Note cliniche");

        // CONTROLLO 6: Indirizzo non valido
        System.out.println("[STEP 6] Test Indirizzo non valido...");
        eseguiAzione("Mario", "Rossi", "2000-01-01", "RSSMRA00A01F205X",
                "Via Roma #1", "1234567890", "mario@test.it", "Note cliniche");

        // CONTROLLO 7: Telefono non valido (lunghezza)
        System.out.println("[STEP 7] Test Telefono troppo corto...");
        eseguiAzione("Mario", "Rossi", "2000-01-01", "RSSMRA00A01F205X",
                "Via Roma 1", "12345", "mario@test.it", "Note cliniche");

        // CONTROLLO 8: Email errata
        System.out.println("[STEP 8] Test Email Errata (mario@test)...");
        eseguiAzione("Mario", "Rossi", "2000-01-01", "RSSMRA00A01F205X",
                "Via Roma 1", "1234567890", "mario@test", "Note cliniche");

        System.out.println(">>> Tutti i test completati con successo <<<");
    }

    private void eseguiAzione(String nome, String cognome, String dataNascita,
                              String cf, String indirizzo, String telefono,
                              String email, String note) throws Exception {

        RegistrazionePazienteController controller = new RegistrazionePazienteController();

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                TextField nomeField = new TextField();
                TextField cognomeField = new TextField();
                TextField dataNascitaField = new TextField();
                TextField cfField = new TextField();
                TextField indirizzoField = new TextField();
                TextField telefonoField = new TextField();
                TextField emailField = new TextField();
                TextArea noteField = new TextArea();

                setPrivateField(controller, "nomeField", nomeField);
                setPrivateField(controller, "cognomeField", cognomeField);
                setPrivateField(controller, "dataNascitaField", dataNascitaField);
                setPrivateField(controller, "codiceFiscaleField", cfField);
                setPrivateField(controller, "indirizzoField", indirizzoField);
                setPrivateField(controller, "telefonoField", telefonoField);
                setPrivateField(controller, "emailField", emailField);
                setPrivateField(controller, "noteClinicheField", noteField);

                // Riempie i campi
                nomeField.setText(nome);
                cognomeField.setText(cognome);
                dataNascitaField.setText(dataNascita);
                cfField.setText(cf);
                indirizzoField.setText(indirizzo);
                telefonoField.setText(telefono);
                emailField.setText(email);
                noteField.setText(note);

                // Chiama onRegistra()
                Method m = controller.getClass().getDeclaredMethod("onRegistra");
                m.setAccessible(true);
                m.invoke(controller);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        boolean finished = latch.await(5, TimeUnit.SECONDS);
        if (!finished) {
            throw new RuntimeException("Test bloccato su eseguiAzione: " + nome + " / " + cognome + " / " + email);
        }
    }

    private void setPrivateField(Object obj, String name, Object val) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, val);
    }
}
