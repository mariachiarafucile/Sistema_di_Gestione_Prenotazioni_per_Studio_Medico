package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Modello che rappresenta un segretario.
 */
public class Segretari {

    private StringProperty nome;
    private StringProperty cognome;
    private StringProperty email;
    private StringProperty password;

    /**
     * Costruttore di default.
     */
    public Segretari() {
        this(null, null, null, null);
    }

    /**
     * Costruttore con parametri.
     *
     * @param nome
     * @param cognome
     * @param email
     * @param password
     */
    public Segretari(String nome, String cognome, String email, String password) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
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
        return nome.getValue() + ", " + cognome.getValue() + ", " + email.getValue() + ", " + password.getValue();
    }


}

