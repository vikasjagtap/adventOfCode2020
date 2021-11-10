package advent;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.commons.lang3.StringUtils;

public class Day16 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day16.txt"))) {
            System.out.println(part2(bufferedReader));
        }
    }

    private static long part2(BufferedReader bufferedReader) throws IOException {

        String line;

        boolean myTicket = false;
        boolean nearByTickets = false;
        Map<String, Set<Long>> fields = new HashMap<>();
        Map<Integer, Set<Long>> numberByPos = new HashMap<>();
        Set<Long> allValues = new LinkedHashSet<>();
        long[] myTicketNumbers = null;

        while ((line = bufferedReader.readLine()) != null) {

            if (line.length() == 0) {
                continue;
            }

            if (line.startsWith("nearby tickets:")) {
                nearByTickets = true;
                myTicket = false;
                continue;
            }

            if (line.startsWith("your ticket:")) {
                myTicket = true;
                nearByTickets = false;
                continue;
            }

            if (myTicket) {
                final String[] numbersString = line.split(",");

                final int length = numbersString.length;
                myTicketNumbers = new long[length];
                for (int i = 0; i < length; i++) {
                    String number = numbersString[i];
                    myTicketNumbers[i] = Long.parseLong(number);
                }
                continue;
            }

            if (nearByTickets) {
                boolean errorRate = false;
                final String[] numbersString = line.split(",");
                final int length = numbersString.length;
                final long[] numbers = new long[length];
                for (int i = 0; i < length; i++) {
                    String number = numbersString[i];
                    long n = Long.parseLong(number);
                    if (!allValues.contains(n)) {
                        errorRate = true;
                        break;
                    }
                    numbers[i] = n;

                }
                if (!errorRate) {
                    for (int i = 0, numbersLength = numbers.length; i < numbersLength; i++) {
                        long n = numbers[i];
                        numberByPos.computeIfAbsent(i, num -> new LinkedHashSet<>()).add(n);
                    }
                }

                continue;
            }

            final String[] split = line.split(": ");
            final String field = split[0];
            final LinkedHashSet<Long> values = new LinkedHashSet<>();
            final String[] numberSplits = split[1].split(" or ");
            for (String numberSplit : numberSplits) {
                String[] nSplit = numberSplit.split("-");
                LongStream.rangeClosed(Long.parseLong(nSplit[0]), Long.parseLong(nSplit[1])).forEach(values::add);
            }
            fields.put(field, values);
            allValues.addAll(values);

        }

        Map<String, Set<Integer>> fieldPos = new HashMap<>();

        numberByPos.forEach((pos, numbers) -> {
            fields.forEach((field, fieldNumbers) -> {
                if (fieldNumbers.containsAll(numbers)) {
                    fieldPos.computeIfAbsent(field, integer -> new HashSet<>()).add(pos);
                }
            });
        });
        long multiPosCount;
        do {
            Set<Integer> singlePos = fieldPos.values().stream().filter(numbers -> numbers.size() == 1).flatMap(Collection::stream).collect(Collectors.toSet());
            fieldPos.values().stream().filter(numbers -> numbers.size() > 1).forEach(numbers -> numbers.removeAll(singlePos));
            multiPosCount = fieldPos.values().stream().filter(numbers -> numbers.size() > 1).count();
        } while (multiPosCount != 0);
        System.out.println(fieldPos);

        long value = 1;
        for (Map.Entry<String, Set<Integer>> entry : fieldPos.entrySet()) {
            String field = entry.getKey();
            Set<Integer> pos = entry.getValue();
            final Integer[] integers = pos.toArray(new Integer[]{});
            if (field.startsWith("departure")) {
                value *= myTicketNumbers[integers[0]];
            }
        }


        return value;



    }



    private static long part1(BufferedReader bufferedReader) throws IOException {
        long errorRate = 0;

        String line;

        boolean myTicket = false;
        boolean nearByTickets = false;
        Set<Long> allValues = new LinkedHashSet<>();

        while ((line = bufferedReader.readLine()) != null) {

            if (line.length() == 0) {
                continue;
            }

            if (line.startsWith("nearby tickets:")) {
                nearByTickets = true;
                myTicket = false;
                continue;
            }

            if (line.startsWith("your ticket:")) {
                myTicket = true;
                nearByTickets = false;
                continue;
            }

            if (myTicket) {
                continue;
            }

            if (nearByTickets) {
                final String[] numbers = line.split(",");
                for (String number : numbers) {
                    long n = Long.parseLong(number);
                    if (!allValues.contains(n)) {
                        errorRate+=n;
                    }
                }
                continue;
            }

            final String[] split = line.split(": ");
            final String[] numberSplits = split[1].split(" or ");
            for (String numberSplit : numberSplits) {
                String[] nSplit = numberSplit.split("-");
                LongStream.rangeClosed(Long.parseLong(nSplit[0]), Long.parseLong(nSplit[1])).forEach(allValues::add);
            }

        }


        return errorRate;



    }


}
