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

    /**
     *
     * @param slide
     * @param regex
     * @return
     */
    @Override
    public Map<Integer, Integer> getLevelsPerText(Slide slide, String regex) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        Map<Integer, Integer> numberPerLevel = new HashMap<>();  //Saves last used bullet number per level (level as xPos, bulletNr)
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
                //Pattern pattern = Pattern.compile("(\\d+[\\.)](?!\\d))");
                Pattern pattern = Pattern.compile(regex);

                Matcher matcher = pattern.matcher(text.getTextparts().get(0).getContent());
                if (matcher.find()) {
                    if (levelPerXPos.containsKey(x) || (!levelPerXPos.containsKey(x) && x > lastX)) {
                        if (!levelPerXPos.containsKey(x) && x > lastX) {
                            levelPerXPos.put(x, lastLevel + 1);
                        }
                        int level = levelPerXPos.get(x);
                        int bulletNr;
                        String bullet = matcher.group(1).replaceAll("\\.", "").replaceAll("\\)", "");
                        try {
                            bulletNr = Integer.parseInt(bullet);
                        } catch (NumberFormatException e) {
                            if (bullet.charAt(0) >= 'A' && bullet.charAt(0) <= 'Z') {
                                bulletNr = bullet.charAt(0) - 'A' + 1;
                            } else {
                                bulletNr = bullet.charAt(0) - 'a' + 1;
                            }
                        }

                        if (numberPerLevel.containsKey(level) && bulletNr == numberPerLevel.get(level) + 1) {
                            numberPerLevel.put(level, bulletNr);
                            numbers.put(index_line, level);
                            lastLevel = level;
                            Object[] keys = numberPerLevel.keySet().toArray();
                            for (Object key : keys) {
                                if ((int) key > tp.getXPosition()) {
                                    numberPerLevel.remove((int) key);
                                }
                            }
                        } else if (tp.getXPosition() > lastX && bulletNr == 1) {
                            numberPerLevel.put(lastLevel + 1, bulletNr);
                            numbers.put(index_line, lastLevel + 1);
                            lastLevel = lastLevel + 1;
                        }
                        lastX = x;
                    }
                }
            }
            index_line++;
        }
        return numbers;
    }
}
