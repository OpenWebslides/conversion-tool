/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import objects.PPT;
import objects.Slide;
import org.apache.commons.io.FileUtils;


public class TemplateWriter extends Writer {
    private final Indentation writer;
    private String course;
    private String chapter;
    
    /**
     *
     * @param writer
     * @param course
     * @param chapter
     */
    public TemplateWriter(Indentation writer, String course, String chapter){
        this.writer = writer;
        this.course = course != null ? course : "Opleidingsonderdeel";
        this.chapter = chapter != null ? chapter : "Hoofdstuk";
    }
    
    /**
     *
     * @param out
     * @param ppt
     * @throws IOException
     */
    @Override
    public void write(BufferedWriter out, PPT ppt) throws IOException{
        //header
        //out.write(HEADER);
        out.write("<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "\t<title>Introduction</title>\n" +
            "\t<meta charset=\"utf-8\">\n" +
            "\t<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n" +
            "\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1\">\n" +
            "\t<link rel=\"stylesheet\" href=\"_shared/styles/screen-16x10.css\">\n" +
            "</head>\n" +
            "<body class=\"shower list\">\n" +
            "\t<header class=\"caption\">\n" +
            "\t\t<h1>\n" +
            "\t\t\t<a class=\"series\" href=\"#title\">" + course + "</a><br>\n" +
            "\t\t\t<a class=\"module\" href=\"#title\">" + chapter + "</a>\n" +
            "\t\t</h1>\n" +
            "\t\t<p>\n" +
            "\t\t</p>\n" +
            "\t</header>\n\n");
        
        //slides
        for(Slide slide : ppt.getSlides()){
            out.newLine();
            out.write(writeSlide(slide));
        }
        
        //footer
        out.write("\n\n\t<footer>\n" +
            "\t</footer>\n" +
            "\t<script src=\"_shared/scripts/shower.min.js\"></script>\n" +
            "\t<script src=\"_shared/scripts/enhancements.js\"></script>\n" +
            "\t<script>ga=function(){ga.q.push(arguments)};ga.q=[['create','UA-6142365-12','auto'],['require','autotrack'],['send','pageview']];ga.l=1*new Date</script>\n" +
            "\t<script async src=\"_shared/scripts/autotrack.js\"></script>\n" +
            "</body>\n" +
            "</html>");
    }
    
    /**
     *
     * @param slide
     * @return
     */
    @Override
    public String writeSlide(Slide slide) {
        return writer.writeSlide(slide, 1);
    }
    
    
    /**
     * Creates a copy of the needed files to use the template into the targetDir.
     * @param targetDir The directory where the files should be copied to.
     * @throws IOException 
     */
    public void copySharedFolder(String targetDir) throws IOException{
        File source = new File("Template/_shared");
        File target = new File(targetDir+"/_shared");
        FileUtils.copyDirectory(source, target);
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
    
    
}
