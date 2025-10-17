package TP5;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

@WebServlet(urlPatterns = {"/qrcode"})
public class QrCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code");
        String libelle = req.getParameter("libelle");
        String type = req.getParameter("type");

        // message de bienvenue si pas de param
        if ((code == null || code.isBlank()) &&
            (libelle == null || libelle.isBlank())) {
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("Bienvenue sur code barre gen 4.2(/qrcode).\n");
            return;
        }

        
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String code = trim(req.getParameter("code"));
        String libelle = trim(req.getParameter("libelle"));
        String type = (req.getParameter("type") == null) ? "datamatrix" : req.getParameter("type").toLowerCase();

        if (code == null || libelle == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println("<h3>parametres manquants</h3><p>fournr <b>code</b> et <b>libelle</b>.</p>");
            return;
        }

        String message = "code:" + code + " :: libelle:" + libelle;

       
        Object bean;
        switch (type) {
            case "code128":
                bean = new Code128Bean();
                break;
            case "datamatrix":
            default:
                bean = new DataMatrixBean();
                break;
        }

        resp.setContentType("image/png");
        
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                resp.getOutputStream(),
                "image/png",
                800,
                BufferedImage.TYPE_BYTE_BINARY,
                false,  
                0      
        );

        if (bean instanceof DataMatrixBean) {
            ((DataMatrixBean) bean).generateBarcode(canvas, message);
        } else if (bean instanceof Code128Bean) {
            ((Code128Bean) bean).generateBarcode(canvas, message);
        }

        canvas.finish();
    }

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}
