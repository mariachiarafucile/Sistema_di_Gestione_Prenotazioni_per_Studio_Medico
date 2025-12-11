package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.FasceMedici;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FasceMediciDAOMySQLImpl implements DAO<FasceMedici> {

    private static DAO dao = null;
    private static Logger logger = null;

    private FasceMediciDAOMySQLImpl() {}

    public static DAO getInstance() {
        if (dao == null) {
            dao = new FasceMediciDAOMySQLImpl();
            logger = Logger.getLogger(FasceMediciDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    @Override
    public List<FasceMedici> select(FasceMedici fm) throws DAOException {
        if (fm == null) {
            fm = new FasceMedici(null, "");
        }

        ArrayList<FasceMedici> lista = new ArrayList<>();
        try {

            if(fm == null || fm.getIdFasciaOraria() == null || fm.getMedicoEmail()  == null) {
                throw new DAOException("In select: idFasciaOraria and medicoEmail cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "SELECT * FROM fasceOrarie_has_medici WHERE medicoEmail = '" + fm.getMedicoEmail() + "'";

            try {
                logger.info("SQL: " + sql);
            } catch (NullPointerException npe) {
                System.out.println("SQL: " + sql);
            }

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                lista.add(new FasceMedici(
                        rs.getInt("fasciaOraria_id"),
                        rs.getString("medicoEmail")
                ));
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq) {
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }

    @Override
    public void delete(FasceMedici fm) throws DAOException {
        if (fm == null || fm.getIdFasciaOraria() == null || fm.getMedicoEmail() == null) {
            throw new DAOException("In delete: idFsciaOraria and medicoEmail cannot be null");
        }

        String sql = "DELETE FROM fasceOrarie_has_medici WHERE " +
                "medicoEmail = '" + fm.getMedicoEmail() + "';";

        try {
            logger.info("SQL: " + sql);
        } catch (NullPointerException npe) {
            System.out.println("SQL: " + sql);
        }

        executeUpdate(sql);
    }

    @Override
    public void insert(FasceMedici fm) throws DAOException {

        verifyObject(fm);

        String sql = "INSERT INTO fasceOrarie_has_medici (fasciaOraria_id, medicoEmail) VALUES ('" +
                fm.getIdFasciaOraria() + "', '" +
                fm.getMedicoEmail() + "');";

        try {
            logger.info("SQL: " + sql);
        } catch (NullPointerException npe) {
            System.out.println("SQL: " + sql);
        }

        executeUpdate(sql);
    }

    @Override
    public void update(FasceMedici fm) throws DAOException {
        // Non ha molto senso aggiornare la PK in una tabella ponte, quindi lo lasciamo vuoto o lanciamo eccezione
        throw new DAOException("Update non supportato per FasceMedici");
    }

    private void verifyObject(FasceMedici fm) throws DAOException {
        if (fm == null || fm.getIdFasciaOraria() == null || fm.getMedicoEmail() == null) {
            throw new DAOException("In verifyObject: fields cannot be null");
        }
    }

    private void executeUpdate(String query) throws DAOException {
        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);
            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("In executeUpdate(): " + e.getMessage());
        }
    }
}