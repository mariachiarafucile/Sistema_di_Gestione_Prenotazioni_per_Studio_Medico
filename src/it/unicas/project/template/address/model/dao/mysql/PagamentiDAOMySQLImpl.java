package it.unicas.project.template.address.model.dao.mysql;

import it.unicas.project.template.address.model.Pagamenti;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PagamentiDAOMySQLImpl implements DAO<Pagamenti> {

    private PagamentiDAOMySQLImpl(){}

    private static DAO dao = null;
    private static Logger logger = null;

    public static DAO getInstance(){
        if (dao == null){
            dao = new PagamentiDAOMySQLImpl();
            logger = Logger.getLogger(PagamentiDAOMySQLImpl.class.getName());
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        PagamentiDAOMySQLImpl c = new PagamentiDAOMySQLImpl();

        c.insert(new Pagamenti(1,"pagata",50.00,2,"sara.vettese@uni.it"));
        c.insert(new Pagamenti(2,"da saldare",75.00,3,"elisa.quagliozzi@uni.it"));
        c.insert(new Pagamenti(3,"pagata",60.00,1,"lorenza.martini@uni.it"));


        List<Pagamenti> list = c.select(null);
        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

        Pagamenti toDelete = new Pagamenti();
        toDelete.setIdPagamento(1);
        toDelete.setStato("");
        toDelete.setImporto(null); //(tipo Double può essere null)
        toDelete.setVisitaIdVisita(null);
        toDelete.setEmailSegretario("");

        c.delete(toDelete);

        list = c.select(null);

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }

    }

    @Override
    public List<Pagamenti> select(Pagamenti g) throws DAOException {

        if (g == null){
            g = new Pagamenti(null, "", null, null,""); // Cerca tutti gli elementi
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
                        rs.getInt("visitaIdVisita"),
                        rs.getString("emailSegretario")));
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

        verifyObject(g);

        String query = "INSERT INTO pagamenti (idPagamento, stato, importo, visitaIdVisita, emailSegretario) VALUES  ('" +
                g.getIdPagamento() + "', '" + g.getStato() + "', '" +
                "', '" + g.getImporto() + "', '" + g.getVisitaIdVisita() + "', '" + g.getEmailSegretario() + "');";
        try {
            logger.info("SQL: " + query);
        } catch (NullPointerException nullPointerException){
            System.out.println("SQL: " + query);
        }
        executeUpdate(query);
    }


    @Override
    public void update(Pagamenti g) throws DAOException {

        verifyObject(g);

        String query = "UPDATE pagamenti SET stato = '" + g.getStato() + "', visitaIdVisita = '" + g.getVisitaIdVisita() + "', emailSegretario = '" + g.getEmailSegretario() + "'";
        query = query + " WHERE idPagamento = " + g.getIdPagamento() + ";";
        logger.info("SQL: " + query);

        executeUpdate(query);

    }

    private void verifyObject(Pagamenti g) throws DAOException {
        if (g == null || g.getIdPagamento() == null
                || g.getStato() == null
                || g.getImporto() == null
                || g.getVisitaIdVisita() == null
                || g.getEmailSegretario() == null) {
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
                "SUM(CASE WHEN LOWER(p.stato) = 'pagato' THEN 1 ELSE 0 END) AS visitePagate, " +
                "SUM(CASE WHEN LOWER(p.stato) = 'pagato' THEN p.importo ELSE 0 END) AS totaleIncassato, " +
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

