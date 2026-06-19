package control;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.IndirizzoDAO;
import DAO.OrdineDAO;
import DAO.OrdineDVDDAO; // <--- Nuovo import necessario
import model.DVDInCart;
import model.Indirizzo;
import model.Ordine;
import model.OrdineDVD;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/checkout")
public class Checkout extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
    private OrdineDAO ordineDAO = new OrdineDAO();
    private OrdineDVDDAO ordineDVDDAO = new OrdineDVDDAO(); // <--- Istanza del nuovo DAO delle righe

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        request.setAttribute("session", session != null ? session : null);
        request.getRequestDispatcher("/WEB-INF/view/Checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String seqId = UUID.randomUUID().toString();
        int userId = (int) session.getAttribute("utenteId");
        
        @SuppressWarnings("unchecked")
        ArrayList<DVDInCart> cartList = session != null ? (ArrayList<DVDInCart>) session.getAttribute("cart") : new ArrayList<>();
        
        String nome      = request.getParameter("nome");
        String cognome   = request.getParameter("cognome");
        String indirizzo = request.getParameter("indirizzo");
        String citta     = request.getParameter("citta");
        String paese     = request.getParameter("paese");
        String cap       = request.getParameter("cap");

        try {
            // 1. Inserimento della testata dell'ordine (Tabella ORDINE)
            Ordine ordine = new Ordine();
            ordine.setSeqId(seqId);
            ordine.setUtenteId(userId);
            // NOTA: Se il tuo model Ordine non ha un costruttore vuoto, usa quello a 4 parametri visto nel DAO:
            // Ordine ordine = new Ordine(0, seqId, userId, null);

            // Salva l'ordine e recupera l'ID autoincrementale generato dal DB
            int ordineId = ordineDAO.insert(ordine);

            // 2. Preparazione e inserimento in batch delle righe (Tabella ORDINE_DVD)
            if (cartList != null && !cartList.isEmpty()) {
                List<OrdineDVD> righeOrdine = new ArrayList<>();
                
                for (DVDInCart dvd : cartList) {
                    OrdineDVD riga = new OrdineDVD();
                    riga.setOrdineId(ordineId);
                    riga.setDvdId(dvd.getId());
                    riga.setQuantita(dvd.getQuantity());
                    riga.setPrezzoUnitario((float) dvd.getPrezzo());
                    // NOTA: Se il tuo model OrdineDVD usa il costruttore a 4 parametri visto nel DAO:
                    // OrdineDVD riga = new OrdineDVD(ordineId, dvd.getId(), dvd.getQuantity(), (float) dvd.getPrezzo());
                    
                    righeOrdine.add(riga);
                }
                
                // Esegue l'insert batch sul DB tramite il nuovo DAO
                ordineDVDDAO.insertAll(righeOrdine);
            }

            // 3. Inserimento dell'indirizzo di spedizione legato al seqId dell'ordine
            indirizzoDAO.insert(new Indirizzo(0, seqId, userId, nome, cognome, indirizzo, citta, cap, paese));
            
            // Svuota il carrello a transazione completata con successo
            if (cartList != null) {
                cartList.clear();
                session.setAttribute("cart", cartList);
            }
            
            response.sendRedirect(request.getContextPath() + "/home");

        } catch (SQLException e) {
            throw new ServletException("Errore DB durante la procedura di checkout: " + e.getMessage(), e);
        }
    }
}