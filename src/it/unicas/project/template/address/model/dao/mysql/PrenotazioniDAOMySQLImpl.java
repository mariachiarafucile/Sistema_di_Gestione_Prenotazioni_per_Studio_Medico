package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Prenotazioni;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PrenotazioniDAOMySQLImpl implements DAO<Prenotazioni> {

    private static DAO dao = null;
    private static Logger logger = null;

    private PrenotazioniDAOMySQLImpl(){}

    public static DAO getInstance() {
        if (dao == null) {
            dao = new PrenotazioniDAOMySQLImpl();
            logger = Logger.getLogger(PrenotazioniDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    @Override
    public List<Prenotazioni> select(Prenotazioni q) throws DAOException {

        if (q == null) {
            q = new Prenotazioni(null, "", "", null);
        }

        ArrayList<Prenotazioni> lista = new ArrayList<>();

        try {

            if (q == null || q.getPazienteCodiceFiscale() == null) {
                throw new DAOException("In select: idPrenotazioni cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "SELECT * FROM prenotazioni WHERE paziente_codiceFiscale = '" + q.getPazienteCodiceFiscale() + "';";

            try {
                logger.info("SQL: " + sql);
            } catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + sql);
            }

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                lista.add(new Prenotazioni(
                        rs.getInt("idPrenotazioni"),
                        rs.getString("paziente_codiceFiscale"),
                        rs.getString("medico_email"),
                        rs.getInt("fasciaOrariaId")
                ));
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq) {
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }

    @Override
    public void delete(Prenotazioni q) throws DAOException {
        if (q == null || q.getIdPrenotazioni() == null) {
            throw new DAOException("In delete: idPrenotazioni cannot be null");
        }

        String sql = "DELETE FROM prenotazioni WHERE idPrenotazioni = '" + q.getIdPrenotazioni() + "';";

        try {
            logger.info("SQL: " + sql);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + sql);
        }

        executeUpdate(sql);
    }

    @Override
    public void insert(Prenotazioni q) throws DAOException {

        verifyObject(q);

        String sql =
                "INSERT INTO prenotazioni (idPrenotazioni, paziente_codiceFiscale, medico_email, fasciaOrariaId) VALUES ('" +
                        q.getIdPrenotazioni() + "', '" +
                        q.getPazienteCodiceFiscale() + "', '" +
                        q.getMedicoEmail() + "', '" +
                        q.getFasciaOrariaId() + "');";

        try {
            logger.info("SQL: " + sql);
        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + sql);
        }

        executeUpdate(sql);
    }

    @Override
    public void update(Prenotazioni q) throws DAOException {

        verifyObject(q);

        String sql = "UPDATE prenotazioni SET paziente_codiceFiscale = '" + q.getPazienteCodiceFiscale() +
                "', medico_email = '" + q.getMedicoEmail() +
                "', fasciaOrariaId = '" + q.getFasciaOrariaId() +
                "' WHERE idPrenotazioni = " + q.getIdPrenotazioni() + ";";

        logger.info("SQL: " + sql);

        executeUpdate(sql);
    }

    private void verifyObject(Prenotazioni q) throws DAOException {
        if (q == null ||
                q.getIdPrenotazioni() == null ||
                q.getPazienteCodiceFiscale() == null ||
                q.getMedicoEmail() == null ||
                q.getFasciaOrariaId() == null) {
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


