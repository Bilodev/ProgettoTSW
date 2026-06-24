package control.admin;

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

@WebServlet("/admin/ordini")
public class AdminOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final OrdineRiepilogoDAO ordineRiepilogoDAO = new OrdineRiepilogoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("admin"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String dataInizio  = request.getParameter("dataInizio");
        String dataFine    = request.getParameter("dataFine");
        String utenteIdStr = request.getParameter("utenteId");
        String vista       = request.getParameter("vista"); // "ordini" o "clienti"

        // valori di default: ultimo mese
        if (dataInizio == null || dataInizio.isEmpty()) dataInizio = "2025-01-01";
        if (dataFine   == null || dataFine.isEmpty())   dataFine   = "2028-12-31";

        try {
            if ("clienti".equals(vista)) {
                List<OrdineRiepilogo> clienti = ordineRiepilogoDAO.findTotaliPerCliente(dataInizio, dataFine);
                request.setAttribute("clienti", clienti);

            // vista degli ordini in base a se è presente o meno parametro utente
            } else if (utenteIdStr != null && !utenteIdStr.isEmpty()) {
                List<OrdineRiepilogo> ordini = ordineRiepilogoDAO.findByUtenteAndDateRange(utenteIdStr, dataInizio, dataFine);
                request.setAttribute("ordini", ordini);

            } else {
                List<OrdineRiepilogo> ordini = ordineRiepilogoDAO.findByDateRange(dataInizio, dataFine);
                request.setAttribute("ordini", ordini);
            }

            request.setAttribute("vista",      vista);
            request.setAttribute("dataInizio", dataInizio);
            request.setAttribute("dataFine",   dataFine);
            request.setAttribute("utenteId",   utenteIdStr);
            request.getRequestDispatcher("/WEB-INF/view/admin/Ordini.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Errore nel recupero degli ordini", e);
        }
    }
}