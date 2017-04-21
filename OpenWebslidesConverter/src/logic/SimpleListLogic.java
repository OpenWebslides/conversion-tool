package logic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import objects.PPTObject;
import objects.Slide;
import objects.Text;

/**
 *
 * @author Joann
 */
public class SimpleListLogic extends AListLogic {

    /*@Override
    public Map<Integer, Integer> getLevelsPerText(Slide slide) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        Map<Integer, Integer> numberPerLevel = new HashMap<>();  //Saves last used bullet number per level
        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        int lastLevel = -1;
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty()) {
                Text text = (Text) object;
                Pattern pattern = Pattern.compile("(\\d+[\\.)](?!\\d))");
                Matcher matcher = pattern.matcher(text.getTextparts().get(0).getContent()&& !(((Text) object).getTextparts().get(0).getContent() == null));
                if (matcher.find()) {
                    int bulletNr = Integer.parseInt(matcher.group(1).replaceAll("\\.", "").replaceAll("\\)", ""));
                    if (numberPerLevel.containsKey(text.getLevel()) && bulletNr == numberPerLevel.get(text.getLevel()) + 1) {
                        numberPerLevel.put(text.getLevel(), bulletNr);
                        numbers.put(index_line, text.getLevel());
                        lastLevel = text.getLevel();
                        Object[] keys = numberPerLevel.keySet().toArray();
                        for (Object key : keys) {
                            if ((int) key > text.getLevel()) {
                                numberPerLevel.remove((int) key);
                            }
                        }
                    } else if (text.getLevel() > lastLevel && bulletNr == 1) {
                        numberPerLevel.put(text.getLevel(), bulletNr);
                        numbers.put(index_line, text.getLevel());
                        lastLevel = text.getLevel();
                    }
                }
            }
            index_line++;
        }
        return numbers;
    }*/
    @Override
    public Map<Integer, Integer> getLevelsPerText(Slide slide) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        Map<Integer, Integer> numberPerLevel = new HashMap<>();  //Saves last used bullet number per level
        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        int lastLevel = -1;
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty() && !(((Text) object).getTextparts().get(0).getContent() == null)) {
                Text text = (Text) object;
                Pattern pattern = Pattern.compile("(\\d+[\\.)](?!\\d))");
                Matcher matcher = pattern.matcher(text.getTextparts().get(0).getContent());
                if (matcher.find()) {
                    int bulletNr = Integer.parseInt(matcher.group(1).replaceAll("\\.", "").replaceAll("\\)", ""));
                    if (numberPerLevel.containsKey(getLevel(text)) && bulletNr == numberPerLevel.get(getLevel(text)) + 1) {
                        numberPerLevel.put(getLevel(text), bulletNr);
                        numbers.put(index_line, getLevel(text));
                        lastLevel = getLevel(text);
                        Object[] keys = numberPerLevel.keySet().toArray();
                        for (Object key : keys) {
                            if ((int) key > getLevel(text)) {
                                numberPerLevel.remove((int) key);
                            }
                        }
                    } else if (getLevel(text) > lastLevel && bulletNr == 1) {
                        numberPerLevel.put(getLevel(text), bulletNr);
                        numbers.put(index_line, getLevel(text));
                        lastLevel = getLevel(text);
                    }
                }
            }
            index_line++;
        }
        return numbers;
    }

}
