package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.Medici;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.MediciDAOMySQLImpl;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RegistrazioneMedicoControllerTest {

    @BeforeClass
    public static void initJFX() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @Test
    public void testValidazioneCompleta() throws Exception {

        //CONTROLLO 1: Nome Errato
        System.out.println("[STEP 1] Test Nome Errato (Mario123)...");
        eseguiAzione("Mario123", "Rossi", "Mario@test.it", "password");

        //CONTROLLO 2: Cognome Errato
        System.out.println("[STEP 2] Test Cognome Errato (Rossi123)...");
        eseguiAzione("Mario", "Rossi123", "Mario@test.it", "password");

        //CONTROLLO 3: Specializzazione Errata
        System.out.println("[STEP 3] Test Specializzazione Errata (Cardio123)...");
        eseguiAzione("Mario", "Rossi", "Mario@test.it", "password", "Cardio123");

        //CONTROLLO 4: Telefono Errato
        System.out.println("[STEP 4] Test Telefono Errato (12345)...");
        eseguiAzione("Mario", "Rossi", "Mario@test.it", "password", "Cardiologia", "12345");

        //CONTROLLO 5: Email Errata
        System.out.println("[STEP 5] Test Email Errata (mario@test)...");
        eseguiAzione("Mario", "Rossi", "mario@test", "password", "Cardiologia", "1234567890");

        System.out.println(">>> Tutti i test completati con successo <<<");
    }

    private void eseguiAzione(String nome, String cognome, String email, String password) throws Exception {
        eseguiAzione(nome, cognome, email, password, "Cardiologia", "1234567890");
    }

    private void eseguiAzione(String nome, String cognome, String email, String password, String specializzazione) throws Exception {
        eseguiAzione(nome, cognome, email, password, specializzazione, "1234567890");
    }

    private void eseguiAzione(String nome, String cognome, String email, String password, String specializzazione, String telefono) throws Exception {

        RegistrazioneMedicoController controller = new RegistrazioneMedicoController();

        DAO mockDao = new DAO<Medici>() {
            @Override
            public void insert(Medici m) throws DAOException {
                System.out.println("insert chiamato per: " + m.getEmail());
            }

            @Override
            public void update(Medici m) throws DAOException {}

            @Override
            public void delete(Medici m) throws DAOException {}

            @Override
            public java.util.List<Medici> select(Medici m) throws DAOException { return null; }
        };

        Field daoField = MediciDAOMySQLImpl.class.getDeclaredField("dao");
        daoField.setAccessible(true);
        daoField.set(null, mockDao);

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                TextField nomeField = new TextField(nome);
                TextField cognomeField = new TextField(cognome);
                TextField specializzazioneField = new TextField(specializzazione);
                TextField telefonoField = new TextField(telefono);
                TextField emailField = new TextField(email);
                PasswordField passwordField = new PasswordField();
                passwordField.setText(password);

                setPrivateField(controller, "nomeField", nomeField);
                setPrivateField(controller, "cognomeField", cognomeField);
                setPrivateField(controller, "specializzazioneField", specializzazioneField);
                setPrivateField(controller, "telefonoField", telefonoField);
                setPrivateField(controller, "emailField", emailField);
                setPrivateField(controller, "passwordField", passwordField);

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

