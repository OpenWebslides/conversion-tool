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
public class UnorderedListLogic extends AListLogic {

    /*@Override
    protected Map<Integer, Integer> getLevelsPerText(Slide slide) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        Map<Integer, Character> charPerLevel = new HashMap<>();  //Saves used symbol/character per level
        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        int lastLevel = -1;
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty()) {
                Text text = (Text) object;
                char symbol = text.getTextparts().get(0).getContent().charAt(0);
                if (!((symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z') || (symbol >= '0' && symbol <= '9'))) {
                    if (charPerLevel.containsKey(text.getLevel()) && symbol == charPerLevel.get(text.getLevel())) {
                        numbers.put(index_line, text.getLevel());
                        lastLevel = text.getLevel();
                    } else if (text.getLevel() > lastLevel) {
                        charPerLevel.put(text.getLevel(), symbol);
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
        Map<Integer, Character> charPerLevel = new HashMap<>();  //Saves used symbol/character per level
        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        int lastLevel = -1;
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && ((Text) object).getLevel() != null && !((Text) object).getTextparts().isEmpty()) {
                Text text = (Text) object;
                char symbol = text.getTextparts().get(0).getContent().charAt(0);
                if (!((symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z') || (symbol >= '0' && symbol <= '9'))) {
                    if (charPerLevel.containsKey(getLevel(text)) && symbol == charPerLevel.get(getLevel(text))) {
                        numbers.put(index_line, getLevel(text));
                        lastLevel = getLevel(text);
                    } else if (getLevel(text) > lastLevel) {
                        charPerLevel.put(getLevel(text), symbol);
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
