package control;

import DAO.UtenteDAO;
import model.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UtenteDAO utenteDAO = new UtenteDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/Login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("error", "Inserisci username/email e password");
            request.getRequestDispatcher("/WEB-INF/view/Login.jsp").forward(request, response);
            return;
        }

        try {
            Utente utente = utenteDAO.findByUsernameOrEmailAndPassword(identifier, password);
            if (utente == null) {
                request.setAttribute("error", "Credenziali non valide");
                request.getRequestDispatcher("/WEB-INF/view/Login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("username", utente.getUsername());
            session.setAttribute("nome", utente.getUsername());
            session.setAttribute("admin", utente.isAdmin());
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
