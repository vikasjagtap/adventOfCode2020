package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Day2PasswordPolicyChecker {

    public static void main(String[] args) throws IOException {
        AtomicInteger part1ValidCount = new AtomicInteger();
        AtomicInteger part2ValidCount = new AtomicInteger();
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("passwords.txt"))) {
            bufferedReader.lines().forEach(line -> {
//                final String line = "6-10 t: tttttbttktt";
                final String[] split = line.split(":");
                final String password = split[1].trim();
                final String[] split1 = split[0].split(" ");
                final char character = split1[1].charAt(0);

                final String[] split2 = split1[0].split("-");
                final int minString = Integer.valueOf(split2[0]);
                final int maxString = Integer.valueOf(split2[1]);

//                //part one
//                long count = password.chars().filter(ch -> ch == character).count();
//
//                if (count <= maxString && count >= minString) {
//                    part1ValidCount.incrementAndGet();
//                }

                final boolean f1 = password.charAt(minString - 1) == character;
                final boolean f2 = password.charAt(maxString - 1) == character;
                if ((f1 != f2)) {
                    part2ValidCount.incrementAndGet();
                }
            });

//            System.out.println(part1ValidCount);
            System.out.println(part2ValidCount);
        }


    }
}
