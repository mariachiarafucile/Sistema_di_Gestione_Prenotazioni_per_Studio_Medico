package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Modello che rappresenta una fascia oraria.
 */
public class FasceOrarie {

    private IntegerProperty idFasciaOraria;
    private StringProperty data;
    private StringProperty oraInizio;
    private StringProperty oraFine;

    /**
     * Costruttore di default.
     */
    public FasceOrarie() {
        this(null, "", "", "");
    }

    /**
     * Costruttore con parametri.
     *
     * @param idFasciaOraria identificativo della fascia oraria
     * @param data data della fascia oraria
     * @param oraInizio orario di inizio
     * @param oraFine orario di fine
     */
    public FasceOrarie(Integer idFasciaOraria, String data, String oraInizio, String oraFine) {
        if (idFasciaOraria != null) {
            this.idFasciaOraria = new SimpleIntegerProperty(idFasciaOraria);
        } else {
            this.idFasciaOraria = null;
        }
        this.data = new SimpleStringProperty(data);
        this.oraInizio = new SimpleStringProperty(oraInizio);
        this.oraFine = new SimpleStringProperty(oraFine);
    }

    public Integer getIdFasciaOraria() {
        if (idFasciaOraria == null) {
            idFasciaOraria = new SimpleIntegerProperty(-1);
        }
        return idFasciaOraria.get();
    }

    public void setIdFasciaOraria(Integer idFasciaOraria) {
        if (this.idFasciaOraria == null) {
            this.idFasciaOraria = new SimpleIntegerProperty();
        }
        this.idFasciaOraria.set(idFasciaOraria);
    }

    public IntegerProperty idFasciaOrariaProperty() {
        return idFasciaOraria;
    }

    public String getData() {
        return data.get();
    }

    public void setData(String data) {
        this.data.set(data);
    }

    public StringProperty dataProperty() {
        return data;
    }

    public String getOraInizio() {
        return oraInizio.get();
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio.set(oraInizio);
    }

    public StringProperty oraInizioProperty() {
        return oraInizio;
    }

    public String getOraFine() {
        return oraFine.get();
    }

    public void setOraFine(String oraFine) {
        this.oraFine.set(oraFine);
    }

    public StringProperty oraFineProperty() {
        return oraFine;
    }

    @Override
    public String toString() {
        return idFasciaOraria.getValue() + ", " +
                data.getValue() + ", " +
                oraInizio.getValue() + ", " +
                oraFine.getValue();
    }

}

