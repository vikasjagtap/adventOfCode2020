package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Day3TobogganTrajectory {
    public static void main(String[] args) throws IOException {
        AtomicLong lineCount = new AtomicLong();
        AtomicLong r3d1StepCount = new AtomicLong();
        AtomicLong r3d1TreeCount = new AtomicLong();

        AtomicLong r1d1StepCount = new AtomicLong();
        AtomicLong r1d1TreeCount = new AtomicLong();

        AtomicLong r5d1StepCount = new AtomicLong();
        AtomicLong r5d1TreeCount = new AtomicLong();

        AtomicLong r7d1StepCount = new AtomicLong();
        AtomicLong r7d1TreeCount = new AtomicLong();

        AtomicLong r1d2StepCount = new AtomicLong();
        AtomicLong r1d2TreeCount = new AtomicLong();

        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day3.txt"))) {
            bufferedReader.lines().forEach(l -> {
                StringBuilder line = new StringBuilder(l.trim());

                if (lineCount.get() != 0) {
                    final int step = r7d1StepCount.intValue();
                    while (step >= line.length()) {
                        line.append(line);
                    }

//                    System.out.println(line);

                    if (line.charAt(r1d1StepCount.intValue()) == '#') {
                        r1d1TreeCount.incrementAndGet();
                    }
                    if (line.charAt(r3d1StepCount.intValue()) == '#') {
                        r3d1TreeCount.incrementAndGet();
                    }
                    if (line.charAt(r5d1StepCount.intValue()) == '#') {
                        r5d1TreeCount.incrementAndGet();
                    }
                    if (line.charAt(r7d1StepCount.intValue()) == '#') {
                        r7d1TreeCount.incrementAndGet();
                    }

                    if (lineCount.get() % 2 == 0) {
                        char c = ' ';
                        if (line.charAt(r1d2StepCount.intValue()) == '#') {
                            r1d2TreeCount.incrementAndGet();
                            c = '#';
                        }
                        r1d2StepCount.incrementAndGet();
                        System.out.println((lineCount.get() + 1) + ":" + r1d2StepCount + " " + c);
                    }
                } else {
                    r1d2StepCount.incrementAndGet();
//                    System.out.println(line);
                }
                lineCount.incrementAndGet();
                IntStream.rangeClosed(1, 1).forEach(value -> {
                    r1d1StepCount.incrementAndGet();
                } );
                IntStream.rangeClosed(1, 3).forEach(value -> r3d1StepCount.incrementAndGet());
                IntStream.rangeClosed(1, 5).forEach(value -> r5d1StepCount.incrementAndGet());
                IntStream.rangeClosed(1, 7).forEach(value -> r7d1StepCount.incrementAndGet());

            });
        }

        System.out.print(r1d1TreeCount.get() + " * ");
        System.out.print(r3d1TreeCount.get()+ " * ");
        System.out.print(r5d1TreeCount.get()+ " * ");
        System.out.print(r7d1TreeCount.get()+ " * ");
        System.out.print(r1d2TreeCount.get()+ " = ");
        System.out.print(r1d1TreeCount.get() * r3d1TreeCount.get() * r5d1TreeCount.get() * r7d1TreeCount.get() * r1d2TreeCount.get());


    }




}
