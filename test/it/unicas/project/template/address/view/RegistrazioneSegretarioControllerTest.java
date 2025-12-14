package it.unicas.project.template.address.view;

import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RegistrazioneSegretarioControllerTest {

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
        eseguiAzione("Mario123", "Rossi", "mario@test.it", "password");

        //CONTROLLO 2: Cognome Errato
        System.out.println("[STEP 2] Test Cognome Errato (Rossi123)...");
        eseguiAzione("Mario", "Rossi123", "mario@test.it", "password");

        //CONTROLLO 3: Email Errata
        System.out.println("[STEP 3] Test Email Errata (mario@test)...");
        eseguiAzione("Mario", "Rossi", "mario@test", "password");

        System.out.println(">>> Tutti i test completati con successo <<<");
    }

    private void eseguiAzione(String nome, String cognome, String email, String password) throws Exception {

        RegistrazioneSegretarioController controller = new RegistrazioneSegretarioController();

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {

                TextField nomeField = new TextField();
                TextField cognomeField = new TextField();
                TextField emailField = new TextField();
                PasswordField passwordField = new PasswordField();

                setPrivateField(controller, "nomeField", nomeField);
                setPrivateField(controller, "cognomeField", cognomeField);
                setPrivateField(controller, "emailField", emailField);
                setPrivateField(controller, "passwordField", passwordField);

                //Riempie i campi con i dati di test
                nomeField.setText(nome);
                cognomeField.setText(cognome);
                emailField.setText(email);
                passwordField.setText(password);

                //Chiama onRegister()
                Method m = controller.getClass().getDeclaredMethod("onRegister");
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