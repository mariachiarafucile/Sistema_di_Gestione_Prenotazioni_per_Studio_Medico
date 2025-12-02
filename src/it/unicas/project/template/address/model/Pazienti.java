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
    //wrapper

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

    /*/**
     * Constructor with some initial data.
     *
     * @param nome
     * @param cognome
     */
   /*public Amici(String nome, String cognome) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        // Some initial dummy data, just for convenient testing.
        this.telefono = new SimpleStringProperty("telefono");
        this.email = new SimpleStringProperty("email@email.com");
        this.compleanno = new SimpleStringProperty("24-10-2017");
        this.idAmici = null;
    }*/


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


    /*public static void main(String[] args) {
        Amici collega = new Amici();
        collega.setNome("Ciao");
        MyChangeListener myChangeListener = new MyChangeListener();
        collega.nomeProperty().addListener(myChangeListener);
        collega.setNome("Mario");


        collega.compleannoProperty().addListener(myChangeListener);

        collega.compleannoProperty().addListener(
                (ChangeListener) (o, oldVal, newVal) -> System.out.println("Compleanno property has changed!"));

        collega.compleannoProperty().addListener(
                (o, old, newVal)-> System.out.println("Compleanno property has changed! (Lambda implementation)")
        );


        collega.setCompleanno("30-10-1971");



        // Use Java Collections to create the List.
        List<Amici> list = new ArrayList<>();

        // Now add observability by wrapping it with ObservableList.
        ObservableList<Amici> observableList = FXCollections.observableList(list);
        observableList.addListener(
          (ListChangeListener) change -> System.out.println("Detected a change! ")
        );

        Amici c1 = new Amici();
        Amici c2 = new Amici();

        c1.nomeProperty().addListener(
                (o, old, newValue)->System.out.println("Ciao")
        );

        c1.setNome("Pippo");

        // Changes to the observableList WILL be reported.
        // This line will print out "Detected a change!"
        observableList.add(c1);

        // Changes to the underlying list will NOT be reported
        // Nothing will be printed as a result of the next line.
        observableList.add(c2);


        observableList.get(0).setNome("Nuovo valore");

        System.out.println("Size: "+observableList.size());

    }
*/

}
