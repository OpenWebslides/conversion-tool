package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import objects.PPT;
import objects.PPTList;
import objects.PPTObject;
import objects.Slide;
import objects.Text;

/**
 *
 * @author Joann
 */
public abstract class AListLogic {

    public void formatList(PPT ppt, String regex, boolean ordered) {
        for (Slide slide : ppt.getSlides()) {
            List<PPTObject> pptobjects = slide.getPptObjects();
            Map<Integer, Integer> numbers = getLevelsPerText(slide, regex); //Index in pptObjects, level

            while (numbers.size() >= 1) {

                List<PPTList> lists = new ArrayList<>();
                PPTList pptList = new PPTList();
                pptList.setOrdered(ordered);
                lists.add(pptList);
                Object[] keys = numbers.keySet().toArray();
                int currentLevel = numbers.get((int) keys[0]);
                for (int i = 0; i < keys.length; i++) {
                    Text text = removeBulletSign((Text) pptobjects.get((int) keys[i]), regex);
                    fillList(numbers.get((int) keys[i]) - currentLevel, text, lists, ordered);
                    consecutiveText(pptobjects, text, i, keys);
                    currentLevel = numbers.get((int) keys[i]);
                }
                //Remove ppt textobjects that are being used in list
                List<Integer> toRemove = new ArrayList<>();
                for (int i = (int) keys[0] + 1; i <= (int) keys[keys.length - 1]; i++) {
                    toRemove.add(i);
                }
                int i = (int) keys[keys.length - 1] + 1;

                double currentX = ((Text) pptobjects.get((int) keys[keys.length - 1])).getTextparts().get(0).getXPosition();  //text.getTextparts().get(0).getXPosition();
                while (i < pptobjects.size() && pptobjects.get(i) instanceof Text && ((Text) pptobjects.get(i)).getTextparts().get(0).getXPosition() >= currentX) {
                    toRemove.add(i);
                    i++;
                }
                for (i = toRemove.size() - 1; i >= 0; i--) {
                    pptobjects.remove((int) toRemove.get(i));
                }
                pptobjects.set((int) keys[0], lists.get(0));
                numbers = getLevelsPerText(slide, regex); //Index in pptObjects, level
            }
        }
    }

    //Adds the Text object to the corresponding PPTList object
    private void fillList(int levelDifference, Text text, List<PPTList> lists, boolean ordered) {
        switch (levelDifference) {
            case 0:
                lists.get(lists.size() - 1).addPPTObject(text);
                break;
            case 1:
                PPTList list = new PPTList();
                list.setOrdered(ordered);
                list.addPPTObject(text);
                lists.get(lists.size() - 1).addPPTObject(list);
                lists.add(list);
                break;
            default:
                while (levelDifference != 0) {
                    lists.remove(lists.size() - 1);
                    levelDifference++;
                }
                lists.get(lists.size() - 1).addPPTObject(text);
                break;
        }
    }

    //Adds multiple line text to the same text object, so there will be only one bullet
    private void consecutiveText(List<PPTObject> pptobjects, Text text, int i, Object[] keys) {
        int nextLineNr = ((int) keys[i]) + 1;
        double currentX = text.getTextparts().get(0).getXPosition();
        while ((i < keys.length - 1 && nextLineNr < (int) keys[i + 1] && pptobjects.get(nextLineNr) instanceof Text) || (i == keys.length - 1 && nextLineNr < pptobjects.size() && pptobjects.get(nextLineNr) instanceof Text && ((Text) pptobjects.get(nextLineNr)).getTextparts().get(0).getXPosition() >= currentX)) {
            text.getTextparts().addAll(((Text) pptobjects.get(nextLineNr)).getTextparts());
            nextLineNr++;
        }
    }

    //Removes regex from the beginning of the text object and returns the text object
    private Text removeBulletSign(Text text, String regex) {
        String tekst = text.getTextparts().get(0).getContent();
        tekst = tekst.replaceFirst(regex + "\\s*", "");
        text.getTextparts().get(0).setContent(tekst);
        return text;
    }

    //Reads all text objects in slide and puts the index where a bullet starts with the level number in a map
    abstract Map<Integer, Integer> getLevelsPerText(Slide slide, String regex);

}
