package it.unicas.project.template.address.view;

import it.unicas.project.template.address.MainApp;
import it.unicas.project.template.address.util.AlertUtils;
import it.unicas.project.template.address.model.FasceMedici;
import it.unicas.project.template.address.model.FasceOrarie;
import it.unicas.project.template.address.model.Prenotazioni;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.mysql.FasceMediciDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.FasceOrarieDAOMySQLImpl;
import it.unicas.project.template.address.model.dao.mysql.PrenotazioniDAOMySQLImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListaPrenotazioniController {

    @FXML
    private VBox rootVBox;

    @FXML
    private GridPane calendarioGrid;

    @FXML
    private VBox prenotazioniBox;

    @FXML
    private Button nuovaPrenotazioneBtn;

    @FXML
    private Label meseLabel;

    private Stage dialogStage;
    private String codiceFiscalePaziente;
    private MainApp mainApp;

    private DAO<Prenotazioni> daoPren;
    private DAO<FasceOrarie> daoFasce;

    private LocalDate currentMonth;

    private String giornoSelezionato = null;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
        this.dialogStage.setWidth(800);
        this.dialogStage.setHeight(600);
    }

    public void setPazienteCF(String cf) {
        this.codiceFiscalePaziente = cf;
        caricaPrenotazioni();
    }

    @FXML
    private void initialize() {
        rootVBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        try {
                            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
                            ((Stage) newWindow).getIcons().add(logo);
                        } catch (Exception e) {
                            System.out.println("Logo non trovato");
                        }
                    }
                });
            }
        });

        daoPren = PrenotazioniDAOMySQLImpl.getInstance();
        daoFasce = FasceOrarieDAOMySQLImpl.getInstance();

        currentMonth = LocalDate.now().withDayOfMonth(1);
        aggiornaEtichettaMese();
        disegnaCalendario();
    }

    private void disegnaCalendario() {
        calendarioGrid.getChildren().clear();

        try {
            Prenotazioni filtro = new Prenotazioni();
            filtro.setPazienteCodiceFiscale(codiceFiscalePaziente);
            List<Prenotazioni> listaPren = daoPren.select(filtro);

            Map<String, List<Prenotazioni>> prenMap = listaPren.stream()
                    .collect(Collectors.groupingBy(p -> {
                        try {
                            List<FasceOrarie> fasce = daoFasce.select(new FasceOrarie(p.getFasciaOrariaId(), "", "", ""));
                            if (!fasce.isEmpty()) {
                                return fasce.get(0).getData();
                            } else {
                                return "";
                            }
                        } catch (Exception e) {
                            return "";
                        }
                    }));

            int giornoSettimana = currentMonth.getDayOfWeek().getValue() % 7;
            int giorniMese = currentMonth.lengthOfMonth();

            int riga = 0;
            int col = giornoSettimana;

            for (int giorno = 1; giorno <= giorniMese; giorno++) {
                LocalDate date = currentMonth.withDayOfMonth(giorno);
                String dataStr = date.toString();

                Button giornoBtn = new Button(String.valueOf(giorno));
                giornoBtn.setPrefWidth(50);
                giornoBtn.setPrefHeight(50);

                if (prenMap.containsKey(dataStr)) {
                    giornoBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    giornoBtn.setOnAction(ev -> mostraPrenotazioniGiorno(dataStr, prenMap.get(dataStr)));
                } else {
                    giornoBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    giornoBtn.setDisable(true);
                }

                calendarioGrid.add(giornoBtn, col, riga);

                col++;
                if (col > 6) {
                    col = 0;
                    riga++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostraPrenotazioniGiorno(String data, List<Prenotazioni> lista) {

        giornoSelezionato = data;

        prenotazioniBox.getChildren().clear();
        prenotazioniBox.getChildren().add(new Label("Prenotazioni del giorno " + data));

        for (Prenotazioni p : lista) {
            try {
                List<FasceOrarie> fasce = daoFasce.select(new FasceOrarie(p.getFasciaOrariaId(), "", "", ""));
                FasceOrarie fo = fasce.isEmpty() ? null : fasce.get(0);

                HBox riga = new HBox(20);

                Label medicoLbl = new Label("Medico: " + p.getMedicoEmail());
                Label oraInizioLbl = new Label("Ora Inizio: " + (fo != null ? fo.getOraInizio() : "-"));
                Label oraFineLbl = new Label("Ora Fine: " + (fo != null ? fo.getOraFine() : "-"));

                Hyperlink btnModifica = new Hyperlink("Modifica");
                btnModifica.setOnAction(event -> {
                    try {
                        //Recupero la fascia oraria e il medico associati alla prenotazione
                        FasceMedici fm = new FasceMedici(
                                p.getFasciaOrariaId(),
                                p.getMedicoEmail()
                        );

                        mainApp.showFormPrenotazioneDialog(codiceFiscalePaziente,p);
                        caricaPrenotazioni();

                        //Ricreo la riga nella tabella ponte
                        FasceMediciDAOMySQLImpl daoFm =
                                (FasceMediciDAOMySQLImpl) FasceMediciDAOMySQLImpl.getInstance();

                        daoFm.insert(fm);   // reinserisco la disponibilità

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                Hyperlink btnElimina = new Hyperlink("Elimina");
                btnElimina.setOnAction(event -> {
                    AlertUtils.showQuestionAlert("Vuoi eliminare questa prenotazione?", () -> {
                        try {
                            daoPren.delete(p);
                            System.out.println("Prenotazione eliminata: " + p);

                            caricaPrenotazioni();

                            //Ricreo la riga nella tabella ponte
                            FasceMedici fm = new FasceMedici(
                                    p.getFasciaOrariaId(),
                                    p.getMedicoEmail()
                            );

                            FasceMediciDAOMySQLImpl daoFm =
                                    (FasceMediciDAOMySQLImpl) FasceMediciDAOMySQLImpl.getInstance();

                            daoFm.insert(fm);   // reinserisco la disponibilità

                            if (giornoSelezionato != null) {
                                List<Prenotazioni> aggiornata =
                                        daoPren.select(new Prenotazioni(null, codiceFiscalePaziente, null, null))
                                                .stream()
                                                .filter(pr -> {
                                                    try {
                                                        List<FasceOrarie> fasce2 = daoFasce.select(new FasceOrarie(pr.getFasciaOrariaId(), "", "", ""));
                                                        return !fasce2.isEmpty() && giornoSelezionato.equals(fasce2.get(0).getData());
                                                    } catch (Exception e) {
                                                        return false;
                                                    }
                                                })
                                                .collect(Collectors.toList());

                                mostraPrenotazioniGiorno(giornoSelezionato, aggiornata);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                });

                riga.getChildren().addAll(medicoLbl, oraInizioLbl, oraFineLbl, btnModifica, btnElimina);
                prenotazioniBox.getChildren().add(riga);

            } catch (Exception e) {
                // ignora
            }
        }
    }


    private void caricaPrenotazioni() {
        aggiornaEtichettaMese();
        disegnaCalendario();
    }

    @FXML
    private void onNuovaPrenotazione() {
        mainApp.showFormPrenotazioneDialog(codiceFiscalePaziente, null);
        caricaPrenotazioni();
    }

    @FXML
    private void mesePrecedente() {
        currentMonth = currentMonth.minusMonths(1);
        aggiornaEtichettaMese();
        disegnaCalendario();
    }

    @FXML
    private void meseSuccessivo() {
        currentMonth = currentMonth.plusMonths(1);
        aggiornaEtichettaMese();
        disegnaCalendario();
    }

    private void aggiornaEtichettaMese() {
        if (meseLabel != null) {
            String meseAnno = currentMonth.getMonth().toString() + " " + currentMonth.getYear();
            meseAnno = meseAnno.substring(0, 1).toUpperCase() + meseAnno.substring(1).toLowerCase();
            meseLabel.setText(meseAnno);
        }
    }
}
