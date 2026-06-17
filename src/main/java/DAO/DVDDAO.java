package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import model.DVD;

public class DVDDAO {
    private final DataSource dataSource;

    public DVDDAO() {
        this.dataSource = DataSourceFactory.getDataSource();
    }

    public List<DVD> searchByName(String query) throws SQLException {
        String sql = "SELECT * FROM DVD WHERE nome LIKE ? AND inCatalogo=TRUE ORDER BY nome";
        List<DVD> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapRow(resultSet));
                }
            }
        }
        return results;
    }

    public List<DVD> findAll() throws SQLException {
        String sql = "SELECT * FROM DVD ORDER BY nome";
        List<DVD> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                results.add(mapRow(resultSet));
            }
        }
        return results;
    }

    public DVD findById(int id) throws SQLException {
        String sql = "SELECT * FROM DVD WHERE id = ?";
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

    public void insert(DVD dvd) throws SQLException {
        String sql = "INSERT INTO DVD(nome, durata, regista, inCatalogo) VALUES (?, ?, ?, TRUE)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dvd.getNome());
            statement.setInt(2, dvd.getDurata());
            statement.setString(3, dvd.getRegista());	
            statement.executeUpdate();
        }
    }

    public void update(DVD dvd) throws SQLException {
        String sql = "UPDATE DVD SET nome = ?, durata = ?, regista = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dvd.getNome());
            statement.setInt(2, dvd.getDurata());
            statement.setString(3, dvd.getRegista());
            statement.setInt(4, dvd.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "UPDATE DVD SET inCatalogo=FALSE WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    
    public void reinsert(int id) throws SQLException {
        String sql = "UPDATE DVD SET inCatalogo=TRUE WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private DVD mapRow(ResultSet resultSet) throws SQLException {
        return new DVD(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                resultSet.getInt("durata"),
                resultSet.getString("regista"),
                resultSet.getBoolean("inCatalogo")
        );
    }
}
