package logic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import objects.PPTObject;
import objects.Slide;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Joann
 */
public class SimpleListLogic extends AListLogic {

    /*@Override
    public Map<Integer, Double> getLevelsPerText(Slide slide) {
        Map<Integer, Double> numbers = new LinkedHashMap<>();  //Index, level
        Map<Double, Integer> numberPerLevel = new HashMap<>();  //Saves last used bullet number per level
        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        double lastLevel = -1;
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty() && !(((Text) object).getTextparts().get(0).getContent() == null) && !(((Text) object).getTextparts().get(0).getYPosition() == 0)) {
                Text text = (Text) object;
                Textpart tp = text.getTextparts().get(0);
                Pattern pattern = Pattern.compile("(\\d+[\\.)](?!\\d))");
                Matcher matcher = pattern.matcher(text.getTextparts().get(0).getContent());
                if (matcher.find()) {
                    int bulletNr = Integer.parseInt(matcher.group(1).replaceAll("\\.", "").replaceAll("\\)", ""));
                    if (numberPerLevel.containsKey(tp.getXPosition()) && bulletNr == numberPerLevel.get(tp.getXPosition()) + 1) {
                        numberPerLevel.put(tp.getXPosition(), bulletNr);
                        numbers.put(index_line, tp.getXPosition());
                        lastLevel = tp.getXPosition();
                        Object[] keys = numberPerLevel.keySet().toArray();
                        for (Object key : keys) {
                            if ((int) key > tp.getXPosition()) {
                                numberPerLevel.remove((double) key);
                            }
                        }
                    } else if (tp.getXPosition() > lastLevel && bulletNr == 1) {
                        numberPerLevel.put(tp.getXPosition(), bulletNr);
                        numbers.put(index_line, tp.getXPosition());
                        lastLevel = tp.getXPosition();
                    }
                }
            }
            index_line++;
        }
        return numbers;
    }*/
    //orig
    @Override
    public Map<Integer, Integer> getLevelsPerText(Slide slide) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        Map<Integer, Integer> numberPerLevel = new HashMap<>();  //Saves last used bullet number per level
        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        int lastLevel = -1;
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty() && ((Text) object).getTextparts().get(0).getContent() != null && !((Text) object).getTextparts().get(0).getContent().equals("")) {
                Text text = (Text) object;
                Textpart tp = text.getTextparts().get(0);
                int x = (int) tp.getXPosition();
                Pattern pattern = Pattern.compile("(\\d+[\\.)](?!\\d))");
                Matcher matcher = pattern.matcher(text.getTextparts().get(0).getContent());
                if (matcher.find()) {
                    int bulletNr = Integer.parseInt(matcher.group(1).replaceAll("\\.", "").replaceAll("\\)", ""));
                    if (numberPerLevel.containsKey(x) && bulletNr == numberPerLevel.get(x) + 1) {
                        numberPerLevel.put(x, bulletNr);
                        numbers.put(index_line, x);
                        lastLevel = x;
                        Object[] keys = numberPerLevel.keySet().toArray();
                        for (Object key : keys) {
                            if ((int) key > tp.getXPosition()) {
                                numberPerLevel.remove((int) key);
                            }
                        }
                    } else if (tp.getXPosition() > lastLevel && bulletNr == 1) {
                        numberPerLevel.put(x, bulletNr);
                        numbers.put(index_line, x);
                        lastLevel = x;
                    }
                }
            }
            index_line++;
        }
        return numbers;
    }
    //Meh pptx niet nodig
    /*@Override
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
    }*/
}
