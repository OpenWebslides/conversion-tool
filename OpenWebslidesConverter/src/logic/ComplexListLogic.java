package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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
public class ComplexListLogic extends AListLogic {

    /**
     *
     * @param slide
     * @return
     */
    @Override
    public Map<Integer, Integer> getLevelsPerText(Slide slide) {
        Map<Integer, Integer> numbers = new LinkedHashMap<>();  //Index, level
        int index_line = 0;
        //Searches for ordered lists that start with the number 1
        ArrayList<String> nextLevels = new ArrayList<>(Arrays.asList("1."));
        //Read all text objects in slide and puts the line number and level number in a map
        for (PPTObject object : slide.getPptObjects()) {
            if (object instanceof Text && !((Text) object).getTextparts().isEmpty() && !(((Text) object).getTextparts().get(0).getContent() == null)) {
                Text text = (Text) object;
                Pattern pattern = Pattern.compile("(\\d+\\.(\\d+\\.)*(?!\\d))");
                Matcher matcher = pattern.matcher(text.getTextparts().get(0).getContent());
                if (matcher.find() && nextLevels.contains(matcher.group(1))) {
                    adjustNextLevels(nextLevels, matcher.group(1));
                    numbers.put(index_line, matcher.group(1).split("\\.").length);
                }
            }
            index_line++;
        }
        return numbers;
    }

    //This method fills the List with the next possible levels
    private void adjustNextLevels(List<String> nextLevels, String currentlevel) {
        nextLevels.clear();
        currentlevel += "0.";
        String[] numbers = currentlevel.split("\\.");

        for (int i = 0; i < numbers.length; i++) {
            String level = "";
            for (int j = 0; j < i; j++) {
                level += numbers[j] + ".";
            }
            level += (Integer.parseInt(numbers[i]) + 1) + ".";
            nextLevels.add(level);
        }
    }

}
