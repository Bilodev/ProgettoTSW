package model;

import DAO.UtenteDAO;
import java.sql.SQLException;

public class UtenteService {
    private final UtenteDAO utenteDAO;

    public UtenteService() {
        this.utenteDAO = new UtenteDAO();
    }

    public Utente login(String identifier, String password) throws SQLException {
        return utenteDAO.findByUsernameOrEmailAndPassword(identifier, password);
    }

    public boolean usernameExists(String username) throws SQLException {
        return utenteDAO.existsByUsername(username);
    }

    public boolean emailExists(String email) throws SQLException {
        return utenteDAO.existsByEmail(email);
    }

    public void register(Utente utente) throws SQLException {
        utenteDAO.insert(utente);
    }
}
