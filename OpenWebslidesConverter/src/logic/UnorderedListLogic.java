package logic;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import objects.PPTObject;
import objects.Slide;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Joann
 */
public class UnorderedListLogic extends AListLogic {

    @Override
    protected Map<Integer, Integer> getLevelsPerText(Slide slide) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        Map<Integer, Character> charPerLevel = new HashMap<>();  //Saves used symbol/character per level
        Map<Integer, Integer> levelPerXPos = new HashMap<>();  //Saves level per xPosition

        int index_line = 0;
        //Read all text objects in slide and puts the line number and level number in a map
        int lastLevel = -1;
        int lastX = -1;

        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty() && ((Text) object).getTextparts().get(0).getContent() != null && !((Text) object).getTextparts().get(0).getContent().equals("")) {
                Text text = (Text) object;
                Textpart tp = text.getTextparts().get(0);
                int x = (int) tp.getXPosition();
                char symbol = text.getTextparts().get(0).getContent().charAt(0);
                if (!((symbol >= 'a' && symbol <= 'z') || (symbol >= 'A' && symbol <= 'Z') || (symbol >= '0' && symbol <= '9'))) {
                    if (levelPerXPos.containsKey(x) || (!levelPerXPos.containsKey(x) && x > lastX)) {
                        if (!levelPerXPos.containsKey(x) && x > lastX) {
                            levelPerXPos.put(x, lastLevel + 1);
                        }
                        int level = levelPerXPos.get(x);
                        if (charPerLevel.containsKey(level) && symbol == charPerLevel.get(level)) {
                            numbers.put(index_line, level);
                            lastLevel = level;
                        } else if (tp.getXPosition() > lastLevel) {
                            charPerLevel.put(lastLevel + 1, symbol);
                            numbers.put(index_line, lastLevel + 1);
                            lastLevel++;
                        }
                        lastX = x;
                    }
                }
            }
            index_line++;
        }
        return numbers;
    }
    /*
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
    }*/

}
