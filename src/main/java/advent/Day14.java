package advent;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Day14 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day14.txt"))) {

            final List<String> lines = bufferedReader.lines().collect(Collectors.toList());

//            System.out.println(part1(lines));
            System.out.println(part2(lines));
        }
    }

    static class Mask {

        private final long mask1;
        private final long mask0;
        private final String maskString;
        public Mask(String line) {
            maskString = line.replace("mask = ", "");
            mask1 = Long.parseLong(maskString.replace('X', '0'), 2);
            mask0 = Long.parseLong(maskString.replace('X', '1'), 2);



        }

        long mask(long number) {
            return number & mask0 | mask1;
        }

        List<Long> newMask(int number) {
            final int combinations = (int) Math.pow(2, StringUtils.countMatches(maskString, 'X'));
            final String binaryString = String.format("%36s", Integer.toBinaryString(number)).replace(' ', '0');
            List<String> maskStrings = new ArrayList<>(combinations);
            for (int i=0; i < combinations; i++) {
                maskStrings.add("");
            }
            int base = 1;
            for (int i = 0; i < 36; i++) {

                final char maskChar = maskString.charAt(i);
                if (maskChar == 'X') {
                    boolean zero = true;
                    int digiCount = 0;

                    for (int j = 0; j < combinations; j++) {
                        String m = maskStrings.get(j);
                        if (m == null) {
                            m = "";
                        }
                        m+=zero?"0":"1";

                        digiCount++;

                        if (digiCount == base) {
                            digiCount = 0;
                            zero = !zero;
                        }


                        maskStrings.set(j, m);
                    }
                    base *= 2;
                } else {
                    for (int j = 0; j < combinations; j++) {
                        String m = maskStrings.get(j);
                        if (m == null) {
                            m = "";
                        }
                        final char m1 = binaryString.charAt(i);
                        m+= maskChar == '1' ? '1' : m1;
                        maskStrings.set(j, m);
                    }
                }
            }

            final List<Long> collect = maskStrings.stream().map(s -> Long.parseLong(s, 2)).collect(Collectors.toList());
            return collect;


        }


    }

    private static long part2(List<String> lines) {
        String maskLine = lines.get(0);
        Mask mask = new Mask(maskLine);

        Map<Long, Long> addresses = new HashMap<>();
        long sum = 0;

        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i);

            if (line.startsWith("mask =")) {
                mask = new Mask(line);
                continue;
            }

            final String[] split = line.split(" = ");
            final int address = Integer.parseInt(split[0].replace("mem[", "").replace("]", ""));
            final long number = Long.parseLong(split[1]);

            for (Long add : mask.newMask(address)) {
                final Long oldValue = addresses.put(add, number);
                if (oldValue != null) {
                    sum -= oldValue;
                }

                sum += number;
            }
        }


        return sum;

    }

    private static long part1(List<String> lines) {
        Mask mask = new Mask(lines.get(0));

        Map<Integer, Long> addresses = new HashMap<>();
        long sum = 0;

        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i);

            if (line.startsWith("mask =")) {
                mask = new Mask(line);
                continue;
            }

            final String[] split = line.split(" = ");
            final int address = Integer.parseInt(split[0].replace("mem[", "").replace("]", ""));
            final long number = Long.parseLong(split[1]);

            final long newValue = mask.mask(number);
            final Long oldValue = addresses.put(address, newValue);
            if (oldValue != null) {
                sum -= oldValue;
            }

            sum += newValue;

        }


        return sum;

    }


}
