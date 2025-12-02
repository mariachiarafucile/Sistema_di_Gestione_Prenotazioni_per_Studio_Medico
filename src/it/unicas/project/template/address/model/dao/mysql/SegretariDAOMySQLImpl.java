package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Segretari;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SegretariDAOMySQLImpl implements DAO<Segretari> {

    private SegretariDAOMySQLImpl(){}

    private static DAO dao = null;
    private static Logger logger = null;

    public static DAO getInstance(){
        if (dao == null){
            dao = new SegretariDAOMySQLImpl();
            logger = Logger.getLogger(SegretariDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        SegretariDAOMySQLImpl c = new SegretariDAOMySQLImpl();

        c.insert(new Segretari("Sara","Vettese","sara.vettese@uni.it","vett123"));
        c.insert(new Segretari("Lorenza","Martini","lorenza.martini@uni.it","marti456"));
        c.insert(new Segretari("Elisa","Quagliozzi","elisa.quagliozzi@uni.it","quag987"));

        List<Segretari> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

        Segretari toDelete = new Segretari();
        toDelete.setNome("");
        toDelete.setCognome("");
        toDelete.setEmail("lorenza.martini@uni.it");
        toDelete.setPassword("");

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Segretari> select(Segretari s) throws DAOException {

        if (s == null){
            s = new Segretari("", "", "", ""); // Cerca tutti gli elementi
        }

        ArrayList<Segretari> lista = new ArrayList<>();
        try{

            if (s == null || s.getEmail() == null){
                throw new DAOException("In select: email cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from segretari where email = '" + s.getEmail() + "';";

            try{
                logger.info("SQL: " + sql);
            } catch(NullPointerException nullPointerException){
                logger.severe("SQL: " + sql);
            }
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                lista.add(new Segretari(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }

    @Override
    public void delete(Segretari s) throws DAOException {
        if (s == null || s.getEmail() == null){
            throw new DAOException("In delete: email cannot be null");
        }
        String query = "DELETE FROM segretari WHERE email ='" + s.getEmail() + "';";

        try{
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);

    }


    @Override
    public void insert(Segretari s) throws DAOException {


        verifyObject(s);


        String query = "INSERT INTO segretari (nome, cognome, email, password) VALUES  ('" +
                s.getNome() + "', '" + s.getCognome() + "', '" +
                 "', '" + s.getEmail() + "', '" + s.getPassword() + "');";
        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }
        executeUpdate(query);
    }


    @Override
    public void update(Segretari s) throws DAOException {

        verifyObject(s);

        String query = "UPDATE segretari SET nome = '" + s.getNome() + "', cognome = '" + s.getCognome() + "', password = '" + s.getPassword() + "'";
        query = query + " WHERE email = " + s.getEmail() + ";";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }

    private void verifyObject(Segretari s) throws DAOException {
        if (s == null || s.getEmail() == null
                || s.getNome() == null
                || s.getCognome() == null
                || s.getPassword() == null){
            throw new DAOException("In select: any field can be null");
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

