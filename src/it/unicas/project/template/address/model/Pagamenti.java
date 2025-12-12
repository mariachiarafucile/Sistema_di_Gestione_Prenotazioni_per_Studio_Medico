package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Model class for Pagamenti.
 */
public class Pagamenti {

    private IntegerProperty idPagamento;
    private StringProperty stato;
    private DoubleProperty importo;
    private StringProperty emailSegretario;
    private IntegerProperty visitaIdVisita;
    //wrapper

    /**
     * Default constructor.
     */
    public Pagamenti() {
        this(null, null, null, null, null);
    }

    public Pagamenti(Integer idPagamento, String stato, Double importo, String emailSegretario, Integer visitaIdVisita) {
        this.idPagamento = new SimpleIntegerProperty(idPagamento);
        this.stato = new SimpleStringProperty(stato);
        this.importo = new SimpleDoubleProperty(importo);
        this.emailSegretario = new SimpleStringProperty(emailSegretario);
        this.visitaIdVisita = new SimpleIntegerProperty(visitaIdVisita);
    }

    public Integer getIdPagamento(){
        if (idPagamento == null){
            idPagamento= new SimpleIntegerProperty(-1);
        }
        return idPagamento.get();
    }

    public void setIdPagamento(Integer idPagamento) {
        if (this.idPagamento == null){
            this.idPagamento = new SimpleIntegerProperty();
        }
        this.idPagamento.set(idPagamento);
    }

    public String getStato() {
        return stato.get();
    }

    public void setStato(String stato) {
        this.stato.set(stato);
    }

    public StringProperty statoProperty() {
        return stato;
    }

    public Double getImporto() {
        return importo.get();
    }

    public void setImporto(Double importo) {
        this.importo.set(importo);
    }

    public DoubleProperty importoProperty() {
        return importo;
    }

    public Integer getVisitaIdVisita() {
        return visitaIdVisita.get();
    }

    public void setVisitaIdVisita(Integer visitaIdVisita) {
        this.visitaIdVisita.set(visitaIdVisita);
    }

    public IntegerProperty visitaIdVisitaProperty() {
        return visitaIdVisita;
    }

    public String getEmailSegretario() {
        return emailSegretario.get();
    }

    public void setEmailSegretario(String emailSegretario) {
        this.emailSegretario.set(emailSegretario);
    }

    public StringProperty emailSegretarioProperty() {
        return emailSegretario;
    }


    public String toString(){
        return idPagamento.getValue() + ", " + stato.getValue() + ", " + importo.getValue() + ", " + emailSegretario.getValue() + ", " + visitaIdVisita.getValue();
    }


}

