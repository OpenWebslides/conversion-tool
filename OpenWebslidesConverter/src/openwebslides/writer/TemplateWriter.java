/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import objects.PPT;
import objects.Slide;

/**
 * A Writer that puts a HTML5 template around the Slides. 
 * @author Jonas
 */
public class TemplateWriter extends Writer {
    private final Indentation writer;
    private String course;
    private String chapter;
    
    /**
     * Constructor of the object.
     * @param writer The writer that implements the Indentation interface that creates the code in the body of the HTML page.
     * @param course The name of the course the presentation is part of.
     * @param chapter The name of the chapter the presentation is part of.
     */
    public TemplateWriter(Indentation writer, String course, String chapter){
        this.writer = writer;
        this.course = course != null ? course : "Opleidingsonderdeel";
        this.chapter = chapter != null ? chapter : "Hoofdstuk";
    }
    
    /**
     * Overrides the default implementation of the Writer class.
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
     * Implementation of the Writer class.
     * @param slide
     * @return
     */
    @Override
    public String writeSlide(Slide slide) {
        return writer.writeSlide(slide, 1);
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
