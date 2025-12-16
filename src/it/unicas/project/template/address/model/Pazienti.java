package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Model class for Pazienti.
 */
public class Pazienti {

    private StringProperty nome;
    private StringProperty cognome;
    private StringProperty dataNascita;
    private StringProperty codiceFiscale;
    private StringProperty indirizzo;
    private StringProperty telefono;
    private StringProperty email;
    private StringProperty noteCliniche;

    /**
     * Default constructor.
     */
    public Pazienti() {
        this(null, null, null, null, null, null, null, null);
    }

    public Pazienti(String nome, String cognome, String dataNascita, String codiceFiscale, String indirizzo, String telefono, String email, String noteCliniche) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.dataNascita = new SimpleStringProperty(dataNascita);
        this.codiceFiscale = new SimpleStringProperty(codiceFiscale);
        this.indirizzo = new SimpleStringProperty(indirizzo);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
        this.noteCliniche = new SimpleStringProperty(noteCliniche);
    }

    public String getCodiceFiscale(){
        if (codiceFiscale == null){
            codiceFiscale= new SimpleStringProperty("");
        }
        return codiceFiscale.get();
    }

    public void setCodiceFiscale(String codiceFiscale) {
        if (this.codiceFiscale == null){
            this.codiceFiscale = new SimpleStringProperty();
        }
        this.codiceFiscale.set(codiceFiscale);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getCognome() {
        return cognome.get();
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public StringProperty cognomeProperty() {
        return cognome;
    }

    public String getDataNascita() {
        return dataNascita.get();
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita.set(dataNascita);
    }

    public StringProperty dataNascitaProperty() {
        return dataNascita;
    }

    public String getIndirizzo() {
        return indirizzo.get();
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo.set(indirizzo);
    }

    public StringProperty indirizzoProperty() {
        return indirizzo;
    }
    
    public String getTelefono() {
        return telefono.get();
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getNoteCliniche() {
        return noteCliniche.get();
    }

    public void setNoteCliniche(String noteCliniche) {
        this.noteCliniche.set(noteCliniche);
    }

    public StringProperty noteClinicheProperty() {
        return noteCliniche;
    }


    public String toString(){
        return nome.getValue() + ", " + cognome.getValue() + ", " + dataNascita.getValue() + ", " + codiceFiscale.getValue() + ", " + indirizzo.getValue() + ", " + telefono.getValue() + ", " + email.getValue() + ", " + noteCliniche.getValue();
    }


}
