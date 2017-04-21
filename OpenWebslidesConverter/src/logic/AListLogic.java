package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    /**
     *
     * @param ppt
     * @param regex
     * @param multiLevels
     *
     *
     */
    public void formatList(PPT ppt, String regex, boolean multiLevels) {
        for (Slide slide : ppt.getSlides()) {
            List<PPTObject> pptobjects = slide.getPptObjects();
            Map<Integer, Integer> numbers = getLevelsPerText(slide);
            if ((multiLevels && numbers.size() > 1 && new HashSet<>(numbers.values()).size() > 1) || (!multiLevels && numbers.size() > 1)) {
                List<PPTList> lists = new ArrayList<>();
                lists.add(new PPTList());
                Object[] keys = numbers.keySet().toArray();
                int currentLevel = numbers.get((int) keys[0]);
                for (int i = 0; i < keys.length; i++) {
                    Text text = removeBulletSign((Text) pptobjects.get((int) keys[i]), regex);
                    fillList(numbers.get((int) keys[i]) - currentLevel, text, lists);
                    consecutiveText(pptobjects, text, i, keys);
                    currentLevel = numbers.get((int) keys[i]);
                }
                //Remove ppt textobjects that are being used in list
                for (int i = pptobjects.size() - 1; i > (int) keys[0]; i--) {
                    pptobjects.remove(i);
                }
                pptobjects.set((int) keys[0], lists.get(0));
            }
        }
    }

    //Adds the Text object to the corresponding PPTList object
    private void fillList(int levelDifference, Text text, List<PPTList> lists) {
        switch (levelDifference) {
            case 0:
                lists.get(lists.size() - 1).addPPTObject(text);
                break;
            case 1:
                PPTList list = new PPTList();
                list.addPPTObject(text);
                lists.add(list);
                lists.get(lists.size() - 2).addPPTObject(list);
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
        //Add consecutive text parts
        int nextLineNr = ((int) keys[i]) + 1;
        while ((i < keys.length - 1 && nextLineNr < (int) keys[i + 1]) || (i == keys.length - 1 && nextLineNr < pptobjects.size())) {
            if (pptobjects.get(nextLineNr) instanceof Text) {
                text.getTextparts().addAll(((Text) pptobjects.get(nextLineNr)).getTextparts());
            }
            nextLineNr++;
        }
    }

    //Removes regex from the beginning of the text object and returns the text object
    private Text removeBulletSign(Text text, String regex) {
        String tekst = text.getTextparts().get(0).getContent();
        tekst = tekst.replaceAll(regex, "");
        text.getTextparts().get(0).setContent(tekst);
        return text;
    }

    protected int getLevel(Text text) {
        int lastLevel = 0;
        Pattern pattern = Pattern.compile("Lijst level (\\d+) -(\\d+)");
        if (text.getLevel() != null) {
            Matcher matcher = pattern.matcher(text.getLevel());
            if (matcher.find()) {
                lastLevel = Integer.parseInt(matcher.group(1));
            }
        }
        return lastLevel;
    }

    abstract Map<Integer, Integer> getLevelsPerText(Slide slide);

}
