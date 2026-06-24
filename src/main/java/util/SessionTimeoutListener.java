package util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import model.DVDInCart;

import java.sql.SQLException;
import java.util.ArrayList;

import DAO.DVDDAO;


@WebListener
public class SessionTimeoutListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        // Questo metodo si attiva AUTOMATICAMENTE quando la sessione scade o viene invalidata
        ArrayList<DVDInCart> cartList = (ArrayList<DVDInCart>) session.getAttribute("cart");
        if (cartList == null) cartList = new ArrayList<>();

        DVDDAO dao = new DVDDAO();
        try {
        	for (DVDInCart e : cartList) {
        		dao.updateQuantita(e.getId(), e.getQuantitaSelezionata());
        	}
        	
        }catch (SQLException e) {
        	e.printStackTrace();
        }
    }
}