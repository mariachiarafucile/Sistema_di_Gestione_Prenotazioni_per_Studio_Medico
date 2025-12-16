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

/**
 * Classe DAO per la gestione delle prenotazioni.
 */

public class PrenotazioniDAOMySQLImpl implements DAO<Prenotazioni> {

    private static Logger logger = null;

    /**
     * Costruttore privato per impedire l'istanziazione diretta.
     */

    private PrenotazioniDAOMySQLImpl(){}

    private static PrenotazioniDAOMySQLImpl dao = null;

    /**
     * Restituisce istanza della classe.
     * Se l'istanza non esiste, viene creata e inizializzato il logger.
     */

    public static PrenotazioniDAOMySQLImpl getInstance() {
        if (dao == null) {
            dao = new PrenotazioniDAOMySQLImpl();
            logger = Logger.getLogger(PrenotazioniDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    /**
     * Seleziona una lista di prenotazioni dal database in base ai criteri
     * specificati nell'oggetto passato come parametro.
     * La selezione viene effettuata utilizzando il codice fiscale
     * del paziente.
     *
     * @param q
     */

    @Override
    public List<Prenotazioni> select(Prenotazioni q) throws DAOException {

        if (q == null) {
            q = new Prenotazioni(null, "", "", null);
        }

        ArrayList<Prenotazioni> lista = new ArrayList<>();

        try {

            if (q == null || q.getIdPrenotazioni() == null) {
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

    /**
     * Elimina una prenotazione dal database.
     *
     * @param q
     */

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

    /**
     * Inserisce una nuova prenotazione nel database.
     *
     * @param q
     */

    @Override
    public void insert(Prenotazioni q) throws DAOException {

        verifyObject(q);

        String sql =
                "INSERT INTO prenotazioni (paziente_codiceFiscale, medico_email, fasciaOrariaId) VALUES ('" +
                        q.getPazienteCodiceFiscale() + "', '" +
                        q.getMedicoEmail() + "', '" +
                        q.getFasciaOrariaId() + "');";

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
                int generatedId = rs.getInt(1);
                q.setIdPrenotazioni(generatedId);
            }

            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException e) {
            throw new DAOException("In insert(): " + e.getMessage());
        }

    }

    /**
     * Aggiorna una prenotazione esistente nel database.
     *
     * @param q
     */

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

    /**
     * Verifica la validità dell'oggetto.
     *
     * @param q
     */

    private void verifyObject(Prenotazioni q) throws DAOException {
        if (q == null ||
                q.getIdPrenotazioni() == null ||
                q.getPazienteCodiceFiscale() == null ||
                q.getMedicoEmail() == null ||
                q.getFasciaOrariaId() == null) {
            throw new DAOException("In verifyObject: fields cannot be null");
        }
    }

    /**
     * Esegue una query di aggiornamento.
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
     * Restituisce l'elenco delle prenotazioni scadute.
     * Una prenotazione è considerata scaduta se la data e l'ora di fine
     * della fascia oraria associata sono precedenti all'istante corrente.
     */

    public List<Prenotazioni> selectPrenotazioniScadute() throws DAOException {

        List<Prenotazioni> lista = new ArrayList<>();

        String sql =
                "SELECT p.* " +
                        "FROM prenotazioni p " +
                        "JOIN fasceOrarie f ON p.fasciaOrariaId = f.idFasciaOraria " +
                        "WHERE STR_TO_DATE(CONCAT(f.data, ' ', f.oraFine), '%Y-%m-%d %H:%i') < NOW()";

        try {
            Statement st = DAOMySQLSettings.getStatement();
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

        } catch (SQLException e) {
            throw new DAOException("Errore selectPrenotazioniScadute: " + e.getMessage());
        }

        return lista;
    }

}


