package control;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cart")
public class Cart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // HttpSession session = request.getSession(false);
        request.getRequestDispatcher("/WEB-INF/view/Cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        String action = request.getParameter("action");
        String cartJson = (String) session.getAttribute("cart");
        if (cartJson == null) cartJson = ""; 
        StringBuilder cart = new StringBuilder();

        if (cartJson != null && !cartJson.isEmpty() && !cartJson.equals("[]")) {
            cart.append(cartJson.substring(0, cartJson.length() - 1)); // remove closing ]
            if (!cartJson.equals("[")) cart.append(","); // add comma if not empty
        } else {
            cart.append("[");
        }

        if ("add".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String nome = request.getParameter("nome");
            int durata = Integer.parseInt(request.getParameter("durata"));
            String regista = request.getParameter("regista");

            cart.append("{\"id\":").append(id).append(",")
                .append("\"nome\":\"" ).append(escapeJson(nome)).append("\",")
                .append("\"durata\":").append(durata).append(",")
                .append("\"regista\":\"" ).append(escapeJson(regista)).append("\"}");

 
        } else if ("remove".equals(action)) {
	        int id = Integer.parseInt(request.getParameter("id"));
	        if (cartJson != null && !cartJson.isEmpty()) {
	            String[] items = cartJson.substring(1, cartJson.length() - 1).split("\\},");
	            StringBuilder newCart = new StringBuilder("[");
	            for (String item : items) {
	                String normalized = item.endsWith("}") ? item : item + "}";
	                if (!normalized.contains("\"id\":" + id)) { // ← rimosso lo spazio
	                    if (newCart.length() > 1) newCart.append(",");
	                    newCart.append(normalized);
	                }
	            }
	        newCart.append("]");
	        cart = newCart;
        }
    }

        if (!cart.toString().endsWith("]")) {
            cart.append("]");
        }
        session.setAttribute("cart", cart.toString());
        out.print("{\"success\":true,\"cart\":" + cart.toString() + "}");
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
