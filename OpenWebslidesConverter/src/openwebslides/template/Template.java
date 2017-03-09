/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import objects.PPT;
import objects.Slide;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Jonas
 */
public class Template {
    private PPT ppt;
    private String course;
    private String chapter;
    
    public Template(PPT ppt, String course, String chapter){
        this.course = course != null ? course : "Opleidingsonderdeel";
        this.chapter = chapter != null ? chapter : "Hoofdstuk";
        this.ppt = ppt;
    }
    
    public void print(BufferedWriter out) throws IOException{
        out.write(HEADER);
        //out.write("\t"+DUMMYSLIDES);
        
        for(Slide s : ppt.getSlides()){
            out.write(s.toHtml(1));
        }
        
        out.write(FOOTER);
    }
    
    public void copySharedFolder(String targetDir) throws IOException{
        File source = new File("Template/_shared");
        File target = new File(targetDir+"/_shared");
        FileUtils.copyDirectory(source, target);
    }
    
    private final static String DUMMYSLIDES = "<div class=\"slide\" id=\"testid\"></div>";
    
    private final static String HEADER = "<!DOCTYPE html>\n" +
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
        "\t\t\t<a class=\"series\" href=\"#title\">Opleidingsonderdeel</a><br>\n" +
        "\t\t\t<a class=\"module\" href=\"#title\">Hoofdstuk</a>\n" +
        "\t\t</h1>\n" +
        "\t\t<p>\n" +
        "\t\t</p>\n" +
        "\t</header>\n\n";
    
    private final static String FOOTER = "\n\n\t<footer>\n" +
        "\t\t<p class=\"license\">\n" +
        "\t\t\t<a rel=\"license\" href=\"http://creativecommons.org/licenses/by/4.0/\"><img alt=\"Creative Commons License\" src=\"_shared/images/cc-by-small.svg\" /></a>\n" +
        "\t\t\tExcept where otherwise noted, the content of these slides is licensed under a <a rel=\"license\" href=\"http://creativecommons.org/licenses/by/4.0/\">Creative Commons Attribution 4.0 International License</a>.\n" +
        "\t\t</p>\n" +
        "\t</footer>\n" +
        "\t<script src=\"_shared/scripts/shower.min.js\"></script>\n" +
        "\t<script src=\"_shared/scripts/enhancements.js\"></script>\n" +
        "\t<script>ga=function(){ga.q.push(arguments)};ga.q=[['create','UA-6142365-12','auto'],['require','autotrack'],['send','pageview']];ga.l=1*new Date</script>\n" +
        "\t<script async src=\"_shared/scripts/autotrack.js\"></script>\n" +
        "</body>\n" +
        "</html>";
}
