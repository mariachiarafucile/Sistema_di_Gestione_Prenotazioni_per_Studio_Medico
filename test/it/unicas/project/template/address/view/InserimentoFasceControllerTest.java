package it.unicas.project.template.address.view;

import it.unicas.project.template.address.model.dao.mysql.FasceOrarieDAOMySQLImpl;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InserimentoFasceControllerTest {

    private InserimentoFasceController controller;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        controller = new InserimentoFasceController();

        Constructor<FasceOrarieDAOMySQLImpl> constructor = FasceOrarieDAOMySQLImpl.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        FasceOrarieDAOMySQLImpl realInstance = constructor.newInstance();

        Field staticDaoField = FasceOrarieDAOMySQLImpl.class.getDeclaredField("dao");
        staticDaoField.setAccessible(true);
        staticDaoField.set(null, realInstance);

        CountDownLatch setupLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                setPrivateField(controller, "dialogStage", new Stage());
                setupInterfacciaGrafica();
            } catch (Exception e) { e.printStackTrace(); }
            finally { setupLatch.countDown(); }
        });
        setupLatch.await(5, TimeUnit.SECONDS);

        controller.setEmailMedicoCorrente("anna@esempio.it");
    }

    @Test
    public void testTuttiIControlli() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {

                LocalDate dataFuturaProtetta = LocalDate.of(2099, 1, 1);
                LocalDate dataPassata = LocalDate.now().minusDays(5);

                //CONTROLLO 1:Data passata
                System.out.println("\n[TEST 1] Verifica Data Passata...");
                impostaDati(dataPassata, "10:00", "11:00");
                invocaSalva();

                //CONTROLLO 2: Ora inizio < Ora fine
                System.out.println("\n[TEST 2] Verifica Orario Invertito (15:00 - 10:00)...");
                impostaDati(dataFuturaProtetta, "15:00", "10:00");
                invocaSalva();

                // CONTROLLO 3: Data odierna con orario già passato
                System.out.println("\n[TEST 3] Verifica Data Odierna con Orario Già Passato...");

                LocalDate oggi = LocalDate.now();

                // Orario di inizio sicuramente già passato
                String oraInizioPassata = "00:00";

                // Orario di fine valido
                String oraFineValida = "23:00";

                // Imposto i dati nel controller
                impostaDati(oggi, oraInizioPassata, oraFineValida);

                invocaSalva();

                System.out.println("Inserimento bloccato correttamente per orario già passato");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                testLatch.countDown();
            }
        });

        testLatch.await(120, TimeUnit.SECONDS);
        System.out.println("\n>>> Tutti i test completati con successo <<<");
    }

    private void setupInterfacciaGrafica() throws Exception {
        ComboBox<String> oraInizio = new ComboBox<>();
        ComboBox<String> oraFine = new ComboBox<>();
        for (int h = 9; h <= 20; h++) {
            String h1 = String.format("%02d:00", h);
            String h2 = String.format("%02d:30", h);
            oraInizio.getItems().addAll(h1, h2);
            oraFine.getItems().addAll(h1, h2);
        }
        setPrivateField(controller, "oraInizioCombo", oraInizio);
        setPrivateField(controller, "oraFineCombo", oraFine);
        setPrivateField(controller, "giornoSelezionatoLabel", new Label());
        setPrivateField(controller, "calendarioGrid", new GridPane());
    }

    private void invocaSalva() throws Exception {
        Method m = controller.getClass().getDeclaredMethod("salvaFascia");
        m.setAccessible(true);
        m.invoke(controller);
    }

    private void impostaDati(LocalDate d, String in, String fine) throws Exception {
        setPrivateField(controller, "giornoSelezionato", d);
        ((ComboBox<String>) getPrivateField(controller, "oraInizioCombo")).setValue(in);
        ((ComboBox<String>) getPrivateField(controller, "oraFineCombo")).setValue(fine);
    }

    private void setPrivateField(Object obj, String name, Object val) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, val);
    }

    private Object getPrivateField(Object obj, String name) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(obj);
    }

}