package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Medici;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MediciDAOMySQLImpl implements DAO<Medici> {

    private MediciDAOMySQLImpl(){}

    private static DAO dao = null;
    private static Logger logger = null;

    public static DAO getInstance(){
        if (dao == null){
            dao = new MediciDAOMySQLImpl();
            logger = Logger.getLogger(MediciDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        MediciDAOMySQLImpl c = new MediciDAOMySQLImpl();

        c.insert(new Medici("Chiara","Neri","Neurologia","3275678901","chiara.neri@uni.it","neuro321"));
        c.insert(new Medici("Lorenzo","Marini","Ortopedia","3296789012","lorenzo.marini@uni.it","orto654"));
        c.insert(new Medici("Elena","Greco","Dermatologia","3317890123","elena.greco@uni.it","derma987"));
        c.insert(new Medici("Matteo","Bruno","Pediatria","3338901234","matteo.bruno@uni.it","pedi321"));
        c.insert(new Medici("Federica","Galli","Cardiologia","3359012345","federica.galli@uni.it","cardio654"));
        c.insert(new Medici("Davide","Fontana","Angiologo","3370123456","davide.fontana@uni.it","angio987"));

        List<Medici> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }


        Medici toDelete = new Medici();
        toDelete.setNome("");
        toDelete.setCognome("");
        toDelete.setSpecializzazione("");
        toDelete.setTelefono("");
        toDelete.setEmail("chiara.neri@uni.it");
        toDelete.setPassword("");

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Medici> select(Medici m) throws DAOException {

        if (m == null){
            m = new Medici("", "", "", "", "", ""); // Cerca tutti gli elementi
        }

        ArrayList<Medici> lista = new ArrayList<>();
        try{

            if (m == null || m.getEmail() == null){
                throw new DAOException("In select: email cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from medici where email = '" + m.getEmail() + "';";

            try{
                logger.info("SQL: " + sql);
            } catch(NullPointerException nullPointerException){
                logger.severe("SQL: " + sql);
            }
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                lista.add(new Medici(rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("specializzazione"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }

    //Query per la ricerca delle credenziali del medico
    public boolean existsMedico(String email, String password) throws DAOException {
        String sql = "SELECT COUNT(*) AS cnt FROM medici WHERE email='" + email +
                "' AND password='" + password + "';";

        try {
            Statement st = DAOMySQLSettings.getStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("In existsMedico(): " + e.getMessage());
        }
        return false;
    }



    @Override
    public void delete(Medici m) throws DAOException {
        if (m == null || m.getEmail() == null){
            throw new DAOException("In delete: email cannot be null");
        }
        String query = "DELETE FROM medici WHERE email ='" + m.getEmail() + "';";

        try{
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);

    }


    @Override
    public void insert(Medici m) throws DAOException {


        verifyObject(m);


        String query = "INSERT INTO medici (nome, cognome, specializzazione, telefono, email, password) VALUES  ('" +
                m.getNome() + "', '" + m.getCognome() + "', '" +
                m.getSpecializzazione() + "', '" + m.getTelefono() + "', '" + m.getEmail() + "', '" + m.getPassword() + "');";
        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }
        executeUpdate(query);
    }


    @Override
    public void update(Medici m) throws DAOException {

        verifyObject(m);

        String query = "UPDATE medici SET nome = '" + m.getNome() + "', cognome = '" + m.getCognome() + "',  specializzazione = '" + m.getSpecializzazione() +  "', telefono = '" + m.getTelefono() + "', password = '" + m.getPassword() + "'";
        query = query + " WHERE email = " + m.getEmail() + ";";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }

    private void verifyObject(Medici m) throws DAOException {
        if (m == null || m.getEmail() == null
                || m.getNome() == null
                || m.getCognome() == null
                || m.getSpecializzazione() == null
                || m.getTelefono() == null
                || m.getPassword() == null){
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
