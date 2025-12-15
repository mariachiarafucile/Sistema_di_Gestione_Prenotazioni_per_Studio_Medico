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

    @Override
    public List<Pazienti> select(Pazienti p) throws DAOException {

        if (p == null){
            p = new Pazienti("", "", "", "", "", "", "", "");
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
