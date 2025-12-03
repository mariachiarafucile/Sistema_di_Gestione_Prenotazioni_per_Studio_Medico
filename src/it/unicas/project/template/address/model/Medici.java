package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Model class for Medici.
 */
public class Medici {

    private StringProperty nome;
    private StringProperty cognome;
    private StringProperty specializzazione;
    private StringProperty telefono;
    private StringProperty email;
    private StringProperty password;
    //wrapper


    /**
     * Default constructor.
     */
    public Medici() {
        this(null, null, null, null, null, null);
    }

    public Medici(String nome, String cognome, String specializzazione, String telefono, String email, String password) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.specializzazione = new SimpleStringProperty(specializzazione);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
    }

    public String getEmail(){
        if (email == null){
            email= new SimpleStringProperty("");
        }
        return email.get();
    }

    public void setEmail(String email) {
        if (this.email == null){
            this.email = new SimpleStringProperty();
        }
        this.email.set(email);
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

    public String getSpecializzazione() {
        return specializzazione.get();
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione.set(specializzazione);
    }

    public StringProperty specializzazioneProperty() {
        return specializzazione;
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

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String toString(){
        return nome.getValue() + ", " + cognome.getValue() + ", " + specializzazione.getValue() + ", " + telefono.getValue() + ", " + email.getValue() + ", " + password.getValue();
    }


}

