package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Day6 {
    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day6.txt"))) {
            StringBuilder group = new StringBuilder();
            int personCount = 0;
            int sumOfCount = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() == 0) {
                    sumOfCount += countAnswers(group, personCount);
                    group.setLength(0);
                    personCount = 0;
                } else {
                    group.append(line);
                    personCount++;
                }
            }
            sumOfCount += countAnswers(group, personCount);
            System.out.println(sumOfCount);
        }
    }

    private static int countAnswers(StringBuilder group, int personCount) {
        Map<Character, Integer> uniqueAnswers = new LinkedHashMap<>();
        for (char c : group.toString().toLowerCase().toCharArray()) {
            uniqueAnswers.put(c, uniqueAnswers.getOrDefault(c, 0) + 1);
        }

        int count = 0;
        for (Map.Entry<Character, Integer> entry : uniqueAnswers.entrySet()) {
            if (entry.getValue() == personCount) {
                count++;
            }
        }

        return count;

    }
}
