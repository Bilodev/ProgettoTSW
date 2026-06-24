package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import util.DataSourceFactory;
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

	public List<DVD> findAllInCatalogo() throws SQLException {
		String sql = "SELECT * FROM DVD WHERE inCatalogo=TRUE ORDER BY nome";
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

	public int insert(DVD dvd) throws SQLException {
		String sql = "INSERT INTO DVD(nome, durata, regista, prezzo, inCatalogo, quantita) VALUES (?, ?, ?, ?, TRUE, ?)";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql,
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, dvd.getNome());
			statement.setInt(2, dvd.getDurata());
			statement.setString(3, dvd.getRegista());
			statement.setFloat(4, dvd.getPrezzo());
			statement.setInt(5, dvd.getQuantita());
			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getInt(1);
				}
			}
		}
		throw new SQLException("Insert fallita: nessun ID generato");
	}

	public void update(DVD dvd) throws SQLException {
		String sql = "UPDATE DVD SET nome = ?, durata = ?, regista = ?, prezzo = ? WHERE id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, dvd.getNome());
			statement.setInt(2, dvd.getDurata());
			statement.setString(3, dvd.getRegista());
			statement.setFloat(4, dvd.getPrezzo());
			statement.setInt(5, dvd.getId());
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

	/**
	 * Aggiorna la quantità di un DVD nel database applicando un delta. delta > 0 →
	 * aumenta (restituzione al magazzino) delta < 0 → diminuisce (prelievo per il
	 * carrello) Usa MAX(0, quantità + delta) per evitare valori negativi.
	 */
	public void updateQuantita(int id, int delta) throws SQLException {
		String sql = "UPDATE DVD SET quantita = GREATEST(0, quantita + ?) WHERE id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, delta);
			statement.setInt(2, id);
			statement.executeUpdate();
		}
	}

	/**
	 * Imposta la quantità di un DVD a un valore assoluto (usato dall'admin).
	 */
	public void updateQuantitaAssoluta(int id, int quantita) throws SQLException {
		String sql = "UPDATE DVD SET quantita = ? WHERE id = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, quantita);
			statement.setInt(2, id);
			statement.executeUpdate();
		}
	}

	private DVD mapRow(ResultSet resultSet) throws SQLException {
		return new DVD(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getInt("durata"),
				resultSet.getString("regista"), resultSet.getFloat("prezzo"), resultSet.getBoolean("inCatalogo"),
				resultSet.getInt("quantita"));
	}
}