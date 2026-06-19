package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import control.DataSourceFactory;
import model.OrdineDVD;

public class OrdineDVDDAO {
    private final DataSource dataSource;

    public OrdineDVDDAO() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    // Tutte le righe di un ordine
    public List<OrdineDVD> findByOrdine(int ordineId) throws SQLException {
        String sql = "SELECT * FROM ORDINE_DVD WHERE ordine_id = ?";
        List<OrdineDVD> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ordineId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    // Tutti gli ordini che contengono un certo DVD
    public List<OrdineDVD> findByDVD(int DVDId) throws SQLException {
        String sql = "SELECT * FROM ORDINE_DVD WHERE DVD_id = ?";
        List<OrdineDVD> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, DVDId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    // Inserisce una riga singola
    public void insert(OrdineDVD riga) throws SQLException {
        String sql = "INSERT INTO ORDINE_DVD(ordine_id, DVD_id, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, riga.getOrdineId());
            statement.setInt(2, riga.getDvdId());
            statement.setInt(3, riga.getQuantita());
            statement.setFloat(4, riga.getPrezzoUnitario());
            statement.executeUpdate();
        }
    }

    // Inserisce tutte le righe di un ordine in un'unica transazione
    public void insertAll(List<OrdineDVD> righe) throws SQLException {
        String sql = "INSERT INTO ORDINE_DVD(ordine_id, DVD_id, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (OrdineDVD riga : righe) {
                    statement.setInt(1, riga.getOrdineId());
                    statement.setInt(2, riga.getDvdId());
                    statement.setInt(3, riga.getQuantita());
                    statement.setFloat(4, riga.getPrezzoUnitario());
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void deleteByOrdine(int ordineId) throws SQLException {
        String sql = "DELETE FROM ORDINE_DVD WHERE ordine_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ordineId);
            statement.executeUpdate();
        }
    }

    private OrdineDVD mapRow(ResultSet rs) throws SQLException {
        return new OrdineDVD(
            rs.getInt("ordine_id"),
            rs.getInt("DVD_id"),
            rs.getInt("quantita"),
            rs.getFloat("prezzo_unitario")
        );
    }
}