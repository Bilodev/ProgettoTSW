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
import java.util.Collections;
import java.util.List;

@WebServlet("/search")
public class SearchDVD extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DVDDAO dvdDAO = new DVDDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("q");
        List<DVD> results;
        if (query == null || query.isBlank()) {
            results = Collections.emptyList();
        } else {
            try {
                results = dvdDAO.searchByName(query);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        request.setAttribute("results", results);
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/view/Home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
