package logic;

import java.util.ArrayList;
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
        formatTitle(ppt);
        UnorderedListLogic ull = new UnorderedListLogic();
        ull.formatList(ppt, "([^\\u0000-\\u00ff‘’]|-)", false);
        OrderedListLogic sll = new OrderedListLogic();
        sll.formatList(ppt, "^(\\d+[\\.)](?!\\d))", true);
        sll.formatList(ppt, "^([A-Z][\\.)])", true);
        sll.formatList(ppt, "^([a-z][\\.)])", true);
        groupFontdecoration(ppt);
    }

    private void groupTextparts(Text text) {
        List<Integer> toRemove = new ArrayList<>();
        int i = 0;
        while (i < text.getTextparts().size() - 1) {
            Textpart tp1 = text.getTextparts().get(i);
            Textpart tp2 = text.getTextparts().get(i + 1);
            while (tp1.getType().equals(tp2.getType()) && i < text.getTextparts().size() - 1) {
                i++;
                tp1.setContent(tp1.getContent() + tp2.getContent());
                if (i < text.getTextparts().size() - 1) {
                    tp2 = text.getTextparts().get(i + 1);
                }
                toRemove.add(i);
            }
            i++;
        }
        for (int j = toRemove.size() - 1; j >= 0; j--) {
            text.getTextparts().remove((int) toRemove.get(j));
        }
    }

    private void groupTextparts(PPTList list) {
        for (PPTObject obj : list.getBullets()) {
            groupTextparts(obj);
        }
    }

    private void groupTextparts(PPTObject obj) {
        if (obj instanceof Text) {
            groupTextparts((Text) obj);
        } else if (obj instanceof PPTList) {
            groupTextparts((PPTList) obj);
        }
    }

    private void groupFontdecoration(PPT ppt) {
        for (Slide slide : ppt.getSlides()) {
            for (PPTObject obj : slide.getPptObjects()) {
                groupTextparts(obj);
            }
        }
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
            List<Integer> toRemove = new ArrayList<>();
            List<Title> toAdd = new ArrayList<>();
            for (int j = 0; j < slide.getPptObjects().size(); j++) {
                if (!hasTitle && slide.getPptObjects().get(j) instanceof Text && !((Text) slide.getPptObjects().get(j)).getTextparts().isEmpty()) {
                    int firstSize = ((Text) slide.getPptObjects().get(j)).getTextparts().get(0).getSize();
                    if (firstSize != 0 && ((firstSize < 1000 && firstSize >= maxSize - 5/*(minSize + (maxSize - minSize) * 4 / 5)*/) || (firstSize >= 1000 && (firstSize > (minSize + (maxSize - minSize) / 3) || firstSize >= 4000)))) {
                        Title title = new Title();
                        title.getTextparts().addAll(((Text) slide.getPptObjects().get(j)).getTextparts());
                        toRemove.add(j);
                        toAdd.add(title);
                    }
                }
            }

            for (int j = toRemove.size() - 1; j >= 0; j--) {
                slide.getPptObjects().remove((int) toRemove.get(j));
            }

            for (int j = toAdd.size() - 1; j >= 0; j--) {
                slide.getPptObjects().add(0, toAdd.get(j));
            }
        }
    }
}
