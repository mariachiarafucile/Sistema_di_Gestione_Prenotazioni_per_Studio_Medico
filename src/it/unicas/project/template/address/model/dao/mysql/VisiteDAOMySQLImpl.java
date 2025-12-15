package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Visite;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VisiteDAOMySQLImpl implements DAO<Visite> {

    private VisiteDAOMySQLImpl(){}

    private static VisiteDAOMySQLImpl instance = null;
    private static Logger logger = null;

    public static VisiteDAOMySQLImpl getInstance(){
        if (instance == null) {
            instance = new VisiteDAOMySQLImpl();
            logger = Logger.getLogger(VisiteDAOMySQLImpl.class.getName());
        }
        return instance;
    }

    @Override
    public List<Visite> select(Visite v) throws DAOException {

        if (v == null){
            v = new Visite(null, "", "", "","");
        }

        ArrayList<Visite> lista = new ArrayList<>();
        try{

            if (v == null || v.getIdVisita() == null){
                throw new DAOException("In select: idVisita cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from visite where idVisita = '" + v.getIdVisita() + "';";

            try{
                logger.info("SQL: " + sql);
            } catch(NullPointerException nullPointerException){
                logger.severe("SQL: " + sql);
            }
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                lista.add(new Visite(rs.getInt("idVisita"),
                        rs.getString("dataOra"),
                        rs.getString("prescrizione"),
                        rs.getString("pazienteCodiceFiscale"),
                        rs.getString("segretarioEmail")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }

    @Override
    public void delete(Visite v) throws DAOException {
        if (v == null || v.getIdVisita() == null){
            throw new DAOException("In delete: idVisita cannot be null");
        }
        String query = "DELETE FROM visite WHERE idVisita = '" + v.getIdVisita() + "';";

        try{
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);

    }

    @Override
    public void insert(Visite v) throws DAOException {

        verifyObject(v);

        String query = "INSERT INTO visite (dataOra, prescrizione, pazienteCodiceFiscale, segretarioEmail) VALUES ('"
                + v.getDataOra() + "', '"
                + v.getPrescrizione() + "', '"
                + v.getPazienteCodiceFiscale() + "', '"
                + v.getSegretarioEmail() + "');";

        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }
        try {
            Statement st = DAOMySQLSettings.getStatement();
            st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);  // Esegue l'inserimento e permette di recuperare l'id
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                v.setIdVisita(rs.getInt(1));  // Aggiorna l’oggetto con l’id generato dal DB
            }
            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("Errore insert(): " + e.getMessage());
        }

    }

    @Override
    public void update(Visite v) throws DAOException {

        verifyObject(v);

        String query = "UPDATE visite SET dataOra = '" + v.getDataOra() + "', prescrizione = '" + v.getPrescrizione() + "', pazienteCodiceFiscale = '" + v.getPazienteCodiceFiscale() + "', segretarioEmail = '" + v.getSegretarioEmail() + "'";
        query = query + " WHERE idVisita = " + v.getIdVisita() + ";";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }

    private void verifyObject(Visite v) throws DAOException {
        if (v == null || v.getIdVisita() == null
                || v.getDataOra() == null
                || v.getPazienteCodiceFiscale() == null
                || v.getSegretarioEmail() == null) {
            throw new DAOException("In select: any field apart from 'prescrizione' can be null");
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

    public List<Visite> selectByCF(String codiceFiscale) throws DAOException {
        ArrayList<Visite> lista = new ArrayList<>();

        try {
            Statement st = DAOMySQLSettings.getStatement();

            String sql = "SELECT * FROM visite WHERE pazienteCodiceFiscale = '" + codiceFiscale + "';";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Integer id = rs.getObject("idVisita") != null ? (Integer) rs.getObject("idVisita") : 0;
                lista.add(new Visite(
                        id,
                        rs.getString("dataOra"),
                        rs.getString("prescrizione"),
                        rs.getString("pazienteCodiceFiscale"),
                        rs.getString("segretarioEmail")
                ));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (Exception e) {
            throw new DAOException("Errore selectByCF(): " + e.getMessage());
        }

        return lista;
    }


}

