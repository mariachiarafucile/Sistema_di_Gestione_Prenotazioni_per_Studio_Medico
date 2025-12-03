package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Model class for Visite.
 */
public class Visite {

    private IntegerProperty idVisita;
    private StringProperty dataOra;
    private StringProperty prescrizione;
    private StringProperty pazienteCodiceFiscale;
    private StringProperty segretarioEmail;
    //wrapper

    /**
     * Default constructor.
     */
    public Visite() {
        this(null, null, null, null,null);
    }

    public Visite(Integer idVisita, String dataOra, String prescrizione, String pazienteCodiceFiscale, String segretarioEmail) {
        this.idVisita = new SimpleIntegerProperty(idVisita);
        this.dataOra = new SimpleStringProperty(dataOra);
        this.prescrizione = new SimpleStringProperty(prescrizione);
        this.pazienteCodiceFiscale = new SimpleStringProperty(pazienteCodiceFiscale);
        this.segretarioEmail = new SimpleStringProperty(segretarioEmail);
    }

    public Integer getIdVisita(){
        if (idVisita == null){
            idVisita= new SimpleIntegerProperty(-1);
        }
        return idVisita.get();
    }

    public void setIdVisita(Integer idVisita) {
        if (this.idVisita == null){
            this.idVisita = new SimpleIntegerProperty();
        }
        this.idVisita.set(idVisita);
    }

    public String getDataOra() {
        return dataOra.get();
    }

    public void setDataOra(String dataOra) {
        this.dataOra.set(dataOra);
    }

    public StringProperty dataOraProperty() {
        return dataOra;
    }

    public String getPrescrizione() {
        return prescrizione.get();
    }

    public void setPrescrizione(String prescrizione) {
        this.prescrizione.set(prescrizione);
    }

    public StringProperty prescrizioneProperty() {
        return prescrizione;
    }

    public String getPazienteCodiceFiscale() {
        return pazienteCodiceFiscale.get();
    }

    public void setPazienteCodiceFiscale(String pazienteCodiceFiscale) {
        this.pazienteCodiceFiscale.set(pazienteCodiceFiscale);
    }

    public StringProperty pazienteCodiceFiscaleProperty() {
        return pazienteCodiceFiscale;
    }

    public String getSegretarioEmail() {
        return segretarioEmail.get();
    }

    public void setSegretarioEmail(String segretarioEmail) {
        this.segretarioEmail.set(segretarioEmail);
    }

    public StringProperty segretarioEmailProperty() {
        return segretarioEmail;
    }

    public String toString(){
        return idVisita.getValue() + ", " + dataOra.getValue() + ", " + prescrizione.getValue() + ", " + pazienteCodiceFiscale.getValue() + ", " + segretarioEmail.getValue();
    }


}

