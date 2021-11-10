package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
    public static void main(String[] args) throws IOException {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day4.txt"))) {
            StringBuilder passport = new StringBuilder();
            AtomicInteger atomicInteger = new AtomicInteger();
            bufferedReader.lines().forEach(l -> {
                String line = l.trim();
                if (line.length() == 0) {
                    checkPassport(passport, atomicInteger);
                    passport.setLength(0);
                } else {
                    passport.append(" ").append(line);
                }
            });
            checkPassport(passport, atomicInteger);
            System.out.println(atomicInteger);
        }
    }

    private static void checkPassport(StringBuilder passport, AtomicInteger atomicInteger) {
        Set<String> allMatches = new HashSet<>();
        Matcher m = Pattern
            .compile("(byr:(19[2-9][0-9]|200[0-2]))|(iyr:(201[0-9]|2020))|(eyr:(202[0-9]|2030))|(hgt:(1[5-8][0-9]|19[0-3]cm)|(59|6[0-9]|7[0-6]in))|(hcl:#\\w{6})|(ecl:(amb|blu|brn|gry|grn|hzl|oth))|(pid:[0-9]{9})")
            .matcher(" " + passport.toString());
        while (m.find()) {
            allMatches.add(m.group());
        }

        if (allMatches.size() == 7) {
            atomicInteger.incrementAndGet();
        }
    }
}
