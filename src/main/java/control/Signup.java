package control;

import DAO.UtenteDAO;
import model.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signup")
public class Signup extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UtenteDAO utenteDAO = new UtenteDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/Signup.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (utenteDAO.existsByUsername(username)) {
                request.setAttribute("error", "Username già in uso");
                request.getRequestDispatcher("/WEB-INF/view/Signup.jsp").forward(request, response);
                return;
            }
            if (utenteDAO.existsByEmail(email)) {
                request.setAttribute("error", "Email già in uso");
                request.getRequestDispatcher("/WEB-INF/view/Signup.jsp").forward(request, response);
                return;
            }
            utenteDAO.insert(new Utente(0, email, username, password, false));
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
