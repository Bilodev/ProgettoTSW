package DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import util.DataSourceFactory;
import model.Indirizzo;


public class IndirizzoDAO {
    private final DataSource dataSource;

    public IndirizzoDAO() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    public List<Indirizzo> findAll() throws SQLException {
        String sql = "SELECT * FROM INDIRIZZO";
        List<Indirizzo> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                results.add(mapRow(resultSet));
            }
        }
        return results;
    }

    public Indirizzo findById(int id) throws SQLException {
        String sql = "SELECT * FROM INDIRIZZO WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public Indirizzo findByOrder(int orderID) throws SQLException {
        String sql = "SELECT * FROM INDIRIZZO WHERE seqId = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public List<Indirizzo> findByUser(int userID) throws SQLException {
        String sql = "SELECT * FROM INDIRIZZO WHERE userID = ?";
        List<Indirizzo> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapRow(resultSet));
                }
            }
        }
        return results;
    }

    public void insert(Indirizzo indirizzo) throws SQLException {
        String sql = "INSERT INTO INDIRIZZO(seqId, userID, nome, cognome, indirizzo, citta, cap, paese) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, indirizzo.getOrderID());
            statement.setInt(2, indirizzo.getUserID());
            statement.setString(3, indirizzo.getNome());
            statement.setString(4, indirizzo.getCognome());
            statement.setString(5, indirizzo.getIndirizzo());
            statement.setString(6, indirizzo.getCitta());
            statement.setString(7, indirizzo.getCap());
            statement.setString(8, indirizzo.getPaese());
            statement.executeUpdate();
        }
    }

    public void update(Indirizzo indirizzo) throws SQLException {
        String sql = "UPDATE INDIRIZZO SET nome = ?, cognome = ?, indirizzo = ?, citta = ?, cap = ?, paese = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, indirizzo.getNome());
            statement.setString(2, indirizzo.getCognome());
            statement.setString(3, indirizzo.getIndirizzo());
            statement.setString(4, indirizzo.getCitta());
            statement.setString(5, indirizzo.getCap());
            statement.setString(6, indirizzo.getPaese());
            statement.setInt(7, indirizzo.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM INDIRIZZO WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Indirizzo mapRow(ResultSet resultSet) throws SQLException {
        return new Indirizzo(
                resultSet.getInt("id"),
                resultSet.getString("seqId"),
                resultSet.getInt("userID"),
                resultSet.getString("nome"),
                resultSet.getString("cognome"),
                resultSet.getString("indirizzo"),
                resultSet.getString("citta"),
                resultSet.getString("cap"),
                resultSet.getString("paese")
        );
    }
}