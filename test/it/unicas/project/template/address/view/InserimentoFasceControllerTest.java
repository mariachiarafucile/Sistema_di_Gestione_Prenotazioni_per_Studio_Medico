package it.unicas.project.template.address.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InserimentoFasceControllerTest {

    private InserimentoFasceController controller;

    @Before
    public void setUp() throws Exception {
        // 1. Inizializza l'ambiente JavaFX
        new JFXPanel();
        controller = new InserimentoFasceController();

        // 2. Creazione dello Stage nel thread JavaFX (per evitare IllegalStateException)
        CountDownLatch setupLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Assegniamo uno stage finto per permettere al controller di chiamare .close()
                setPrivateField(controller, "dialogStage", new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setupLatch.countDown();
            }
        });
        setupLatch.await(5, TimeUnit.SECONDS);

        // 3. Configura l'email del medico (deve esistere nel tuo DB)
        controller.setEmailMedicoCorrente("lina@gmail.com");

        // 4. Inizializzazione componenti grafici tramite Reflection
        ComboBox<String> oraInizio = new ComboBox<>();
        ComboBox<String> oraFine = new ComboBox<>();
        for (int h = 9; h <= 20; h++) {
            oraInizio.getItems().addAll(String.format("%02d:00", h), String.format("%02d:30", h));
            oraFine.getItems().addAll(String.format("%02d:00", h), String.format("%02d:30", h));
        }

        setPrivateField(controller, "oraInizioCombo", oraInizio);
        setPrivateField(controller, "oraFineCombo", oraFine);
        setPrivateField(controller, "giornoSelezionatoLabel", new Label());
        setPrivateField(controller, "calendarioGrid", new GridPane());

        System.out.println("--- Setup completato correttamente ---");
    }

    @Test
    public void testCompletoInsequenza() throws Exception {
        CountDownLatch testLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // --- TEST 1: DATA PASSATA ---
                System.out.println("TEST 1: Data Passata");
                impostaDati(LocalDate.now().minusDays(1), "10:00", "11:00");
                invocaSalva();

                // --- TEST 2: ORARIO INVERTITO ---
                System.out.println("TEST 2: Orario Invertito");
                impostaDati(LocalDate.now().plusDays(1), "15:00", "14:00");
                invocaSalva();

                // --- TEST 3: SOVRAPPOSIZIONE ---
                // Uso una data specifica nel futuro per non sovrapporsi ai tuoi dati reali
                System.out.println("TEST 3: Sovrapposizione");
                LocalDate dataTest = LocalDate.of(2026, 2, 17);

                // A. Inserimento prima fascia (Deve dare Alert Successo)
                System.out.println("   -> Inserimento fascia base 09:00-11:00");
                impostaDati(dataTest, "09:00", "11:00");
                invocaSalva();

                // B. Tentativo sovrapposizione (Deve dare Alert Errore)
                System.out.println("   -> Tentativo sovrapposizione 10:00-10:30");
                impostaDati(dataTest, "10:00", "10:30");
                invocaSalva();

            } catch (Exception e) {
                System.err.println("ERRORE DURANTE IL TEST: " + e.getMessage());
                e.printStackTrace();
            } finally {
                testLatch.countDown();
            }
        });

        // JUnit attende che tu clicchi tutti gli OK (massimo 60 secondi)
        if (!testLatch.await(60, TimeUnit.SECONDS)) {
            System.err.println("TIMEOUT: Test interrotto per inattività.");
        }
        System.out.println(">>> FINE TUTTI I TEST <<<");
    }

    // --- METODI DI SUPPORTO ---

    private void invocaSalva() throws Exception {
        Method m = controller.getClass().getDeclaredMethod("salvaFascia");
        m.setAccessible(true);
        m.invoke(controller);
    }

    private void impostaDati(LocalDate d, String in, String fine) throws Exception {
        // Imposta la data
        Field fGiorno = controller.getClass().getDeclaredField("giornoSelezionato");
        fGiorno.setAccessible(true);
        fGiorno.set(controller, d);

        // Imposta l'ora inizio nella combo
        Field f1 = controller.getClass().getDeclaredField("oraInizioCombo");
        f1.setAccessible(true);
        ((ComboBox<String>) f1.get(controller)).setValue(in);

        // Imposta l'ora fine nella combo
        Field f2 = controller.getClass().getDeclaredField("oraFineCombo");
        f2.setAccessible(true);
        ((ComboBox<String>) f2.get(controller)).setValue(fine);
    }

    private void setPrivateField(Object obj, String name, Object val) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, val);
    }
}