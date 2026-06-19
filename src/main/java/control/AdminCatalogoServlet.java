package control;

import DAO.DVDDAO;
import model.DVD;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/catalogo")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB — soglia prima di scrivere su disco
    maxFileSize      = 10 * 1024 * 1024,  // 10 MB max per file
    maxRequestSize   = 15 * 1024 * 1024   // 15 MB max per intera richiesta
)
public class AdminCatalogoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final DVDDAO dvdDAO = new DVDDAO();

    // Percorso relativo alla webapp root dove salvare le immagini
    private static final String IMG_DIR = "static";

    // ------------------------------------------------------------------ GET --
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                request.setAttribute("mode", "add");
                request.getRequestDispatcher("/WEB-INF/view/admin/Catalogo.jsp")
                       .forward(request, response);
                return;
            }
            if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                DVD dvd = dvdDAO.findById(id);
                request.setAttribute("mode", "edit");
                request.setAttribute("dvd", dvd);
                request.getRequestDispatcher("/WEB-INF/view/admin/Catalogo.jsp")
                       .forward(request, response);
                return;
            }
            List<DVD> dvds = dvdDAO.findAll();
            request.setAttribute("mode", "list");
            request.setAttribute("dvds", dvds);
            request.getRequestDispatcher("/WEB-INF/view/admin/Catalogo.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // ----------------------------------------------------------------- POST --
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                DVD dvd = buildDVDFromRequest(request);
                int newId = dvdDAO.insert(dvd);   // insert deve restituire l'ID generato
                saveImage(request, newId);

            } else if ("update".equals(action)) {
                DVD dvd = buildDVDFromRequest(request);
                dvd.setId(Integer.parseInt(request.getParameter("id")));
                dvdDAO.update(dvd);
                // Sovrascrive l'immagine solo se ne è stata caricata una nuova
                Part imgPart = request.getPart("immagine");
                if (imgPart != null && imgPart.getSize() > 0) {
                    saveImage(request, dvd.getId());
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dvdDAO.delete(id);

            } else if ("reinsert".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                dvdDAO.reinsert(id);
            }

            response.sendRedirect(request.getContextPath() + "/admin/catalogo?action=list");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // --------------------------------------------------------- helper: form --
    private DVD buildDVDFromRequest(HttpServletRequest request) {
        String nome    = request.getParameter("nome");
        int    durata  = Integer.parseInt(request.getParameter("durata"));
        String regista = request.getParameter("regista");
        float  prezzo  = Float.parseFloat(request.getParameter("prezzo"));
        return new DVD(0, nome, durata, regista, prezzo, true);
    }

    // ------------------------------------------------------ helper: immagine --
    /**
     * Salva il file caricato nel campo "immagine" nella cartella
     * <webapp>/static/img/dvd/<id>.<ext>
     * Se non viene caricato nessun file, il metodo non fa nulla.
     */
    private void saveImage(HttpServletRequest request, int dvdId)
            throws IOException, ServletException {

        Part part = request.getPart("immagine");
        if (part == null || part.getSize() == 0) return;

        // Estrae l'estensione dal nome originale del file
        String originalName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            ext = originalName.substring(dot); // es. ".jpg"
        }

        // Cartella di destinazione: <webapp_root>/static/
        String webappRoot = getServletContext().getRealPath("/");
        File   imgFolder  = new File(webappRoot, IMG_DIR);
        if (!imgFolder.exists()) {
            imgFolder.mkdirs();
        }

        // File finale: es. static/img/dvd/42.jpg
        File destFile = new File(imgFolder, dvdId + ext);

        try (InputStream in = part.getInputStream()) {
            Files.copy(in, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}