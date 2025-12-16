package it.unicas.project.template.address.model;

import javafx.beans.property.*;

public class Prenotazioni {

    private IntegerProperty idPrenotazioni;
    private StringProperty pazienteCodiceFiscale;
    private StringProperty medicoEmail;
    private IntegerProperty fasciaOrariaId;

    public Prenotazioni() {
        this(null, "", "", null);
    }

    public Prenotazioni(Integer idPrenotazioni, String pazienteCodiceFiscale, String medicoEmail, Integer fasciaOrariaId) {
        if (idPrenotazioni != null) {
            this.idPrenotazioni = new SimpleIntegerProperty(idPrenotazioni);
        } else {
            this.idPrenotazioni = null;
        }
        this.pazienteCodiceFiscale = new SimpleStringProperty(pazienteCodiceFiscale);
        this.medicoEmail = new SimpleStringProperty(medicoEmail);
        if (fasciaOrariaId != null) {
            this.fasciaOrariaId = new SimpleIntegerProperty(fasciaOrariaId);
        } else {
            this.fasciaOrariaId = null;
        }
    }

    public Integer getIdPrenotazioni() {
        if (idPrenotazioni == null) {
            idPrenotazioni = new SimpleIntegerProperty(-1);
        }
        return idPrenotazioni.get();
    }

    public void setIdPrenotazioni(Integer idPrenotazioni) {
        if (this.idPrenotazioni == null) {
            this.idPrenotazioni = new SimpleIntegerProperty();
        }
        this.idPrenotazioni.set(idPrenotazioni);
    }

    public IntegerProperty idPrenotazioniProperty() {
        return idPrenotazioni;
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

    public String getMedicoEmail() {
        return medicoEmail.get();
    }

    public void setMedicoEmail(String medicoEmail) {
        this.medicoEmail.set(medicoEmail);
    }

    public StringProperty medicoEmailProperty() {
        return medicoEmail;
    }

    public Integer getFasciaOrariaId() {
        return fasciaOrariaId.get();
    }

    public void setFasciaOrariaId(Integer fasciaOrariaId) {
        this.fasciaOrariaId.set(fasciaOrariaId);
    }

    public IntegerProperty fasciaOrariaIdProperty() {
        return fasciaOrariaId;
    }

    @Override
    public String toString() {
        return idPrenotazioni.getValue() + ", " +
                pazienteCodiceFiscale.getValue() + ", " +
                medicoEmail.getValue() + ", " +
                fasciaOrariaId.getValue();
    }
}
