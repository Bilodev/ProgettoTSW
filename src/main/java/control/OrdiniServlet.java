package control;

import DAO.OrdineRiepilogoDAO;
import model.OrdineRiepilogo;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ordini")
public class OrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrdineRiepilogoDAO ordineRiepilogoDAO = new OrdineRiepilogoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utenteId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int utenteId = (int) session.getAttribute("utenteId");
        try {
            List<OrdineRiepilogo> ordini = ordineRiepilogoDAO.findByUtente(utenteId);
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/view/Ordini.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Errore nel recupero degli ordini", e);
        }
    }
}