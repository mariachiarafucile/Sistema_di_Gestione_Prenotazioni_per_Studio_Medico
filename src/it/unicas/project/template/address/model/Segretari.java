package it.unicas.project.template.address.model;

import javafx.beans.property.*;

/**
 * Model class for Segretari.
 */
public class Segretari {

    private StringProperty nome;
    private StringProperty cognome;
    private StringProperty email;
    private StringProperty password;
    //wrapper

    /**
     * Default constructor.
     */
    public Segretari() {
        this(null, null, null, null);
    }

    public Segretari(String nome, String cognome, String email, String password) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
    }

   /* /**
     * Constructor with some initial data.
     *
     * @param nome
     * @param cognome
     */ /*

    public Amici(String nome, String cognome) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        // Some initial dummy data, just for convenient testing.
        this.telefono = new SimpleStringProperty("telefono");
        this.email = new SimpleStringProperty("email@email.com");
        this.compleanno = new SimpleStringProperty("24-10-2017");
        this.idAmici = null;
    }*/


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

