package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {
    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day7.txt"))) {

            Map<String, Map<String, Integer>> containers = new LinkedHashMap<>();

            final Pattern pattern = Pattern.compile("([a-zA-Z]+[ ][a-zA-Z]+ bags contain)|([0-9]+ [a-zA-Z]+[ ][a-zA-Z]+ bag[s]?)");

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                String container = null;
                boolean first = true;
               while (m.find()) {
                   if (first) {
                       container = m.group().replace(" bags contain", "");
                       first = false;
                   } else {
                       String innerBag = m.group().replace(" bags", "").replace(" bag", "");

                       final int indexOf = innerBag.indexOf(" ");
                       final Integer number = Integer.valueOf(innerBag.substring(0, indexOf));
                       innerBag = innerBag.substring(indexOf + 1);

                       containers.computeIfAbsent(container, s -> new HashMap<>()).put(innerBag, number);
                   }
               }

            }

            System.out.println(numberOfInnerBags(containers, "shiny gold"));
        }


    }

    private static int numberOfInnerBags(Map<String, Map<String, Integer>> containers, String bag) {
        int count = 0;
        final Map<String, Integer> innerBags = containers.get(bag);
        if (innerBags == null || innerBags.isEmpty()) {
            return count;
        }

        for (Map.Entry<String, Integer> entry : innerBags.entrySet()) {
            String s = entry.getKey();
            Integer innerCount = entry.getValue();
            final int innerBagsCount = numberOfInnerBags(containers, s);
            count += innerCount * (1 +  innerBagsCount);
        }

        return count;

    }

    private static  Set<String> numberOfContainers(Map<String, Set<String>> containers, Set<String> bags, String bag) {
        final Set<String> innerBags = containers.get(bag);
        if (innerBags != null && !innerBags.isEmpty()) {
            bags.addAll(innerBags);
            for (String containerBag: innerBags) {
                numberOfContainers(containers, bags, containerBag);
            }
        }
        return bags;
    }
}
