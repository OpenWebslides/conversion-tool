/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

/**
 *
 * @author Karel
 */
public class PPTXMLConstants {

    //Global elements
    public static final String FRAGMENT = "xml-fragment";
    public static final String IDELEMENT = "p:cNvPr";
    public static final String GFRAME = "p:graphicFrame";

    //Global values
    public static final String VALUE = "val";
    public static final String ID = "id";

    //Text elements
    public static final String TEXTBODY = "p:txBody";
    public static final String TEXT = "a:p";
    public static final String ORDEREDLIST = "a:buAutoNum";
    public static final String UNORDEREDLIST = "a:buChar";
    public static final String TEXTLEVEL = "a:pPr";
    public static final String TEXTPART = "a:r";
    public static final String TEXTCONTENT = "a:t";
    public static final String TEXTDETAILS = "a:rPr";
    public static final String TEXTFONT = "a:latin";
    public static final String COLORDETAIL = "a:srgbClr";
    public static final String TEXTTYPE = "p:ph";
    public static final String NOLIST = "a:buNone";

    //Text attributes
    public static final String LEVEL = "lvl";
    public static final String BOLD = "b";
    public static final String ITALIC = "i";
    public static final String STRIKE = "strike";
    public static final String UNDERLINE = "u";
    public static final String SIZE = "sz";
    public static final String CHARACTERSPACING = "spc";
    public static final String TYPEFACE = "typeface";
    public static final String TEXTTYPEATTR = "type";
    public static final String SOLOWORD = "err";
    
    
    //Text title values
    public static final String TEXT_TITLE_HEADER = "ctrTitle";
    public static final String TEXT_SUBTITLE_HEADER = "subTitle";
    public static final String TEXT_TITLE = "title";
    
    //Text default sizes
    public static final int TEXT_TITLE_HEADER_SIZE = 6000; //60 font size
    public static final int TEXT_SUBTITLE_HEADER_SIZE = 2400; //24
    public static final int TEXT_TITLE_SIZE = 4400; //44
    public static final int TEXT_DEFAULT_LARGE_SIZE = 2800; //28
    public static final int TEXT_DEFAULT_SMALL_SIZE = 1800; //18

    //Image & Video elements
    public static final String MEDIABODY = "p:pic";
    public static final String MEDIASIZE = "a:ext";
    public static final String MEDIALOCATION = "a:off";
    public static final String MEDIABOX = "a:xfrm";
    public static final String MEDIADETAILS = "p:cNvPr";
    public static final String VIDEOBODY = "a:videoFile";

    //Image & Video attributes
    public static final String MEDIAWIDTH = "cx";
    public static final String MEDIAHEIGHT = "cy";
    public static final String MEDIALOCX = "x";
    public static final String MEDIALOCY = "y";

    //Chart elements
    public static final String CHARTBODY = "p:xfrm";
    
    //Table elements
    public static final String TABLE = "a:tbl";

}
