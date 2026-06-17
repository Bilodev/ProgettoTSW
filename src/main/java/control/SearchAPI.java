package control;

import DAO.DVDDAO;
import model.DVD;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/search")
public class SearchAPI extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DVDDAO dvdDAO = new DVDDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String query = request.getParameter("q");
        try {
            StringBuilder json = new StringBuilder("[");
            List<DVD> results = query != null && !query.isBlank() ? dvdDAO.searchByName(query) : List.of();
            
            for (int i = 0; i < results.size(); i++) {
                DVD dvd = results.get(i);
                if (i > 0) json.append(",");
                json.append("{");
                json.append("\"id\":").append(dvd.getId()).append(",");
                json.append("\"nome\":\"" ).append(escapeJson(dvd.getNome())).append("\",");
                json.append("\"durata\":").append(dvd.getDurata()).append(",");
                json.append("\"regista\":\"" ).append(escapeJson(dvd.getRegista())).append("\"");
                json.append("}");
            }
            json.append("]");
            out.print(json.toString());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("[]");
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
