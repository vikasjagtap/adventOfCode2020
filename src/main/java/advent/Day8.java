package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day8.txt"))) {

            final List<String> lines = bufferedReader.lines().collect(Collectors.toList());

            final Set<Integer> replacedLines = new LinkedHashSet<>();


            final Set<Integer> lineExecCount = new LinkedHashSet<>();
            int index = 0;
            int acc = 0;
            boolean replaced = false;
            while(index < lines.size()) {

                if (lineExecCount.contains(index)) {
                    lineExecCount.clear();
                    replaced = false;
                    acc = 0;
                    index = 0;
                }

                final String line = lines.get(index);
                String[] splits = line.split(" ");
                lineExecCount.add(index);
                final boolean replace = !replaced && !replacedLines.contains(index);

                switch (splits[0]) {
                    case "nop": {
                        if (replace) {
                            replaced = true;
                            replacedLines.add(index);
                            index = jump(index, splits[1]);
                        } else {
                            index = nop(index);
                        }
                        break;
                    }
                    case "acc" : {
                        acc += Integer.parseInt(splits[1]);
                        index++;
                        break;
                    }
                    case "jmp": {
                        if (replace) {
                            replaced = true;
                            replacedLines.add(index);
                            index = nop(index);
                        } else {
                            index = jump(index, splits[1]);
                        }
                        break;
                    }

                }

            }


            System.out.println(acc);
        }


    }

    private static int nop(int index) {
        index++;
        return index;
    }

    private static int jump(int index, String split) {
        int jump = Integer.parseInt(split);
        index += jump;
        return index;
    }
}
