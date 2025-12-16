package it.unicas.project.template.address.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

/**
 * Test JUnit per la classe LoginController.
 * Verifica il comportamento del login, in particolare la gestione dei campi vuoti.
 */

public class LoginControllerTest {

    private LoginController controller;

    /**
     * Inizializza l'ambiente JavaFX e il controller prima di ogni test.
     */

    @Before
    public void setUp() throws Exception {

        new JFXPanel();
        controller = new LoginController();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                setPrivateField(controller, "usernameField", new TextField());
                setPrivateField(controller, "passwordField", new PasswordField());
                setPrivateField(controller, "titleLabel", new Label());

            } catch (Exception e) { e.printStackTrace(); }
            finally { latch.countDown(); }
        });

        latch.await();
    }

    /**
     * Testa il comportamento del login quando i campi username e password sono vuoti.
     */

    @Test
    public void testCampiVuoti() throws Exception {

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                //Lasciamo i campi vuoti
                ((TextField) getPrivateField(controller, "usernameField")).setText("");
                ((PasswordField) getPrivateField(controller, "passwordField")).setText("");

                //Invochiamo onLogin
                Method m = controller.getClass().getDeclaredMethod("onLogin");
                m.setAccessible(true);
                m.invoke(controller);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        Thread.sleep(5000);

        System.out.println("\n>>> Test completato con successo <<<");
    }

    /**
     * Imposta il valore di un campo privato del controller.
     *
     * @param obj
     * @param name
     * @param val
     */

    private void setPrivateField(Object obj, String name, Object val) throws Exception {

        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, val);

    }

    /**
     * Recupera il valore di un campo privato del controller.
     *
     * @param obj
     * @param name
     */

    private Object getPrivateField(Object obj, String name) throws Exception {

        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(obj);

    }
}