package control;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import DAO.DVDDAO;
import model.DVD;
import model.DVDInCart;

@WebServlet("/cart")
public class Cart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/Cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null) {
            out.print("{\"success\":false, \"error\":\"Sessione non valida\"}");
            return;
        }

        String action = request.getParameter("action");
        ArrayList<DVDInCart> cartList = (ArrayList<DVDInCart>) session.getAttribute("cart");
        if (cartList == null) cartList = new ArrayList<>();

        DVDDAO dao = new DVDDAO();

        try {
            if ("add".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));

                // Leggi la quantità attuale dal DB
                DVD dvd = dao.findById(id);
                if (dvd == null || !dvd.isInCatalogo()) {
                    out.print("{\"success\":false, \"error\":\"DVD non trovato o non disponibile\"}");
                    return;
                }
                if (dvd.getQuantita() <= 0) {
                    out.print("{\"success\":false, \"error\":\"DVD esaurita\"}");
                    return;
                }

                // Decrementa la quantità nel DB
                dvd.setQuantita(dvd.getQuantita() - 1);
                dao.updateQuantita(dvd.getId(), -1);

                // Aggiorna o aggiungi nel carrello
                boolean found = false;
                for (DVDInCart e : cartList) {
                    if (e.getId() == id) {
                        e.setQuantitaSelezionata(e.getQuantitaSelezionata() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cartList.add(new DVDInCart(
                        dvd.getId(), dvd.getNome(), dvd.getDurata(),
                        dvd.getRegista(), dvd.getPrezzo(), true, 1, 1
                    ));
                }

            } else if ("remove".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));

                // Trova la quantità nel carrello prima di rimuovere
                int qtInCart = 0;
                for (DVDInCart e : cartList) {
                    if (e.getId() == id) {
                        qtInCart = e.getQuantitaSelezionata();
                        break;
                    }
                }

                if (qtInCart > 0) {
                    // Reincrementa nel DB la quantità restituita
                    dao.updateQuantita(id, +qtInCart);
                }

                cartList.removeIf(e -> e.getId() == id);

            } else if ("updateQuantity".equals(action)) {
                int id  = Integer.parseInt(request.getParameter("id"));
                int qt  = Integer.parseInt(request.getParameter("qt")); // delta: +1 o -1

                if (qt > 0) {
                    // Vogliamo aumentare: controlla disponibilità nel DB
                    DVD dvd = dao.findById(id);
                    if (dvd == null || dvd.getQuantita() <= 0) {
                        out.print("{\"success\":false, \"error\":\"Quantità non disponibile\"}");
                        return;
                    }
                }

                boolean updated = false;
                for (DVDInCart e : cartList) {
                    if (e.getId() == id) {
                        int nuovaQt = e.getQuantitaSelezionata() + qt;
                        if (nuovaQt <= 0) {
                            // Quantità nel carrello azzereata: rimuovi e restituisci al DB
                            dao.updateQuantita(id, e.getQuantitaSelezionata()); // restituisce tutto
                            cartList.remove(e);
                        } else {
                            e.setQuantitaSelezionata(nuovaQt);
                            dao.updateQuantita(id, -qt); // qt>0 → decremento DB; qt<0 → incremento DB
                        }
                        updated = true;
                        break;
                    }
                }
                if (!updated) {
                    out.print("{\"success\":false, \"error\":\"Elemento non trovato nel carrello\"}");
                    return;
                }

            } else if ("emptyCart".equals(action)) {
                // Reincrementa nel DB la quantità di ogni DVD presente nel carrello
                for (DVDInCart e : cartList) {
                    dao.updateQuantita(e.getId(), e.getQuantitaSelezionata());
                }
                cartList.clear();
            }

            session.setAttribute("cart", cartList);
            out.print("{\"success\":true}");

        } catch (NumberFormatException e) {
            out.print("{\"success\":false, \"error\":\"Parametri non validi\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"error\":\"Errore database: " + e.getMessage() + "\"}");
        }
    }
}