package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Pagamenti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PagamentiDAOMySQLImpl implements DAO<Pagamenti> {

    private PagamentiDAOMySQLImpl(){}

    private static Logger logger = null;

    private static PagamentiDAOMySQLImpl instance = null;

    public static PagamentiDAOMySQLImpl getInstance() {
        if (instance == null) {
            instance = new PagamentiDAOMySQLImpl();
            logger = Logger.getLogger(PagamentiDAOMySQLImpl.class.getName());
        }
        return instance;
    }

    public static void main(String args[]) throws DAOException {
        PagamentiDAOMySQLImpl c = new PagamentiDAOMySQLImpl();

        c.insert(new Pagamenti(1,"pagata",50.00,"sara.vettese@uni.it", 2));
        c.insert(new Pagamenti(2,"da saldare",75.00,"elisa.quagliozzi@uni.it", 3));
        c.insert(new Pagamenti(3,"pagata",60.00,"lorenza.martini@uni.it", 1));


        List<Pagamenti> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

        Pagamenti toDelete = new Pagamenti();
        toDelete.setIdPagamento(1);
        toDelete.setStato("");
        toDelete.setImporto(null); //(tipo Double può essere null)
        toDelete.setEmailSegretario("");
        toDelete.setVisitaIdVisita(null);

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Pagamenti> select(Pagamenti g) throws DAOException {

        if (g == null){
            g = new Pagamenti(null, "", null, "",null); // Cerca tutti gli elementi
        }

        ArrayList<Pagamenti> lista = new ArrayList<>();
        try{

            if (g == null || g.getIdPagamento() == null){
                throw new DAOException("In select: idPagamento cannot be null");
            }

            Statement st = DAOMySQLSettings.getStatement();

            String sql = "select * from pagamenti where idPagamento = '" + g.getIdPagamento() + "';";

            try{
                logger.info("SQL: " + sql);
            } catch(NullPointerException nullPointerException){
                logger.severe("SQL: " + sql);
            }
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                lista.add(new Pagamenti(rs.getInt("idPagamento"),
                        rs.getString("stato"),
                        rs.getDouble("importo"),
                        rs.getString("segretarioEmail"),
                        rs.getInt("visitaIdVisita")));
            }
            DAOMySQLSettings.closeStatement(st);

        } catch (SQLException sq){
            throw new DAOException("In select(): " + sq.getMessage());
        }
        return lista;
    }

    @Override
    public void delete(Pagamenti g) throws DAOException {
        if (g == null || g.getIdPagamento() == null){
            throw new DAOException("In delete: idPagamento cannot be null");
        }
        String query = "DELETE FROM pagamenti WHERE idPagamento = '" + g.getIdPagamento() + "';";

        try{
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }

        executeUpdate(query);

    }

    @Override
    public void insert(Pagamenti g) throws DAOException {

        // Verifica che i campi obbligatori non siano null
        verifyObject(g);

        String sql = "INSERT INTO pagamenti (stato, importo, segretarioEmail, visitaIdVisita) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = DAOMySQLSettings.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Imposta i valori dei parametri
            ps.setString(1, g.getStato());
            ps.setDouble(2, g.getImporto());
            ps.setString(3, g.getEmailSegretario());
            ps.setInt(4, g.getVisitaIdVisita());

            // Esegue l'INSERT
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Insert failed, no rows affected.");
            }

            // Recupera l'ID generato dal DB e lo imposta sull'oggetto
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    g.setIdPagamento(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Insert failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("In insert(): " + e.getMessage());
        }
    }



    @Override
    public void update(Pagamenti g) throws DAOException {

        verifyObject(g);

        String query = "UPDATE pagamenti SET " + "stato = '" + g.getStato() + "', " + "importo = " + g.getImporto() + ", " + "segretarioEmail = '" + g.getEmailSegretario() + "', " + "visitaIdVisita = " + g.getVisitaIdVisita() + " " +
                "WHERE idPagamento = " + g.getIdPagamento() + ";";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }

    private void verifyObject(Pagamenti g) throws DAOException {
        if (g == null || g.getStato() == null
                || g.getImporto() == null
                || g.getEmailSegretario() == null
                || g.getVisitaIdVisita() == null){
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

    public Pagamenti selectByVisitaId(Integer idVisita) throws DAOException {
        Pagamenti p = null;
        try {
            Statement st = DAOMySQLSettings.getStatement();
            String sql = "SELECT * FROM pagamenti WHERE visitaIdVisita = " + idVisita + " LIMIT 1;";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                p = new Pagamenti(
                        rs.getInt("idPagamento"),
                        rs.getString("stato"),
                        rs.getDouble("importo"),
                        rs.getString("segretarioEmail"), //Visualizzare nome corretto nel proprio database
                        rs.getInt("visitaIdVisita")
                );
            }
            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("Errore selectByVisitaId(): " + e.getMessage());
        }
        return p;
    }


    // DTO per i risultati del report
    public static class ReportResult {
        public int visitePagate;
        public int visiteDaSaldare;
        public double totaleIncassato;
        public double totaleDaIncassare;
    }

    public ReportResult getReportByMonth(int mese) throws DAOException {
        ReportResult result = new ReportResult();

        String query = "SELECT " +
                "SUM(CASE WHEN LOWER(p.stato) = 'pagata' THEN 1 ELSE 0 END) AS visitePagate, " +
                "SUM(CASE WHEN LOWER(p.stato) = 'pagata' THEN p.importo ELSE 0 END) AS totaleIncassato, " +
                "SUM(CASE WHEN LOWER(p.stato) = 'da saldare' THEN 1 ELSE 0 END) AS visiteDaSaldare, " +
                "SUM(CASE WHEN LOWER(p.stato) = 'da saldare' THEN p.importo ELSE 0 END) AS totaleDaIncassare " +
                "FROM pagamenti p " +
                "JOIN visite v ON p.visitaIdVisita = v.idVisita " +
                "WHERE MONTH(STR_TO_DATE(v.dataOra, '%Y-%m-%d %H:%i:%s')) = " + mese + ";";

        try {
            Statement st = DAOMySQLSettings.getStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                result.visitePagate = rs.getInt("visitePagate");
                result.totaleIncassato = rs.getDouble("totaleIncassato");
                result.visiteDaSaldare = rs.getInt("visiteDaSaldare");
                result.totaleDaIncassare = rs.getDouble("totaleDaIncassare");
            }

            DAOMySQLSettings.closeStatement(st);
        } catch (SQLException e) {
            throw new DAOException("In getReportByMonth(): " + e.getMessage());
        }

        return result;
    }


}

