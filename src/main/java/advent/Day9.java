package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day9 {
    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day9.txt"))) {
            final long[] numbers = bufferedReader.lines().mapToLong(Long::parseLong).toArray();

            int preamble = 25;
            int setSize = 25;
            AtomicInteger numberIndex = new AtomicInteger(preamble);
            long number = findPart1(numbers, preamble, setSize, numberIndex);

            part2(numbers, numberIndex, number);
            part2Jo(numbers, numberIndex, number);
        }
    }

    private static void part2Jo(long[] numbers, AtomicInteger numberIndex, long number) {
        long sum = 0;
        int itCount = 0;
        List<Long> n = new LinkedList<>();
        for (int i = 0; i < numberIndex.get(); i++) {
            itCount++;
            final long currentNUmber = numbers[i];
            sum += currentNUmber;

            n.add(currentNUmber);

            while (sum > number) {
                itCount++;
                sum -= n.remove(0);
            }
            if (sum == number) {
                break;
            }
        }

        Collections.sort(n);
        System.out.println(n.get(0) + n.get(n.size()-1));
        System.out.println(itCount);
    }

    private static void part2(long[] numbers, AtomicInteger numberIndex, long number) {
        int index = 0;
        long sum = 0;
        boolean found = false;
        List<Long> n = new ArrayList<>();
        int itCount = 0;
        do {
            for (int i = index; i < numberIndex.get(); i++) {
                itCount++;
                final long currentNUmber = numbers[i];
                sum += currentNUmber;

                n.add(currentNUmber);

                if (sum > number) {
                    index++;
                    sum =0;
                    n.clear();
                    break;
                } else if (sum == number) {
                    found = true;
                    break;
                }
            }
        } while (!found);

        Collections.sort(n);
        System.out.println(n.get(0) + n.get(n.size()-1));
        System.out.println(itCount);
    }

    private static long findPart1(long[] numbers, int preamble, int setSize, AtomicInteger index) {
        long number;
        do {
            number = numbers[index.get()];
            boolean found = false;
            for (int i = index.get() - setSize; i < index.get() -1; i++) {
                for (int j = i +1; j < index.get(); j++) {
                    if (number == numbers[i] + numbers[j]) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                break;
            }
            index.incrementAndGet();

        } while(true);

        System.out.println(number);
        return number;
    }
}
