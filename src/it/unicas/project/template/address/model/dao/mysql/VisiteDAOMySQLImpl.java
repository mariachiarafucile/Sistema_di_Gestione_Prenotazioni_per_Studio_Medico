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

    private static DAO dao = null;
    private static Logger logger = null;

    public static DAO getInstance(){
        if (dao == null){
            dao = new VisiteDAOMySQLImpl();
            logger = Logger.getLogger(VisiteDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        VisiteDAOMySQLImpl c = new VisiteDAOMySQLImpl();

        c.insert(new Visite(1,"2024-12-01 10:00:00","Farmaco: Relaxinol 20 mg","RNLPLO92C14D612X","sara.vettese@uni.it"));
        c.insert(new Visite(2,"2025-05-12 11:30:00","Farmaco: Ibuprofen 400mg","CNTMRT95C54H501X","lorenza.martini@uni.it"));
        c.insert(new Visite(3,"2024-01-25 09:30:00","Farmaco: Pevaryl crema 30g 1% - classe c","BNCMRA90C52F205K","lorenza.martini@uni.it"));

        List<Visite> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

        Visite toDelete = new Visite();
        toDelete.setDataOra("");
        toDelete.setPrescrizione("");
        toDelete.setIdVisita(1);
        toDelete.setPazienteCodiceFiscale("");
        toDelete.setSegretarioEmail("");

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Visite> select(Visite v) throws DAOException {

        if (v == null){
            v = new Visite(null, "", "", "",""); // Cerca tutti gli elementi
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


        String query = "INSERT INTO visite (idVisita, dataOra, prescrizione, pazienteCodiceFiscale, segretarioEmail) VALUES  ('" +
                v.getIdVisita() + "', '" + v.getDataOra() + "', '" +
                "', '" + v.getPrescrizione() + "', '" + v.getPazienteCodiceFiscale() + "', '" + v.getSegretarioEmail() + "');";
        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }
        executeUpdate(query);
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


}

