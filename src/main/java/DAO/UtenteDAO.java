package DAO;

import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import util.DataSourceFactory;
import model.Utente;

public class UtenteDAO {
    private final DataSource dataSource;

    public UtenteDAO() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    public Utente findByUsernameOrEmailAndPassword(String identifier, String password) throws SQLException {
        String sql = "SELECT id, email, username, password, admin FROM UTENTE WHERE (username = ? OR email = ?) AND password = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, identifier);
            statement.setString(2, identifier);
            statement.setString(3, PasswordUtil.sha256(password));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Utente(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("admin")
                    );
                }
            }
        }
        return null;
    }

    public boolean existsByUsername(String username) throws SQLException {
        return exists("SELECT 1 FROM UTENTE WHERE username = ?", username);
    }

    public boolean existsByEmail(String email) throws SQLException {
        return exists("SELECT 1 FROM UTENTE WHERE email = ?", email);
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insert(Utente utente) throws SQLException {
        String sql = "INSERT INTO UTENTE (email, username, password, admin) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, utente.getEmail());
            statement.setString(2, utente.getUsername());
            statement.setString(3, PasswordUtil.sha256(utente.getPassword()));
            statement.setBoolean(4, utente.isAdmin());
            statement.executeUpdate();
        }
    }
}
