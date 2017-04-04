/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
 
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dhoogla
 */
@WebServlet(name = "FileDownloadServlet", urlPatterns = {"/download"})
public class FileDownloadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String WSSessionToken = request.getParameter("WSSessionToken");
            String fileName= request.getParameter("fileName");
            String path = System.getProperty("user.home")+File.separator+"tiwi"+File.separator+"download"+File.separator+WSSessionToken+File.separator+fileName+File.separator+"index.html";
            System.out.println("Looking for the right file to download, should be here:\n"+path);
            File toDownload = new File(path);
            FileInputStream iStream = new FileInputStream(toDownload);
            
            ServletContext ctx = getServletContext();            
            String mimeType = ctx.getMimeType(path);
            if(mimeType == null){
                mimeType = "application/octet-stream";
            }
            System.out.println("MIME Type file download: "+mimeType);
            
            response.setContentType(mimeType);
            response.setContentLength((int) toDownload.length());
            response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\"", fileName.substring(0,fileName.lastIndexOf('.'))+".html"));
            
            OutputStream oStream = response.getOutputStream();
            
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            
            while((bytesRead = iStream.read(buffer)) != -1){
                oStream.write(buffer,0,bytesRead);
            }
            iStream.close();
            oStream.close();
    }

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
        processRequest(request, response);
    }
  
   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
