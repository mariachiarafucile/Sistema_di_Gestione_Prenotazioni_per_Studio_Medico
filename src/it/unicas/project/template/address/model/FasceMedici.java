package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Modello che rappresenta l'associazione tra una fascia oraria e un medico.
 */
public class FasceMedici {

    private IntegerProperty idFasciaOraria;
    private StringProperty medicoEmail;

    /**
     * Costruttore di default.
     */
    public FasceMedici() { this(null, "");}

    /**
     * Costruttore con parametri.
     *
     * @param idFasciaOraria
     * @param medicoEmail
     */
    public FasceMedici(Integer idFasciaOraria, String medicoEmail) {
        if (idFasciaOraria != null) {
            this.idFasciaOraria = new SimpleIntegerProperty(idFasciaOraria);
        } else {
            this.idFasciaOraria = null;
        }
        this.medicoEmail = new SimpleStringProperty(medicoEmail);
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

    public String getMedicoEmail() {
        return medicoEmail.get();
    }

    public void setMedicoEmail(String medicoEmail) {
        this.medicoEmail.set(medicoEmail);
    }

    public StringProperty medicoEmailProperty() {
        return medicoEmail;
    }

    @Override
    public String toString() {
        return idFasciaOraria.getValue() + ", " +
                medicoEmail.getValue();
    }
}