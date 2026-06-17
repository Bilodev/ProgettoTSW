package control;

import DAO.DVDDAO;
import model.DVD;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DVDDAO dvdDAO = new DVDDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                request.setAttribute("mode", "add");
                request.getRequestDispatcher("/WEB-INF/view/admin/Admin.jsp").forward(request, response);
                return;
            }

            if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                DVD dvd = dvdDAO.findById(id);
                request.setAttribute("mode", "edit");
                request.setAttribute("dvd", dvd);
                request.getRequestDispatcher("/WEB-INF/view/admin/Admin.jsp").forward(request, response);
                return;
            }

            List<DVD> dvds = dvdDAO.findAll();
            request.setAttribute("mode", "list");
            request.setAttribute("dvds", dvds);
            request.getRequestDispatcher("/WEB-INF/view/admin/Admin.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                DVD dvd = buildDVDFromRequest(request);
                dvdDAO.insert(dvd);
            } else if ("update".equals(action)) {
                DVD dvd = buildDVDFromRequest(request);
                dvd.setId(Integer.parseInt(request.getParameter("id")));
                dvdDAO.update(dvd);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dvdDAO.delete(id);
            }else if ("reinsert".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dvdDAO.reinsert(id);
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?action=list");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private DVD buildDVDFromRequest(HttpServletRequest request) {
        String nome = request.getParameter("nome");
        int durata = Integer.parseInt(request.getParameter("durata"));
        String regista = request.getParameter("regista");
        return new DVD(0, nome, durata, regista, true);
    }
}
