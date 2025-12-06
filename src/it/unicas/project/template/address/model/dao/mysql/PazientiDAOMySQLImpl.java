package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Pazienti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PazientiDAOMySQLImpl implements DAO<Pazienti> {

    private PazientiDAOMySQLImpl(){}

    private static DAO dao = null;
    private static Logger logger = null;

    public static DAO getInstance(){
        if (dao == null){
            dao = new PazientiDAOMySQLImpl();
            logger = Logger.getLogger(PazientiDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        PazientiDAOMySQLImpl c = new PazientiDAOMySQLImpl();

        c.insert(new Pazienti("Maria","Bianchi","1990-03-12","BNCMRA90C52F205K","Via delle Magnolie 42,Milano (MI)","3289456721","mbianchi@uni.it",null));
        c.insert(new Pazienti("Martina","Conti","1995-03-14","CNTMRT95C54H501X","Via del Sole 47,Roma(RM)","3471128940","mconti@uni.it",null));
        c.insert(new Pazienti("Paolo","Rinaldi","1992-05-27","RNLPLO92C14D612X","Via dei Tigli 89,Firenze(FI)","3335802194","prinaldi@uni.it","Diabete tipo 2 controllato"));
        c.insert(new Pazienti("Alessandro","Ricci","1998-11-09","RCCLSN88S09H501T","Via Verdi 6,Padova (PD)","3927641185","aricci@uni.it","Asma lieve persistente"));
        c.insert(new Pazienti("Giulia","Esposito","1987-07-22","SPTGLI87L62F839K","Via Roma 15,Napoli(NA)","3456789123","gesposito@uni.it","Allergia nota alle penicilline,manifestata con eruzione cutanea diffusa e prurito.Evitare tutti gli antibiotici della stessa classe."));
        c.insert(new Pazienti("Luca","Ferrari","1993-09-30","FRRLCA93P30H501Y","Via Milano 23,Torino(TO)","3294567812","lferrari@uni.it","Storia di lieve anemia da carenza di ferro,in trattamento con integratori orali.Controlli ematici in miglioramento."));
        c.insert(new Pazienti("Francesca","Russo","1991-12-05","RSSFNC91T45F205Z","Via Venezia 78,Bologna(BO)","3389123456","frusso@uni.it",null));
        c.insert(new Pazienti("Sara","Gallo","1994-04-18","GLLSRA94D58H501W","Via Firenze 34,Genova(GE)","3478912345","sgallo@uni.it","Lieve ipertensione controllata con monitoraggio domiciliare della pressione arteriosa."));


       List<Pazienti> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }


        Pazienti toDelete = new Pazienti();
        toDelete.setNome("");
        toDelete.setCognome("");
        toDelete.setDataNascita("");
        toDelete.setCodiceFiscale("GLLSRA94D58H501W");
        toDelete.setIndirizzo("");
        toDelete.setTelefono("");
        toDelete.setEmail("");
        toDelete.setNoteCliniche("");

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Pazienti> select(Pazienti p) throws DAOException {

        if (p == null){
            p = new Pazienti("", "", "", "", "", "", "", ""); // Cerca tutti gli elementi
        }

        ArrayList<Pazienti> lista = new ArrayList<>();
        try{

            if (p == null || p.getCodiceFiscale() == null){
                throw new DAOException("In select: codiceFiscale cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from pazienti where codiceFiscale = '" + p.getCodiceFiscale() + "';";

            try{
                logger.info("SQL: " + sql);
            } catch(NullPointerException nullPointerException){
                logger.severe("SQL: " + sql);
            }
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                lista.add(new Pazienti(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("dataNascita"),
                        rs.getString("codiceFiscale"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("noteCliniche")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }

    @Override
    public void delete(Pazienti p) throws DAOException {
        if (p == null || p.getCodiceFiscale() == null){
            throw new DAOException("In delete: codiceFiscale cannot be null");
        }
        String query = "DELETE FROM pazienti WHERE codiceFiscale='" + p.getCodiceFiscale() + "';";

        try{
          logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
          System.out.println("SQL: " + query);
        }

        executeUpdate(query);

    }


    @Override
    public void insert(Pazienti p) throws DAOException {


        verifyObject(p);


        String query = "INSERT INTO pazienti (nome, cognome, dataNascita, codiceFiscale, indirizzo, telefono, email, noteCliniche) VALUES  ('" +
                p.getNome() + "', '" + p.getCognome() + "', '" +
                p.getDataNascita() + "', '" + p.getCodiceFiscale() + "', '" +
                p.getIndirizzo() + "', '" + p.getTelefono() + "', '" + p.getEmail() + "', '" + p.getNoteCliniche() + "');";
        try {
          logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
          System.out.println("SQL: " + query);
        }
        executeUpdate(query);
    }


    @Override
    public void update(Pazienti p) throws DAOException {

        verifyObject(p);

        String query = "UPDATE pazienti SET nome = '" + p.getNome() + "', cognome = '" + p.getCognome() + "',  dataNascita = '" + p.getDataNascita() + "', indirizzo = '" + p.getIndirizzo() + "', telefono = '" + p.getTelefono() + "'" + ", email = '" + p.getEmail() + "', noteCliniche = '" + p.getNoteCliniche() + "'";
        query = query + " WHERE codiceFiscale = '" + p.getCodiceFiscale() + "';";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }

    private void verifyObject(Pazienti p) throws DAOException {
      if (p == null || p.getCodiceFiscale() == null
        || p.getNome() == null
        || p.getCognome() == null
        || p.getDataNascita() == null
        || p.getIndirizzo() == null
        || p.getTelefono() == null
        || p.getEmail() == null){
        throw new DAOException("In select: any field apart from 'noteCliniche' can be null");
      }
    }

    private void executeUpdate(String query) throws DAOException{
      try {
        Statement st = DAOMySQLSettings.getStatement();
        int n = st.executeUpdate(query);

        DAOMySQLSettings.closeStatement(st);

      } catch (SQLException e) {
        throw new DAOException("In insert(): " + e.getMessage());
      }
    }


}
