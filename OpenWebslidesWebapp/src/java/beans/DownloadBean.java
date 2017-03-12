/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.omnifaces.util.Faces;

/**
 *
 * @author Gertjan
 */
@ManagedBean
@SessionScoped
public class DownloadBean {

    /**
     * Creates a new instance of DownloadBean
     */
    public DownloadBean() {
        
    }

    public void download() throws IOException {
        File file = new File("C:\\Temp\\fto.jpg");
        Faces.sendFile(file, true);

    }

}
