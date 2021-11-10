package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class day10 {

    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day10.txt"))) {
            final List<Integer> joltageRatings = bufferedReader.lines()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

            int jolts = 3;
            joltageRatings.add(0);
            Collections.sort(joltageRatings);
            joltageRatings.add(findMaxJoltageRatings(joltageRatings, jolts));


            part2(joltageRatings, jolts);



        }
    }

    private static void part1(List<Integer> joltageRatings, int jolts) {
        final Map<Integer, AtomicInteger> counts = new TreeMap<>();
        for (int i = 0; i < joltageRatings.size(); i++) {
            for (int j = 1; j <= jolts; j++) {
                if ((i + 1) < joltageRatings.size() &&  joltageRatings.get(i + 1) == (joltageRatings.get(i) + j)) {
                    counts.computeIfAbsent(j, integer -> new AtomicInteger()).incrementAndGet();
                    break;
                }
            }
        }

        System.out.println(counts.get(1).get() * counts.get(3).get());
    }

    private static void part2(List<Integer> joltageRatings, int jolts) {
        final Map<Integer, Set<Integer>> visited = new TreeMap<>();
        final Set<Integer> allValues = new HashSet<>(joltageRatings);

        for (Integer joltage :  joltageRatings) {
            IntStream.range(1, jolts + 1).forEach(value -> {
                final int o = joltage + value;
                if (allValues.contains(o)) {
                    visited.computeIfAbsent(joltage, integer -> new HashSet<>()).add(o);
                }
            });
        }

        int endJoltage = joltageRatings.get(joltageRatings.size()-1);
        long count = visitNode(endJoltage, visited, 0, new HashMap<>());
        System.out.println(count);
    }

    private static long visitNode(int endJoltage, Map<Integer, Set<Integer>> visited, int node, Map<Integer, Long> visitedNode) {
        final Long aLong = visitedNode.get(node);
        if (aLong != null) {
            return aLong;
        }

        long count = 0;
        if(visited.containsKey(node)) {
            final Set<Integer> integers = visited.get(node);
            if (integers.contains(endJoltage)) {
                return 1;
            }

            for (Integer integer: integers) {
                count += visitNode(endJoltage, visited, integer, visitedNode);
            }
        }

        visitedNode.put(node, count);

        return count;
    }

    private static int findMaxJoltageRatings(List<Integer> numbers, int jolts) {
        return numbers.get(numbers.size() - 1) + jolts;
    }
}
