package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.FasceOrarie;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe DAO per la gestione delle entità FasceOrarie.
 */

public class FasceOrarieDAOMySQLImpl implements DAO<FasceOrarie> {

    private static DAO dao = null;
    private static Logger logger = null;

    /**
     * Costruttore privato della classe.
     */

    private FasceOrarieDAOMySQLImpl() {
    }

    /**
     * Restituisce un'istanza del DAO.
     * Se l'istanza non esiste, viene creata e inizializzato il logger.
     */

    public static DAO getInstance() {

        if (dao == null) {
            dao = new FasceOrarieDAOMySQLImpl();
            logger = Logger.getLogger(FasceOrarieDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    /**
     * Seleziona una fascia oraria dal database in base all'ID della fascia oraria.
     *
     * @param f
     */

    @Override
    public List<FasceOrarie> select(FasceOrarie f) throws DAOException {

        if (f == null) {
            f = new FasceOrarie(null, "", "", "");
        }

        ArrayList<FasceOrarie> lista = new ArrayList<>();

        try {

            if (f == null || f.getIdFasciaOraria() == null) {
                throw new DAOException("In select: idFasciaOraria cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "SELECT * FROM fasceOrarie WHERE idFasciaOraria = '" + f.getIdFasciaOraria() + "';";

            try {
                logger.info("SQL: " + sql);

            } catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + sql);
            }

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                lista.add(new FasceOrarie(
                        rs.getInt("idFasciaOraria"),
                        rs.getString("data"),
                        rs.getString("oraInizio"),
                        rs.getString("oraFine")
                ));
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq) {
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }

    /**
     * Elimina una fascia oraria dal database.
     *
     * @param f
     */

    @Override
    public void delete(FasceOrarie f) throws DAOException {

        if (f == null || f.getIdFasciaOraria() == null) {
            throw new DAOException("In delete: idFasciaOraria cannot be null");
        }

        String sql = "DELETE FROM fasceOrarie WHERE idFasciaOraria = '" + f.getIdFasciaOraria() + "';";

        try {
            logger.info("SQL: " + sql);

        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + sql);
        }

        executeUpdate(sql);
    }

    /**
     * Inserisce una nuova fascia oraria nel database.
     *
     * @param f
     */

    @Override
    public void insert(FasceOrarie f) throws DAOException {

        verifyObject(f);

        int generatedId = -1;

        String sql =
                "INSERT INTO fasceOrarie (data, oraInizio, oraFine) VALUES ('" +
                        f.getData() + "', '" +
                        f.getOraInizio() + "', '" +
                        f.getOraFine() + "');";

        try {
            logger.info("SQL: " + sql);

        } catch (NullPointerException nullPointerException) {
            System.out.println("SQL: " + sql);
        }

        try {
            Statement st = DAOMySQLSettings.getStatement();

            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                f.setIdFasciaOraria(generatedId);
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In insert(): " + e.getMessage());
        }

    }

    /**
     * Aggiorna una fascia oraria esistente nel database.
     *
     * @param f
     */

    @Override
    public void update(FasceOrarie f) throws DAOException {

        verifyObject(f);

        String sql = "UPDATE fasceOrarie SET data = '" + f.getData() +
                "', oraInizio = '" + f.getOraInizio() +
                "', oraFine = '" + f.getOraFine() +
                "' WHERE idFasciaOraria = " + f.getIdFasciaOraria() + ";";

        logger.info("SQL: " + sql);

        executeUpdate(sql);
    }

    /**
     * Verifica la validità dell'oggetto FasceOrarie.
     *
     * @param f
     */

    private void verifyObject(FasceOrarie f) throws DAOException {

        if (f == null || f.getIdFasciaOraria() == null ||
                f.getData() == null ||
                f.getOraInizio() == null ||
                f.getOraFine() == null) {
            throw new DAOException("In verifyObject: fields cannot be null");
        }
    }

    /**
     * Esegue una query di aggiornamento sul database.
     *
     * @param query
     */

    private void executeUpdate(String query) throws DAOException {

        try {
            Statement st = DAOMySQLSettings.getStatement();
            int n = st.executeUpdate(query);
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In executeUpdate(): " + e.getMessage());
        }
    }

    /**
     * Restituisce tutte le fasce orarie associate a un determinato medico.
     *
     * @param emailMedico
     */

    public List<FasceOrarie> findByMedico(String emailMedico) throws DAOException {

        List<FasceOrarie> lista = new ArrayList<>();

        String sql = "SELECT f.idFasciaOraria, f.data, f.oraInizio, f.oraFine " +
                "FROM fasceOrarie f " +
                "JOIN fasceOrarie_has_medici fm ON f.idFasciaOraria = fm.fasciaOraria_id " +
                "WHERE fm.medicoEmail = '" + emailMedico + "'";

        try {
            Statement st = DAOMySQLSettings.getStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                FasceOrarie f = new FasceOrarie(
                        rs.getInt("idFasciaOraria"),
                        rs.getString("data"),
                        rs.getString("oraInizio"),
                        rs.getString("oraFine")
                );
                lista.add(f);
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("Errore in findByMedico(): " + e.getMessage());
        }

        return lista;
    }

    /**
     * Seleziona le fasce orarie in base a data e orari.
     *
     * @param f
     */

    public List<FasceOrarie> selectByDataOra(FasceOrarie f) throws DAOException {

        ArrayList<FasceOrarie> lista = new ArrayList<>();

        try {

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "SELECT * FROM fasceOrarie WHERE data = '" + f.getData()+ "' " +
                    "AND oraInizio = '" + f.getOraInizio() + "' " +
                    "AND oraFine = '" + f.getOraFine() + "';";

            try {
                logger.info("SQL: " + sql);

            } catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + sql);
            }

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                lista.add(new FasceOrarie(
                        rs.getInt("idFasciaOraria"),
                        rs.getString("data"),
                        rs.getString("oraInizio"),
                        rs.getString("oraFine")
                ));
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq) {
            throw new DAOException("In select(): " + sq.getMessage());
        }

        return lista;
    }

    /**
     * Seleziona le fasce orarie associate a una specifica data
     * e a un determinato medico.
     *
     * @param data
     * @param emailMedico
     */

    public List<FasceOrarie> selectByDataAndMedico(String data, String emailMedico) throws DAOException {

        ArrayList<FasceOrarie> lista = new ArrayList<>();

        try {

            Statement st = DAOMySQLSettings.getStatement();

            String sql =
                    "SELECT fo.* FROM fasceOrarie fo " +
                            "JOIN fasceorarie_has_medici fm ON fo.idFasciaOraria = fm.fasciaOraria_id " +
                            "WHERE fo.data = '" + data + "' " +
                            "AND fm.medicoEmail = '" + emailMedico + "';";

            try {
                logger.info("SQL: " + sql);

            } catch (NullPointerException nullPointerException) {
                System.out.println("SQL: " + sql);
            }

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                lista.add(new FasceOrarie(
                        rs.getInt("idFasciaOraria"),
                        rs.getString("data"),
                        rs.getString("oraInizio"),
                        rs.getString("oraFine")
                ));
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq) {
            throw new DAOException("In selectByDataAndMedico(): " + sq.getMessage());
        }

        return lista;
    }

}