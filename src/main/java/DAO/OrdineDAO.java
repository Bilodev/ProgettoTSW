package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import control.DataSourceFactory;
import model.Ordine;

public class OrdineDAO {
    private final DataSource dataSource;

    public OrdineDAO() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    public List<Ordine> findAll() throws SQLException {
        String sql = "SELECT * FROM ORDINE ORDER BY data_ordine DESC";
        List<Ordine> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) results.add(mapRow(resultSet));
        }
        return results;
    }

    public Ordine findById(int id) throws SQLException {
        String sql = "SELECT * FROM ORDINE WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public Ordine findBySeqId(String seqId) throws SQLException {
        String sql = "SELECT * FROM ORDINE WHERE seqId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seqId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Ordine> findByUtente(int utenteId) throws SQLException {
        String sql = "SELECT * FROM ORDINE WHERE utente_id = ? ORDER BY data_ordine DESC";
        List<Ordine> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, utenteId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    // Restituisce l'ID generato — serve subito dopo per inserire le righe in ORDINE_DVD
    public int insert(Ordine ordine) throws SQLException {
        String sql = "INSERT INTO ORDINE(seqId, utente_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ordine.getSeqId());
            statement.setInt(2, ordine.getUtenteId());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Insert ORDINE fallita: nessun ID generato");
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ORDINE WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Ordine mapRow(ResultSet rs) throws SQLException {
        return new Ordine(
            rs.getInt("id"),
            rs.getString("seqId"),
            rs.getInt("utente_id"),
            rs.getTimestamp("data_ordine")
        );
    }
}