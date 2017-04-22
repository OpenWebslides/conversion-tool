package logic;

import java.util.List;
import objects.*;

/**
 *
 * @author Joann
 */
public class Logic implements ILogic {

    /**
     * Searches for lists and titles in ppt and converts them
     *
     * @param ppt
     */
    @Override
    public void format(PPT ppt) {
        //Ordered list checks for multiple levels first in case there is a list in simple form
        //Needs indentation in Text objects (level)
        /*ComplexListLogic cll = new ComplexListLogic();
        cll.formatList(ppt, "(\\d+\\.(\\d+\\.)*(?!\\d))\\s+", true);
        SimpleListLogic sll = new SimpleListLogic();
        sll.formatList(ppt, "(\\d+[\\.)](?!\\d))\\s+", false);
        UnorderedListLogic ull = new UnorderedListLogic();
        ull.formatList(ppt, "([^a-zA-z0-9]+)\\s+", false);*/
        //TODO: ordered lists with a,b,c
        //formatTitle(ppt);
    }

    private void formatTitle(PPT ppt) {

        int minSize = 100000;
        int maxSize = 0;

        for (int i = 1; i < ppt.getSlides().size(); i++) {
            List<PPTObject> pptobjects = ppt.getSlides().get(i).getPptObjects();
            for (PPTObject obj : pptobjects) {
                if (obj instanceof Text) {
                    for (Textpart part : ((Text) obj).getTextparts()) {
                        if (part.getSize() > maxSize) {
                            maxSize = part.getSize();
                        } else if (part.getSize() < minSize && part.getSize() != 0) {
                            minSize = part.getSize();
                        }
                    }
                }
            }
        }

        for (Slide slide : ppt.getSlides()) {
            boolean hasTitle = false;
            List<PPTObject> pptobjects = slide.getPptObjects();
            int i = 0;
            while (i < pptobjects.size() && !hasTitle) {
                if (pptobjects.get(i) instanceof Title) {
                    hasTitle = true;
                }
                i++;
            }
            for (int j = 0; j < slide.getPptObjects().size(); j++) {
                if (!hasTitle && slide.getPptObjects().get(j) instanceof Text && !((Text) slide.getPptObjects().get(j)).getTextparts().isEmpty()) {
                    int firstSize = ((Text) slide.getPptObjects().get(j)).getTextparts().get(0).getSize();
                    if ((firstSize < 1000 && firstSize > (minSize + (maxSize - minSize) * 4 / 5)) || (firstSize >= 1000 && (firstSize > (minSize + (maxSize - minSize) / 3) || firstSize >= 4000))) {
                        Title title = new Title();
                        title.getTextparts().addAll(((Text) slide.getPptObjects().get(j)).getTextparts());
                        slide.getPptObjects().set(j, title);
                    }
                }
            }
        }
    }

}
