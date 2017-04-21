/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joann
 */
@WebServlet
public class LanguageServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String language;    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);

        language = request.getParameter("language");
        if (language == null) {
            Locale.setDefault(new Locale("en"));
            language = request.getLocale().getLanguage();
        }

        ResourceBundle bundle = ResourceBundle.getBundle("I18n.ResourceBundle", new Locale(language));
        Enumeration bundleKeys = bundle.getKeys();

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print("{");

            if (bundleKeys.hasMoreElements()) {
                String key = (String) bundleKeys.nextElement();
                out.print("\"" + key + "\"");
                out.print(":");
                String value = "\"" + (String) bundle.getString(key) + "\"";
                out.print(value);
            }

            while (bundleKeys.hasMoreElements()) {
                out.print(",");
                String key = (String) bundleKeys.nextElement();
                out.print("\"" + key + "\"");
                out.print(":");
                String value = "\"" + bundle.getString(key) + "\"";
                out.print(value);
            }
            out.print("}");
        }

    }

   
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
