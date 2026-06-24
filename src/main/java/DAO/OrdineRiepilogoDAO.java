package DAO;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import util.DataSourceFactory;
import model.OrdineRiepilogo;

public class OrdineRiepilogoDAO {
	private final DataSource dataSource;

	public OrdineRiepilogoDAO() {
		this.dataSource = DataSourceFactory.getDataSource();
	}

	// Costante SQL comune per evitare la duplicazione delle lunghe clausole di
	// SELECT e JOIN delle testate
	private static final String BASE_SELECT_RIEPILOGO = "SELECT o.id AS ordine_id, o.seqId, o.data_ordine, "
			+ "SUM(od.prezzo_unitario * od.quantita) AS totale, " + "u.username, u.email, "
			+ "i.nome, i.cognome, i.indirizzo, i.citta, i.cap, i.paese, "
			+ "GROUP_CONCAT(d.nome ORDER BY d.nome SEPARATOR ', ') AS dvd_acquistati " + "FROM ORDINE o "
			+ "JOIN UTENTE u ON u.id = o.utente_id " + "JOIN INDIRIZZO i ON i.seqId = o.seqId "
			+ "JOIN ORDINE_DVD od ON o.id = od.ordine_id " + "JOIN DVD d ON d.id = od.DVD_id ";

	private static final String BASE_GROUP_BY = " GROUP BY o.id, o.seqId, o.data_ordine, u.username, u.email, "
			+ "i.nome, i.cognome, i.indirizzo, i.citta, i.cap, i.paese ";

	// Ordini di un utente specifico
	public List<OrdineRiepilogo> findByUtente(int utenteId) throws SQLException {
		String sql = BASE_SELECT_RIEPILOGO + " WHERE u.id = ? " + BASE_GROUP_BY + " ORDER BY o.data_ordine DESC";
		List<OrdineRiepilogo> results = new ArrayList<>();

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, utenteId);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					results.add(mapRow(rs));
				}
			}
		}
		return results;
	}

	// Tutti gli ordini in un certo range di date
	public List<OrdineRiepilogo> findByDateRange(String dataInizio, String dataFine) throws SQLException {
		String sql = BASE_SELECT_RIEPILOGO + " WHERE o.data_ordine BETWEEN ? AND ? " + BASE_GROUP_BY
				+ " ORDER BY o.data_ordine DESC";
		List<OrdineRiepilogo> results = new ArrayList<>();

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, dataInizio + " 00:00:00");
			statement.setString(2, dataFine + " 23:59:59");
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					results.add(mapRow(rs));
				}
			}
		}
		return results;
	}

	// Ordini di un utente in un range di date
	public List<OrdineRiepilogo> findByUtenteAndDateRange(String utenteId, String dataInizio, String dataFine)
			throws SQLException {
		String sql = BASE_SELECT_RIEPILOGO + " WHERE u.email = ? AND o.data_ordine BETWEEN ? AND ? " + BASE_GROUP_BY
				+ " ORDER BY o.data_ordine DESC";
		List<OrdineRiepilogo> results = new ArrayList<>();

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, utenteId);
			statement.setString(2, dataInizio + " 00:00:00");
			statement.setString(3, dataFine + " 23:59:59");
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					results.add(mapRow(rs));
				}
			}
		}
		return results;
	}

	// Classifica/totale speso per ciascun cliente nel range di date
	public List<OrdineRiepilogo> findTotaliPerCliente(String dataInizio, String dataFine) throws SQLException {
		String sql = "SELECT u.username, u.email, " + "COUNT(DISTINCT o.id) AS num_ordini, " + // Conta gli ordini
																								// univoci sulla tabella
																								// principale
				"SUM(od.prezzo_unitario * od.quantita) AS totale_speso " + "FROM UTENTE u "
				+ "JOIN ORDINE o ON u.id = o.utente_id " + "JOIN ORDINE_DVD od ON o.id = od.ordine_id "
				+ "WHERE o.data_ordine BETWEEN ? AND ? " + "GROUP BY u.username, u.email "
				+ "ORDER BY totale_speso DESC";

		List<OrdineRiepilogo> results = new ArrayList<>();
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, dataInizio + " 00:00:00");
			statement.setString(2, dataFine + " 23:59:59");
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					OrdineRiepilogo o = new OrdineRiepilogo();
					o.setUsername(rs.getString("username"));
					o.setEmail(rs.getString("email"));
					o.setNumOrdini(rs.getInt("num_ordini"));
					o.setTotaleSpeso(rs.getFloat("totale_speso"));
					results.add(o);
				}
			}
		}
		return results;
	}

	private OrdineRiepilogo mapRow(ResultSet rs) throws SQLException {
		OrdineRiepilogo o = new OrdineRiepilogo();
		o.setOrdineId(rs.getInt("ordine_id")); // Assegna il nuovo ID numerico dell'ordine
		o.setSeqId(rs.getString("seqId"));
		o.setDataOrdine(rs.getTimestamp("data_ordine"));
		o.setTotale(rs.getFloat("totale"));
		o.setUsername(rs.getString("username"));
		o.setEmail(rs.getString("email"));
		o.setNome(rs.getString("nome"));
		o.setCognome(rs.getString("cognome"));
		o.setIndirizzo(rs.getString("indirizzo"));
		o.setCitta(rs.getString("citta"));
		o.setCap(rs.getString("cap"));
		o.setPaese(rs.getString("paese"));
		o.setDvdAcquistati(rs.getString("dvd_acquistati"));
		return o;
	}
}